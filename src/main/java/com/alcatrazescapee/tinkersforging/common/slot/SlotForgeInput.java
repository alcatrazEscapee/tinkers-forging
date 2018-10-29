/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.slot;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import com.alcatrazescapee.alcatrazcore.inventory.slot.SlotTileCore;
import com.alcatrazescapee.alcatrazcore.tile.TileInventory;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem;

@ParametersAreNonnullByDefault
public class SlotForgeInput extends SlotTileCore
{
    public SlotForgeInput(IItemHandler inventory, int idx, int x, int y, TileInventory te)
    {
        super(inventory, idx, x, y, te);
    }

    @Override
    @Nonnull
    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
    {
        // If the item has not been worked, clear the IForgeItem capability (since it only stores recipe info at this point)
        CapabilityForgeItem.clearStackCheckRecipe(stack);
        this.onSlotChanged();
        TinkersForging.getLog().info("Returning: {}", !stack.hasTagCompound() ? "null" : stack.getTagCompound().toString());
        return stack;
    }
}
