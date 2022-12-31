package net.damqn4etobg.endlessexpansion.networking.packet;

import net.damqn4etobg.endlessexpansion.block.entity.RadioactiveGeneratorBlockEntity;
import net.damqn4etobg.endlessexpansion.screen.RadioactiveGeneratorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FluidWasteSyncS2CPacket {
    private final FluidStack fluidStack;
    private final BlockPos pos;

    public FluidWasteSyncS2CPacket(FluidStack fluidStack, BlockPos pos) {
        this.fluidStack = fluidStack;
        this.pos = pos;
    }

    public FluidWasteSyncS2CPacket(FriendlyByteBuf buf) {
        this.fluidStack = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFluidStack(fluidStack);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof RadioactiveGeneratorBlockEntity blockEntity) {
                blockEntity.setFluidWaste(this.fluidStack);

                if(Minecraft.getInstance().player.containerMenu instanceof RadioactiveGeneratorMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setFluidWaste(this.fluidStack);
                }
            }
        });
        return true;
    }
}
