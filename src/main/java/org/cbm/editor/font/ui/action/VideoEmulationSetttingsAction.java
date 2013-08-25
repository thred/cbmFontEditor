package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.ui.video.VideoEmulationSettingsDialog;

public class VideoEmulationSetttingsAction extends AbstractAction
{

	private static final long serialVersionUID = 4661904401283250416L;

	public VideoEmulationSetttingsAction()
	{
		super("Video Emulation Settings...", Icon.PAL.getIcon());

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl shift V"));
		putValue(SHORT_DESCRIPTION, "Allows adjustment of the video emulation settings");
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		final VideoEmulationSettingsDialog dialog = new VideoEmulationSettingsDialog();

		dialog.setVisible(true);
	}

}
