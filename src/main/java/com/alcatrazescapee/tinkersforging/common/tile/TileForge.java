/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.tile;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.alcatrazescapee.alcatrazcore.tile.ITileFields;
import com.alcatrazescapee.alcatrazcore.tile.TileInventory;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem;
import com.alcatrazescapee.tinkersforging.common.capability.IForgeItem;

import static com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem.MAX_TEMPERATURE;
import static com.alcatrazescapee.tinkersforging.util.property.IBurnBlock.LIT;

@ParametersAreNonnullByDefault
public class TileForge extends TileInventory implements ITickable, ITileFields
{
    public static final int SLOT_FUEL = 0;
    public static final int SLOT_INPUT_MIN = 1;
    public static final int SLOT_INPUT_MAX = 3;

    public static final int FIELD_FUEL = 0;
    public static final int FIELD_FUEL_MAX = 1;
    public static final int FIELD_TEMPERATURE = 2;

    private int fuelTicksRemaining;
    private int fuelTicksMax;
    private float temperature;

    public TileForge()
    {
        super(4);

        markDirty();
    }

    public boolean tryLight()
    {
        // Returns the lit state
        if (fuelTicksRemaining > 0)
        {
            return true;
        }
        consumeFuel();
        return fuelTicksRemaining > 0;
    }

    @Override
    public void update()
    {
        if (fuelTicksRemaining > 0)
        {
            // Consume fuel ticks
            fuelTicksRemaining--;

            if (fuelTicksRemaining == 0)
            {
                consumeFuel();

                if (fuelTicksRemaining == 0)
                {
                    // Couldn't consume any more fuel
                    world.setBlockState(pos, world.getBlockState(pos).withProperty(LIT, false));
                }
            }

            // Update temperature
            if (temperature < MAX_TEMPERATURE)
            {
                temperature += (float) ModConfig.BALANCE.forgeTemperatureModifier;
                if (temperature > MAX_TEMPERATURE)
                    temperature = MAX_TEMPERATURE;
            }

            for (int i = SLOT_INPUT_MIN; i <= SLOT_INPUT_MAX; i++)
            {
                ItemStack stack = inventory.getStackInSlot(i);
                IForgeItem cap = stack.getCapability(CapabilityForgeItem.CAPABILITY, null);

                if (cap != null)
                {
                    // Add temperature
                    if (cap.getTemperature() < temperature)
                    {
                        CapabilityForgeItem.addTemp(cap, 1.0f + (float) ModConfig.BALANCE.forgeTemperatureModifier);
                    }

                    if (cap.isMolten())
                    {
                        // The thing melted!
                        inventory.setStackInSlot(i, ItemStack.EMPTY);
                        world.playSound(null, pos, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    }
                }
            }
        }
        else if (temperature > 0)
        {
            // When it is not burning fuel, then decrease the temperature until it reaches zero
            temperature -= (float) ModConfig.BALANCE.forgeTemperatureModifier;
            if (temperature < 0)
                temperature = 0;
        }
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        switch (slot)
        {
            case SLOT_FUEL:
                return TileEntityFurnace.isItemFuel(stack);
            default:
                return stack.hasCapability(CapabilityForgeItem.CAPABILITY, null);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        temperature = nbt.getFloat("temp");
        fuelTicksRemaining = nbt.getInteger("ticks");
        fuelTicksMax = nbt.getInteger("maxTicks");

        super.readFromNBT(nbt);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setFloat("temp", temperature);
        nbt.setInteger("ticks", fuelTicksRemaining);
        nbt.setInteger("maxTicks", fuelTicksMax);

        return super.writeToNBT(nbt);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public int getFieldCount()
    {
        return 3;
    }

    @Override
    public int getField(int index)
    {
        switch (index)
        {
            case FIELD_FUEL:
                return fuelTicksRemaining;
            case FIELD_FUEL_MAX:
                return fuelTicksMax;
            case FIELD_TEMPERATURE:
                return (int) temperature;
            default:
                TinkersForging.getLog().warn("Invalid field ID!");
                return 0;
        }
    }

    @Override
    public void setField(int index, int value)
    {
        switch (index)
        {
            case FIELD_FUEL:
                fuelTicksRemaining = value;
                break;
            case FIELD_FUEL_MAX:
                fuelTicksMax = value;
                break;
            case FIELD_TEMPERATURE:
                temperature = (float) value;
                break;
            default:
                TinkersForging.getLog().warn("Invalid field ID!");

        }
    }

    private void consumeFuel()
    {
        // Consume fuel
        ItemStack stack = inventory.getStackInSlot(SLOT_FUEL);
        int ticks = TileEntityFurnace.getItemBurnTime(stack);
        if (ticks > 0)
        {
            inventory.setStackInSlot(SLOT_FUEL, CoreHelpers.consumeItem(stack));
            fuelTicksRemaining += (int) (ticks * ModConfig.BALANCE.forgeFuelModifier);
            fuelTicksMax = fuelTicksRemaining;
        }
    }
}
