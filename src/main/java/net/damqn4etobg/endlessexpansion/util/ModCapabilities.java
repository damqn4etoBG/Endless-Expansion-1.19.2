package net.damqn4etobg.endlessexpansion.util;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.energy.IEnergyStorage;

public class ModCapabilities {
    public static final Capability<ITemperature> TEMPERATURE = CapabilityManager.get(new CapabilityToken<>(){});
}
