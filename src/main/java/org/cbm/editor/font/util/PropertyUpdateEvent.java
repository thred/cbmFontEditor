package org.cbm.editor.font.util;

public class PropertyUpdateEvent
{

	private final Object object;
	private final String name;
	private final Object value;

	public PropertyUpdateEvent(Object object, String name, Object value)
	{
		super();

		this.object = object;
		this.name = name;
		this.value = value;
	}

	public Object getObject()
	{
		return object;
	}

	public String getName()
	{
		return name;
	}

	public Object getValue()
	{
		return value;
	}

	public boolean is(Object object, String name)
	{
		return (this.name.equals(name));
	}

	public boolean is(String name)
	{
		return (this.name.equals(name));
	}

}
