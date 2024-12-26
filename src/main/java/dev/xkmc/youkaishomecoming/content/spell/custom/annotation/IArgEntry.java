package dev.xkmc.youkaishomecoming.content.spell.custom.annotation;

public interface IArgEntry {

	static IArgEntry of(Class<?> cls, ArgRange range) {
		if (cls.isEnum()) {
			return new EnumArgEntry(cls.getEnumConstants());
		}
		if (range.low() > 0 || range.high() > 0) {
			return new IntArgEntry(range.low(), range.high());
		}
		if (range.base() > 0 || range.factor() > 0) {
			return new ExpArgEntry(range.base(), range.factor(), range.decimal());
		}
		return UnitArgEntry.UNIT;
	}

	boolean verify(Object invoke) throws Exception;

}
