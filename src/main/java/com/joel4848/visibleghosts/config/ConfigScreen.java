package com.joel4848.visibleghosts.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private ButtonWidget toggleButton;
    private TransparencySlider transparencySlider;

    public ConfigScreen(Screen parent) {
        super(Text.literal("Visible Ghosts Configuration"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // Toggle button for render invisible players
        boolean currentValue = ModConfig.getInstance().isRenderInvisiblePlayers();
        this.toggleButton = ButtonWidget.builder(
                        Text.literal("Render Invisible Players: ")
                                .append(Text.literal(currentValue ? "Enabled" : "Disabled")
                                        .formatted(currentValue ? Formatting.GREEN : Formatting.RED)),
                        button -> {
                            boolean newValue = !ModConfig.getInstance().isRenderInvisiblePlayers();
                            ModConfig.getInstance().setRenderInvisiblePlayers(newValue);
                            button.setMessage(Text.literal("Render Invisible Players: ")
                                    .append(Text.literal(newValue ? "Enabled" : "Disabled")
                                            .formatted(newValue ? Formatting.GREEN : Formatting.RED)));
                        })
                .dimensions(this.width / 2 - 100, this.height / 2 - 40, 200, 20)
                .build();
        this.addDrawableChild(this.toggleButton);

        // Slider for ghost transparency
        this.transparencySlider = new TransparencySlider(
                this.width / 2 - 100,
                this.height / 2 - 10,
                200,
                20,
                ModConfig.getInstance().getGhostTransparency()
        );
        this.addDrawableChild(this.transparencySlider);

        // Done button
        this.addDrawableChild(ButtonWidget.builder(
                        ScreenTexts.DONE,
                        button -> {
                            if (this.client != null) {
                                this.client.setScreen(this.parent);
                            }
                        })
                .dimensions(this.width / 2 - 100, this.height / 2 + 40, 200, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Draw title
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title,
                this.width / 2,
                20,
                0xFFFFFF
        );
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }

    private static class TransparencySlider extends SliderWidget {
        public TransparencySlider(int x, int y, int width, int height, int initialValue) {
            super(x, y, width, height, Text.empty(), initialValue / 255.0);
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            int value = (int) (this.value * 255);
            this.setMessage(Text.literal("Ghost Transparency: ")
                    .append(Text.literal(String.valueOf(value)).formatted(Formatting.GOLD)));
        }

        @Override
        protected void applyValue() {
            int value = (int) (this.value * 255);
            ModConfig.getInstance().setGhostTransparency(value);
        }
    }
}