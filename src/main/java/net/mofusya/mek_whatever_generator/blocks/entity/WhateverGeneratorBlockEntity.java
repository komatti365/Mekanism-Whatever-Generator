package net.mofusya.mek_whatever_generator.blocks.entity;

import com.google.gson.JsonObject;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.mofusya.mek_whatever_generator.C;
import net.mofusya.mek_whatever_generator.blocks.WgBlockEntityTypes;
import net.mofusya.mek_whatever_generator.config.WgJsonConfigs;
import net.mofusya.mek_whatever_generator.handler.ForgeEnergyHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Function;

public class WhateverGeneratorBlockEntity extends BlockEntity {

    private final ForgeEnergyHandler mainEnergyHandler;
    private final ScrapItemHandler itemHandler;
    private final ScrapGasHandler gasHandler;
    private final ScrapEnergyHandler subEnergyHandler;
    private LazyOptional<IEnergyStorage> lazyMainEnergyHandler;
    private LazyOptional<IItemHandler> lazyItemHandler;
    private LazyOptional<IGasHandler> lazyGasHandler;
    private LazyOptional<IEnergyStorage> lazySubEnergyHandler;

    public WhateverGeneratorBlockEntity(BlockPos pos, BlockState state) {
        //Initialize super.
        super(WgBlockEntityTypes.WHATEVER_GENERATOR.get(), pos, state);

        //Initialize lazy/normal main energyHandler.
        this.mainEnergyHandler = new ForgeEnergyHandler(Integer.MAX_VALUE, 0, Integer.MAX_VALUE) {
            @Override
            public void onChanged() {
                WhateverGeneratorBlockEntity.this.setChange();
            }
        };
        this.lazyMainEnergyHandler = LazyOptional.empty();

        //Initialize lazy/normal itemHandler.
        this.itemHandler = new ScrapItemHandler() {
            @Override
            void onChanged() {
                WhateverGeneratorBlockEntity.this.setChange();
            }
        };
        this.lazyItemHandler = LazyOptional.empty();

        //Initialize lazy/normal gasHandler.
        this.gasHandler = new ScrapGasHandler() {
            @Override
            void onChanged() {
                WhateverGeneratorBlockEntity.this.setChange();
            }
        };
        this.lazyGasHandler = LazyOptional.empty();

        //Initialize lazy/normal sub energyHandler.
        this.subEnergyHandler = new ScrapEnergyHandler() {
            @Override
            void onChanged() {
                WhateverGeneratorBlockEntity.this.setChange();
            }
        };
        this.lazySubEnergyHandler = LazyOptional.empty();
    }

    public void tick(Level level, BlockPos pos, BlockState state) {

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
        //If the level is null, return super.
        if (this.getLevel() == null) return super.getCapability(cap, side);

        //If side is null,
        if (side == null) {
            //Check if cap is energy cap. If so, return the main lazy energyHandler.
            if (cap == ForgeCapabilities.ENERGY) {
                return this.lazyMainEnergyHandler.cast();
            }
            //Check if cap is gas cap. If so, Return the lazy gasHandler.
            if (cap == Capabilities.GAS_HANDLER) {
                return this.lazyGasHandler.cast();
            }
        } else {
            //Check if cap is energy cao. If so return the main/sub lazy energyHandler.
            if (cap == ForgeCapabilities.ENERGY) {
                //If side is down, return the main lazy energyHandler. If not, return the sub lazy energyHandler.
                if (side == Direction.DOWN) {
                    return this.lazyMainEnergyHandler.cast();
                } else {
                    return this.lazySubEnergyHandler.cast();
                }
            }
            //Check if cap is energy cap and side is not down. If so, return the itemHandler.
            if (cap == ForgeCapabilities.ITEM_HANDLER && side != Direction.DOWN) {
                return this.lazyItemHandler.cast();
            }
            //Check if cap is gas cap and side is not down. If so, return the gasHandler.
            if (cap == Capabilities.GAS_HANDLER && side != Direction.DOWN) {
                return this.lazyItemHandler.cast();
            }
        }

        //Return super.
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        //Fire super.
        super.onLoad();
        //Initialize lazy main energyHandler.
        this.lazyMainEnergyHandler = LazyOptional.of(() -> this.mainEnergyHandler);
        //Initialize lazy itemHandler.
        this.lazyItemHandler = LazyOptional.of(() -> this.itemHandler);
        //Initialize lazy gasHandler.
        this.lazyGasHandler = LazyOptional.of(() -> this.gasHandler);
        //Initialize lazy sub energyHandler.
        this.lazySubEnergyHandler = LazyOptional.of(() -> this.subEnergyHandler);
    }

