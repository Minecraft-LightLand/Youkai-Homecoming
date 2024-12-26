package dev.xkmc.youkaishomecoming.content.spell.custom.screen;

import dev.xkmc.youkaishomecoming.content.spell.custom.data.RingSpellFormData;
import dev.xkmc.youkaishomecoming.content.spell.custom.editor.SpellOptionInstances;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class EditorScreen extends Screen {

	private final SpellOptionInstances<?> ins;
	private OptionsList list;


	public EditorScreen(RingSpellFormData data) {
		super(Component.literal(""));
		ins = SpellOptionInstances.create(data);
	}

	@Override
	protected void init() {
		super.init();
		list = new OptionsList(minecraft, width, height, 32, height - 32, 25);
		list.setRenderBackground(false);
		list.setRenderTopAndBottom(false);
		ins.add(list);
		addRenderableWidget(list);
		int w = width / 4;
		int dw = w / 4;
		addRenderableWidget(new Button.Builder(
				Component.translatable("gui.cancel"),
				e -> onClose()
		).pos(dw, height - 26).size(w, 20).build());
		addRenderableWidget(new Button.Builder(
				YHLangData.EDITOR_RESET.get(),
				e -> reset()
		).pos(w + dw * 2, height - 26).size(w, 20).build());
		addRenderableWidget(new Button.Builder(
				Component.translatable("gui.done"),
				e -> save()
		).pos(w * 2 + dw * 3, height - 26).size(w, 20).build());
	}

	public void reset() {
		ins.reset();
		removeWidget(list);
		list = new OptionsList(minecraft, width, height, 32, height - 32, 25);
		list.setRenderBackground(false);
		list.setRenderTopAndBottom(false);
		ins.add(list);
		addRenderableWidget(list);
	}

	public void save() {
		ins.save();
		onClose();
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		renderBackground(g);
		super.render(g, mx, my, pTick);
	}

	@Override
	public void renderBackground(GuiGraphics g) {
		super.renderBackground(g);
	}

}
