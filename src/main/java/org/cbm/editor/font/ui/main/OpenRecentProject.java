package org.cbm.editor.font.ui.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;

import org.cbm.editor.font.ui.action.OpenRecentProjectAction;
import org.cbm.editor.font.util.Prefs;

public class OpenRecentProject
{

    private final int SIZE = 4;

    private final OpenRecentProjectAction[] actions;

    public OpenRecentProject()
    {
        super();

        actions = new OpenRecentProjectAction[SIZE];

        for (int i = 0; i < actions.length; i += 1)
        {
            actions[i] = new OpenRecentProjectAction(Integer.valueOf(i));
        }

    }

    public void init(final JMenu menu)
    {
        for (final OpenRecentProjectAction action : actions)
        {
            action.updateState();
            menu.add(action);
        }
    }

    public void recent(File file)
    {
        file = file.getAbsoluteFile();

        final List<File> files = new ArrayList<>();

        for (int i = 0; i < SIZE; i += 1)
        {
            files.add(getRecent(i));
        }

        files.remove(file);
        files.add(0, file);

        for (int i = 0; i < files.size() && i < SIZE; i += 1)
        {
            final File current = files.get(i);

            if (current != null)
            {
                Prefs.set("recent." + i, current.toString());
            }
        }

        for (final OpenRecentProjectAction action : actions)
        {
            action.updateState();
        }
    }

    public File getRecent(final int index)
    {
        final String file = Prefs.get("recent." + index, null);

        return file != null ? new File(file) : null;
    }

}
