package org.cbm.editor.font.ui.main;

import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.ui.action.AcceptSelectionAction;
import org.cbm.editor.font.ui.action.AddBlockAction;
import org.cbm.editor.font.ui.action.AddFontAction;
import org.cbm.editor.font.ui.action.ClearSelectionAction;
import org.cbm.editor.font.ui.action.CloseProjectAction;
import org.cbm.editor.font.ui.action.CopyAction;
import org.cbm.editor.font.ui.action.CreateCharacterBlocksAction;
import org.cbm.editor.font.ui.action.CutAction;
import org.cbm.editor.font.ui.action.DeleteBlockAction;
import org.cbm.editor.font.ui.action.DeleteFontAction;
import org.cbm.editor.font.ui.action.DismissSelectionAction;
import org.cbm.editor.font.ui.action.DrawToolAction;
import org.cbm.editor.font.ui.action.ExitAction;
import org.cbm.editor.font.ui.action.ExportAction;
import org.cbm.editor.font.ui.action.FillToolAction;
import org.cbm.editor.font.ui.action.ImportAction;
import org.cbm.editor.font.ui.action.MoveBlockDownAction;
import org.cbm.editor.font.ui.action.MoveBlockUpAction;
import org.cbm.editor.font.ui.action.MoveFontDownAction;
import org.cbm.editor.font.ui.action.MoveFontUpAction;
import org.cbm.editor.font.ui.action.NewProjectAction;
import org.cbm.editor.font.ui.action.NextBlockAction;
import org.cbm.editor.font.ui.action.NextFontAction;
import org.cbm.editor.font.ui.action.OpenProjectAction;
import org.cbm.editor.font.ui.action.PasteAction;
import org.cbm.editor.font.ui.action.PointerToolAction;
import org.cbm.editor.font.ui.action.PreviousBlockAction;
import org.cbm.editor.font.ui.action.PreviousFontAction;
import org.cbm.editor.font.ui.action.RedoAction;
import org.cbm.editor.font.ui.action.SaveProjectAction;
import org.cbm.editor.font.ui.action.SaveProjectAsAction;
import org.cbm.editor.font.ui.action.SelectionToolAction;
import org.cbm.editor.font.ui.action.UndoAction;
import org.cbm.editor.font.ui.action.VideoEmulationSetttingsAction;
import org.cbm.editor.font.util.UIUtils;

class MainMenuBar extends JMenuBar
{

    private static final long serialVersionUID = 3705783952058650018L;

    public MainMenuBar()
    {
        super();

        final JMenu file = UIUtils.createMenu("File", KeyEvent.VK_F);
        file.add(Registry.get(NewProjectAction.class));
        file.add(Registry.get(OpenProjectAction.class));
        file.add(Registry.get(SaveProjectAction.class));
        file.add(Registry.get(SaveProjectAsAction.class));
        file.add(Registry.get(CloseProjectAction.class));
        file.addSeparator();
        file.add(Registry.get(VideoEmulationSetttingsAction.class));
        file.addSeparator();
        Registry.get(OpenRecentProject.class).init(file);
        file.addSeparator();
        file.add(Registry.get(ExitAction.class));
        add(file);

        final JMenu edit = UIUtils.createMenu("Edit", KeyEvent.VK_E);
        edit.add(Registry.get(UndoAction.class));
        edit.add(Registry.get(RedoAction.class));
        edit.addSeparator();
        edit.add(Registry.get(CutAction.class));
        edit.add(Registry.get(CopyAction.class));
        edit.add(Registry.get(PasteAction.class));
        edit.addSeparator();
        edit.add(new JCheckBoxMenuItem(Registry.get(PointerToolAction.class)));
        edit.add(new JCheckBoxMenuItem(Registry.get(SelectionToolAction.class)));
        edit.add(new JCheckBoxMenuItem(Registry.get(DrawToolAction.class)));
        edit.add(new JCheckBoxMenuItem(Registry.get(FillToolAction.class)));
        edit.addSeparator();
        edit.add(new JCheckBoxMenuItem(Registry.get(AcceptSelectionAction.class)));
        edit.add(new JCheckBoxMenuItem(Registry.get(DismissSelectionAction.class)));
        edit.add(new JCheckBoxMenuItem(Registry.get(ClearSelectionAction.class)));
        add(edit);

        final JMenu font = UIUtils.createMenu("Font", KeyEvent.VK_B);
        font.add(Registry.get(AddFontAction.class));
        font.add(Registry.get(DeleteFontAction.class));
        font.addSeparator();
        font.add(Registry.get(MoveFontUpAction.class));
        font.add(Registry.get(MoveFontDownAction.class));
        font.addSeparator();
        font.add(Registry.get(NextFontAction.class));
        font.add(Registry.get(PreviousFontAction.class));
        font.addSeparator();
        font.add(Registry.get(ImportAction.class));
        font.add(Registry.get(ExportAction.class));

        add(font);

        final JMenu block = UIUtils.createMenu("Block", KeyEvent.VK_B);
        block.add(Registry.get(AddBlockAction.class));
        block.add(Registry.get(CreateCharacterBlocksAction.class));
        block.add(Registry.get(DeleteBlockAction.class));
        block.addSeparator();
        block.add(Registry.get(MoveBlockUpAction.class));
        block.add(Registry.get(MoveBlockDownAction.class));
        block.addSeparator();
        block.add(Registry.get(NextBlockAction.class));
        block.add(Registry.get(PreviousBlockAction.class));

        add(block);

        final JMenu help = UIUtils.createMenu("Help", KeyEvent.VK_H);
        add(help);

        setBorderPainted(false);
    }

}
