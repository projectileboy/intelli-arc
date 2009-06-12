package com.bitbakery.plugin.arc.repl;

/*
 * Copyright (c) Kurt Christensen, 2009
 *
 *  Licensed under the Artistic License, Version 2.0 (the "License"); you may not use this
 *  file except in compliance with the License. You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/artistic-license-2.0.php
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under
 *  the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 *  OF ANY KIND, either express or implied. See the License for the specific language
 *  governing permissions and limitations under the License..
 */

import com.bitbakery.plugin.arc.ArcIcons;
import static com.bitbakery.plugin.arc.ArcStrings.message;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.*;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.EditorComponentImpl;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.List;

public class ReplToolWindow implements ProjectComponent {

    private static final String REPL_TOOL_WINDOW_ID = "repl.toolWindow";

    private Project myProject;
    private List<Repl> replList = new ArrayList<Repl>();
    private JTabbedPane tabbedPane;
    private ToolWindow toolWindow;
    private ActionPopupMenu popup;


    public ReplToolWindow(Project project) {
        myProject = project;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                disposeComponent();
            }
        });
    }

    public void requestFocus() {
        toolWindow.activate(null, true);
        if (tabbedPane.getSelectedIndex() > -1) {
            Repl repl = replList.get(tabbedPane.getSelectedIndex());
            repl.view.getPreferredFocusableComponent().requestFocusInWindow();
        }
    }

    public String writeToCurrentRepl(String s) {
        return writeToCurrentRepl(s, true);
    }

    public String writeToCurrentRepl(String s, boolean requestFocus) {

        // TODO - We should perhaps create a new REPL if one doesn't exist...?
        if (tabbedPane.getSelectedIndex() > -1) {
            final PipedWriter pipeOut;
            PipedReader pipeIn = null;
            try {
                if (requestFocus) requestFocus();
                final Repl repl = replList.get(tabbedPane.getSelectedIndex());

                pipeOut = new PipedWriter();
                pipeIn = new PipedReader(pipeOut);
                BufferedReader in = new BufferedReader(pipeIn);

                ProcessListener processListener = new ProcessAdapter() {
                    @Override
                    public void onTextAvailable(ProcessEvent event, Key outputType) {
                        try {
                            pipeOut.write(event.getText());
                            pipeOut.flush();
                            pipeOut.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                repl.processHandler.addProcessListener(processListener);

                repl.view.print(s + "\r\n", ConsoleViewContentType.USER_INPUT);

                StringBuffer buf = new StringBuffer();
                String str;
                while ((str = in.readLine()) != null) {
                    buf.append(str);
                }
                buf.delete(0, 5); // Remove the 'arc> ' prefix

                repl.processHandler.removeProcessListener(processListener);

                return buf.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (pipeIn != null) {
                    try {
                        pipeIn.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public void projectOpened() {
        try {
            initToolWindow();
        } catch (Exception e) {
            // TODO - Handle me for real...
            e.printStackTrace();  // TODO - Some sort of real error handling
        }
    }

    public void projectClosed() {
        // ??? unregisterToolWindow();
    }

    public void initComponent() {
        // Empty

        // TODO - We need a way to open and close the REPL tool window, not just individual REPLs
        // toolWindow.setAvailable(true);
    }

    public void disposeComponent() {
/* TODO - Memory leaks galore from this guy... need to call on the EventDispatch thread; who *knows* what else I'm doing wrong?? <sigh>...
        ToolWindowManager.getInstance(myProject).unregisterToolWindow(message("repl.toolWindowName"));
        tabbedPane.removeAll();
*/

        for (Repl repl : replList) {
            repl.close();
        }
    }

    @NotNull
    public String getComponentName() {
        return REPL_TOOL_WINDOW_ID;
    }


    private void initToolWindow() throws ExecutionException, IOException {
        if (myProject != null) {
            tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(tabbedPane, BorderLayout.CENTER);

            ActionManager am = ActionManager.getInstance();
            ActionGroup group = (ActionGroup) am.getAction("ArcReplActionGroup");
            ActionToolbar toolbar = am.createActionToolbar(message("repl.title"), group, false);
            panel.add(toolbar.getComponent(), BorderLayout.WEST);

            toolWindow = ToolWindowManager.getInstance(myProject).registerToolWindow(message("repl.title"), false, ToolWindowAnchor.BOTTOM);
            ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
            Content content = contentFactory.createContent(panel, null, true);
            toolWindow.getContentManager().addContent(content);
            toolWindow.setIcon(ArcIcons.ARC_REPL_ICON);
            // toolWindow.setToHideOnEmptyContent(true);

            popup = am.createActionPopupMenu(message("repl.title"), group);
            panel.setComponentPopupMenu(popup.getComponent());
            toolWindow.getComponent().setComponentPopupMenu(popup.getComponent());
            toolbar.getComponent().setComponentPopupMenu(popup.getComponent());

            createRepl();
        }
    }

    public void createRepl() {
        try {
            Repl repl = new Repl();
            replList.add(repl);

            tabbedPane.addTab(message("repl.title"), repl.view.getComponent());
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        } catch (IOException e) {
            e.printStackTrace(); // TODO - Do something magnificent
        } catch (ConfigurationException e) {
            JOptionPane.showMessageDialog(null,
                    message("config.error.replNotConfiguredMessage"),
                    message("config.error.replNotConfiguredTitle"),
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public void removeCurrentRepl() {
        int i = tabbedPane.getSelectedIndex();
        if (i > -1) {
            replList.remove(i).close();
            tabbedPane.removeTabAt(i);
        }
    }

    public void renameCurrentRepl() {
        int tabIndex = tabbedPane.getSelectedIndex();
        if (tabIndex > -1) {
            String oldTitle = tabbedPane.getTitleAt(tabIndex);

            // TODO - Should build my own small tool window dialog, positioned wherever the user clicked
            String newTitle = (String) JOptionPane.showInputDialog(
                    tabbedPane.getSelectedComponent(), null, message("repl.rename"),
                    JOptionPane.PLAIN_MESSAGE, null, null, oldTitle);
            if (newTitle != null) {
                tabbedPane.setTitleAt(tabIndex, newTitle);
            }
        }
    }


    private class Repl {
        public ConsoleView view;
        private ProcessHandler processHandler;

        public Repl() throws IOException, ConfigurationException {
            TextConsoleBuilder builder = TextConsoleBuilderFactory.getInstance().createBuilder(myProject);
            view = builder.getConsole();

            // TODO - What does the "help ID" give us??
            // view.setHelpId("Kurt's Help ID");

            processHandler = new ArcProcessHandler();
            ProcessTerminatedListener.attach(processHandler);
            processHandler.startNotify();
            view.attachToProcess(processHandler);

            tabbedPane.addTab(message("repl.title"), view.getComponent());


            final EditorEx ed = getEditor();
            ed.getContentComponent().addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent event) {
                    // TODO - This is probably wrong, actually, but it's a start...
                    ed.getCaretModel().moveToOffset(view.getContentSize());
                    ed.getScrollingModel().scrollToCaret(ScrollType.MAKE_VISIBLE);
                }
            });


            // TODO - Experimental... Play around with what widgetry we'd like to see in the REPL
            ed.getSettings().setSmartHome(true);
            ed.getSettings().setVariableInplaceRenameEnabled(true);
            ed.getSettings().setAnimatedScrolling(true);
            ed.getSettings().setFoldingOutlineShown(true);
            //e.getSettings().setLineNumbersShown(true);

            ed.getContentComponent().setComponentPopupMenu(popup.getComponent());
            view.getComponent().setComponentPopupMenu(popup.getComponent());

/*
            // TODO - Register TransferHandler with the *code* editor; we *never* want to move, only copy. Also, we'd like a custom icon.
            e.getContentComponent().setTransferHandler(new TransferHandler() {

                public Icon getVisualRepresentation(Transferable transferable) {
                    return ArcIcons.ARC_LARGE_ICON;
                }
            });
*/

/*
            ed.getContentComponent().setDropTarget(new DropTarget() {
                public synchronized void drop(DropTargetDropEvent event) {
                    try {
                        Transferable transferable = event.getTransferable();
                        String s = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                        view.print(s.trim(), ConsoleViewContentType.USER_INPUT);
                        event.dropComplete(true);

                    } catch (UnsupportedFlavorException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
*/


            // This is how we can add add'l tooling to our REPL window!
            // e.setHeaderComponent(new JLabel("Only a test"));

            // TODO - Ctrl-v (paste) should trim string before dropping
            // TODO - Drag-n-drop text isn't working from editor to console... why not?
            // Unfortunately, the following two lines do nothing to make either of the above things happen...
            //e.getSettings().setDndEnabled(true);
            //e.getContentComponent().getDropTarget().setActive(true);

        }

        public EditorEx getEditor() {
            EditorComponentImpl eci = (EditorComponentImpl) view.getPreferredFocusableComponent();
            return eci.getEditor();
        }

        public void close() {
            view.print("(quit)\r\n", ConsoleViewContentType.USER_INPUT);
            if (processHandler != null) {
                processHandler.destroyProcess();
            }
        }
    }
}
