package org.cbm.editor.font.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Events implements Runnable
{

	/**
	 * A weak reference with a hashCode and an equals method that checks for identity of the references object. Warning:
	 * the hashCode and the equals methods only work if the reference has not been garbage collected.
	 * 
	 * @author Manfred HANTSCHEL
	 * @param <TYPE> the type of the reference
	 */
	private static class WeakIdentityReference<TYPE> extends WeakReference<TYPE>
	{

		private final int hashCode;

		public WeakIdentityReference(final TYPE referent)
		{
			super(referent);

			hashCode = referent.hashCode();
		}

		public WeakIdentityReference(final TYPE referent, final ReferenceQueue<? super TYPE> referenceQueue)
		{
			super(referent, referenceQueue);

			hashCode = referent.hashCode();
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			return hashCode;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(final Object obj)
		{
			if (obj == this)
			{
				return true;
			}

			if (obj == null)
			{
				return false;
			}

			if (!(obj instanceof WeakIdentityReference))
			{
				return false;
			}

			return (((WeakIdentityReference<?>) obj).get() == get());
		}
	}

	/**
	 * A link between a consumer and a producer. Both, the consumer and producer in the class are hard references,
	 * because instances of this class won't live long.
	 * 
	 * @author Manfred HANTSCHEL
	 */
	private static class Link
	{
		private final Object producer;
		private final Object consumer;

		public Link(final Object producer, final Object consumer)
		{
			super();

			this.consumer = consumer;
			this.producer = producer;
		}

		/**
		 * Returns the producer
		 * 
		 * @return the producer, never null
		 */
		public Object getProducer()
		{
			return producer;
		}

		/**
		 * Returns the consumer
		 * 
		 * @return the consumer, never null
		 */
		public Object getConsumer()
		{
			return consumer;
		}

	}

	/**
	 * An entry for an queued event. The producer in the class is a hard reference, because instances of this class
	 * won't live long.
	 * 
	 * @author Manfred HANTSCHEL
	 */
	private static class Event
	{
		private final Object producer;
		private final Object event;

		/**
		 * Creates a queued event for the specified event fired by the specified producer
		 * 
		 * @param producer the producer
		 * @param event the event
		 */
		public Event(final Object producer, final Object event)
		{
			super();

			this.producer = producer;
			this.event = event;
		}

		/**
		 * Return the producer of the event
		 * 
		 * @return the producer
		 */
		public Object getProducer()
		{
			return producer;
		}

		/**
		 * Returns the event
		 * 
		 * @return the event
		 */
		public Object getEvent()
		{
			return event;
		}
	}

	/**
	 * A producer with its consumers
	 * 
	 * @author Manfred HANTSCHEL
	 */
	private static class Producer
	{

		/**
		 * The set of references to consumers. The set it not synchronized. All calls are made by the events thread.
		 */
		private final Set<Reference<Object>> consumerReferences;

		public Producer()
		{
			super();

			consumerReferences = new LinkedHashSet<Reference<Object>>();
		}

		/**
		 * Adds the reference to the consumer
		 * 
		 * @param consumerReference the reference to the consumer
		 */
		public void add(final Reference<Object> consumerReference)
		{
			consumerReferences.add(consumerReference);
		}

		/**
		 * Removes the reference to the consumer
		 * 
		 * @param consumerReference the reference to the consumer
		 */
		public void remove(final Reference<?> consumerReference)
		{
			consumerReferences.remove(consumerReference);
		}

		/**
		 * Returns true if the set of consumers is empty
		 * 
		 * @return true if empty
		 */
		public boolean isEmpty()
		{
			return consumerReferences.isEmpty();
		}

		/**
		 * Fires an event to all consumers
		 * 
		 * @param event the event
		 */
		public void fire(final Object event)
		{
			for (final Reference<Object> consumerReference : consumerReferences)
			{
				final Object consumer = consumerReference.get();

				if (consumer != null)
				{
					Class<? extends Object> eventType = event.getClass();

					eventTypeLoop: while (eventType != null)
					{

						if (fire(consumer, eventType, event))
						{
							break;
						}

						final Class<?>[] eventInterfaces = eventType.getInterfaces();

						for (final Class<?> eventInterface : eventInterfaces)
						{

							if (fire(consumer, eventInterface, event))
							{
								break eventTypeLoop;
							}

						}

						eventType = eventType.getSuperclass();
					}
				}
			}
		}

		/**
		 * Calls the handleEvent method of the consumer if appropriate
		 * 
		 * @param consumer the consumer
		 * @param eventType the type of the event
		 * @param event the event
		 * @return
		 */
		private boolean fire(final Object consumer, final Class<? extends Object> eventType, final Object event)
		{
			try
			{
				final Method method = consumer.getClass().getMethod("handleEvent", eventType);

				method.invoke(consumer, event);

				return true;
			}
			catch (final SecurityException e)
			{
				throw new RuntimeException("Failed to handle event", e);
			}
			catch (final NoSuchMethodException e)
			{
				// ignore
			}
			catch (final IllegalArgumentException e)
			{
				throw new RuntimeException("Failed to handle event", e);
			}
			catch (final IllegalAccessException e)
			{
				throw new RuntimeException("Failed to handle event", e);
			}
			catch (final InvocationTargetException e)
			{
				throw new RuntimeException("Failed to handle event", e);
			}

			return false;
		}

	}

	/**
	 * The runnable for the thread for firing the events
	 */
	private static final Events INSTANCE = new Events();

	/**
	 * The thread for firing the events
	 */
	private static final Thread EVENTS_THREAD;

	/**
	 * The thread local variable containing the count for disabled events
	 */
	private static final ThreadLocal<Integer> DISABLED = new ThreadLocal<Integer>();

	static
	{
		EVENTS_THREAD = new Thread(INSTANCE, "Events Thread");
		EVENTS_THREAD.setDaemon(true);
		EVENTS_THREAD.start();
	}

	/**
	 * Binds the specified consumer to the specified producer
	 * 
	 * @param producer the producer
	 * @param consumer the consumer / the listener
	 */
	public static void bind(final Object producer, final Object consumer)
	{
		if (producer == null)
		{
			throw new IllegalArgumentException("Producer is null");
		}

		if (consumer == null)
		{
			throw new IllegalArgumentException("Consumer is null");
		}

		INSTANCE.enqueueBind(producer, consumer);
	}

	/**
	 * Unbinds the specified consumer from the specified producer
	 * 
	 * @param producer the producer
	 * @param consumer the consumer / the listener
	 */
	public static void unbind(final Object producer, final Object consumer)
	{
		if (producer == null)
		{
			throw new IllegalArgumentException("Producer is null");
		}

		if (consumer == null)
		{
			throw new IllegalArgumentException("Consumer is null");
		}

		INSTANCE.enqueueUnbind(producer, consumer);
	}

	/**
	 * Fires the specified event from the specified producer
	 * 
	 * @param producer the producer
	 * @param event the event
	 */
	public static void fire(final Object producer, final Object event)
	{
		if (producer == null)
		{
			throw new IllegalArgumentException("Producer is null");
		}

		if (event == null)
		{
			throw new IllegalArgumentException("Event is null");
		}

		if (isDisabled())
		{
			return;
		}

		INSTANCE.enqueueEvent(producer, event);
	}

	/**
	 * Disables events from the current thread. Make sure to call the enable method by using a finally block! Multiple
	 * calls to disable, increase a counter and it is necessary to call enable as often as you have called disable.
	 */
	public static void disable()
	{
		final Integer count = DISABLED.get();

		if (count != null)
		{
			DISABLED.set(Integer.valueOf(count.intValue() + 1));
		}
		else
		{
			DISABLED.set(Integer.valueOf(1));
		}
	}

	/**
	 * Returns true if events from the current thread are disabled
	 * 
	 * @return true if disabled
	 */
	public static boolean isDisabled()
	{
		final Integer count = DISABLED.get();

		return ((count != null) && (count.intValue() > 0));
	}

	/**
	 * Enables events from the current thread. It is wise to place call to this method within a finally block. Multiple
	 * calls to disable, increase a counter and it is necessary to call enable as often as you have called disable.
	 * Throws an {@link IllegalStateException} if events from the current thread are not disabled.
	 */
	public static void enable()
	{
		final Integer count = DISABLED.get();

		if ((count == null) || (count.intValue() <= 0))
		{
			throw new IllegalStateException("Events not disabled");
		}

		DISABLED.set(Integer.valueOf(count - 1));
	}

	/**
	 * A linked queue for pending binds
	 */
	private final Queue<Link> pendingBinds;

	/**
	 * A linked queue for pending unbinds
	 */
	private final Queue<Link> pendingUnbinds;

	/**
	 * The queue containing events which wait to get fired
	 */
	private final BlockingQueue<Event> pendingEvents;

	/**
	 * A map containing all {@link Producer} objects containing the consumers by the producers.
	 */
	private final Map<Reference<Object>, Producer> producers;

	/**
	 * The reference queue for all weak references used to get rid of them if the object has been garbage collected
	 */
	private final ReferenceQueue<Object> referenceQueue;

	private Events()
	{
		super();

		pendingBinds = new ConcurrentLinkedQueue<Link>();
		pendingUnbinds = new ConcurrentLinkedQueue<Link>();
		pendingEvents = new LinkedBlockingQueue<Event>();
		producers = new ConcurrentHashMap<Reference<Object>, Producer>();
		referenceQueue = new ReferenceQueue<Object>();
	}

	/**
	 * Adds a bind to the pending list of binds
	 * 
	 * @param producer the producer
	 * @param consumer the consumer
	 */
	private void enqueueBind(final Object producer, final Object consumer)
	{
		pendingBinds.add(new Link(producer, consumer));
	}

	/**
	 * Adds an unbind to the pending list of unbinds
	 * 
	 * @param producer the producer
	 * @param consumer the consumer
	 */
	private void enqueueUnbind(final Object producer, final Object consumer)
	{
		pendingBinds.add(new Link(producer, consumer));
	}

	/**
	 * Enqueues an event
	 * 
	 * @param producer the producer
	 * @param event the event
	 */
	private void enqueueEvent(final Object producer, final Object event)
	{
		pendingEvents.add(new Event(producer, event));
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				final Event event = pendingEvents.take();

				handlePendingLinks();
				fireEvent(event);
				cleanupReferences();
			}
			catch (final InterruptedException e)
			{
				throw new RuntimeException("Events Thread got interrupted", e);
			}
			catch (final Exception e)
			{
				System.err.println("UNHANDLED EXCEPTION in Event Thread: " + e.getMessage());

				e.printStackTrace(System.err);
			}
		}
	}

	/**
	 * Adds binds waiting in the pending binds queue, removes binds waiting in the pending unbinds queue.
	 */
	private void handlePendingLinks()
	{
		Link pendingBind;

		synchronized (pendingBinds)
		{
			while ((pendingBind = pendingBinds.poll()) != null)
			{
				final Reference<Object> consumerReference = new WeakIdentityReference<Object>(pendingBind.getConsumer());
				final Reference<Object> producerReference = new WeakIdentityReference<Object>(pendingBind.getProducer());

				Producer producer = producers.get(producerReference);

				if (producer == null)
				{
					producer = new Producer();

					producers.put(producerReference, producer);
				}

				producer.add(consumerReference);
			}
		}

		Link pendingUnbind;

		while ((pendingUnbind = pendingUnbinds.poll()) != null)
		{
			final Reference<Object> consumerReference = new WeakIdentityReference<Object>(pendingUnbind.getConsumer());
			final Reference<Object> producerReference = new WeakIdentityReference<Object>(pendingUnbind.getProducer());
			final Producer producer = producers.get(producerReference);

			if (producer == null)
			{
				continue;
			}

			producer.remove(consumerReference);
		}
	}

	private void fireEvent(final Event event)
	{
		final Reference<Object> producerReference = new WeakIdentityReference<Object>(event.getProducer());
		final Producer producer = producers.get(producerReference);

		if (producer != null)
		{
			producer.fire(event.getEvent());
		}
	}

	/**
	 * Uses the reference queue to clean up unused references to garbage collected objects
	 */
	private void cleanupReferences()
	{
		Reference<?> reference;

		while ((reference = referenceQueue.poll()) != null)
		{
			final Iterator<Entry<Reference<Object>, Producer>> it = producers.entrySet().iterator();

			while (it.hasNext())
			{
				final Entry<Reference<Object>, Producer> entry = it.next();
				final Reference<Object> producerReference = entry.getKey();

				if (producerReference == reference)
				{
					it.remove();
					break;
				}

				final Producer producer = entry.getValue();

				producer.remove(reference);

				if (producer.isEmpty())
				{
					it.remove();
				}
			}
		}
	}

}
