package dev.xkmc.youkaishomecoming.content.spell.game;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import dev.xkmc.youkaishomecoming.content.entity.boss.*;
import dev.xkmc.youkaishomecoming.content.entity.fairy.*;
import dev.xkmc.youkaishomecoming.content.entity.reimu.MaidenEntity;
import dev.xkmc.youkaishomecoming.content.entity.youkai.GeneralYoukaiEntity;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.SpellCard;
import dev.xkmc.youkaishomecoming.content.spell.spellcard.SpellCardWrapper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class TouhouSpellCards {

	private static final Map<String, Supplier<SpellCard>> MAP = new ConcurrentHashMap<>();

	public static void registerSpell(String id, Supplier<SpellCard> card) {
		MAP.put(id, card);
	}

	public static void registerSpells() {
		registerSpell("touhou_little_maid:hakurei_reimu", ReimuSpell::new);
		registerSpell("touhou_little_maid:yukari_yakumo", YukariSpell::new);
		registerSpell("touhou_little_maid:cirno", CirnoSpell::new);
		registerSpell("touhou_little_maid:kochiya_sanae", SanaeSpell::new);
		registerSpell("touhou_little_maid:komeiji_koishi", KoishiSpell::new);
		registerSpell("touhou_little_maid:kirisame_marisa", MarisaSpell::new);
		registerSpell("touhou_little_maid:mystia_lorelei", MystiaSpell::new);
		registerSpell("touhou_little_maid:sunny_milk", SunnySpell::new);
		registerSpell("touhou_little_maid:luna_child", LunaSpell::new);
		registerSpell("touhou_little_maid:star_sapphire", StarSpell::new);
		registerSpell("touhou_little_maid:doremy_sweet", DoremiSpell::new);
		registerSpell("touhou_little_maid:kisin_sagume", KisinSpell::new);
		registerSpell("touhou_little_maid:remilia_scarlet", RemiliaSpell::new);
		registerSpell("touhou_little_maid:eternity_larva", LarvaSpell::new);
		registerSpell("touhou_little_maid:clownpiece", ClownSpell::new);
	}

	public static void setSpell(GeneralYoukaiEntity e, String id) {
		e.spellCard = new SpellCardWrapper();
		e.spellCard.modelId = id;
		var sup = MAP.get(id);
		if (sup != null) e.spellCard.card = sup.get();
		e.syncModel();
		if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID) && id.startsWith(TouhouLittleMaid.MOD_ID)) {
			var rl = new ResourceLocation(id);
			var name = Component.translatable(rl.toLanguageKey("model") + ".name");
			var desc = Component.translatable(rl.toLanguageKey("model") + ".desc");
			e.setCustomName(name.append(" - ").append(desc));
		}
	}

	public static void setReimu(MaidenEntity e) {
		setSpell(e, "touhou_little_maid:hakurei_reimu");
		if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			e.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(InitItems.HAKUREI_GOHEI.get(), 1));
		}
	}

	public static void setCirno(CirnoEntity e) {
		setSpell(e, "touhou_little_maid:cirno");
	}

	public static void setYukari(YukariEntity e) {
		setSpell(e, "touhou_little_maid:yukari_yakumo");
	}

	public static void setSanae(SanaeEntity e) {
		setSpell(e, "touhou_little_maid:kochiya_sanae");
	}

	public static void setMarisa(MarisaEntity e) {
		setSpell(e, "touhou_little_maid:kirisame_marisa");
	}

	public static void setKoishi(KoishiEntity e) {
		setSpell(e, "touhou_little_maid:komeiji_koishi");
	}

	public static void setRemilia(RemiliaEntity e) {
		setSpell(e, "touhou_little_maid:remilia_scarlet");
	}

	public static void setMystia(MystiaEntity e) {
		setSpell(e, "touhou_little_maid:mystia_lorelei");
	}

	public static void setLuna(LunaEntity e) {
		setSpell(e, "touhou_little_maid:luna_child");
	}

	public static void setSunny(SunnyEntity e) {
		setSpell(e, "touhou_little_maid:sunny_milk");
	}

	public static void setStar(StarEntity e) {
		setSpell(e, "touhou_little_maid:star_sapphire");
	}

	public static void setLarva(LarvaEntity e) {
		setSpell(e, "touhou_little_maid:eternity_larva");
	}

	public static void setClown(ClownEntity e) {
		setSpell(e, "touhou_little_maid:clownpiece");
	}

}
