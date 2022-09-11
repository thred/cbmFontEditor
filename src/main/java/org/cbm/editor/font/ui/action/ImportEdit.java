package org.cbm.editor.font.ui.action;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;

public class ImportEdit extends AbstractEdit
{

    private static final long serialVersionUID = 8909409178192874523L;

    private final byte[] bytes;
    private final Font font;

    private byte[] backup;

    public ImportEdit(final byte[] bytes)
    {
        super("Import");

        this.bytes = bytes;

        font = Registry.get(FontAdapter.class).getFont();
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#execute()
     */
    @Override
    public void execute()
    {
        final Project project = Registry.get(ProjectAdapter.class).getProject();

        backup = font.getBytes(0, new byte[2048]);
        font.setBytes(0, bytes);
        project.setModified(true);
        Registry.get(StatusBarController.class).setMessage("Imported font.");
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
     */
    @Override
    public void rollback()
    {
        final Project project = Registry.get(ProjectAdapter.class).getProject();

        font.setBytes(0, backup);
        project.setModified(true);
        Registry.get(StatusBarController.class).setMessage("Reverted import of font.");
    }

}
