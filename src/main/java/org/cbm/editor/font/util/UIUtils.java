package org.cbm.editor.font.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.border.Border;

import org.cbm.editor.font.Icon;

public class UIUtils
{

	public static JLabel createLabel(final String text)
	{
		final JLabel label = new JLabel();

		label.setText(text);

		return label;
	}

	public static JButton createButton(final String id, final Object onObject, final String text, final int mnemonic)
	{
		final JButton button = new JButton(text);

		button.setMnemonic(mnemonic);

		return button;
	}

	public static JButton createButton(final Icon icon)
	{
		final JButton button = new JButton(icon.getIcon());

//		button.setFocusPainted(false);

		return button;
	}

	public static JButton createToolBarButton(final Action action)
	{
		final JButton button = new JButton(action);

		button.setFocusPainted(false);
		button.setFocusable(false);
		button.setBorderPainted(false);
		//		button.setHorizontalTextPosition(SwingConstants.CENTER);
		//		button.setVerticalTextPosition(SwingConstants.BOTTOM);
		//		button.setFont(button.getFont().deriveFont(button.getFont().getSize() * 0.8f));
		button.setHideActionText(true);

		return button;
	}

	public static JToggleButton createToolBarToggleButton(final Action action)
	{
		final JToggleButton button = new JToggleButton(action);

		button.setFocusPainted(false);
		button.setFocusable(false);
		//		button.setHorizontalTextPosition(SwingConstants.CENTER);
		//		button.setVerticalTextPosition(SwingConstants.BOTTOM);
		//		button.setFont(button.getFont().deriveFont(button.getFont().getSize() * 0.8f));
		button.setHideActionText(true);

		return button;
	}

	public static JCheckBox createCheckBox(final String name, final boolean selected)
	{
		final JCheckBox checkBox = new JCheckBox(name);

		checkBox.setSelected(selected);

		return checkBox;
	}

	public static JMenu createMenu(final String name, final int mnemonic)
	{
		final JMenu menu = new JMenu(name);

		menu.setMnemonic(mnemonic);

		return menu;
	}

	public static Border createTitledBorder(final String title)
	{
		return BorderFactory.createTitledBorder(title);
	}

	public static void center(final JDialog dialog, final JFrame frame)
	{
		final Dimension dialogSize = dialog.getSize();
		final Point frameLocation = frame.getLocation();
		final Dimension frameSize = frame.getSize();

		dialog.setLocation(new Point(frameLocation.x + (frameSize.width - dialogSize.width) / 2, frameLocation.y
		    + (frameSize.height - dialogSize.height) / 2));
	}

	public static void persistentSplit(final String name, final JSplitPane pane, final int dividerLocation)
	{
		final String prefix = name + ":";

		final int location = Prefs.get(prefix + "location", dividerLocation);

		pane.setDividerLocation(location);

		pane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener()
		{

			@Override
			public void propertyChange(final PropertyChangeEvent evt)
			{
				Prefs.set(prefix + "location", pane.getDividerLocation());
			}

		});
	}

	public static void persistentLocation(final String name, final Component component, final Component relativeTo, int width, int height)
	{
		final String prefix = name + ":";

		int x, y;

		if (relativeTo == null)
		{
			final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			x = (screenSize.width - width) / 2;
			y = (screenSize.height - height) / 2;
		}
		else
		{
			final Point location = relativeTo.getLocation();
			final Dimension size = relativeTo.getSize();

			x = location.x + (size.width - width) / 2;
			y = location.y + (size.height - height) / 2;
		}

		x = Prefs.get(prefix + "x", x);
		y = Prefs.get(prefix + "y", y);
		width = Prefs.get(prefix + "width", width);
		height = Prefs.get(prefix + "height", height);

		component.setLocation(x, y);
		component.setSize(width, height);

		component.addComponentListener(new ComponentAdapter()
		{

			/**
			 * @see java.awt.event.ComponentAdapter#componentMoved(java.awt.event.ComponentEvent)
			 */
			@Override
			public void componentMoved(final ComponentEvent e)
			{
				final Point location = ((Component) e.getSource()).getLocation();

				Prefs.set(prefix + "x", location.x);
				Prefs.set(prefix + "y", location.y);
			}

			/**
			 * @see java.awt.event.ComponentAdapter#componentResized(java.awt.event.ComponentEvent)
			 */
			@Override
			public void componentResized(final ComponentEvent e)
			{
				final Dimension dimension = ((Component) e.getSource()).getSize();

				Prefs.set(prefix + "width", dimension.width);
				Prefs.set(prefix + "height", dimension.height);
			}

		});
	}

	public static boolean confirm(final Component parent, final String title, final String message)
	{
		final int result = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION);

		return (result == JOptionPane.YES_OPTION);
	}

	public static int confirmOrAbort(final Component parent, final String title, final String message)
	{
		return JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_CANCEL_OPTION);
	}

	public static void error(final Component parent, final String title, final String message)
	{
		JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
	}

}
