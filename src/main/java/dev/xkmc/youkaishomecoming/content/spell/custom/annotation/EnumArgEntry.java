package dev.xkmc.youkaishomecoming.content.spell.custom.annotation;

public record EnumArgEntry(Object[] vals) implements IArgEntry {

	@Override
	public boolean verify(Object val) throws Exception {
		return true;
	}

}
