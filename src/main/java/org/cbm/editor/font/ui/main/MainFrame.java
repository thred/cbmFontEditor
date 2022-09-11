package org.cbm.editor.font.ui.main;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.GUIAdapter;
import org.cbm.editor.font.ui.blocklist.BlockListPanelController;
import org.cbm.editor.font.ui.edit.EditPanelController;
import org.cbm.editor.font.ui.fontlist.FontListPanelController;
import org.cbm.editor.font.ui.util.TitledPanel;
import org.cbm.editor.font.util.UIUtils;

class MainFrame extends JFrame
{

    private static final long serialVersionUID = 7895421449482994080L;

    public MainFrame() throws HeadlessException
    {
        super("CBM Font Editor");

        GUIAdapter guiAdapter = Registry.get(GUIAdapter.class);

        setIconImage(Icon.COMMODORE.getImage());
        setLayout(new BorderLayout());

        setJMenuBar(Registry.get(MainMenuBarController.class).getView());

        final TitledPanel editTitledPanel =
            new TitledPanel("Edit", "Edit the selected block", Registry.get(EditPanelController.class).getView());

        editTitledPanel.setFocusForComponent(guiAdapter.getEditComponent());

        final TitledPanel blockListTitledPanel = new TitledPanel("Block", "Predefine blocks built of characters",
            Registry.get(BlockListPanelController.class).getView());

        blockListTitledPanel.setFocusForComponent(guiAdapter.getBlockComponent());

        final TitledPanel fontTitledPanel = new TitledPanel("Font", "Reference of the font as defined in memory",
            Registry.get(FontListPanelController.class).getView());

        fontTitledPanel.setFocusForComponent(guiAdapter.getFontComponent());

        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplitPane.setResizeWeight(0.5);
        verticalSplitPane.setTopComponent(blockListTitledPanel);
        verticalSplitPane.setBottomComponent(fontTitledPanel);
        //		verticalSplitPane.setOneTouchExpandable(true);
        verticalSplitPane.setBorder(null);

        JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        horizontalSplitPane.setResizeWeight(1);
        horizontalSplitPane.setLeftComponent(editTitledPanel);
        horizontalSplitPane.setRightComponent(verticalSplitPane);
        //		horizontalSplitPane.setOneTouchExpandable(true);
        horizontalSplitPane.setBorder(null);

        add(Registry.get(MainToolBarController.class).getView(), BorderLayout.NORTH);
        add(horizontalSplitPane, BorderLayout.CENTER);
        add(Registry.get(StatusBarController.class).getView(), BorderLayout.SOUTH);

        UIUtils.persistentLocation("mainFrame", this, null, 800, 600);
        UIUtils.persistentSplit("horizontalSplit", horizontalSplitPane, 550);
        UIUtils.persistentSplit("verticalSplit", verticalSplitPane, 300);
    }

}
