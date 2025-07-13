package dev.xkmc.youkaishomecoming.content.pot.overlay;

import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface InfoTile {

	@Nullable
	TileTooltip getImage(boolean shift, BlockHitResult hit);

	List<Component> lines(boolean shift, BlockHitResult hit);

}
