package org.cbm.editor.font.util;

public class Strings
{

	public static String toHex(final byte value)
	{
		String s = Integer.toHexString(0xff & value);

		while (s.length() < 2)
		{
			s = "0" + s;
		}

		return "0x" + s;
	}

	public static Object toHex(final int value)
	{
		String s = Integer.toHexString(value);

		while (s.length() < 4)
		{
			s = "0" + s;
		}

		return "0x" + s;
	}
}
