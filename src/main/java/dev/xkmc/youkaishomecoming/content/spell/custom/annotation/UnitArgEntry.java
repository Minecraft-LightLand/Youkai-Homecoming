package dev.xkmc.youkaishomecoming.content.spell.custom.annotation;

public record UnitArgEntry() implements IArgEntry {

	public static final UnitArgEntry UNIT = new UnitArgEntry();

	@Override
	public boolean verify(Object invoke) {
		if (!(invoke instanceof Number num)) return false;
		double val = num.doubleValue();
		return val >= 0 && val <= 1;
	}

}
