package net.damqn4etobg.endlessexpansion.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab PHYSICS_TAB = new CreativeModeTab("modtab_physics") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.URANIUM_INGOT.get());
        }
    };
}
