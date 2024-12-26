package dev.xkmc.youkaishomecoming.content.spell.custom.annotation;

public record ExpArgEntry(int base, int factor, int decimal) implements IArgEntry {

	double max() {
		return base * factor;
	}

	@Override
	public boolean verify(Object invoke) {
		if (!(invoke instanceof Number num)) return false;
		double val = num.doubleValue();
		return val >= 0 && val <= max();
	}

}
