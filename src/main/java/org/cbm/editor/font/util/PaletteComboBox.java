package org.cbm.editor.font.util;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

public class PaletteComboBox extends JComboBox<Palette>
{

    private static final long serialVersionUID = -8892107293666868905L;

    protected class ColorCellRenderer extends DefaultListCellRenderer
    {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index,
            final boolean isSelected, final boolean cellHasFocus)
        {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            final Palette color = (Palette) value;

            setText(color.getName());
            setIcon(color.getIcon());

            return this;
        }
    }

    public PaletteComboBox(final Palette color)
    {
        super(Palette.values());

        setRenderer(new ColorCellRenderer());
        setSelectedItem(color);
    }

    public void set(final Palette color)
    {
        setSelectedItem(color);
    }

    public Palette get()
    {
        return (Palette) getSelectedItem();
    }

}
