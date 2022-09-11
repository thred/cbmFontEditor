package org.cbm.editor.font.ui.action;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;

public class AddFontEdit extends AbstractEdit
{

    private static final long serialVersionUID = 6650633640271610623L;

    private final int index;

    private Font font = null;

    public AddFontEdit(final int index)
    {
        super("Font added");

        this.index = index;
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#execute()
     */
    @Override
    public void execute()
    {
        final Project project = Registry.get(ProjectAdapter.class).getProject();

        font = new Font();

        project.addFont(index, font);
        Registry.get(FontAdapter.class).setFont(font);
        project.setModified(true);

        Registry.get(StatusBarController.class).setMessage("Font added.");
        Registry.get(FontAdapter.class).setFont(font);
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
     */
    @Override
    public void rollback()
    {
        final Project project = Registry.get(ProjectAdapter.class).getProject();

        project.removeFont(font);
        project.setModified(true);

        //		Registry.get(BlockListComponent.class).clearSelection();

        Registry.get(StatusBarController.class).setMessage("Reverted addition of font.");
    }

}
