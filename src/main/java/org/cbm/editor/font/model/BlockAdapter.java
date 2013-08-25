package org.cbm.editor.font.model;

import java.util.Collection;
import java.util.HashSet;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.events.BlockSwitchedEvent;
import org.cbm.editor.font.model.events.ProjectEvent;
import org.cbm.editor.font.util.Events;

public class BlockAdapter
{

	private final Collection<Object> consumers;
	private final ProjectAdapter projectAdapter;

	private Block block;

	public BlockAdapter()
	{
		super();

		consumers = new HashSet<Object>();
		projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
	}

	public void handleEvent(ProjectEvent event)
	{
		if (projectAdapter.getProject() == null)
		{
			setBlock(null);
		}
	}

	public BlockAdapter bind(Object consumer)
	{
		synchronized (consumers)
		{
			consumers.add(consumer);

			Events.bind(this, consumer);

			if (block != null)
			{
				Events.bind(block, consumer);
			}
		}

		return this;
	}

	public BlockAdapter unbind(Object consumer)
	{
		synchronized (consumers)
		{
			consumers.remove(consumer);

			Events.unbind(this, consumer);

			if (block != null)
			{
				Events.unbind(block, consumer);
			}
		}

		return this;
	}

	public Block getBlock()
	{
		return block;
	}

	public BlockAdapter setBlock(Block block)
	{
		synchronized (consumers)
		{
			if (this.block == block)
			{
				return this;
			}

			for (Object consumer : consumers)
			{
				if (this.block != null)
				{
					Events.unbind(this.block, consumer);
				}

				if (block != null)
				{
					Events.bind(block, consumer);
				}
			}

			BlockSwitchedEvent event = new BlockSwitchedEvent(this.block, block);

			this.block = block;

			Events.fire(this, event);
		}

		return this;
	}

}
