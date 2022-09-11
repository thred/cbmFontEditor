package org.cbm.editor.font.ui.fontlist;

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
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.FontPropertyModifiedEvent;
import org.cbm.editor.font.model.events.FontSwitchedEvent;
import org.cbm.editor.font.model.events.ProjectEvent;
import org.cbm.editor.font.ui.action.FontNameEdit;
import org.cbm.editor.font.ui.action.NextFontAction;
import org.cbm.editor.font.ui.action.PreviousFontAction;
import org.cbm.editor.font.ui.util.Constraints;
import org.cbm.editor.font.ui.util.LoweredPanel;
import org.cbm.editor.font.util.Objects;
import org.cbm.editor.font.util.UIUtils;

public class FontListPanel extends JPanel implements ActionListener, FocusListener
{

    private static final long serialVersionUID = -7525044265060335116L;

    private final ProjectAdapter projectAdapter;
    private final FontAdapter fontAdapter;
    private final FontListComponent fontList;
    private final JSplitPane splitPane;
    private final JTextField nameField;

    public FontListPanel(JPanel blockPanel)
    {
        super(new BorderLayout());

        projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
        fontAdapter = Registry.get(FontAdapter.class).bind(this);

        //		setOpaque(false);

        fontList = Registry.get(FontListComponent.class);

        nameField = new JTextField();
        nameField.addActionListener(this);
        nameField.addFocusListener(this);
        nameField.setEnabled(false);

        final JPanel outer = new JPanel(new BorderLayout());
        final JPanel header = new LoweredPanel(new GridBagLayout());

        final Constraints c = new Constraints();
        header.add(UIUtils.createToolBarButton(Registry.get(PreviousFontAction.class)), c);
        //		header.add(UIUtils.createLabel("Name of Font:"), c.next());
        header.add(nameField, c.next().weight(1).fillHorizontal());
        header.add(UIUtils.createToolBarButton(Registry.get(NextFontAction.class)), c.next());

        outer.add(header, BorderLayout.NORTH);
        outer.add(blockPanel, BorderLayout.CENTER);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(outer);
        splitPane.setRightComponent(fontList);
        splitPane.setBorder(null);
        //		splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.5);

        add(splitPane, BorderLayout.CENTER);

        UIUtils.persistentSplit("fontListSplit", splitPane, 240);

        updateState();
    }

    private void updateState()
    {
        Project project = projectAdapter.getProject();
        Font font = fontAdapter.getFont();

        nameField.setEnabled(project != null && font != null);

        if (project != null && font != null)
        {
            nameField.setText(font.getName());
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

    public void handleEvent(FontSwitchedEvent event)
    {
        updateState();
    }

    public void handleEvent(FontPropertyModifiedEvent event)
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
        Font font = fontAdapter.getFont();

        if (font == null)
        {
            return;
        }

        if (!Objects.equals(nameField.getText(), font.getName()))
        {
            Registry.execute(new FontNameEdit(font, nameField.getText()));
        }
    }

}
