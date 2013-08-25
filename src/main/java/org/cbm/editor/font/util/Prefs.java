package org.cbm.editor.font.util;

import java.util.prefs.Preferences;

public class Prefs
{

	private static final Preferences PREFS = Preferences.userNodeForPackage(Prefs.class);

	public static boolean exists(final String key)
	{
		return PREFS.get(key, null) != null;
	}

	public static int get(final String key, final int defaultValue)
	{
		return PREFS.getInt(key, defaultValue);
	}

	public static boolean set(final String key, final int value)
	{
		if ((exists(key)) && (Objects.equals(PREFS.getInt(key, 0), value)))
		{
			return false;
		}

		PREFS.putInt(key, value);

		return true;
	}

	public static float get(final String key, final float defaultValue)
	{
		return PREFS.getFloat(key, defaultValue);
	}

	public static boolean set(final String key, final float value)
	{
		if ((exists(key)) && (Objects.equals(PREFS.getFloat(key, 0), value)))
		{
			return false;
		}

		PREFS.putFloat(key, value);

		return true;
	}

	public static String get(final String key, final String defaultValue)
	{
		return PREFS.get(key, defaultValue);
	}

	public static boolean set(final String key, final String value)
	{
		if ((exists(key)) && (Objects.equals(PREFS.get(key, null), value)))
		{
			return false;
		}

		PREFS.put(key, value);

		return true;
	}

}
