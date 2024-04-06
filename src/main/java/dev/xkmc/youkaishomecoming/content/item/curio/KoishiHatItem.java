
package dev.xkmc.youkaishomecoming.content.item.curio;

import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.youkaishomecoming.content.client.HatModel;
import dev.xkmc.youkaishomecoming.content.client.KoishiHatModel;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class KoishiHatItem extends TouhouHatItem {

	public KoishiHatItem(Properties properties) {
		super(properties, TouhouMat.KOISHI_HAT);
	}

	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new HatModel(KoishiHatModel.LAYER_LOCATION));
	}

	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return YoukaisHomecoming.MODID + ":textures/models/koishi_hat.png";
	}

	@Override
	protected void tick(ItemStack stack, Level level, Player player) {
		if (player.getCooldowns().isOnCooldown(this)) return;
		EffectUtil.refreshEffect(player, new MobEffectInstance(YHEffects.UNCONSCIOUS.get(), 40, 0,
				true, true), EffectUtil.AddReason.SELF, player);
	}

}
