package org.cbm.editor.font.ui.video;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.ui.main.MainFrameController;
import org.cbm.editor.font.ui.util.Constraints;
import org.cbm.editor.font.ui.util.TitledPanel;
import org.cbm.editor.font.ui.util.ValuedSlider;
import org.cbm.editor.font.util.UIUtils;

public class VideoEmulationSettingsDialog extends JDialog implements ActionListener
{

    private static final long serialVersionUID = -2733719755761082649L;

    private final GlobalVideoEmulationSettings settings;
    private final VideoEmulationSettings backup;
    private final JComboBox<VideoEmulationSignalMode> signalModeComboBox;
    private final JTextArea signalModeArea;
    private final JComboBox<VideoEmulationDisplayMode> displayModeComboBox;
    private final JTextArea displayModeArea;
    private final ValuedSlider lumaSharpnessSlider;
    private final ValuedSlider shiftSlider;
    private final ValuedSlider physicalResolutionSlider;
    private final JButton resetButton;
    private final JButton okButton;
    private final JButton cancelButton;

    public VideoEmulationSettingsDialog()
    {
        super(Registry.get(MainFrameController.class).getView(), "Video Emulation Settings", true);

        settings = Registry.get(GlobalVideoEmulationSettings.class);
        backup = settings.get();

        setLayout(new BorderLayout());

        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 32, 16, 32));

        final Constraints c = new Constraints();

        signalModeComboBox = new JComboBox<>(VideoEmulationSignalMode.values());
        signalModeComboBox.addActionListener(this);

        signalModeArea = new JTextArea(5, 40);
        signalModeArea.setLineWrap(true);
        signalModeArea.setWrapStyleWord(true);
        signalModeArea.setEditable(false);

        displayModeComboBox = new JComboBox<>(VideoEmulationDisplayMode.values());
        displayModeComboBox.addActionListener(this);

        displayModeArea = new JTextArea(5, 40);
        displayModeArea.setLineWrap(true);
        displayModeArea.setWrapStyleWord(true);
        displayModeArea.setEditable(false);

        lumaSharpnessSlider = new ValuedSlider(-1f, 1f);
        lumaSharpnessSlider.addActionListener(this);

        shiftSlider = new ValuedSlider(0f, 1f);
        shiftSlider.addActionListener(this);

        physicalResolutionSlider = new ValuedSlider(0.5f, 2f);
        physicalResolutionSlider.addActionListener(this);

        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);

        okButton = new JButton("Ok");
        okButton.addActionListener(this);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        panel.add(UIUtils.createLabel("Signal Mode"), c);
        panel.add(signalModeComboBox, c.next().fillHorizontal());

        panel.add(signalModeArea, c.nextLine().width(2).fillHorizontal());

        panel.add(new JSeparator(), c.nextLine().width(2).fillHorizontal());

        panel.add(new JLabel("Luminance Sharpness"), c.nextLine());
        panel.add(lumaSharpnessSlider, c.next().fillHorizontal());

        panel.add(new JLabel("Interlace Shift"), c.nextLine());
        panel.add(shiftSlider, c.next().fillHorizontal());

        panel.add(new JSeparator(), c.nextLine().width(2).fillHorizontal());

        panel.add(UIUtils.createLabel("Display Mode"), c.nextLine());
        panel.add(displayModeComboBox, c.next().fillHorizontal());

        panel.add(displayModeArea, c.nextLine().width(2).fillHorizontal());

        panel.add(new JSeparator(), c.nextLine().width(2).fillHorizontal());

        panel.add(new JLabel("Physical Resolution"), c.nextLine());
        panel.add(physicalResolutionSlider, c.next().fillHorizontal());

        add(new TitledPanel("Video Emulation Mode", "", panel, resetButton, okButton, cancelButton));

        pack();
        UIUtils.center(this, Registry.get(MainFrameController.class).getView());

        updateState();
    }

    public void updateState()
    {
        signalModeComboBox.setSelectedItem(settings.getSignalMode());
        signalModeArea.setText(settings.getSignalMode().getDescription());
        signalModeArea.setRows(5);
        lumaSharpnessSlider.setValue(settings.getLumaSharpness());
        shiftSlider.setValue(settings.getShift());
        displayModeComboBox.setSelectedItem(settings.getDisplayMode());
        displayModeArea.setText(settings.getDisplayMode().getDescription());
        displayModeArea.setRows(5);
        physicalResolutionSlider.setValue(settings.getPhysicalResolution());
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() == cancelButton)
        {
            setVisible(false);
            settings.set(backup);
        }
        else if (e.getSource() == okButton)
        {
            setVisible(false);
            settings.store();
        }
        else if (e.getSource() == okButton)
        {
            settings.set(backup);
            updateState();
        }
        else if (e.getSource() == signalModeComboBox)
        {
            settings.setSignalMode((VideoEmulationSignalMode) signalModeComboBox.getSelectedItem());
            updateState();
        }
        else if (e.getSource() == lumaSharpnessSlider)
        {
            settings.setLumaSharpness(lumaSharpnessSlider.getValue());
            updateState();
        }
        else if (e.getSource() == shiftSlider)
        {
            settings.setShift(shiftSlider.getValue());
            updateState();
        }
        else if (e.getSource() == displayModeComboBox)
        {
            settings.setDisplayMode((VideoEmulationDisplayMode) displayModeComboBox.getSelectedItem());
            updateState();
        }
        else if (e.getSource() == physicalResolutionSlider)
        {
            settings.setPhysicalResolution(physicalResolutionSlider.getValue());
            updateState();
        }
    }

}
