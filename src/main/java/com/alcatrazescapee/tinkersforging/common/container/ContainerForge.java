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
import com.alcatrazescapee.tinkersforging.common.tile.TileForge;

import static com.alcatrazescapee.tinkersforging.common.tile.TileForge.*;

public class ContainerForge extends ContainerTileInventory<TileForge>
{
    public ContainerForge(InventoryPlayer playerInv, TileForge tile)
    {
        super(playerInv, tile);
    }

    @Override
    protected void addContainerSlots()
    {
        IItemHandler cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null)
        {
            addSlotToContainer(new SlotTileCore(cap, SLOT_FUEL, 80, 59, tile));
            for (int i = SLOT_INPUT_MIN; i <= SLOT_INPUT_MAX; i++)
                addSlotToContainer(new SlotTileCore(cap, i, 62 + 18 * (i - SLOT_INPUT_MIN), 23, tile));
        }
    }
}
