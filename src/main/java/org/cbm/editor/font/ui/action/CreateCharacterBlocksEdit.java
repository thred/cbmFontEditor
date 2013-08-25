package org.cbm.editor.font.ui.action;

import java.util.ArrayList;
import java.util.List;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;
import org.cbm.editor.font.util.Palette;

public class CreateCharacterBlocksEdit extends AbstractEdit
{

	private static final long serialVersionUID = -7745470346052670956L;

	private static final Characters[] UNSHIFTED_LETTERS = {
			Characters.A_UNSHIFTED, Characters.B_UNSHIFTED, Characters.C_UNSHIFTED, Characters.D_UNSHIFTED,
			Characters.E_UNSHIFTED, Characters.F_UNSHIFTED, Characters.G_UNSHIFTED, Characters.H_UNSHIFTED,
			Characters.I_UNSHIFTED, Characters.J_UNSHIFTED, Characters.K_UNSHIFTED, Characters.L_UNSHIFTED,
			Characters.M_UNSHIFTED, Characters.N_UNSHIFTED, Characters.O_UNSHIFTED, Characters.P_UNSHIFTED,
			Characters.Q_UNSHIFTED, Characters.R_UNSHIFTED, Characters.S_UNSHIFTED, Characters.T_UNSHIFTED,
			Characters.U_UNSHIFTED, Characters.V_UNSHIFTED, Characters.W_UNSHIFTED, Characters.X_UNSHIFTED,
			Characters.Y_UNSHIFTED, Characters.Z_UNSHIFTED
	};

	private static final Characters[] SHIFTED_LETTERS = {
			Characters.A_SHIFTED, Characters.B_SHIFTED, Characters.C_SHIFTED, Characters.D_SHIFTED,
			Characters.E_SHIFTED, Characters.F_SHIFTED, Characters.G_SHIFTED, Characters.H_SHIFTED,
			Characters.I_SHIFTED, Characters.J_SHIFTED, Characters.K_SHIFTED, Characters.L_SHIFTED,
			Characters.M_SHIFTED, Characters.N_SHIFTED, Characters.O_SHIFTED, Characters.P_SHIFTED,
			Characters.Q_SHIFTED, Characters.R_SHIFTED, Characters.S_SHIFTED, Characters.T_SHIFTED,
			Characters.U_SHIFTED, Characters.V_SHIFTED, Characters.W_SHIFTED, Characters.X_SHIFTED,
			Characters.Y_SHIFTED, Characters.Z_SHIFTED,

			Characters.a_SHIFTED, Characters.b_SHIFTED, Characters.c_SHIFTED, Characters.d_SHIFTED,
			Characters.e_SHIFTED, Characters.f_SHIFTED, Characters.g_SHIFTED, Characters.h_SHIFTED,
			Characters.i_SHIFTED, Characters.j_SHIFTED, Characters.k_SHIFTED, Characters.l_SHIFTED,
			Characters.m_SHIFTED, Characters.n_SHIFTED, Characters.o_SHIFTED, Characters.p_SHIFTED,
			Characters.q_SHIFTED, Characters.r_SHIFTED, Characters.s_SHIFTED, Characters.t_SHIFTED,
			Characters.u_SHIFTED, Characters.v_SHIFTED, Characters.w_SHIFTED, Characters.x_SHIFTED,
			Characters.y_SHIFTED, Characters.z_SHIFTED
	};

	public static final Characters[] NUMBERS = {
			Characters.DIGIT_0, Characters.DIGIT_1, Characters.DIGIT_2, Characters.DIGIT_3, Characters.DIGIT_4,
			Characters.DIGIT_5, Characters.DIGIT_6, Characters.DIGIT_7, Characters.DIGIT_8, Characters.DIGIT_9
	};

