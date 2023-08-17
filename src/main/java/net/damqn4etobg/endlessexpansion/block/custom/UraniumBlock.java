package net.damqn4etobg.endlessexpansion.block.custom;

import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UraniumBlock extends Block {
    public static final int RADIUS = 5;
    private LivingEntity player;

    public UraniumBlock(Properties properties) {
        super(properties);
    }

    private void GiveRadioactiveEffects(BlockPos blockPosUranium) {
        BlockPos blockPosPlayer = this.player.blockPosition();
        double distance = blockPosPlayer.distSqr(blockPosUranium);

        if (blockPosPlayer.distSqr(blockPosUranium) <= RADIUS * RADIUS) {
            // give radioactive effects to player
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 200));
        }
    }
}




