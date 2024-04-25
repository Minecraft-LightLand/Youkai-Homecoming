package dev.xkmc.youkaishomecoming.content.spell.spellcard;

import dev.xkmc.danmaku.entity.DanmakuMovement;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class SpellCardWrapper extends SpellCard {

	@SerialClass.SerialField
	public String modelId;

	@SerialClass.SerialField
	public SpellCard card;

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		if (card != null) card.tick(holder);
	}

	@Override
	public DanmakuMovement move(int code, int tickCount, Vec3 vec) {
		if (card != null) return card.move(code, tickCount, vec);
		return super.move(code, tickCount, vec);
	}

	@Nullable
	public String getModelId(){
		return modelId;
	}

}
