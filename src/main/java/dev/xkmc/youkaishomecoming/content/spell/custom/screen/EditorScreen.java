package dev.xkmc.youkaishomecoming.content.spell.custom.screen;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.content.spell.custom.data.ISpellFormData;
import dev.xkmc.youkaishomecoming.content.spell.custom.editor.SpellOptionInstances;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import dev.xkmc.youkaishomecoming.init.data.YHModConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class EditorScreen extends Screen {

	private final SpellOptionInstances<?> ins;
	private Button cancel, reset, save;
	private OptionsList list;


	public EditorScreen(ISpellFormData<?> data) {
		super(Component.literal(""));
		ins = SpellOptionInstances.create(Wrappers.cast(data));
	}

	@Override
	protected void init() {
		super.init();
		int w = width / 4;
		int dw = w / 4;
		addRenderableWidget(cancel = new Button.Builder(
				Component.translatable("gui.cancel"),
				e -> onClose()
		).pos(dw, height - 26).size(w, 20).build());
		addRenderableWidget(reset = new Button.Builder(
				YHLangData.EDITOR_RESET.get(),
				e -> reset()
		).pos(w + dw * 2, height - 26).size(w, 20).build());
		addRenderableWidget(save = new Button.Builder(
				Component.translatable("gui.done"),
				e -> save()
		).pos(w * 2 + dw * 3, height - 26).size(w, 20).build());

		addOptions();
	}

	private void addOptions() {
		list = new OptionsList(minecraft, width, height, 32, height - 32, 25);
		list.setRenderBackground(false);
		list.setRenderTopAndBottom(false);
		ins.add(list, this::setChanged);
		addRenderableWidget(list);
		reset.active = false;
	}

	private void setChanged() {
		reset.active = true;
		updateSave();
	}

	private void reset() {
		ins.reset();
		removeWidget(list);
		addOptions();
		updateSave();
	}

	private void updateSave() {
		var e = ins.build();
		save.setTooltip(null);
		save.active = false;
		if (e != null) {
			int max = YHModConfig.COMMON.customSpellMaxDuration.get();
			int dur = e.getDuration();
			if (dur > max) {
				save.setTooltip(Tooltip.create(YHLangData.INVALID_TIME.get(max, dur)));
			} else {
				save.active = true;
			}
		}
	}

	private void save() {
		ins.save();
		onClose();
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		renderBackground(g);
		super.render(g, mx, my, pTick);
	}

}
