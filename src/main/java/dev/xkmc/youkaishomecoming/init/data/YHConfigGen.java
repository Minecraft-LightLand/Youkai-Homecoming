package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;

import java.util.concurrent.CompletableFuture;

public class YHConfigGen extends ConfigDataProvider {
	public YHConfigGen(DataGenerator generator, CompletableFuture<HolderLookup.Provider> pvd) {
		super(generator, pvd, "Youkai Homecoming Config");
	}

	@Override
	public void add(Collector collector) {
	}

}
