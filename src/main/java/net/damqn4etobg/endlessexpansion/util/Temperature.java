package net.damqn4etobg.endlessexpansion.util;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public class Temperature implements ITemperature, INBTSerializable<Tag> {
    protected int temperature;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public Temperature(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public Temperature(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public Temperature(int capacity, int maxReceive, int maxExtract)
    {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public Temperature(int capacity, int maxReceive, int maxExtract, int energy)
    {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.temperature = Math.max(0 , Math.min(capacity, energy));
    }

    @Override
    public int receiveTemperature(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int temperatureRecieved = Math.min(capacity - temperature, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            temperature += temperatureRecieved;
        return temperatureRecieved;
    }

    @Override
    public int extractTemperature(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int temperatureExtracted = Math.min(temperature, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            temperature -= temperatureExtracted;
        return temperatureExtracted;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    @Override
    public int getMaxTemperature() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    @Override
    public Tag serializeNBT() {
        return IntTag.valueOf(this.getTemperature());
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if (!(nbt instanceof IntTag intNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.temperature = intNbt.getAsInt();
    }
}
