package dev.xkmc.youkaishomecoming.content.item.curio.hat;

import com.google.common.collect.ImmutableMultimap;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.youkaishomecoming.content.capability.PlayerStatusData;
import dev.xkmc.youkaishomecoming.content.client.RumiaHairbandModel;
import dev.xkmc.youkaishomecoming.content.entity.rumia.RumiaModel;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class RumiaHairbandItem extends TouhouHatItem {

	private static final UUID ID = MathHelper.getUUIDFromString("rumia_hairband");

	public RumiaHairbandItem(Properties properties) {
		super(properties, TouhouMat.RUMIA_HAIRBAND);
	}

	@Override
	protected void addModifiers(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ID, "rumia_hairband", 0.25, AttributeModifier.Operation.MULTIPLY_BASE));
	}

	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new RumiaHairbandModel(RumiaModel.HAIRBAND));
	}

	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return YoukaisHomecoming.MODID + ":textures/entities/rumia.png";
	}

	@Override
	protected void tick(ItemStack stack, Level level, Player player) {
		if (PlayerStatusData.HOLDER.isProper(player)) {
			PlayerStatusData.HOLDER.get(player).lock(PlayerStatusData.Status.YOUKAIFYING);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		boolean obtain = showTooltip();
		if (obtain) {
			list.add(YHLangData.OBTAIN.get().append(YHLangData.OBTAIN_RUMIA_HAIRBAND.get()));
			list.add(YHLangData.USAGE.get().append(YHLangData.USAGE_RUMIA_HAIRBAND.get()));
		} else {
			list.add(YHLangData.OBTAIN.get().append(YHLangData.UNKNOWN.get()));
			list.add(YHLangData.USAGE.get().append(YHLangData.UNKNOWN.get()));
		}
	}

}
