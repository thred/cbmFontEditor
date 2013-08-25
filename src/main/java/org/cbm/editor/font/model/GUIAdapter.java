package org.cbm.editor.font.model;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Collection;
import java.util.HashSet;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.events.GUIComponentActivatedEvent;
import org.cbm.editor.font.model.events.GUIToolEvent;
import org.cbm.editor.font.ui.block.BlockComponent;
import org.cbm.editor.font.ui.blocklist.BlockListPanelController;
import org.cbm.editor.font.ui.edit.EditPanelController;
import org.cbm.editor.font.ui.fontlist.FontListPanelController;
import org.cbm.editor.font.util.Events;

public class GUIAdapter implements FocusListener
{

	private final Collection<Object> consumers;

	private final BlockComponent editComponent;
	private final BlockComponent blockComponent;
	private final BlockComponent fontComponent;

	private GUIComponentType activeComponentType = GUIComponentType.NONE;
	private Tool tool = Tool.POINTER;
	private Tool lastTool = Tool.DRAW;

	public GUIAdapter()
	{
		super();

		consumers = new HashSet<Object>();

		editComponent = Registry.get(EditPanelController.class).getBlockPanelController().getBlockComponent();
		editComponent.addFocusListener(this);

		blockComponent = Registry.get(BlockListPanelController.class).getBlockPanelController().getBlockComponent();
		blockComponent.addFocusListener(this);

		fontComponent = Registry.get(FontListPanelController.class).getBlockPanelController().getBlockComponent();
		fontComponent.addFocusListener(this);
	}

	public GUIAdapter bind(Object consumer)
	{
		synchronized (consumers)
		{
			consumers.add(consumer);

			Events.bind(this, consumer);
		}

		return this;
	}

	public GUIAdapter unbind(Object consumer)
	{
		synchronized (consumers)
		{
			consumers.remove(consumer);

			Events.unbind(this, consumer);
		}

		return this;
	}

	public BlockComponent getEditComponent()
	{
		return editComponent;
	}

	public BlockComponent getBlockComponent()
	{
		return blockComponent;
	}

	public BlockComponent getFontComponent()
	{
		return fontComponent;
	}

	public GUIComponentType getActiveComponentType()
	{
		return activeComponentType;
	}

	public void setActiveComponentType(GUIComponentType activeComponentType)
	{
		if (this.activeComponentType != activeComponentType)
		{
			this.activeComponentType = activeComponentType;

			Events.fire(this, new GUIComponentActivatedEvent(activeComponentType));
		}
	}

	public BlockComponent getActiveComponent()
	{
		if (activeComponentType != null)
		{
			switch (activeComponentType)
			{
				case EDIT:
					return editComponent;

				case BLOCK:
					return blockComponent;

				case FONT:
					return fontComponent;
			}
		}

		return null;
	}

	public Tool getTool()
	{
		return tool;
	}

	public Tool getLastTool()
	{
		return lastTool;
	}

	public void selectTool(final Tool tool)
	{
		if (this.tool != tool)
		{
			lastTool = this.tool;
			this.tool = tool;
			Events.fire(this, new GUIToolEvent(tool));
		}
	}

	public void selectLastTool()
	{
		selectTool(lastTool);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e)
	{
		if (e.getComponent() == editComponent)
		{
			setActiveComponentType(GUIComponentType.EDIT);
		}
		else if (e.getComponent() == blockComponent)
		{
			setActiveComponentType(GUIComponentType.BLOCK);
		}
		else if (e.getComponent() == fontComponent)
		{
			setActiveComponentType(GUIComponentType.FONT);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent e)
	{
		if ((e.getComponent() == editComponent) && (getActiveComponentType() == GUIComponentType.EDIT))
		{
			setActiveComponentType(null);
		}
		else if ((e.getComponent() == blockComponent) && (getActiveComponentType() == GUIComponentType.BLOCK))
		{
			setActiveComponentType(null);
		}
		else if ((e.getComponent() == fontComponent) && (getActiveComponentType() == GUIComponentType.FONT))
		{
			setActiveComponentType(null);
		}
	}

}
