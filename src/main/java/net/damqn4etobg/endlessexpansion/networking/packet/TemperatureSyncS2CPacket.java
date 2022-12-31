package net.damqn4etobg.endlessexpansion.networking.packet;

import net.damqn4etobg.endlessexpansion.block.entity.RadioactiveGeneratorBlockEntity;
import net.damqn4etobg.endlessexpansion.screen.RadioactiveGeneratorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TemperatureSyncS2CPacket {
    private final int temperature;
    private final BlockPos pos;

    public TemperatureSyncS2CPacket(int temperature, BlockPos pos) {
        this.temperature = temperature;
        this.pos = pos;
    }

    public TemperatureSyncS2CPacket(FriendlyByteBuf buf) {
        this.temperature = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(temperature);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof RadioactiveGeneratorBlockEntity blockEntity) {
                blockEntity.setTemperatureLevel(temperature);

                if(Minecraft.getInstance().player.containerMenu instanceof RadioactiveGeneratorMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    blockEntity.setTemperatureLevel(temperature);
                }
            }
        });
        return true;
    }
}
