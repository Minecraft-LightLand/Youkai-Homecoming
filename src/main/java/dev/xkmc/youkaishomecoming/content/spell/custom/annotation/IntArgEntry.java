package dev.xkmc.youkaishomecoming.content.spell.custom.annotation;

public record IntArgEntry(int low, int high) implements IArgEntry {

	@Override
	public boolean verify(Object invoke) {
		if (!(invoke instanceof Number num)) return false;
		double val = num.intValue();
		return val >= low && val <= high;
	}

}
