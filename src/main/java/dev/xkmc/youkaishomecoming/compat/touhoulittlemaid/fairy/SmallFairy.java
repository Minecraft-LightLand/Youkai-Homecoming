package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.fairy;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.TLMRegistries;
import dev.xkmc.youkaishomecoming.content.entity.fairy.FairyEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class SmallFairy extends FairyEntity {

	public SmallFairy(EntityType<? extends FairyEntity> type, Level level) {
		super(type, level);
	}

	public void trySummonReinforcementOnDeath(LivingEntity le) {
		if (le.level().isClientSide()) return;
		if (!ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) return;
		if (le.getRandom().nextDouble() > YHModConfig.COMMON.fairySummonReinforcement.get())
			return;
		FairyEntity e;
		if (getRandom().nextFloat() > 0.05f) {
			e = new SmallFairy(TLMRegistries.SMALL_FAIRY.get(), le.level());
			TouhouSpellCards.setSpell(e, "fairy:" + random().nextInt(18));
		} else {
			List<EntityType<? extends FairyEntity>> list = new ArrayList<>(List.of(
					YHEntities.CIRNO.get(), YHEntities.SUNNY.get(), YHEntities.STAR.get(), YHEntities.LUNA.get()
			));
			e = list.get(random().nextInt(list.size())).create(le.level());
			if (e == null) return;
			e.initSpellCard();
		}
		e.setPos(position);
		e.setTarget(le);
		le.level().addFreshEntity(e);
	}

	@Override
	public float percentageDamage(LivingEntity le) {
		return super.percentageDamage(le) / 2;
	}

	@Override
	public void initSpellCard() {
		super.initSpellCard();
	}

}
