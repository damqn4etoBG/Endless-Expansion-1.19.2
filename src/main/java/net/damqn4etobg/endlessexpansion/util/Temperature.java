package net.damqn4etobg.endlessexpansion.util;

import net.minecraftforge.common.capabilities.Capability;

public abstract class Temperature {
    public static final Capability<Temperature> TEMPERATURE = null;
    private float temperature;
    private float maxTemperature;

    public Temperature(float temperature, float maxTemperature) {
        this.temperature = temperature;
        this.maxTemperature = maxTemperature;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void addTemperature(float tempToAdd) {
        this.temperature += tempToAdd;
    }

    public void removeTemperature(float tempToRemove) {
        this.temperature -= tempToRemove;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public abstract void onTemperatureChanged();
}

