package dev.xkmc.youkaishomecoming.content.spell.custom.annotation;

import dev.xkmc.l2serial.util.Wrappers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record ArgGroup(Class<?> cls, List<ArgField> list) implements IArgEntry {

	private static final Map<Class<?>, ArgGroup> CACHE = new LinkedHashMap<>();

	public synchronized static ArgGroup of(Class<?> cls) {
		if (!cls.isRecord()) throw new IllegalArgumentException("Class " + cls + " is not a record");
		var old = CACHE.get(cls);
		if (old != null) return old;
		List<ArgField> entries = new ArrayList<>();
		for (var e : cls.getRecordComponents()) {
			var arg = e.getAnnotation(ArgRange.class);
			if (arg == null) {
				entries.add(new ArgField(e, of(e.getType())));
			} else {
				entries.add(new ArgField(e, IArgEntry.of(e.getType(), arg)));
			}
		}
		old = new ArgGroup(cls, entries);
		CACHE.put(cls, old);
		return old;
	}

	public boolean verify(Object o) throws Exception {
		if (!cls.isInstance(o)) return false;
		for (var e : list) {
			if (!e.verifyField(o)) return false;
		}
		return true;
	}

	public boolean verifySafe(Object data) {
		try {
			return verify(data);
		} catch (Exception ignored) {
			return false;
		}
	}

	public <T> T make(List<Object> args) throws Exception {
		List<Class<?>> clss = new ArrayList<>();
		for (var e : list) {
			clss.add(e.field().getType());
		}
		var factory = cls.getConstructor(clss.toArray(Class[]::new));
		return Wrappers.cast(factory.newInstance(args.toArray()));
	}

}
