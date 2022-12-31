package net.damqn4etobg.endlessexpansion.block.entity;

import net.damqn4etobg.endlessexpansion.EndlessExpansion;
import net.damqn4etobg.endlessexpansion.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EndlessExpansion.MOD_ID);

    public static final RegistryObject<BlockEntityType<RadioactiveGeneratorBlockEntity>> RADIOACTIVE_GENERATOR =
            BLOCK_ENTITIES.register("radioactive_generator", () ->
                    BlockEntityType.Builder.of(RadioactiveGeneratorBlockEntity::new,
                            ModBlocks.RADIOACTIVE_GENERATOR.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
