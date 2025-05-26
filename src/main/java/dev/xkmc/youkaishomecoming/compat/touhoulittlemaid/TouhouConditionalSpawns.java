package dev.xkmc.youkaishomecoming.compat.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import dev.xkmc.youkaishomecoming.compat.touhoulittlemaid.fairy.SmallFairy;
import dev.xkmc.youkaishomecoming.content.entity.fairy.FairyEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.game.TouhouSpellCards;
import dev.xkmc.youkaishomecoming.events.ReimuEventHandlers;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import dev.xkmc.youkaishomecoming.init.registrate.YHEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class TouhouConditionalSpawns {

	public static void triggerKoishi(LivingEntity le, Vec3 pos) {
		if (le.level().isClientSide()) return;
		if (!ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) return;
		var e = YHEntities.KOISHI.create(le.level());
		if (e == null) return;
		e.setPos(pos);
		e.setTargetAndInitSession(le);
		e.initSpellCard();
		ReimuEventHandlers.setRandomizedPos(le, e, le.blockPosition, 5);
		le.level().addFreshEntity(e);
	}

	public static void triggetYukari(LivingEntity le, Vec3 pos) {
		if (le.level().isClientSide()) return;
		if (!ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) return;
		var e = YHEntities.YUKARI.create(le.level());
		if (e == null) return;
		e.setPos(pos);
		e.setTargetAndInitSession(le);
		e.initSpellCard();
		ReimuEventHandlers.setRandomizedPos(le, e, e.blockPosition, 5);
		le.level().addFreshEntity(e);

	}

	public static void triggetFairyReinforcement(FairyEntity self, LivingEntity le, Vec3 pos) {
		if (le.level().isClientSide()) return;
		if (!ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) return;
		GeneralYoukaiEntity e;
		if (le.level().dimension().equals(Level.NETHER)) {
			e = YHEntities.CLOWN.get().create(le.level());
			if (e == null) return;
			e.initSpellCard();
		} else {
			if (self instanceof SmallFairy) {
				if (le.getRandom().nextDouble() > YHModConfig.COMMON.smallFairySummonReinforcement.get())
					return;
			} else {
				if (le.getRandom().nextDouble() > YHModConfig.COMMON.fairySummonReinforcement.get())
					return;
			}
			if (self instanceof SmallFairy && self.getRandom().nextFloat() > YHModConfig.COMMON.smallFairySummonStrongFairy.get()) {
				e = new SmallFairy(TLMRegistries.SMALL_FAIRY.get(), le.level());
				TouhouSpellCards.setSpell(e, "fairy:" + self.random().nextInt(18));
			} else {
				List<EntityType<? extends GeneralYoukaiEntity>> list = new ArrayList<>(List.of(
						YHEntities.CIRNO.get(),
						YHEntities.SUNNY.get(), YHEntities.STAR.get(), YHEntities.LUNA.get(),
						YHEntities.LARVA.get()
				));
				list.remove(self.getType());
				e = list.get(self.random().nextInt(list.size())).create(le.level());
				if (e == null) return;
				e.initSpellCard();
			}
		}
		e.setPos(pos);
		e.setTargetAndInitSession(le);
		ReimuEventHandlers.setRandomizedPos(le, e, e.blockPosition, 5);
		le.level().addFreshEntity(e);
	}

}
