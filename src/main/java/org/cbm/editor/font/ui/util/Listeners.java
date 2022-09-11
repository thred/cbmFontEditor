package org.cbm.editor.font.ui.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Listeners
{

    public static void bindActionListener(final JComponent component, final Object consumer)
    {
        bindActionListener(component, null, consumer);
    }

    public static void bindActionListener(final JComponent component, final String command, final Object consumer)
    {
        try
        {
            Method method = component.getClass().getMethod("addActionListener", ActionListener.class);

            method.invoke(component, (ActionListener) e -> SwingUtilities.invokeLater(() -> {
                try
                {
                    String actionCommand = command == null ? e.getActionCommand() : command;

                    for (Method method1 : consumer.getClass().getMethods())
                    {
                        OnAction annotation = method1.getAnnotation(OnAction.class);

                        if (annotation == null)
                        {
                            continue;
                        }

                        if (annotation.value().length() == 0 || annotation.value().equals(actionCommand))
                        {
                            Class<?>[] parameterTypes = method1.getParameterTypes();

                            if (parameterTypes.length == 0)
                            {
                                method1.invoke(consumer);
                            }
                            else if (parameterTypes.length == 1 && parameterTypes[0] == ActionEvent.class)
                            {
                                method1.invoke(consumer, e);
                            }
                            else
                            {
                                throw new IllegalArgumentException("Invalid action handler: " + method1);
                            }
                        }
                    }
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }));
        }
        catch (NoSuchMethodException e)
        {
            throw new IllegalArgumentException("Component has not action listener", e);
        }
        catch (SecurityException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
        catch (InvocationTargetException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
    }

    public static void bindChangeListener(final JComponent component, final Object consumer)
    {
        bindChangeListener(component, null, consumer);
    }

    public static void bindChangeListener(final JComponent component, final String command, final Object consumer)
    {
        try
        {
            Method method = component.getClass().getMethod("addChangeListener", ChangeListener.class);

            method.invoke(component, (ChangeListener) e -> SwingUtilities.invokeLater(() -> {
                try
                {
                    for (Method method1 : consumer.getClass().getMethods())
                    {
                        OnChange annotation = method1.getAnnotation(OnChange.class);

                        if (annotation == null)
                        {
                            continue;
                        }

                        if (annotation.value().length() == 0 || annotation.value().equals(command))
                        {
                            Class<?>[] parameterTypes = method1.getParameterTypes();

                            if (parameterTypes.length == 0)
                            {
                                method1.invoke(consumer);
                            }
                            else if (parameterTypes.length == 1 && parameterTypes[0] == ChangeEvent.class)
                            {
                                method1.invoke(consumer, e);
                            }
                            else
                            {
                                throw new IllegalArgumentException("Invalid change handler: " + method1);
                            }
                        }
                    }
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }));
        }
        catch (NoSuchMethodException e)
        {
            throw new IllegalArgumentException("Component has not change listener", e);
        }
        catch (SecurityException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
        catch (InvocationTargetException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
    }

    public static void bindListSelectionListener(final JComponent component, final Object consumer)
    {
        bindChangeListener(component, null, consumer);
    }

    public static void bindListSelectionListener(final JComponent component, final String command,
        final Object consumer)
    {
        try
        {
            Method method = component.getClass().getMethod("addListSelectionListener", ListSelectionListener.class);

            method.invoke(component, (ListSelectionListener) e -> {
                if (e.getValueIsAdjusting())
                {
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    try
                    {
                        for (Method method1 : consumer.getClass().getMethods())
                        {
                            OnListSelection annotation = method1.getAnnotation(OnListSelection.class);

                            if (annotation == null)
                            {
                                continue;
                            }

                            if (annotation.value().length() == 0 || annotation.value().equals(command))
                            {
                                Class<?>[] parameterTypes = method1.getParameterTypes();

                                if (parameterTypes.length == 0)
                                {
                                    method1.invoke(consumer);
                                }
                                else if (parameterTypes.length == 1 && parameterTypes[0] == ListSelectionEvent.class)
                                {
                                    method1.invoke(consumer, e);
                                }
                                else
                                {
                                    throw new IllegalArgumentException("Invalid list selection handler: " + method1);
                                }
                            }
                        }
                    }
                    catch (Exception e1)
                    {
                        e1.printStackTrace();
                    }
                });
            });
        }
        catch (NoSuchMethodException e)
        {
            throw new IllegalArgumentException("Component has not change listener", e);
        }
        catch (SecurityException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
        catch (InvocationTargetException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
    }

    public static void bindFocusListener(final JComponent component, Object consumer)
    {
        bindFocusListener(component, consumer);
    }

    public static void bindFocusListener(final JComponent component, final String command, final Object consumer)
    {
        try
        {
            Method method = component.getClass().getMethod("addFocusListener", FocusListener.class);

            method.invoke(component, new FocusListener()
            {

                @Override
                public void focusGained(final FocusEvent e)
                {
                    SwingUtilities.invokeLater(() -> {
                        try
                        {
                            for (Method method1 : consumer.getClass().getMethods())
                            {
                                OnFocusGained annotation = method1.getAnnotation(OnFocusGained.class);

                                if (annotation == null)
                                {
                                    continue;
                                }

                                if (annotation.value().length() == 0 || annotation.value().equals(command))
                                {
                                    Class<?>[] parameterTypes = method1.getParameterTypes();

                                    if (parameterTypes.length == 0)
                                    {
                                        method1.invoke(consumer);
                                    }
                                    else if (parameterTypes.length == 1 && parameterTypes[0] == FocusEvent.class)
                                    {
                                        method1.invoke(consumer, e);
                                    }
                                    else
                                    {
                                        throw new IllegalArgumentException("Invalid focus gained handler: " + method1);
                                    }
                                }
                            }
                        }
                        catch (Exception e1)
                        {
                            e1.printStackTrace();
                        }
                    });
                }

                @Override
                public void focusLost(final FocusEvent e)
                {
                    SwingUtilities.invokeLater(() -> {
                        try
                        {
                            for (Method method1 : consumer.getClass().getMethods())
                            {
                                OnFocusLost annotation = method1.getAnnotation(OnFocusLost.class);

                                if (annotation == null)
                                {
                                    continue;
                                }

                                if (annotation.value().length() == 0 || annotation.value().equals(command))
                                {
                                    Class<?>[] parameterTypes = method1.getParameterTypes();

                                    if (parameterTypes.length == 0)
                                    {
                                        method1.invoke(consumer);
                                    }
                                    else if (parameterTypes.length == 1 && parameterTypes[0] == FocusEvent.class)
                                    {
                                        method1.invoke(consumer, e);
                                    }
                                    else
                                    {
                                        throw new IllegalArgumentException("Invalid focus lost handler: " + method1);
                                    }
                                }
                            }
                        }
                        catch (Exception e1)
                        {
                            e1.printStackTrace();
                        }
                    });
                }

            });
        }
        catch (NoSuchMethodException e)
        {
            throw new IllegalArgumentException("Component has not change listener", e);
        }
        catch (SecurityException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
        catch (InvocationTargetException e)
        {
            throw new IllegalArgumentException("Failed to add listener", e);
        }
    }

}
