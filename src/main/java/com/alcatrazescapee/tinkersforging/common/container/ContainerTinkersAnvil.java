/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.container;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.alcatrazescapee.alcatrazcore.inventory.container.ContainerTileInventory;
import com.alcatrazescapee.alcatrazcore.inventory.slot.SlotOutput;
import com.alcatrazescapee.alcatrazcore.inventory.slot.SlotTileCore;
import com.alcatrazescapee.tinkersforging.common.slot.SlotDisplay;
import com.alcatrazescapee.tinkersforging.common.tile.TileTinkersAnvil;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeStep;

import static com.alcatrazescapee.tinkersforging.common.tile.TileTinkersAnvil.*;

@ParametersAreNonnullByDefault
public class ContainerTinkersAnvil extends ContainerTileInventory<TileTinkersAnvil>
{
    public ContainerTinkersAnvil(InventoryPlayer playerInv, TileTinkersAnvil tile)
    {
        super(playerInv, tile, 0, 56);
    }

    public void onReceivePacket(int buttonID)
    {
        switch (buttonID)
        {
            case 8:
                tile.cycleForgeRecipe(false);
                break;
            case 9:
                tile.cycleForgeRecipe(true);
                break;
            default:
                tile.addStep(ForgeStep.valueOf(buttonID));
                break;
        }
    }

    @Override
    protected void addContainerSlots()
    {
        IItemHandler cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null)
        {
            // Forging Slots
            addSlotToContainer(new SlotTileCore(cap, SLOT_INPUT, 21, 25, tile));
            addSlotToContainer(new SlotOutput(cap, SLOT_OUTPUT, 21, 45));

            // Welding Slots
            addSlotToContainer(new SlotTileCore(cap, SLOT_WELD_1, 129, 25, tile));
            addSlotToContainer(new SlotTileCore(cap, SLOT_WELD_2, 147, 25, tile));
            addSlotToContainer(new SlotOutput(cap, SLOT_WELD_OUTPUT, 138, 45));
        }
        // Special Display Slot
        addSlotToContainer(new SlotDisplay(tile.getDisplayInventory(), SLOT_DISPLAY, 80, 20));
    }
}
