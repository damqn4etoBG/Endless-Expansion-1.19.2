package net.damqn4etobg.endlessexpansion.util;

public abstract class ModTemperature extends Temperature {
    public ModTemperature(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    @Override
    public int extractTemperature(int maxExtract, boolean simulate) {
        int extractedTemperature = super.extractTemperature(maxExtract, simulate);
        if(extractedTemperature != 0) {
            onTemperatureChanged();
        }

        return extractedTemperature;
    }

    @Override
    public int receiveTemperature(int maxReceive, boolean simulate) {
        int receiveTemperature = super.receiveTemperature(maxReceive, simulate);
        if(receiveTemperature != 0) {
            onTemperatureChanged();
        }

        return receiveTemperature;
    }

    public int setTemperature(int temperature) {
        this.temperature = temperature;
        return temperature;
    }

    public abstract void onTemperatureChanged();
}
