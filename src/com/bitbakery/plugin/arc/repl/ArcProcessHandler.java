package com.bitbakery.plugin.arc.repl;

import static com.bitbakery.plugin.arc.ArcStrings.message;
import com.bitbakery.plugin.arc.config.ArcConfiguration;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import static com.intellij.openapi.util.SystemInfo.isMac;
import static com.intellij.openapi.util.SystemInfo.isWindows;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.util.Alarm;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.*;

/**
 * Ripped off from OSProcessHandler, minus the command line noise
 */
public class ArcProcessHandler extends ProcessHandler {
    private static final Logger LOG = Logger.getInstance(ArcProcessHandler.class.getName());
    private static ExecutorService ourThreadExecutorsService = null;

    private final Process myProcess;
    private final ProcessWaitFor myWaitFor;

    public static Future<?> executeOnPooledThread(Runnable task) {
        final Application application = ApplicationManager.getApplication();

        if (application != null) {
            return application.executeOnPooledThread(task);
        } else {
            if (ourThreadExecutorsService == null) {
                ourThreadExecutorsService = new ThreadPoolExecutor(
                        10,
                        Integer.MAX_VALUE,
                        60L,
                        TimeUnit.SECONDS,
                        new SynchronousQueue<Runnable>(),
                        new ThreadFactory() {
                            @SuppressWarnings({"HardCodedStringLiteral"})
                            public Thread newThread(Runnable r) {
                                return new Thread(r, "OSProcessHandler pooled thread");
                            }
                        }
                );
            }

            return ourThreadExecutorsService.submit(task);
        }
    }

