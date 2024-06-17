package dev.xkmc.youkaishomecoming.content.item.misc;

import dev.xkmc.youkaishomecoming.content.entity.misc.FrozenFrog;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FrozenFrogItem extends Item {

	public final FrogVariant variant;

	public FrozenFrogItem(Properties pProperties, FrogVariant variant) {
		super(pProperties);
		this.variant = variant;
	}

	public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW,
				SoundSource.PLAYERS, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!pLevel.isClientSide) {
			FrozenFrog frog = new FrozenFrog(pLevel, player);
			frog.setItem(itemstack);
			frog.shootFromRotation(player, player.getXRot(), player.getYRot(),
					0.0F, 1.5F, 1.0F);
			pLevel.addFreshEntity(frog);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.getAbilities().instabuild) {
			itemstack.shrink(1);
		}

		return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(YHLangData.OBTAIN.get().append(YHLangData.OBTAIN_FROZEN_FROG.get()));
		list.add(YHLangData.USAGE.get().append(YHLangData.USEAGE_FROZEN_FROG.get()));
	}

}