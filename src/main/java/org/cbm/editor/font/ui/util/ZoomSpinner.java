package org.cbm.editor.font.ui.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractSpinnerModel;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;

public class ZoomSpinner extends JSpinner
{

	private static final long serialVersionUID = -4623445201957798466L;

	private static final Map<Double, Double> SPECIAL_ZOOM_IN = new HashMap<Double, Double>();
	private static final Map<Double, Double> SPECIAL_ZOOM_OUT = new HashMap<Double, Double>();

	static
	{
		SPECIAL_ZOOM_IN.put(Double.valueOf(1), Double.valueOf(1.5));
		SPECIAL_ZOOM_IN.put(Double.valueOf(1.5), Double.valueOf(2));
		SPECIAL_ZOOM_IN.put(Double.valueOf(2), Double.valueOf(3));
		SPECIAL_ZOOM_IN.put(Double.valueOf(3), Double.valueOf(4));
		SPECIAL_ZOOM_IN.put(Double.valueOf(4), Double.valueOf(5));
		SPECIAL_ZOOM_IN.put(Double.valueOf(5), Double.valueOf(6));
		SPECIAL_ZOOM_IN.put(Double.valueOf(6), Double.valueOf(8));

		SPECIAL_ZOOM_OUT.put(Double.valueOf(8), Double.valueOf(6));
		SPECIAL_ZOOM_OUT.put(Double.valueOf(6), Double.valueOf(5));
		SPECIAL_ZOOM_OUT.put(Double.valueOf(5), Double.valueOf(4));
		SPECIAL_ZOOM_OUT.put(Double.valueOf(4), Double.valueOf(3));
		SPECIAL_ZOOM_OUT.put(Double.valueOf(3), Double.valueOf(2));
		SPECIAL_ZOOM_OUT.put(Double.valueOf(2), Double.valueOf(1.5));
		SPECIAL_ZOOM_OUT.put(Double.valueOf(1.5), Double.valueOf(1));

	}
	private static final double MINIMUM = 0.25;
	private static final double MAXIMUM = 64;

	protected static class ZoomSpinnerModel extends AbstractSpinnerModel
	{

		private static final long serialVersionUID = 3811491024601496997L;

		private Double value = Double.valueOf(1);

		/**
		 * @see javax.swing.SpinnerModel#getValue()
		 */
		@Override
		public Object getValue()
		{
			return value;
		}

		/**
		 * @see javax.swing.SpinnerModel#setValue(java.lang.Object)
		 */
		@Override
		public void setValue(final Object value)
		{
			if (value instanceof Number)
			{
				this.value = validate(((Number) value).doubleValue());
			}
			else
			{
				try
				{
					this.value = validate(NumberFormat.getInstance().parse(String.valueOf(value)).doubleValue());
				}
				catch (final ParseException e)
				{
					// ignore
				}
			}

			fireStateChanged();
		}

		/**
		 * @see javax.swing.SpinnerModel#getNextValue()
		 */
		@Override
		public Object getNextValue()
		{
			Double nextValue = SPECIAL_ZOOM_IN.get(value);

			if (nextValue == null)
			{
				nextValue = value * 2;
			}

			return (nextValue <= MAXIMUM) ? nextValue : MAXIMUM;
		}

		/**
		 * @see javax.swing.SpinnerModel#getPreviousValue()
		 */
		@Override
		public Object getPreviousValue()
		{
			Double previousValue = SPECIAL_ZOOM_OUT.get(value);

			if (previousValue == null)
			{
				previousValue = value / 2;
			}

			return (previousValue >= MINIMUM) ? previousValue : MINIMUM;
		}

	}

	protected static class ZoomEditor extends DefaultEditor
	{

		private static final long serialVersionUID = -5905023527875065328L;

		protected ZoomEditor(final JSpinner spinner)
		{
			super(spinner);

			final JFormattedTextField textField = getTextField();

			textField.setColumns(5);
			textField.setHorizontalAlignment(SwingConstants.RIGHT);
			textField.setEditable(true);
			textField.setFormatterFactory(new DefaultFormatterFactory(new DefaultFormatter()
			{

				private static final long serialVersionUID = 3115837157819981956L;

				/**
				 * @see javax.swing.text.DefaultFormatter#valueToString(java.lang.Object)
				 */
				@Override
				public String valueToString(final Object value) throws ParseException
				{
					return (value != null) ? String.format("%.0f %%", ((Number) value).doubleValue() * 100) : "?";
				}

				/**
				 * @see javax.swing.text.DefaultFormatter#stringToValue(java.lang.String)
				 */
				@Override
				public Object stringToValue(final String text) throws ParseException
				{
					try
					{
						return Double.valueOf(NumberFormat.getNumberInstance().parse(text).doubleValue() / 100);
					}
					catch (final ParseException e)
					{
						// ignore
					}

					return null;
				}

			}));

		}

	}

	public ZoomSpinner()
	{
		super(new ZoomSpinnerModel());

		setEditor(new ZoomEditor(this));
		setFocusable(false);
	}

	public double getZoom()
	{
		return ((Number) getValue()).doubleValue();
	}

	public void next()
	{
		setValue(((ZoomSpinnerModel) getModel()).getNextValue());
	}

	public void previous()
	{
		setValue(((ZoomSpinnerModel) getModel()).getPreviousValue());
	}

	public static Double validate(final Double value)
	{
		if (value.doubleValue() < MINIMUM)
		{
			return Double.valueOf(MINIMUM);
		}

		if (value.doubleValue() > MAXIMUM)
		{
			return Double.valueOf(MAXIMUM);
		}

		return value;
	}

}
