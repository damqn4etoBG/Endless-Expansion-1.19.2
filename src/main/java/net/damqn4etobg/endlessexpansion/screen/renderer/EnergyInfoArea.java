package net.damqn4etobg.endlessexpansion.screen.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.IEnergyStorage;

import java.text.DecimalFormat;
import java.util.List;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense"
 *  Details can be found in the license file in the root folder of this project
 */
public class EnergyInfoArea extends InfoArea {
    private final IEnergyStorage energy;

    public EnergyInfoArea(int xMin, int yMin)  {
        this(xMin, yMin, null,4,54);
    }

    public EnergyInfoArea(int xMin, int yMin, IEnergyStorage energy)  {
        this(xMin, yMin, energy,4,54);
    }

    public EnergyInfoArea(int xMin, int yMin, IEnergyStorage energy, int width, int height)  {
        super(new Rect2i(xMin, yMin, width, height));
        this.energy = energy;
    }

    public List<Component> getTooltips() {
        DecimalFormat df = new DecimalFormat("#.##");
        String energyString = energy.getEnergyStored() + " / " + energy.getMaxEnergyStored() + " FE";
        if (energy.getEnergyStored() >= 1000) {
            energyString = df.format(energy.getEnergyStored() / 1000.0) + " kFE / " + df.format(energy.getMaxEnergyStored() / 1000.0) + " kFE";
        }
        if (energy.getEnergyStored() >= 1000000) {
            energyString = df.format(energy.getEnergyStored() / 1000000.0) + " MFE / " + df.format(energy.getMaxEnergyStored() / 1000000.0) + " MFE";
        }
        return List.of(Component.literal(energyString));
    }

    @Override
    public void draw(PoseStack transform) {
        final int height = area.getHeight();
        int stored = (int)(height*(energy.getEnergyStored()/(float)energy.getMaxEnergyStored()));
        fillGradient(
                transform,
                area.getX(), area.getY()+(height-stored),
                area.getX() + area.getWidth(), area.getY() +area.getHeight(),
                0xff00ff00, 0xff00b100
        );
    }
}
