package org.cbm.editor.font;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.cbm.editor.font.ui.main.MainFrameController;
import org.cbm.editor.font.util.Prefs;

public class CBMFontEditor
{

    public static void main(final String[] args)
    {
        //		Events.setLogger(new Events.Logger()
        //		{
        //
        //			@Override
        //			public boolean isEnabled()
        //			{
        //				return true;
        //			}
        //
        //			@Override
        //			public void firing(final Object event)
        //			{
        //				System.out.println("Firing event (" + System.currentTimeMillis() + "): " + event);
        //			}
        //
        //		});

        activateLookAndFeel();

        MainFrameController controller = Registry.get(MainFrameController.class);

        controller.getView().setVisible(true);
    }

    public static void activateLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(Prefs.get("lookAndFeel", "javax.swing.plaf.nimbus.NimbusLookAndFeel"));
            //			UIManager.setLookAndFeel(Prefs.get("lookAndFeel", "javax.swing.plaf.nimbus.NimbusLookAndFeel"));
            //			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (final ClassNotFoundException e)
        {
            throw new RuntimeException("Invalid look and feel", e);
        }
        catch (final InstantiationException e)
        {
            throw new RuntimeException("Invalid look and feel", e);
        }
        catch (final IllegalAccessException e)
        {
            throw new RuntimeException("Invalid look and feel", e);
        }
        catch (final UnsupportedLookAndFeelException e)
        {
            throw new RuntimeException("Invalid look and feel", e);
        }
    }

}
