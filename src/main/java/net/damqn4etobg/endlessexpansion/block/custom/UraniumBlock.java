package net.damqn4etobg.endlessexpansion.block.custom;

import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UraniumBlock extends Block {
    private static final int RADIUS = 5;

    public UraniumBlock(Properties properties) {
        super(properties);
    }
}




