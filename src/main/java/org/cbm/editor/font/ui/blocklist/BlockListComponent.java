package org.cbm.editor.font.ui.blocklist;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.BlockSwitchedEvent;
import org.cbm.editor.font.model.events.ProjectSwitchedEvent;
import org.cbm.editor.font.ui.action.AddBlockAction;
import org.cbm.editor.font.ui.action.DeleteBlockAction;
import org.cbm.editor.font.ui.action.MoveBlockDownAction;
import org.cbm.editor.font.ui.action.MoveBlockUpAction;
import org.cbm.editor.font.ui.util.BottomToolBar;
import org.cbm.editor.font.ui.util.Colors;
import org.cbm.editor.font.ui.util.FocusableJScrollPane;
import org.cbm.editor.font.util.UIUtils;

public class BlockListComponent extends JPanel implements ListSelectionListener
{

    private static final long serialVersionUID = -2351694771933229775L;

    private final ProjectAdapter projectAdapter;
    private final BlockAdapter blockAdapter;
    private final JScrollPane listScrollPane;
    private final JList<Block> blockList;

    public BlockListComponent()
    {
        super(new BorderLayout());

        projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
        blockAdapter = Registry.get(BlockAdapter.class).bind(this);

        setOpaque(false);

        blockList = new JList<>(Registry.get(BlockListModel.class));
        blockList.addListSelectionListener(this);
        blockList.setFocusable(false);
        blockList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listScrollPane = new FocusableJScrollPane(blockList);
        listScrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Colors.darker(getBackground(), 0.1f)));

        final JToolBar toolBar = new BottomToolBar();
        toolBar.setFloatable(false);

        toolBar.add(UIUtils.createToolBarButton(Registry.get(AddBlockAction.class)));
        toolBar.add(UIUtils.createToolBarButton(Registry.get(DeleteBlockAction.class)));
        toolBar.add(UIUtils.createToolBarButton(Registry.get(MoveBlockUpAction.class)));
        toolBar.add(UIUtils.createToolBarButton(Registry.get(MoveBlockDownAction.class)));

        add(listScrollPane, BorderLayout.CENTER);
        add(toolBar, BorderLayout.SOUTH);
    }

    public void handleEvent(ProjectSwitchedEvent event)
    {
        Project project = projectAdapter.getProject();

        blockList.setEnabled(project != null);
        blockList.clearSelection();
    }

    public void handleEvent(BlockSwitchedEvent event)
    {
        blockList.setSelectedValue(blockAdapter.getBlock(), true);
    }

    /**
     * {@inheritDoc}
     *
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent event)
    {
        blockAdapter.setBlock(blockList.getSelectedValue());
    }

}
