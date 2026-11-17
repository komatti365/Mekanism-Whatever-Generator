package net.mofusya.mek_everything_generator.blocks.entity;

import com.google.gson.JsonObject;
import mekanism.api.Action;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.mofusya.mek_everything_generator.C;
import net.mofusya.mek_everything_generator.blocks.EgBlockEntityTypes;
import net.mofusya.mek_everything_generator.config.EgJsonConfigs;
import net.mofusya.mek_everything_generator.handler.ForgeEnergyHandler;
import net.mofusya.mek_everything_generator.recipes.SimpleItemGasFluidRLContainer;
import net.mofusya.mek_everything_generator.recipes.recipe.EverythingGeneratingRecipe;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class EverythingGeneratorBlockEntity extends BlockEntity {

    private final ForgeEnergyHandler mainEnergyHandler;
    private final ScrapItemHandler itemHandler;
    private final ScrapFluidHandler fluidHandler;
    private final ScrapGasHandler gasHandler;
    private final ScrapEnergyHandler subEnergyHandler;

    public EverythingGeneratorBlockEntity(BlockPos pos, BlockState state) {
        //Initialize super.
        super(EgBlockEntityTypes.EVERYTHING_GENERATOR.get(), pos, state);

        //Initialize main energyHandler.
        this.mainEnergyHandler = new ForgeEnergyHandler(Integer.MAX_VALUE, 0, Integer.MAX_VALUE) {
            @Override
            public void onChanged() {
                EverythingGeneratorBlockEntity.this.setChange();
            }
        };

        //Initialize itemHandler.
        this.itemHandler = new ScrapItemHandler() {
            @Override
            public Level getLevel() {
                return EverythingGeneratorBlockEntity.this.getLevel();
            }

            @Override
            void onChanged() {
                EverythingGeneratorBlockEntity.this.setChange();
            }
        };

        //Initialize fluidHandler.
        this.fluidHandler = new ScrapFluidHandler() {
            @Override
            public Level getLevel() {
                return EverythingGeneratorBlockEntity.this.getLevel();
            }

            @Override
            void onChanged() {
                EverythingGeneratorBlockEntity.this.setChange();
            }
        };

        //Initialize chemical (gas) handler.
        this.gasHandler = new ScrapGasHandler() {
            @Override
            public Level getLevel() {
                return EverythingGeneratorBlockEntity.this.getLevel();
            }

            @Override
            void onChanged() {
                EverythingGeneratorBlockEntity.this.setChange();
            }
        };

        //Initialize sub energyHandler.
        this.subEnergyHandler = new ScrapEnergyHandler() {
            @Override
            public Level getLevel() {
                return EverythingGeneratorBlockEntity.this.getLevel();
            }

            @Override
            void onChanged() {
                EverythingGeneratorBlockEntity.this.setChange();
            }
        };
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                EgBlockEntityTypes.EVERYTHING_GENERATOR.get(),
                (be, side) -> (side == null || side == Direction.DOWN) ? be.getMainEnergyHandler() : be.getSubEnergyHandler()
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                EgBlockEntityTypes.EVERYTHING_GENERATOR.get(),
                (be, side) -> (side != null && side != Direction.DOWN) ? be.getItemHandler() : null
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                EgBlockEntityTypes.EVERYTHING_GENERATOR.get(),
                (be, side) -> (side == null || side != Direction.DOWN) ? be.getFluidHandler() : null
        );
        event.registerBlockEntity(
                mekanism.common.capabilities.Capabilities.CHEMICAL.block(),
                EgBlockEntityTypes.EVERYTHING_GENERATOR.get(),
                (be, side) -> (side == null || side != Direction.DOWN) ? be.getGasHandler() : null
        );
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        //Client check.
        if (level.isClientSide) return;

        this.getItemHandler().generateToEnergyHandler(this.getMainEnergyHandler());
        this.getFluidHandler().generateToEnergyHandler(this.getMainEnergyHandler());
        this.getGasHandler().generateToEnergyHandler(this.getMainEnergyHandler());
        this.getSubEnergyHandler().generateToEnergyHandler(this.getMainEnergyHandler());
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        //Fire super.
        super.saveAdditional(tag, registries);
        //Serialize main energyHandler, itemHandler, sub energyHandler.
        tag.put(C.ENERGY_HANDLER, this.getMainEnergyHandler().serializeNBT(registries));
        tag.put(C.SCRAP_ITEM_HANDLER, this.getItemHandler().serializeNBT(registries));
        tag.put(C.SCRAP_FLUID_HANDLER, this.getFluidHandler().serializeNBT(registries));
        tag.put(C.SCRAP_GAS_HANDLER, this.getGasHandler().serializeNBT(registries));
        tag.put(C.SCRAP_ENERGY_HANDLER, this.getSubEnergyHandler().serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        //Fire super.
        super.loadAdditional(tag, registries);
        //Deserialize main energyHandler, itemHandler, sub energyHandler.
        if (tag.contains(C.ENERGY_HANDLER)) {
            this.getMainEnergyHandler().deserializeNBT(registries, tag.get(C.ENERGY_HANDLER));
        }
        if (tag.contains(C.SCRAP_ITEM_HANDLER, Tag.TAG_COMPOUND)) {
            this.getItemHandler().deserializeNBT(registries, tag.getCompound(C.SCRAP_ITEM_HANDLER));
        }
        if (tag.contains(C.SCRAP_FLUID_HANDLER, Tag.TAG_COMPOUND)) {
            this.getFluidHandler().deserializeNBT(registries, tag.getCompound(C.SCRAP_FLUID_HANDLER));
        }
        if (tag.contains(C.SCRAP_GAS_HANDLER, Tag.TAG_COMPOUND)) {
            this.getGasHandler().deserializeNBT(registries, tag.getCompound(C.SCRAP_GAS_HANDLER));
        }
        if (tag.contains(C.SCRAP_ENERGY_HANDLER, Tag.TAG_COMPOUND)) {
            this.getSubEnergyHandler().deserializeNBT(registries, tag.getCompound(C.SCRAP_ENERGY_HANDLER));
        }
    }

    public ForgeEnergyHandler getMainEnergyHandler() {
        return mainEnergyHandler;
    }

    public ScrapItemHandler getItemHandler() {
        return itemHandler;
    }

    public ScrapFluidHandler getFluidHandler() {
        return fluidHandler;
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
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithFullMetadata(registries);
    }

    public void setChange() {
        Level level = this.getLevel();
        if (level == null) return;

        super.setChanged();
        level.sendBlockUpdated(EverythingGeneratorBlockEntity.this.getBlockPos(), EverythingGeneratorBlockEntity.this.getBlockState(), EverythingGeneratorBlockEntity.this.getBlockState(), 3);
    }

    public interface ScrapHandlerSerializable extends INBTSerializable<CompoundTag> {
        long getScrap();

        void setScrap(long scrap);

        default void setScrap(Function<Long, Long> func) {
            long scrap = func.apply(this.getScrap());
            if (scrap < 0) scrap = Long.MAX_VALUE;
            this.setScrap(scrap);
        }

        Level getLevel();

        @Override
        default CompoundTag serializeNBT(HolderLookup.Provider registries) {
            CompoundTag tag = new CompoundTag();
            tag.putLong(C.SCRAP, this.getScrap());
            return tag;
        }

        @Override
        default void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
            this.setScrap(nbt.getLong(C.SCRAP));
        }

        default void generateToEnergyHandler(ForgeEnergyHandler energyHandler) {
            var jsonConfig = EgJsonConfigs.MAIN_CONFIG.get();
            int energyHandlerSpace = energyHandler.getMaxEnergyStored() - energyHandler.getEnergyStored();
            int generateScrap = (int) Math.min(this.getScrap(), (long) ((float) energyHandlerSpace / GsonHelper.getAsFloat(jsonConfig, C.ENERGY_SCRAP_MULTIPLIER)));
            this.setScrap(scrap -> scrap - generateScrap);
            energyHandler.receiveEnergyFromInsider(generateScrap, false);
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
            if (!simulate && !stack.isEmpty()) {
                SimpleItemGasFluidRLContainer container = new SimpleItemGasFluidRLContainer(stack, null, null);
                Optional<RecipeHolder<EverythingGeneratingRecipe>> recipe = this.getLevel().getRecipeManager().getRecipeFor(EverythingGeneratingRecipe.Type.INSTANCE, container, this.getLevel());

                int itemBonus = recipe.map(holder -> holder.value().getScrapResult()).orElse(1);

                this.setScrap(scrap -> scrap + (long) stack.getCount() * itemBonus);
            }
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

    public static abstract class ScrapFluidHandler implements ScrapHandlerSerializable, IFluidHandler {
        private long scrap = 0;

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return FluidStack.EMPTY;
        }

        @Override
        public int getTankCapacity(int tank) {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return true;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.isEmpty()) return 0;
            if (action.execute()) {
                SimpleItemGasFluidRLContainer container = new SimpleItemGasFluidRLContainer(null, NeoForgeRegistries.FLUID_TYPES.getKey(resource.getFluid().getFluidType()), null);
                Optional<RecipeHolder<EverythingGeneratingRecipe>> recipe = this.getLevel().getRecipeManager().getRecipeFor(EverythingGeneratingRecipe.Type.INSTANCE, container, this.getLevel());

                int itemBonus = recipe.map(holder -> holder.value().getScrapResult()).orElse(1);

                this.setScrap(scrap -> scrap + (long) resource.getAmount() * itemBonus);
                this.onChanged();
            }
            return resource.getAmount();
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        public long getScrap() {
            return this.scrap;
        }

        @Override
        public void setScrap(long scrap) {
            this.scrap = scrap;
        }

        abstract void onChanged();
    }

    public static abstract class ScrapGasHandler extends ScrapChemicalHandler {
    }

    public static abstract class ScrapChemicalHandler implements ScrapHandlerSerializable, IChemicalHandler {
        private long scrap = 0;

        @Override
        public int getChemicalTanks() {
            return 1;
        }

        @Override
        public long getChemicalTankCapacity(int tank) {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isValid(int tank, ChemicalStack stack) {
            return true;
        }

        @Override
        @NotNull
        public ChemicalStack getChemicalInTank(int tank) {
            return ChemicalStack.EMPTY;
        }

        @Override
        @NotNull
        public ChemicalStack extractChemical(int tank, long amount, Action action) {
            return ChemicalStack.EMPTY;
        }

        @Override
        public void setChemicalInTank(int tank, ChemicalStack stack) {
            if (!stack.isEmpty()) {
                this.setScrap(scrap -> scrap + stack.getAmount());
            }
        }

        @Override
        @NotNull
        public ChemicalStack insertChemical(int tank, ChemicalStack stack, Action action) {
            if (stack.isEmpty()) return ChemicalStack.EMPTY;

            long insertAmount = Math.min(stack.getAmount(), this.getChemicalTankCapacity(0));
            long remaining = stack.getAmount() - insertAmount;
            ChemicalStack remainder = remaining <= 0 ? ChemicalStack.EMPTY : stack.copyWithAmount(remaining);

            //If action is simulate, return remainder
            if (action.simulate()) return remainder;

            SimpleItemGasFluidRLContainer container = new SimpleItemGasFluidRLContainer(null, null, MekanismAPI.CHEMICAL_REGISTRY.getKey(stack.getChemical()));
            Optional<RecipeHolder<EverythingGeneratingRecipe>> recipe = this.getLevel().getRecipeManager().getRecipeFor(EverythingGeneratingRecipe.Type.INSTANCE, container, this.getLevel());

            //get gasBonus.
            int gasBonus = recipe.map(holder -> holder.value().getScrapResult()).orElse(1);
            //Add to scrap.
            this.setScrap(scrap -> scrap + insertAmount * gasBonus);
            return remainder;
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

    public static abstract class ScrapEnergyHandler implements ScrapHandlerSerializable, IEnergyStorage {

        private long scrap = 0;

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            //If simulate is true, return maxReceive.
            if (simulate) return maxReceive;

            //Get config.
            JsonObject jsonConfig = EgJsonConfigs.MAIN_CONFIG.get();
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
