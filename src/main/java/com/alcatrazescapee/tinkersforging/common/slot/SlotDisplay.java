/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.slot;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotDisplay extends SlotItemHandler
{
    public SlotDisplay(IItemHandler itemHandler, int index, int xPos, int yPos)
    {
        super(itemHandler, index, xPos, yPos);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        return false;
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack)
    {
        return 1;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn)
    {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int amount)
    {
        return getStack();
    }
}
