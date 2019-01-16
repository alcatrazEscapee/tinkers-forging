/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.tile;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.alcatrazescapee.alcatrazcore.tile.ITileFields;
import com.alcatrazescapee.alcatrazcore.tile.TileInventory;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.common.blocks.ModBlocks;
import com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem;
import com.alcatrazescapee.tinkersforging.common.capability.IForgeItem;

import static com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem.MAX_TEMPERATURE;
import static com.alcatrazescapee.tinkersforging.util.property.IBurnBlock.LIT;
import static com.alcatrazescapee.tinkersforging.util.property.IPileBlock.LAYERS;

@ParametersAreNonnullByDefault
public class TileCharcoalForge extends TileInventory implements ITickable, ITileFields
{
    public static final int SLOT_INPUT_MIN = 0;
    public static final int SLOT_INPUT_MAX = 5;

    public static final int FIELD_FUEL = 0;
    public static final int FIELD_TEMPERATURE = 1;
    public static final int FUEL_TICKS_MAX = 1600;

    public static void light(World world, BlockPos pos)
    {
        TileCharcoalForge tile = CoreHelpers.getTE(world, pos, TileCharcoalForge.class);
        if (tile != null)
        {
            tile.consumeFuel();
            tile.updateClosedState();
        }
    }

    public static boolean isValidSideBlocks(World world, BlockPos pos)
    {
        for (EnumFacing face : EnumFacing.HORIZONTALS)
        {
            if (!TileCharcoalForge.isValidSideBlock(world.getBlockState(pos.offset(face))))
            {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidSideBlock(IBlockState state)
    {
        return state.isNormalCube() && state.getMaterial() == Material.ROCK;
    }

    private int fuelTicksRemaining;
    private float temperature;
    private boolean isClosed;

    public TileCharcoalForge()
    {
        super(5);
    }

    public void updateClosedState()
    {
        for (EnumFacing face : EnumFacing.HORIZONTALS)
        {
            if (isValidSideBlock(world.getBlockState(pos.offset(face))))
                continue;

            isClosed = false;
            return;
        }
        isClosed = true;
    }

    @Override
    public void update()
    {
        if (world.isRemote)
        {
            return;
        }

        if (fuelTicksRemaining > 0)
        {
            // Consume fuel ticks
            fuelTicksRemaining -= isClosed ? 1 : 2;

            if (fuelTicksRemaining == 0)
            {
                consumeFuel();

                if (fuelTicksRemaining == 0)
                {
                    // Couldn't consume any more fuel
                    IBlockState state = world.getBlockState(pos);
                    if (state.getBlock() == ModBlocks.CHARCOAL_FORGE)
                    {
                        world.setBlockState(pos, world.getBlockState(pos).withProperty(LIT, false));
                    }
                    else
                    {
                        onBreakBlock();
                        world.setBlockToAir(pos);
                    }
                }
            }

            // Update temperature
            float actualMaxTemp = isClosed ? MAX_TEMPERATURE : MAX_TEMPERATURE * 0.35f;
            if (temperature < actualMaxTemp)
            {
                temperature += (float) ModConfig.BALANCE.charcoalForgeTemperatureModifier;
                if (temperature > actualMaxTemp)
                    temperature = actualMaxTemp;
            }
            else if (temperature > actualMaxTemp)
            {
                temperature -= (float) ModConfig.BALANCE.charcoalForgeTemperatureModifier;
            }

            for (int i = SLOT_INPUT_MIN; i < SLOT_INPUT_MAX; i++)
            {
                ItemStack stack = inventory.getStackInSlot(i);
                IForgeItem cap = stack.getCapability(CapabilityForgeItem.CAPABILITY, null);

                if (cap != null)
                {
                    // Add temperature
                    if (cap.getTemperature() < temperature)
                    {
                        CapabilityForgeItem.addTemp(cap, 1.0f + (float) ModConfig.BALANCE.charcoalForgeTemperatureModifier);
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
            temperature -= (float) ModConfig.BALANCE.charcoalForgeTemperatureModifier;
            if (temperature < 0)
                temperature = 0;
        }

        if (world != null)
        {
            world.markChunkDirty(pos, this);
        }
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return stack.hasCapability(CapabilityForgeItem.CAPABILITY, null);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        temperature = nbt.getFloat("temp");
        fuelTicksRemaining = nbt.getInteger("ticks");
        isClosed = nbt.getBoolean("closed");

        super.readFromNBT(nbt);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setFloat("temp", temperature);
        nbt.setInteger("ticks", fuelTicksRemaining);
        nbt.setBoolean("closed", isClosed);

        return super.writeToNBT(nbt);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public int getFieldCount()
    {
        return 2;
    }

    @Override
    public int getField(int index)
    {
        switch (index)
        {
            case FIELD_FUEL:
                return fuelTicksRemaining;
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
        IBlockState state = world.getBlockState(pos);
        if (state.getValue(LAYERS) > 2)
        {
            world.setBlockState(pos, state.withProperty(LAYERS, state.getValue(LAYERS) - 1));
            fuelTicksRemaining = (int) (FUEL_TICKS_MAX * ModConfig.BALANCE.charcoalForgeFuelModifier);
        }
    }
}
