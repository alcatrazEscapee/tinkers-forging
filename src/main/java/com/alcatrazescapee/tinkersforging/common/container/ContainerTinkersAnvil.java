/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.container;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
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
    private final EntityPlayer player;

    public ContainerTinkersAnvil(EntityPlayer player, TileTinkersAnvil tile)
    {
        super(player.inventory, tile, 0, 56);
        this.player = player;
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
                if (damageHammer())
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

            // Hammer Slot
            addSlotToContainer(new SlotTileCore(cap, SLOT_HAMMER, 138, 35, tile));
        }
        // Special Display Slot
        addSlotToContainer(new SlotDisplay(tile.getDisplayInventory(), SLOT_DISPLAY, 80, 20));
    }

    private boolean damageHammer()
    {
        Slot slot = inventorySlots.get(SLOT_HAMMER);
        if (slot != null)
        {
            ItemStack stack = slot.getStack();
            if (!stack.isEmpty())
            {
                stack.damageItem(1, player);
                if (stack.getCount() <= 0)
                {
                    slot.putStack(ItemStack.EMPTY);
                }
                else
                {
                    slot.putStack(stack);
                }
                return true;
            }
        }
        return false;
    }
}
