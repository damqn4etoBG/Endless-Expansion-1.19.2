package net.damqn4etobg.endlessexpansion.screen.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.damqn4etobg.endlessexpansion.util.ITemperature;
import net.damqn4etobg.endlessexpansion.util.Temperature;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;
import java.util.List;

public class TemperatureInfoArea extends InfoArea {
    private ITemperature temperature;

    public TemperatureInfoArea(int xMin, int yMin) {
        this(xMin, yMin, null, 4, 54);
    }

    public TemperatureInfoArea(int xMin, int yMin, ITemperature temperature) {
        this(xMin, yMin, temperature, 4, 54);
    }

    public TemperatureInfoArea(int xMin, int yMin, ITemperature temperature, int width, int height) {
        super(new Rect2i(xMin, yMin, width, height));
        this.temperature = temperature;
    }

    public List<Component> getTooltips() {
        DecimalFormat df = new DecimalFormat("#.##");
        String temperatureString = temperature.getTemperature() + " / " + temperature.getMaxTemperature() + " °C";
        return List.of(Component.literal(temperatureString));
    }

    @Override
    public void draw(PoseStack transform) {
        final int height = area.getHeight();
        int stored = (int) (height * (temperature.getTemperature() / (float) temperature.getMaxTemperature()));
        fillGradient(
                transform,
                area.getX(), area.getY()+(height-stored),
                area.getX() + area.getWidth(), area.getY() +area.getHeight(),
                0xffff0000, 0xffb10000
        );
    }
}
