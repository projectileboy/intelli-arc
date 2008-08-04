package com.bitbakery.plugin.arc.repl;

import com.bitbakery.plugin.arc.ArcIcons;
import static com.bitbakery.plugin.arc.ArcStrings.message;
import com.bitbakery.plugin.arc.psi.Def;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.editor.impl.EditorComponentImpl;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.PsiReferenceProvider;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.impl.source.resolve.reference.ReferenceType;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.ui.JScrollPane2;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ReplToolWindow implements ProjectComponent {

    private Project myProject;
    private ConsoleView view;
    private ProcessHandler processHandler;

    public ReplToolWindow(Project project) {
        myProject = project;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                disposeComponent();
            }
        });

        // TODO - This doesn't belong here... we should have some sort of "ArcProjectLoader"...
        ReferenceProvidersRegistry.getInstance(project).registerReferenceProvider(Def.class, new PsiReferenceProvider() {
            @NotNull
            public PsiReference[] getReferencesByElement(PsiElement psiElement) {
                return new PsiReference[0];  //To change body of implemented methods use File | Settings | File Templates.
            }

            @NotNull
            public PsiReference[] getReferencesByElement(PsiElement psiElement, ReferenceType referenceType) {
                return new PsiReference[0];  //To change body of implemented methods use File | Settings | File Templates.
            }

            @NotNull
            public PsiReference[] getReferencesByString(String s, PsiElement psiElement, ReferenceType referenceType, int i) {
                return new PsiReference[0];  //To change body of implemented methods use File | Settings | File Templates.
            }

            public void handleEmptyContext(PsiScopeProcessor psiScopeProcessor, PsiElement psiElement) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    public void projectOpened() {
        try {
            initToolWindow();
        } catch (Exception e) {
            // TODO - Handle me for real...
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void projectClosed() {
    }

    public void initComponent() {
    }

    public void disposeComponent() {
        ToolWindowManager.getInstance(myProject).unregisterToolWindow(message("repl.toolWindowId"));
        if (processHandler != null) {
            processHandler.destroyProcess();
        }
    }

    public void requestFocus() {
        view.getPreferredFocusableComponent().requestFocusInWindow();
    }

    public void writeToRepl(String s) {
        view.print(s + "\r\n", ConsoleViewContentType.USER_INPUT);
    }

    @NotNull
    public String getComponentName() {
        return "ReplToolWindow.ArcPlugin";
    }

    private void initToolWindow() throws ExecutionException, IOException {
        if (myProject != null) {
            TextConsoleBuilder builder = TextConsoleBuilderFactory.getInstance().createBuilder(myProject);
            view = builder.getConsole();

            // TODO - What does the "help ID" give us??
            // view.setHelpId("Kurt's Help ID");

            processHandler = new ArcProcessHandler(myProject);
            ProcessTerminatedListener.attach(processHandler);
            processHandler.startNotify();
            view.attachToProcess(processHandler);

            ToolWindowManager manager = ToolWindowManager.getInstance(myProject);
            ToolWindow window = manager.registerToolWindow(message("repl.title"), view.getComponent(), ToolWindowAnchor.BOTTOM);
            window.setIcon(ArcIcons.ARC_REPL_ICON);

            final EditorImpl repl = getReplContent();
            repl.getContentComponent().addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent event) {
                    repl.getCaretModel().moveToOffset(view.getContentSize());
                }
            });
            repl.getContentComponent().addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent event) {
                    repl.getCaretModel().moveToOffset(view.getContentSize());
                }

                public void focusLost(FocusEvent event) {
                    // Do nothing
                }
            });

/*
            // TODO - Register TransferHandler with the *code* editor; we *never* want to move, only copy. Also, we'd like a custom icon.
            e.getContentComponent().setTransferHandler(new TransferHandler() {

                public Icon getVisualRepresentation(Transferable transferable) {
                    return ArcIcons.ARC_LARGE_ICON;
                }
            });
*/

            repl.getContentComponent().setDropTarget(new DropTarget() {
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

            //e.getSettings().setLineNumbersShown(true);

            // This is how we can add add'l tooling to our REPL window!
            // e.setHeaderComponent(new JLabel("Only a test"));

            // TODO - Ctrl-v (paste) should trim string before dropping
            // TODO - Drag-n-drop text isn't working from editor to console... why not?
            // Unfortunately, the following two lines do nothing to make either of the above things happen...
            //e.getSettings().setDndEnabled(true);
            //e.getContentComponent().getDropTarget().setActive(true);
        }
    }

    /**
     * A bit of a hack; needed until JetBrains opens up the ConsoleView class.
     */
    private EditorImpl getReplContent() {
        final JPanel editorPanel = (JPanel) view.getComponent().getComponent(0);
        JScrollPane2 scrollPane = (JScrollPane2) editorPanel.getComponents()[1];
        JViewport port = (JViewport) scrollPane.getComponents()[0];
        EditorComponentImpl ed = (EditorComponentImpl) port.getComponents()[0];
        return ed.getEditor();
    }
}
