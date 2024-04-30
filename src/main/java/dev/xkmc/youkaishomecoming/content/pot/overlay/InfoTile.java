package dev.xkmc.youkaishomecoming.content.pot.overlay;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface InfoTile {

	TileTooltip getImage();

	List<Component> lines();

}
