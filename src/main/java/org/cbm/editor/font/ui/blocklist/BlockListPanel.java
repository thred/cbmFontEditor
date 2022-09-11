package org.cbm.editor.font.ui.blocklist;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.BlockPropertyModifiedEvent;
import org.cbm.editor.font.model.events.BlockSwitchedEvent;
import org.cbm.editor.font.model.events.ProjectEvent;
import org.cbm.editor.font.ui.action.BlockNameEdit;
import org.cbm.editor.font.ui.action.NextBlockAction;
import org.cbm.editor.font.ui.action.PreviousBlockAction;
import org.cbm.editor.font.ui.util.Constraints;
import org.cbm.editor.font.ui.util.LoweredPanel;
import org.cbm.editor.font.util.Objects;
import org.cbm.editor.font.util.UIUtils;

public class BlockListPanel extends JPanel implements ActionListener, FocusListener
{

    private static final long serialVersionUID = -7525044265060335116L;

    private final ProjectAdapter projectAdapter;
    private final BlockAdapter blockAdapter;
    private final BlockListComponent blockList;
    private final JSplitPane splitPane;
    private final JTextField nameField;

    public BlockListPanel(JPanel blockPanel)
    {
        super(new BorderLayout());

        projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
        blockAdapter = Registry.get(BlockAdapter.class).bind(this);

        setOpaque(false);

        blockList = Registry.get(BlockListComponent.class);

        nameField = new JTextField();
        nameField.addActionListener(this);
        nameField.addFocusListener(this);
        nameField.setEnabled(false);

        final JPanel outer = new JPanel(new BorderLayout());
        final JPanel header = new LoweredPanel(new GridBagLayout());

        final Constraints c = new Constraints();
        header.add(UIUtils.createToolBarButton(Registry.get(PreviousBlockAction.class)), c);
        //		header.add(UIUtils.createLabel("Name of Block:"), c.next());
        header.add(nameField, c.next().weight(1).fillHorizontal());
        header.add(UIUtils.createToolBarButton(Registry.get(NextBlockAction.class)), c.next());

        outer.add(header, BorderLayout.NORTH);
        outer.add(blockPanel, BorderLayout.CENTER);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(outer);
        splitPane.setRightComponent(blockList);
        splitPane.setBorder(null);
        //		splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.5);

        add(splitPane, BorderLayout.CENTER);

        UIUtils.persistentSplit("blockListSplit", splitPane, 240);

        updateState();
    }

    private void updateState()
    {
        Project project = projectAdapter.getProject();
        Block block = blockAdapter.getBlock();

        nameField.setEnabled(project != null && block != null);

        if (project != null && block != null)
        {
            nameField.setText(block.getName());
        }
        else
        {
            nameField.setText("");
        }
    }

    public void handleEvent(ProjectEvent event)
    {
        updateState();
    }

    public void handleEvent(BlockSwitchedEvent event)
    {
        updateState();
    }

    public void handleEvent(BlockPropertyModifiedEvent event)
    {
        updateState();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    @Override
    public void focusGained(FocusEvent e)
    {
        // intentionally left blank
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    @Override
    public void focusLost(FocusEvent e)
    {
        updateState();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Block block = blockAdapter.getBlock();

        if (block == null)
        {
            return;
        }

        if (!Objects.equals(nameField.getText(), block.getName()))
        {
            Registry.execute(new BlockNameEdit(block, nameField.getText()));
        }
    }

}
