/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.alcatrazescapee.alcatrazcore.inventory.container.ContainerTileInventory;
import com.alcatrazescapee.alcatrazcore.inventory.slot.SlotTileCore;
import com.alcatrazescapee.tinkersforging.common.tile.TileCharcoalForge;

import static com.alcatrazescapee.tinkersforging.common.tile.TileCharcoalForge.SLOT_INPUT_MAX;
import static com.alcatrazescapee.tinkersforging.common.tile.TileCharcoalForge.SLOT_INPUT_MIN;

public class ContainerCharcoalForge extends ContainerTileInventory<TileCharcoalForge>
{
    public ContainerCharcoalForge(InventoryPlayer playerInv, TileCharcoalForge tile)
    {
        super(playerInv, tile);
    }

    @Override
    protected void addContainerSlots()
    {
        IItemHandler cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null)
        {
            for (int i = SLOT_INPUT_MIN; i < SLOT_INPUT_MAX; i++)
                addSlotToContainer(new SlotTileCore(cap, i, 44 + 18 * (i - SLOT_INPUT_MIN), 23, tile));
        }
    }
}
