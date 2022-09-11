package org.cbm.editor.font.ui.action;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;

public class DeleteFontEdit extends AbstractEdit
{

    private static final long serialVersionUID = 7228739635936283735L;

    private final Font font;
    private int index;

    public DeleteFontEdit(Font font)
    {
        super("Font deleted");

        this.font = font;
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#execute()
     */
    @Override
    public void execute()
    {
        final Project project = Registry.get(ProjectAdapter.class).getProject();

        index = project.removeFont(font);

        int indexToShow = index > 0 ? index - 1 : 0;
        Registry.get(FontAdapter.class).setFont(
            indexToShow < project.getNumberOfFonts() ? project.getFont(indexToShow) : null);

        project.setModified(true);
        Registry.get(StatusBarController.class).setMessage("Font deleted.");
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
     */
    @Override
    public void rollback()
    {
        final Project project = Registry.get(ProjectAdapter.class).getProject();

        project.addFont(index, font);
        project.setModified(true);
        Registry.get(StatusBarController.class).setMessage("Reverted deletion of block.");
    }

}
