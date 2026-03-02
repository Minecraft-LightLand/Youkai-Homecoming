package dev.xkmc.youkaishomecoming.content.pot.basin;

import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public class BasinInput extends RecipeWrapper {

	protected final BasinBlockEntity be;

	public BasinInput(BasinBlockEntity be) {
		super(new InvWrapper(be.items));
		this.be = be;
	}

}
