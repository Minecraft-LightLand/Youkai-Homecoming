package dev.xkmc.youkaishomecoming.init.data;

import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import net.minecraft.data.DataGenerator;

public class YHConfigGen extends ConfigDataProvider {

	public YHConfigGen(DataGenerator generator) {
		super(generator, "Youkai Homecoming Config");
	}

	@Override
	public void add(Collector collector) {
	}

}
