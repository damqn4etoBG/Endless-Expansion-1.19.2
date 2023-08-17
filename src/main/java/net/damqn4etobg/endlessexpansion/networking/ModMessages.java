package net.damqn4etobg.endlessexpansion.networking;

import net.damqn4etobg.endlessexpansion.EndlessExpansion;
import net.damqn4etobg.endlessexpansion.networking.packet.EnergySyncS2CPacket;
import net.damqn4etobg.endlessexpansion.networking.packet.FluidSyncS2CPacket;
import net.damqn4etobg.endlessexpansion.networking.packet.FluidWasteSyncS2CPacket;
import net.damqn4etobg.endlessexpansion.networking.packet.TemperatureSyncS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(EndlessExpansion.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(EnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EnergySyncS2CPacket::new)
                .encoder(EnergySyncS2CPacket::toBytes)
                .consumerMainThread(EnergySyncS2CPacket::handle)
                .add();

        net.messageBuilder(FluidSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(FluidSyncS2CPacket::new)
                .encoder(FluidSyncS2CPacket::toBytes)
                .consumerMainThread(FluidSyncS2CPacket::handle)
                .add();

        net.messageBuilder(FluidWasteSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(FluidWasteSyncS2CPacket::new)
                .encoder(FluidWasteSyncS2CPacket::toBytes)
                .consumerMainThread(FluidWasteSyncS2CPacket::handle)
                .add();

        net.messageBuilder(TemperatureSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(TemperatureSyncS2CPacket::new)
                .encoder(TemperatureSyncS2CPacket::toBytes)
                .consumerMainThread(TemperatureSyncS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
