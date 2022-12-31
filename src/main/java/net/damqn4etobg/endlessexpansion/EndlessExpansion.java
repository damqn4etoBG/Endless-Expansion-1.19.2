package net.damqn4etobg.endlessexpansion;

import com.mojang.logging.LogUtils;
import net.damqn4etobg.endlessexpansion.block.ModBlocks;
import net.damqn4etobg.endlessexpansion.block.entity.ModBlockEntities;
import net.damqn4etobg.endlessexpansion.fluid.ModFluidTypes;
import net.damqn4etobg.endlessexpansion.fluid.ModFluids;
import net.damqn4etobg.endlessexpansion.item.ModItems;
import net.damqn4etobg.endlessexpansion.networking.ModMessages;
import net.damqn4etobg.endlessexpansion.screen.ModMenuTypes;
import net.damqn4etobg.endlessexpansion.screen.RadioactiveGeneratorScreen;
import net.damqn4etobg.endlessexpansion.world.feature.ModConfiguredFeatures;
import net.damqn4etobg.endlessexpansion.world.feature.ModPlacedFeatures;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


@Mod(EndlessExpansion.MOD_ID)
public class EndlessExpansion
{
    public static final String MOD_ID = "endlessexpansion";
    private static final Logger LOGGER = LogUtils.getLogger();
    public EndlessExpansion()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModFluids.register(modEventBus);
        ModFluidTypes.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        ModConfiguredFeatures.register(modEventBus);
        ModPlacedFeatures.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();
        });
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_NUCLEAR_WASTE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_NUCLEAR_WASTE.get(), RenderType.translucent());

            MenuScreens.register(ModMenuTypes.RADIOACTIVE_GENERATOR_MENU.get(), RadioactiveGeneratorScreen::new);
        }
    }
}
