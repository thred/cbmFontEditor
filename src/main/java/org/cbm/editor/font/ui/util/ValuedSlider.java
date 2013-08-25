package org.cbm.editor.font.ui.util;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ValuedSlider extends JPanel implements ChangeListener, ActionListener, FocusListener
{

	private static final long serialVersionUID = -938625661052410586L;

	private final JSlider slider;
	private final JTextField field;

	public ValuedSlider(final float min, final float max)
	{
		super(new GridBagLayout());

		slider = new JSlider((int) (min * 1000), (int) (max * 1000));
		slider.addChangeListener(this);
		slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));

		field = new JTextField(5);
		field.setHorizontalAlignment(SwingConstants.RIGHT);
		field.addActionListener(this);
		field.addFocusListener(this);

		final Constraints c = new Constraints();

		add(slider, c.fillHorizontal());
		add(field, c.next().fillHorizontal());

		setValue(0);
	}

	public JSlider getSlider()
	{
		return slider;
	}

	public JTextField getField()
	{
		return field;
	}

	public float getValue()
	{
		return (float) slider.getValue() / 1000;
	}

	public void setValue(final float value)
	{
		slider.setValue((int) (value * 1000));
		field.setText(String.format("%,.3f", getValue()));
	}

	/**
	 * Adds an <code>ActionListener</code> to the button.
	 * 
	 * @param l the <code>ActionListener</code> to be added
	 */
	public void addActionListener(final ActionListener l)
	{
		listenerList.add(ActionListener.class, l);
	}

	/**
	 * Removes an <code>ActionListener</code> from the button. If the listener is the currently set <code>Action</code>
	 * for the button, then the <code>Action</code> is set to <code>null</code>.
	 * 
	 * @param l the listener to be removed
	 */
	public void removeActionListener(final ActionListener l)
	{
		listenerList.remove(ActionListener.class, l);
	}

	protected void fireActionPerformed(final ActionEvent event)
	{
		// Guaranteed to return a non-null array
		final Object[] listeners = listenerList.getListenerList();
		ActionEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ActionListener.class)
			{
				// Lazily create the event:
				if (e == null)
				{
					String actionCommand = event.getActionCommand();
					if (actionCommand == null)
					{
						actionCommand = "mi gfreits nit";
					}
					e = new ActionEvent(ValuedSlider.this, ActionEvent.ACTION_PERFORMED, actionCommand,
							event.getWhen(), event.getModifiers());
				}
				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		}
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e)
	{
		updateValue();
	}

	private void updateValue()
	{
		final String text = field.getText();
		float value = 0;

		try
		{
			value = NumberFormat.getInstance().parse(String.valueOf(text)).floatValue();
		}
		catch (final ParseException e1)
		{
			// ignore
		}

		if (value != getValue())
		{
			setValue(value);

			fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
		}
	}

	@Override
	public void stateChanged(final ChangeEvent e)
	{
		field.setText(String.format("%,.3f", getValue()));

		fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
	}

	@Override
	public void focusGained(final FocusEvent e)
	{
		// ignore
	}

	@Override
	public void focusLost(final FocusEvent e)
	{
		updateValue();
	}

}
