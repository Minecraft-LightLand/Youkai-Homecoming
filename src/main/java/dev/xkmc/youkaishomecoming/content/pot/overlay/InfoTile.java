package dev.xkmc.youkaishomecoming.content.pot.overlay;

import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public interface InfoTile {

	TileTooltip getImage(boolean shift, BlockHitResult hit);

	List<Component> lines(boolean shift, BlockHitResult hit);

}
