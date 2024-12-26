package dev.xkmc.youkaishomecoming.content.spell.custom.editor;

import dev.xkmc.youkaishomecoming.content.spell.custom.annotation.ArgField;

public record OptionPair(ArgField field, ValueProvider<?> option) {

}
