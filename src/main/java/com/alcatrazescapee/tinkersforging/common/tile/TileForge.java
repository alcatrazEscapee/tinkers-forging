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

import com.alcatrazescapee.alcatrazcore.tile.TileInventory;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem;
import com.alcatrazescapee.tinkersforging.common.capability.IForgeItem;

import static com.alcatrazescapee.tinkersforging.common.blocks.BlockForge.LIT;
import static com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem.MAX_TEMPERATURE;

@ParametersAreNonnullByDefault
public class TileForge extends TileInventory implements ITickable
{
    public static final int SLOT_FUEL = 0;
    public static final int SLOT_INPUT_MIN = 1;
    public static final int SLOT_INPUT_MAX = 3;

    private int fuelTicksRemaining;
    private float temperature;

    public TileForge()
    {
        super(4);
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
        if (world.getTotalWorldTime() % 20 == 0)
            TinkersForging.getLog().info("Forge Tick: Fuel {} | Temp: {}", fuelTicksRemaining, temperature);
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

            for (int i = SLOT_INPUT_MIN; i <= SLOT_INPUT_MAX; i++)
            {
                ItemStack stack = inventory.getStackInSlot(i);
                IForgeItem cap = stack.getCapability(CapabilityForgeItem.CAPABILITY, null);

                if (cap != null)
                {
                    // Add temperature
                    if (cap.getTemperature() < temperature)
                    {
                        CapabilityForgeItem.addTemp(stack, cap, 2.0f);
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

        super.readFromNBT(nbt);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setFloat("temp", temperature);
        nbt.setInteger("ticks", fuelTicksRemaining);

        return super.writeToNBT(nbt);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    private void consumeFuel()
    {
        // Consume fuel
        ItemStack stack = inventory.getStackInSlot(SLOT_FUEL);
        int ticks = TileEntityFurnace.getItemBurnTime(stack);
        if (ticks > 0)
        {
            inventory.setStackInSlot(SLOT_FUEL, CoreHelpers.consumeItem(stack));
            fuelTicksRemaining += ticks;
            temperature = MAX_TEMPERATURE * ticks / 6400f;
        }
    }
}
