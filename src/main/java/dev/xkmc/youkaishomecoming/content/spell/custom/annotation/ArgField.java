package dev.xkmc.youkaishomecoming.content.spell.custom.annotation;

import java.lang.reflect.RecordComponent;

public record ArgField(RecordComponent field, IArgEntry range) {

	public boolean verifyField(Object o) throws Exception {
		return range.verify(field.getAccessor().invoke(o));
	}

}
