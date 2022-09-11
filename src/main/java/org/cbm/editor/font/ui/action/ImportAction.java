package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.FontEvent;
import org.cbm.editor.font.model.events.ProjectEvent;
import org.cbm.editor.font.ui.main.MainFrameController;
import org.cbm.editor.font.util.Prefs;
import org.cbm.editor.font.util.UIUtils;

public class ImportAction extends AbstractAction
{

    private static final long serialVersionUID = 5788021882979972006L;

    private final ProjectAdapter projectAdapter;
    private final FontAdapter fontAdapter;

    public ImportAction()
    {
        super("Import...", Icon.IMPORT.getIcon());

        projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
        fontAdapter = Registry.get(FontAdapter.class).bind(this);

        putValue(SHORT_DESCRIPTION, "Imports font data from a file");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl shift I"));

        updateState();
    }

    public void handleEvent(ProjectEvent event)
    {
        updateState();
    }

    public void handleEvent(FontEvent event)
    {
        updateState();
    }

    private void updateState()
    {
        setEnabled(projectAdapter.getProject() != null && fontAdapter.getFont() != null);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent event)
    {
        final File path = new File(Prefs.get("import.path", System.getProperty("user.home")));
        final JFileChooser chooser = new JFileChooser(path);

        final int result = chooser.showOpenDialog(Registry.get(MainFrameController.class).getView());

        if (result != JFileChooser.APPROVE_OPTION)
        {
            return;
        }

        final File file = chooser.getSelectedFile();

        Prefs.set("import.path", file.getParent());

        try
        {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();

            try
            {
                final byte[] buffer = new byte[256];
                final InputStream in = new FileInputStream(file);

                try
                {
                    int length;

                    while ((length = in.read(buffer)) >= 0)
                    {
                        out.write(buffer, 0, length);
                    }
                }
                finally
                {
                    in.close();
                }
            }
            finally
            {
                out.close();
            }

            final byte[] bytes = out.toByteArray();

            if (bytes.length == 2048)
            {
                Registry.execute(new ImportEdit(bytes));
            }
            else if (bytes.length == 2050)
            {
                final byte[] trimmedBytes = new byte[2048];

                System.arraycopy(bytes, 2, trimmedBytes, 0, bytes.length - 2);

                Registry.execute(new ImportEdit(trimmedBytes));
            }
            else
            {
                UIUtils.error(Registry.get(MainFrameController.class).getView(), "Import...",
                    String.format("Invalid font.\nWrong size: %d bytes instead of 2048 or 2050 bytes", bytes.length));
            }
        }
        catch (final IOException e)
        {
            e.printStackTrace(System.err);

            UIUtils.error(Registry.get(MainFrameController.class).getView(), "Import...",
                "Importing failed: " + e.getMessage());
        }
    }
}
