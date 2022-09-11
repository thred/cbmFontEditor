package org.cbm.editor.font.ui.fontlist;

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
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.FontSwitchedEvent;
import org.cbm.editor.font.model.events.ProjectSwitchedEvent;
import org.cbm.editor.font.ui.action.AddFontAction;
import org.cbm.editor.font.ui.action.DeleteFontAction;
import org.cbm.editor.font.ui.action.MoveFontDownAction;
import org.cbm.editor.font.ui.action.MoveFontUpAction;
import org.cbm.editor.font.ui.util.BottomToolBar;
import org.cbm.editor.font.ui.util.Colors;
import org.cbm.editor.font.ui.util.FocusableJScrollPane;
import org.cbm.editor.font.util.UIUtils;

public class FontListComponent extends JPanel implements ListSelectionListener
{

    private static final long serialVersionUID = -2351694771933229775L;

    private final ProjectAdapter projectAdapter;
    private final FontAdapter fontAdapter;
    private final JScrollPane listScrollPane;
    private final JList<Font> fontList;

    public FontListComponent()
    {
        super(new BorderLayout());

        projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
        fontAdapter = Registry.get(FontAdapter.class).bind(this);

        setOpaque(false);

        fontList = new JList<>(Registry.get(FontListModel.class));
        fontList.addListSelectionListener(this);
        fontList.setFocusable(false);
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listScrollPane = new FocusableJScrollPane(fontList);
        listScrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Colors.darker(getBackground(), 0.1f)));

        final JToolBar toolBar = new BottomToolBar();
        toolBar.setFloatable(false);

        toolBar.add(UIUtils.createToolBarButton(Registry.get(AddFontAction.class)));
        toolBar.add(UIUtils.createToolBarButton(Registry.get(DeleteFontAction.class)));
        toolBar.add(UIUtils.createToolBarButton(Registry.get(MoveFontUpAction.class)));
        toolBar.add(UIUtils.createToolBarButton(Registry.get(MoveFontDownAction.class)));

        add(listScrollPane, BorderLayout.CENTER);
        add(toolBar, BorderLayout.SOUTH);
    }

    public void handleEvent(ProjectSwitchedEvent event)
    {
        Project project = projectAdapter.getProject();

        fontList.setEnabled(project != null);
        fontList.clearSelection();
    }

    public void handleEvent(FontSwitchedEvent event)
    {
        fontList.setSelectedValue(fontAdapter.getFont(), true);
    }

    /**
     * {@inheritDoc}
     *
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent event)
    {
        fontAdapter.setFont(fontList.getSelectedValue());
    }

}