	public static final Characters[] SPECIAL_CHARACTERS = {
			Characters.AT, Characters.OPEN_SQUARE_BRACKET, Characters.POUND_SIGN, Characters.CLOSE_SQUARE_BRACKET,
			Characters.ARROW_UP, Characters.ARROW_LEFT, Characters.SPACE, Characters.EXCLAMATION_MARK,
			Characters.QUOTATION_MARK, Characters.NUMBER_SIGN, Characters.DOLLAR_SIGN, Characters.PERCENT_SIGN,
			Characters.AMPERSAND, Characters.APOSTROPHE, Characters.LEFT_PARENTHESIS, Characters.RIGHT_PARENTHESIS,
			Characters.ASTERISK, Characters.PLUS_SIGN, Characters.COMMA, Characters.HYPHEN_MINUS, Characters.FULL_STOP,
			Characters.SOLIDUS, Characters.COLON, Characters.SEMICOLON, Characters.LESS_THAN_SIGN,
			Characters.EQUALS_SIGN, Characters.GREATER_THAN_SIGN, Characters.QUESTION_MARK
	};

	public static final Characters[] UMLAUT_CHARACTERS = {
	// TODO evaluate this
	};

	private final int index;
	private final CharacterMode characterMode;
	private final String prefix;
	private final LetterMode letterMode;
	private final boolean numbers;
	private final boolean specialCharacters;
	private final boolean umlautCharacters;
	private final Palette foreground;
	private final Palette background;
	private final List<Block> blocks;

	private int i;

	public CreateCharacterBlocksEdit(final int index, final CharacterMode characterMode, final String prefix,
			final LetterMode letterMode, final boolean numbers, final boolean specialCharacters,
			final boolean umlautCharacters, final Palette foreground, final Palette background)
	{
		super("Add Character Blocks");

		this.index = index;
		this.characterMode = characterMode;
		this.prefix = prefix;
		this.letterMode = letterMode;
		this.numbers = numbers;
		this.specialCharacters = specialCharacters;
		this.umlautCharacters = umlautCharacters;
		this.foreground = foreground;
		this.background = background;

		blocks = new ArrayList<Block>();
	}

	@Override
	public void execute()
	{
		int count = 0;

		blocks.clear();

		i = index;

		switch (letterMode)
		{
			case NONE:
				break;

			case SHIFTED:
				count += create(SHIFTED_LETTERS);
				break;

			case UNSHIFTED:
				count += create(UNSHIFTED_LETTERS);
				break;
		}

		if (numbers)
		{
			count += create(NUMBERS);
		}

		if (specialCharacters)
		{
			count += create(SPECIAL_CHARACTERS);
		}

		if (umlautCharacters)
		{
			count += create(UMLAUT_CHARACTERS);
		}

		Registry.get(ProjectAdapter.class).getProject().setModified(true);
		Registry.get(StatusBarController.class).setMessage("Added " + count + " blocks.");
	}

	private int create(final Characters[] characters)
	{
		int count = 0;

		for (final Characters character : characters)
		{
			create(character);

			count += 1;
		}

		return count;
	}

	private void create(final Characters character)
	{
		Block block = null;
		final String name = (prefix != null) ? prefix + " " + character.getName() : character.getName();
		final Project project = Registry.get(ProjectAdapter.class).getProject();

		switch (characterMode)
		{
			case CHARACTER_1_1:
				block = Block.create(name, 1, 1, foreground, background, character.getCharacter());
				break;

			case CHARACTER_2_1:
				block = Block.create(name + " [2x1]", 2, 1, foreground, background, character.getCharacter(),
						character.getCharacter() + 128);
				break;

			case CHARACTER_1_2:
				block = Block.create(name + " [1x2]", 1, 2, foreground, background, character.getCharacter(),
						character.getCharacter() + 128);
				break;

			case CHARACTER_2_2:
				block = Block.create(name + " [2x2]", 2, 2, foreground, background, character.getCharacter(),
						character.getCharacter() + 64, character.getCharacter() + 128, character.getCharacter() + 192);
				break;

		}

		blocks.add(block);
		project.addBlock(i, block);

		i += 1;
	}

	/**
	 * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
	 */
	@Override
	public void rollback()
	{
		final Project project = Registry.get(ProjectAdapter.class).getProject();

		for (final Block block : blocks)
		{
			project.removeBlock(block);
		}

		project.setModified(true);
		Registry.get(StatusBarController.class).setMessage("Reverted add of " + blocks.size() + " blocks.");
	}

}