    public ArcProcessHandler(Project project) throws IOException {
        ArcConfiguration arcConfig = ApplicationManager.getApplication().getComponent(ArcConfiguration.class);
        if (notConfigured(arcConfig)) {
            if (!ShowSettingsUtil.getInstance().editConfigurable(project, arcConfig)) {
                // TODO - This isn't doing what you intend...
                JOptionPane.showMessageDialog(null,
                        message("config.error.replNotConfiguredMessage"),
                        message("config.error.replNotConfiguredTitle"),
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        // For now, the executables and command-line args are hard-coded. We may need more flexibility
        //   in the future (e.g., different Schemes with different args)
        String scheme = arcConfig.mzSchemeHome;
        if (!StringUtil.isEmptyOrSpaces(scheme)) {
            scheme += isMac ? "/bin/" : "/";
        }
        scheme += isWindows ? "MzScheme.exe" : "mzscheme";

        String[] myCommandLine = new String[]{scheme, "-m", "-f", arcConfig.arcInitializationFile};

        myProcess = Runtime.getRuntime().exec(myCommandLine, null, new File(arcConfig.arcHome));
        myWaitFor = new ProcessWaitFor(myProcess);
    }

    private boolean notConfigured(ArcConfiguration arcConfig) {
        return StringUtil.isEmptyOrSpaces(arcConfig.arcHome)
                || StringUtil.isEmptyOrSpaces(arcConfig.arcInitializationFile);
    }

    private static class ProcessWaitFor {
        private final com.intellij.util.concurrency.Semaphore myWaitSemaphore = new com.intellij.util.concurrency.Semaphore();

        private final Future<?> myWaitForThreadFuture;
        private int myExitCode;

        public void detach() {
            myWaitForThreadFuture.cancel(true);
            myWaitSemaphore.up();
        }

        public ProcessWaitFor(final Process process) {
            myWaitSemaphore.down();
            final Runnable action = new Runnable() {
                public void run() {
                    try {
                        myExitCode = process.waitFor();
                    }
                    catch (InterruptedException e) {
                        // Do nothing, by design
                    }
                    myWaitSemaphore.up();
                }
            };

            myWaitForThreadFuture = executeOnPooledThread(action);
        }

        public int waitFor() {
            myWaitSemaphore.waitFor();
            return myExitCode;
        }
    }

    public void startNotify() {
        final ReadProcessThread stdoutThread = new ReadProcessThread(createProcessOutReader()) {
            protected void textAvailable(String s) {
                notifyTextAvailable(s, ProcessOutputTypes.STDOUT);
            }
        };

        final ReadProcessThread stderrThread = new ReadProcessThread(createProcessErrReader()) {
            protected void textAvailable(String s) {
                notifyTextAvailable(s, ProcessOutputTypes.STDERR);
            }
        };

        //notifyTextAvailable(myCommandLine + '\n', ProcessOutputTypes.SYSTEM);

        addProcessListener(new ProcessAdapter() {
            public void startNotified(final ProcessEvent event) {
                try {
                    final Future<?> stdOutReadingFuture = executeOnPooledThread(stdoutThread);
                    final Future<?> stdErrReadingFuture = executeOnPooledThread(stderrThread);

                    final Runnable action = new Runnable() {
                        public void run() {
                            int exitCode = 0;

                            try {
                                exitCode = myWaitFor.waitFor();

                                // tell threads that no more attempts to read process' output should be made
                                stderrThread.setProcessTerminated(true);
                                stdoutThread.setProcessTerminated(true);

                                stdErrReadingFuture.get();
                                stdOutReadingFuture.get();
                            }
                            catch (InterruptedException e) {
                                // Do nothing
                            }
                            catch (ExecutionException e) {
                                // Do nothing
                            }

                            onOSProcessTerminated(exitCode);
                        }
                    };

                    executeOnPooledThread(action);
                }
                finally {
                    removeProcessListener(this);
                }
            }
        });

        super.startNotify();
    }

    protected void onOSProcessTerminated(final int exitCode) {
        notifyProcessTerminated(exitCode);
    }

    protected Reader createProcessOutReader() {
        return new BufferedReader(new InputStreamReader(myProcess.getInputStream(), getCharset()));
    }

    protected Reader createProcessErrReader() {
        return new BufferedReader(new InputStreamReader(myProcess.getErrorStream(), getCharset()));
    }

    protected void destroyProcessImpl() {
        try {
            closeStreams();
        }
        finally {
            myProcess.destroy();
        }
    }

    protected void detachProcessImpl() {
        final Runnable runnable = new Runnable() {
            public void run() {
                closeStreams();

                myWaitFor.detach();
                notifyProcessDetached();
            }
        };

        executeOnPooledThread(runnable);
    }

    private void closeStreams() {
        try {
            myProcess.getOutputStream().close();
        }
        catch (IOException e) {
            LOG.error(e);
        }
    }

    public boolean detachIsDefault() {
        return false;
    }

    public OutputStream getProcessInput() {
        return myProcess.getOutputStream();
    }

    public Charset getCharset() {
        return CharsetToolkit.getIDEOptionsCharset();
    }

    private static abstract class ReadProcessThread implements Runnable {
        private static final int NOTIFY_TEXT_DELAY = 300;

        private final Reader myReader;

        private final StringBuffer myBuffer = new StringBuffer();
        private final Alarm myAlarm;

        private boolean myIsClosed = false;
        private boolean myIsProcessTerminated = false;

        public ReadProcessThread(final Reader reader) {
            myReader = reader;
            myAlarm = new Alarm(Alarm.ThreadToUse.SHARED_THREAD);
        }

        public synchronized boolean isProcessTerminated() {
            return myIsProcessTerminated;
        }

        public synchronized void setProcessTerminated(boolean isProcessTerminated) {
            myIsProcessTerminated = isProcessTerminated;
        }

        public void run() {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            try {
                myAlarm.addRequest(new Runnable() {
                    public void run() {
                        if (!isClosed()) {
                            myAlarm.addRequest(this, NOTIFY_TEXT_DELAY);
                            checkTextAvailable();
                        }
                    }
                }, NOTIFY_TEXT_DELAY);

                try {
                    while (!isClosed()) {
                        final int c = readNextByte();
                        if (c == -1) {
                            break;
                        }
                        synchronized (myBuffer) {
                            myBuffer.append((char) c);
                        }
                        if (c == '\n') { // not by '\r' because of possible '\n'
                            checkTextAvailable();
                        }
                    }
                }
                catch (Exception e) {
                    LOG.error(e);
                    e.printStackTrace();
                }

                close();
            }
            finally {
                Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
            }
        }

        private int readNextByte() {
            try {
                while (!myReader.ready()) {
                    if (isProcessTerminated()) {
                        return -1;
                    }
                    try {
                        Thread.sleep(1L);
                    }
                    catch (InterruptedException ignore) {
                    }
                }
                return myReader.read();
            }
            catch (IOException e) {
                return -1; // When process terminated Process.getInputStream()'s underlaying stream becomes closed on Linux.
            }
        }

        private void checkTextAvailable() {
            synchronized (myBuffer) {
                if (myBuffer.length() == 0) return;
                // warning! Since myBuffer is reused, do not use myBuffer.toString() to fetch the string
                // because the created string will get StringBuffer's internal char array as a buffer which is possibly too large.
                final String s = myBuffer.substring(0, myBuffer.length());
                myBuffer.setLength(0);
                textAvailable(s);
            }
        }

        private void close() {
            synchronized (this) {
                if (isClosed()) {
                    return;
                }
                myIsClosed = true;
            }
            //try {
            //  if(Thread.currentThread() != this) {
            //    join(0);
            //  }
            //}
            //catch (InterruptedException e) {
            //}
            // must close after the thread finished its execution, cause otherwise
            // the thread will try to read from the closed (and nulled) stream
            try {
                myReader.close();
            }
            catch (IOException e1) {
                // supressed
            }
            checkTextAvailable();
        }

        protected abstract void textAvailable(final String s);

        private synchronized boolean isClosed() {
            return myIsClosed;
        }

    }
}
