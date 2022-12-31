package net.damqn4etobg.endlessexpansion.item;

import net.damqn4etobg.endlessexpansion.EndlessExpansion;
import net.damqn4etobg.endlessexpansion.fluid.ModFluids;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EndlessExpansion.MOD_ID);

    public static final RegistryObject<Item> URANIUM_INGOT = ITEMS.register("uranium_ingot",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.PHYSICS_TAB)));

    public static final RegistryObject<Item> RAW_URANIUM = ITEMS.register("raw_uranium",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.PHYSICS_TAB)));

    public static final RegistryObject<Item> NUCLEAR_WASTE_BUCKET = ITEMS.register("nuclear_waste_bucket",
            () -> new BucketItem(ModFluids.SOURCE_NUCLEAR_WASTE,
                    new Item.Properties().tab(ModCreativeModeTab.PHYSICS_TAB).craftRemainder(Items.BUCKET).stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
