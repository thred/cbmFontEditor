package org.cbm.editor.font.ui.action;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.MainFrameController;
import org.cbm.editor.font.ui.util.Constraints;
import org.cbm.editor.font.ui.util.TitledPanel;
import org.cbm.editor.font.util.Palette;
import org.cbm.editor.font.util.PaletteComboBox;
import org.cbm.editor.font.util.UIUtils;

public class CreateCharacterBlocksDialog extends JDialog implements ActionListener
{

	private static final long serialVersionUID = -2733719755761082649L;

	private final JTextField prefixField;
	private final JComboBox<CharacterMode> characterModeComboBox;
	private final JComboBox<LetterMode> letterModeComboBox;
	private final JCheckBox numbersCheckBox;
	private final JCheckBox specialCharactersCheckBox;
	private final JCheckBox umlautCharactersCheckBox;
	private final PaletteComboBox foregroundComboBox;
	private final PaletteComboBox backgroundComboBox;
	private final JButton okButton;
	private final JButton cancelButton;

	public CreateCharacterBlocksDialog()
	{
		super(Registry.get(MainFrameController.class).getView(), "Create Blocks", true);

		setLayout(new BorderLayout());

		final JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(16, 32, 16, 32));

		final Constraints c = new Constraints();

		characterModeComboBox = new JComboBox<CharacterMode>(CharacterMode.values());
		characterModeComboBox.setSelectedItem(CharacterMode.CHARACTER_1_1);

		letterModeComboBox = new JComboBox<LetterMode>(LetterMode.values());
		letterModeComboBox.setSelectedItem(LetterMode.SHIFTED);

		okButton = new JButton("Create Blocks");
		okButton.addActionListener(this);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);

		panel.add(UIUtils.createLabel("Character Mode"), c);
		panel.add(characterModeComboBox, c.next().fillHorizontal());

		panel.add(UIUtils.createLabel("Name Prefix"), c.nextLine());
		panel.add(prefixField = new JTextField(), c.next().fillHorizontal());

		panel.add(new JSeparator(), c.nextLine().width(2).fillHorizontal());

		panel.add(UIUtils.createLabel("Letter Mode"), c.nextLine());
		panel.add(letterModeComboBox, c.next().fillHorizontal());

		panel.add(numbersCheckBox = UIUtils.createCheckBox("Create numbers", true), c.nextLine().width(2));

		panel.add(specialCharactersCheckBox = UIUtils.createCheckBox("Create special characters", true), c.nextLine()
				.width(2));

		panel.add(umlautCharactersCheckBox = UIUtils.createCheckBox("Create umlaut characters for german", false), c
				.nextLine().width(2));

		panel.add(new JSeparator(), c.nextLine().width(2).fillHorizontal());

		panel.add(UIUtils.createLabel("Foreground Color"), c.nextLine());
		panel.add(foregroundComboBox = new PaletteComboBox(Palette.LIGHT_BLUE), c.next().fillHorizontal());

		panel.add(UIUtils.createLabel("Background Color"), c.nextLine());
		panel.add(backgroundComboBox = new PaletteComboBox(Palette.BLUE), c.next().fillHorizontal());

		add(new TitledPanel("Create Blocks", "Automatically create blocks for all characters", panel, okButton,
				cancelButton));

		pack();
		UIUtils.center(this, Registry.get(MainFrameController.class).getView());
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
		}
		else if (e.getSource() == okButton)
		{
			final CharacterMode characterMode = (CharacterMode) characterModeComboBox.getSelectedItem();
			final String prefix = (prefixField.getText().trim().length() > 0) ? prefixField.getText().trim() : null;
			final LetterMode letterMode = (LetterMode) letterModeComboBox.getSelectedItem();
			final boolean numbers = numbersCheckBox.isSelected();
			final boolean specialCharacters = specialCharactersCheckBox.isSelected();
			final boolean umlautCharacters = umlautCharactersCheckBox.isSelected();
			final Palette foreground = (Palette) foregroundComboBox.getSelectedItem();
			final Palette background = (Palette) backgroundComboBox.getSelectedItem();

			if ((characterMode == CharacterMode.CHARACTER_2_2) && (letterMode == LetterMode.SHIFTED))
			{
				UIUtils.error(this, "Create Blocks", "Double sized character do only support unshifted letters.");
				return;
			}

			if ((characterMode == CharacterMode.CHARACTER_2_2) && (umlautCharacters))
			{
				UIUtils.error(this, "Create Blocks", "Double sized characters do not support umlauts.");
				return;
			}

			final int index = Registry.get(ProjectAdapter.class).getProject()
					.indexOfBlock(Registry.get(BlockAdapter.class).getBlock()) + 1;

			Registry.execute(new CreateCharacterBlocksEdit(index, characterMode, prefix, letterMode, numbers,
					specialCharacters, umlautCharacters, foreground, background));

			setVisible(false);
		}
	}

}
