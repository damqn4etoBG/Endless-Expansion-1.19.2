package net.damqn4etobg.endlessexpansion.block.entity;

import net.damqn4etobg.endlessexpansion.block.ModBlocks;
import net.damqn4etobg.endlessexpansion.fluid.ModFluids;
import net.damqn4etobg.endlessexpansion.item.ModItems;
import net.damqn4etobg.endlessexpansion.networking.ModMessages;
import net.damqn4etobg.endlessexpansion.networking.packet.EnergySyncS2CPacket;
import net.damqn4etobg.endlessexpansion.networking.packet.FluidSyncS2CPacket;
import net.damqn4etobg.endlessexpansion.networking.packet.TemperatureSyncS2CPacket;
import net.damqn4etobg.endlessexpansion.recipe.RadioactiveGeneratorRecipe;
import net.damqn4etobg.endlessexpansion.screen.RadioactiveGeneratorMenu;
import net.damqn4etobg.endlessexpansion.util.Temperature;
import net.damqn4etobg.endlessexpansion.util.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@SuppressWarnings("ALL")
public class RadioactiveGeneratorBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(10000000, 10000000) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
        }
    };

    private final Temperature temperature = new Temperature(0, 1000) {
        @Override
        public void onTemperatureChanged() {
            setChanged();
            ModMessages.sendToClients(new TemperatureSyncS2CPacket((int) this.getTemperature(), getBlockPos()));
        }
    };


    public static final int ENERGY_REQ = 1;

    private final FluidTank FLUID_TANK = new FluidTank(128000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()) {
                ModMessages.sendToClients(new FluidSyncS2CPacket(this.fluid, worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == Fluids.WATER || stack.getFluid() == ModFluids.SOURCE_NUCLEAR_WASTE.get();
        }
    };

    public void setFluid(FluidStack stack) {
        this.FLUID_TANK.setFluid(stack);
    }

    public FluidStack getFluidStack() {
        return this.FLUID_TANK.getFluid();
    }

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
    private LazyOptional<Temperature> lazyTemperatureHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    public RadioactiveGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RADIOACTIVE_GENERATOR.get(), pos, state);
        this.data = new ContainerData() {

            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> RadioactiveGeneratorBlockEntity.this.progress;
                    case 1 -> RadioactiveGeneratorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> RadioactiveGeneratorBlockEntity.this.progress = value;
                    case 1 -> RadioactiveGeneratorBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Radioactive Generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new RadioactiveGeneratorMenu(id, inventory, this, this.data);
    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setEnergyLevel(int energy) {
        this.ENERGY_STORAGE.setEnergy(energy);
    }

    public void setTemperatureLevel(int temperature) {
        this.temperature.setTemperature(temperature);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }


        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        if(cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }

        if(cap == Temperature.TEMPERATURE) {
            return lazyTemperatureHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
        lazyTemperatureHandler = LazyOptional.of(() -> temperature);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyFluidHandler.invalidate();
        lazyTemperatureHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("radioactive_generator.progress", this.progress);
        nbt.putInt("radioactive_generator.energy", ENERGY_STORAGE.getEnergyStored());
        nbt.putInt("radioactive_generator.temp", (int) temperature.getTemperature());
        nbt = FLUID_TANK.writeToNBT(nbt);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("radioactive_generator.progress");
        ENERGY_STORAGE.setEnergy(nbt.getInt("radioactive_generator.energy"));
        FLUID_TANK.readFromNBT(nbt);
        temperature.setTemperature(nbt.getInt("radioactive_generator.temp"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RadioactiveGeneratorBlockEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }

//        if(hasUraniumInSlot(pEntity)) {
//
//        }
//
//        if(hasUraniumBlockInSlot(pEntity)) {
//
//        }

 //       System.out.println("temp is" + pEntity.temperature.getTemperature());

        if(hasWaterInSlot1(pEntity)) {
            FluidStack waterStack = new FluidStack(Fluids.WATER, 1000);
            pEntity.FLUID_TANK.fill(waterStack, IFluidHandler.FluidAction.EXECUTE);
        }

        if(hasRecipe(pEntity) && hasEnoughEnergy(pEntity) && hasEnoughFluid(pEntity)) {
            pEntity.progress++;
            extractEnergy(pEntity);
            setChanged(level, pos, state);

            if(pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity);
            } else {
                pEntity.resetProgress();
                setChanged(level, pos, state);
            }
        }

        if(hasWaterInSlot1(pEntity)) {
            transferItemFluidToFluidTank(pEntity);
        }

    }

    private static boolean hasEnoughFluid(RadioactiveGeneratorBlockEntity pEntity) {
        return pEntity.FLUID_TANK.getFluidAmount() >= 500;
    }

    private static void transferItemFluidToFluidTank(RadioactiveGeneratorBlockEntity pEntity) {
        pEntity.itemHandler.getStackInSlot(1).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
            int drainAmount = Math.min(pEntity.FLUID_TANK.getSpace(), 1000);

            FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
            if(pEntity.FLUID_TANK.isFluidValid(stack)) {
                stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                fillTankWithFluid(pEntity, stack, handler.getContainer());
            }
        });
    }

    private static void fillTankWithFluid(RadioactiveGeneratorBlockEntity pEntity, FluidStack stack, ItemStack container) {
        pEntity.FLUID_TANK.fill(stack, IFluidHandler.FluidAction.EXECUTE);

        pEntity.itemHandler.extractItem(1, 1 , false);
        pEntity.itemHandler.insertItem(1, container, false);
    }

    private static void extractEnergy(RadioactiveGeneratorBlockEntity pEntity) {
        pEntity.ENERGY_STORAGE.extractEnergy(ENERGY_REQ, false);
    }

    private static boolean hasEnoughEnergy(RadioactiveGeneratorBlockEntity pEntity) {
        return pEntity.ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQ * pEntity.maxProgress;
    }

    private static boolean hasUraniumInSlot(RadioactiveGeneratorBlockEntity pEntity) {
        return pEntity.itemHandler.getStackInSlot(0).getItem() == ModItems.URANIUM_INGOT.get();
    }

    private static boolean hasUraniumBlockInSlot(RadioactiveGeneratorBlockEntity pEntity) {
        return pEntity.itemHandler.getStackInSlot(0).getItem() == ModBlocks.URANIUM_BLOCK.get().asItem();
    }

    private static boolean hasWaterInSlot1(RadioactiveGeneratorBlockEntity pEntity) {
        return pEntity.itemHandler.getStackInSlot(1).getItem() == Items.WATER_BUCKET;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(RadioactiveGeneratorBlockEntity pEntity) {
        if(hasRecipe(pEntity)) {
            pEntity.FLUID_TANK.drain(500, IFluidHandler.FluidAction.EXECUTE);
            pEntity.itemHandler.extractItem(1, 1, false);
            pEntity.resetProgress();
        }
    }

    private static boolean hasRecipe(RadioactiveGeneratorBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<RadioactiveGeneratorRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(RadioactiveGeneratorRecipe.Type.INSTANCE, inventory, level);

        if (entity.ENERGY_STORAGE.getEnergyStored() < 10000000) {
            if (entity.FLUID_TANK.getFluidAmount() >= 1024 && entity.FLUID_TANK.getFluid().getFluid() == Fluids.WATER) {
                    if (hasUraniumInSlot(entity)) {
                        try {
                            Thread.sleep(50); // delay for 1 second (1000 milliseconds)
                        } catch (InterruptedException e) {
                            // handle exception
                        }
                        entity.ENERGY_STORAGE.receiveEnergy(4096, false);
                        entity.FLUID_TANK.drain(1024, IFluidHandler.FluidAction.EXECUTE);
                        entity.itemHandler.extractItem(0, 1, false);
                        entity.temperature.addTemperature(50);
                    }
                }

            if (entity.FLUID_TANK.getFluidAmount() >= 9216 && entity.FLUID_TANK.getFluid().getFluid() == Fluids.WATER) {
                if (hasUraniumBlockInSlot(entity)) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {

                    }
                    entity.ENERGY_STORAGE.receiveEnergy(36864, false);
                    entity.FLUID_TANK.drain(9216, IFluidHandler.FluidAction.EXECUTE);
                    entity.itemHandler.extractItem(0, 1, false);
                    entity.temperature.addTemperature(100);
                }
            }
        }
            if (entity.FLUID_TANK.getFluidAmount() < 128000) {
                if (hasWaterInSlot1(entity)) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {

                    }
                    entity.itemHandler.extractItem(1, 1, false);
                    entity.itemHandler.insertItem(1, new ItemStack(Items.BUCKET), false);
                }
            }

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem());
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(0).getItem() == stack.getItem() || inventory.getItem(0).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(0).getMaxStackSize() > inventory.getItem(0).getCount();
    }

}
