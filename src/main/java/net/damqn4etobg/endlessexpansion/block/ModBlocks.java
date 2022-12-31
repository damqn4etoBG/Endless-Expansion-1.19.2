package net.damqn4etobg.endlessexpansion.block;

import net.damqn4etobg.endlessexpansion.EndlessExpansion;
import net.damqn4etobg.endlessexpansion.block.custom.RadioactiveGeneratorBlock;
import net.damqn4etobg.endlessexpansion.block.custom.UraniumBlock;
import net.damqn4etobg.endlessexpansion.fluid.ModFluids;
import net.damqn4etobg.endlessexpansion.item.ModCreativeModeTab;
import net.damqn4etobg.endlessexpansion.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, EndlessExpansion.MOD_ID);

    public static final RegistryObject<Block> URANIUM_BLOCK = registerBlock("uranium_block",
            () -> new UraniumBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(4f).sound(SoundType.METAL).requiresCorrectToolForDrops()), ModCreativeModeTab.PHYSICS_TAB);

    public static final RegistryObject<Block> RADIOACTIVE_GENERATOR = registerBlock("radioactive_generator",
            () -> new RadioactiveGeneratorBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(4f).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion()), ModCreativeModeTab.PHYSICS_TAB);

    public static final RegistryObject<LiquidBlock> NUCLEAR_WASTE_BLOCK = BLOCKS.register("nuclear_waste_block",
            () -> new LiquidBlock(ModFluids.SOURCE_NUCLEAR_WASTE, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<Block> URANIUM_ORE = registerBlock("uranium_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(5f).sound(SoundType.STONE).requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)), ModCreativeModeTab.PHYSICS_TAB);

    public static final RegistryObject<Block> DEEPSLATE_URANIUM_ORE = registerBlock("deepslate_uranium_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(5f).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)), ModCreativeModeTab.PHYSICS_TAB);


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
