package dev.xkmc.youkaishomecoming.content.pot.cooking.large.mid;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.youkaishomecoming.content.pot.cooking.core.CookingBlockEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;

@SerialClass
public class LargeCookingPotBlockEntity extends CookingBlockEntity {

	public LargeCookingPotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public Item container() {
		return YHBlocks.IRON_POT.asItem();
	}

	protected void animationTick(Level level) {
		BlockPos pos = getBlockPos();
		double x, y, z;
		RandomSource random = level.random;
		double r = 0.2;
		double h = 14f / 16f;
		if (level.getGameTime() % 2 == 0 && random.nextFloat() < 0.3F) {
			x = (double) pos.getX() + 0.5 + (random.nextDouble() * r * 2 - r);
			z = (double) pos.getZ() + 0.5 + (random.nextDouble() * r * 2 - r);
			double motionY = random.nextBoolean() ? 0.015 : 0.005;
			level.addParticle(ModParticleTypes.STEAM.get(), x, pos.getY() + h, z, 0.0, motionY, 0.0);
		}
		if (level.getGameTime() % 2 == 0 && random.nextFloat() < 0.3F) {
			x = (double) pos.getX() + 0.5 + (random.nextDouble() * r * 2 - r);
			y = (double) pos.getY() + h;
			z = (double) pos.getZ() + 0.5 + (random.nextDouble() * r * 2 - r);
			level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0, 0.0, 0.0);
		}
	}

}