    @Override
    public void invalidateCaps() {
        //Fire super.
        super.invalidateCaps();
        //Invalidate all lazy handlers.
        this.lazyMainEnergyHandler.invalidate();
        this.lazyItemHandler.invalidate();
        this.lazyGasHandler.invalidate();
        this.lazySubEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        //Fire super.
        super.saveAdditional(tag);
        //Serialize main energyHandler, itemHandler, sub energyHandler.
        tag.put(C.ENERGY_HANDLER, this.getMainEnergyHandler().serializeNBT());
        tag.put(C.SCRAP_ITEM_HANDLER, this.getItemHandler().serializeNBT());
        tag.put(C.SCRAP_GAS_HANDLER, this.getGasHandler().serializeNBT());
        tag.put(C.SCRAP_ENERGY_HANDLER, this.getSubEnergyHandler().serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        //Fire super.
        super.load(tag);
        //Deserialize main energyHandler, itemHandler, sub energyHandler.
        this.getMainEnergyHandler().deserializeNBT(tag.get(C.ENERGY_HANDLER));
        this.getItemHandler().deserializeNBT((CompoundTag) tag.get(C.SCRAP_ITEM_HANDLER));
        this.getGasHandler().deserializeNBT((CompoundTag) tag.get(C.SCRAP_GAS_HANDLER));
        this.getSubEnergyHandler().deserializeNBT((CompoundTag) tag.get(C.SCRAP_ENERGY_HANDLER));
    }

    public ForgeEnergyHandler getMainEnergyHandler() {
        return mainEnergyHandler;
    }

    public ScrapItemHandler getItemHandler() {
        return itemHandler;
    }

    public ScrapGasHandler getGasHandler() {
        return gasHandler;
    }

    public ScrapEnergyHandler getSubEnergyHandler() {
        return subEnergyHandler;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    public void setChange() {
        Level level = this.getLevel();
        if (level == null) return;

        super.setChanged();
        level.sendBlockUpdated(WhateverGeneratorBlockEntity.this.getBlockPos(), WhateverGeneratorBlockEntity.this.getBlockState(), WhateverGeneratorBlockEntity.this.getBlockState(), 3);
    }

    public interface ScrapHandlerSerializable extends INBTSerializable<CompoundTag> {
        long getScrap();

        void setScrap(long scrap);

        default void setScrap(Function<Long, Long> func) {
            this.setScrap(func.apply(this.getScrap()));
        }

        @Override
        default CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putLong(C.SCRAP, this.getScrap());
            return tag;
        }

        @Override
        default void deserializeNBT(CompoundTag nbt) {
            this.setScrap(nbt.getLong(C.SCRAP));
        }
    }

    public static abstract class ScrapItemHandler implements ScrapHandlerSerializable, IItemHandler {
        private long scrap = 0;

        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            return ItemStack.EMPTY;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            int itemBonus = 1;

            this.setScrap(scrap -> scrap + stack.getCount() * itemBonus);
            return ItemStack.EMPTY;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return true;
        }

        @Override
        public long getScrap() {
            return this.scrap;
        }

        @Override
        public void setScrap(long scrap) {
            this.scrap = scrap;
            this.onChanged();
        }

        abstract void onChanged();
    }

    public static abstract class ScrapGasHandler extends ScrapChemicalHandler<Gas, GasStack> implements IGasHandler {
        @Override
        @NotNull
        public GasStack getEmptyStack() {
            return GasStack.EMPTY;
        }
    }

    public static abstract class ScrapChemicalHandler<CHEMICAL extends Chemical<CHEMICAL>, STACK extends ChemicalStack<CHEMICAL>> implements ScrapHandlerSerializable,  IChemicalHandler<CHEMICAL, STACK> {
        private long scrap = 0;

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public long getTankCapacity(int i) {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isValid(int i, STACK stack) {
            return true;
        }

        @Override
        @NotNull
        public STACK getChemicalInTank(int i) {
            return this.getEmptyStack();
        }

        @Override
        @NotNull
        public STACK extractChemical(int i, long l, Action action) {
            return this.getEmptyStack();
        }

        @Override
        public void setChemicalInTank(int i, STACK stack) {
            this.setScrap(scrap -> scrap + stack.getAmount());
        }

        @Override
        @NotNull
        public STACK insertChemical(int i, STACK stack, Action action) {
            //If action is simulate, return stack
            if (action.simulate()) return stack;

            //Initialize gasBonus.
            int gasBonus = 1;
            //Add to scrap.
            this.setScrap(scrap -> scrap + stack.getAmount() * gasBonus);
            return this.getEmptyStack();
        }

        @Override
        public long getScrap() {
            return this.scrap;
        }

        @Override
        public void setScrap(long scrap) {
            this.scrap = scrap;
            this.onChanged();
        }

        @Override
        public void setScrap(Function<Long, Long> func) {
            ScrapHandlerSerializable.super.setScrap(func);
        }

        @Override
        public CompoundTag serializeNBT() {
            return ScrapHandlerSerializable.super.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            ScrapHandlerSerializable.super.deserializeNBT(nbt);
        }

        abstract void onChanged();

        @Override
        public @NotNull STACK getEmptyStack() {
            return null;
        }
    }

    public static abstract class ScrapEnergyHandler implements ScrapHandlerSerializable, IEnergyStorage {

        private long scrap = 0;

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            //If simulate is true, return maxReceive.
            if (simulate) return maxReceive;

            //Get config.
            JsonObject jsonConfig = WgJsonConfigs.MAIN_CONFIG.get();
            //Add scrap.
            this.setScrap(scrap -> scrap + (long) (maxReceive * GsonHelper.getAsFloat(jsonConfig, C.ENERGY_SCRAP_MULTIPLIER)));
            return maxReceive;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public int getEnergyStored() {
            return 0;
        }

        @Override
        public int getMaxEnergyStored() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return true;
        }

        @Override
        public long getScrap() {
            return this.scrap;
        }

        @Override
        public void setScrap(long scrap) {
            this.scrap = scrap;
            this.onChanged();
        }

        abstract void onChanged();
    }
}
