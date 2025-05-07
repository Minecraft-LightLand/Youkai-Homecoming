package dev.xkmc.fastprojectileapi.render.core;

import java.util.Locale;

public enum DisplayType {
	SOLID, TRANSPARENT, ADDITIVE;

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
