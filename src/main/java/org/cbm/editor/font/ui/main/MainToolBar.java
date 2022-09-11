package org.cbm.editor.font.ui.main;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.ui.action.AcceptSelectionAction;
import org.cbm.editor.font.ui.action.ClearSelectionAction;
import org.cbm.editor.font.ui.action.CopyAction;
import org.cbm.editor.font.ui.action.CutAction;
import org.cbm.editor.font.ui.action.DismissSelectionAction;
import org.cbm.editor.font.ui.action.DrawToolAction;
import org.cbm.editor.font.ui.action.FillToolAction;
import org.cbm.editor.font.ui.action.PasteAction;
import org.cbm.editor.font.ui.action.PointerToolAction;
import org.cbm.editor.font.ui.action.RedoAction;
import org.cbm.editor.font.ui.action.SaveProjectAction;
import org.cbm.editor.font.ui.action.SelectionToolAction;
import org.cbm.editor.font.ui.action.UndoAction;
import org.cbm.editor.font.ui.action.VideoEmulationSetttingsAction;
import org.cbm.editor.font.util.UIUtils;

class MainToolBar extends JToolBar
{

    private static final long serialVersionUID = -1330314465679657601L;

    public MainToolBar()
    {
        super();

        setFloatable(false);
        setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        add(UIUtils.createToolBarButton(Registry.get(SaveProjectAction.class)));
        addSeparator();
        add(UIUtils.createToolBarButton(Registry.get(UndoAction.class)));
        add(UIUtils.createToolBarButton(Registry.get(RedoAction.class)));
        addSeparator();
        add(UIUtils.createToolBarButton(Registry.get(CutAction.class)));
        add(UIUtils.createToolBarButton(Registry.get(CopyAction.class)));
        add(UIUtils.createToolBarButton(Registry.get(PasteAction.class)));
        addSeparator();
        add(UIUtils.createToolBarToggleButton(Registry.get(PointerToolAction.class)));
        add(UIUtils.createToolBarToggleButton(Registry.get(SelectionToolAction.class)));
        add(UIUtils.createToolBarToggleButton(Registry.get(DrawToolAction.class)));
        add(UIUtils.createToolBarToggleButton(Registry.get(FillToolAction.class)));
        addSeparator();
        add(UIUtils.createToolBarButton(Registry.get(AcceptSelectionAction.class)));
        add(UIUtils.createToolBarButton(Registry.get(DismissSelectionAction.class)));
        add(UIUtils.createToolBarButton(Registry.get(ClearSelectionAction.class)));
        addSeparator();
        add(UIUtils.createToolBarButton(Registry.get(VideoEmulationSetttingsAction.class)));
    }

}
