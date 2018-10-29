/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.container;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.alcatrazescapee.alcatrazcore.inventory.container.ContainerTileInventory;
import com.alcatrazescapee.alcatrazcore.inventory.slot.SlotOutput;
import com.alcatrazescapee.alcatrazcore.inventory.slot.SlotTileCore;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem;
import com.alcatrazescapee.tinkersforging.common.capability.IForgeItem;
import com.alcatrazescapee.tinkersforging.common.recipe.AnvilRecipe;
import com.alcatrazescapee.tinkersforging.common.recipe.ModRecipes;
import com.alcatrazescapee.tinkersforging.common.slot.SlotDisplay;
import com.alcatrazescapee.tinkersforging.common.slot.SlotForgeInput;
import com.alcatrazescapee.tinkersforging.common.tile.TileTinkersAnvil;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeStep;

import static com.alcatrazescapee.tinkersforging.ModConstants.MOD_ID;
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
                if (canWork(buttonID % 4))
                    tile.addStep(ForgeStep.valueOf(buttonID));
                break;
        }
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
        // Slot that was clicked
        Slot slot = inventorySlots.get(index);
        if (slot == null || !slot.getHasStack())
            return ItemStack.EMPTY;

        ItemStack stack = slot.getStack().copy();
        ItemStack stackCopy = stack.copy();
        int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size(); // number of slots in the container

        if (index < containerSlots)
        {
            stack = slot.onTake(player, stack);
            // Transfer out of the container
            if (!this.mergeItemStack(stack, containerSlots, inventorySlots.size(), true))
            {
                return ItemStack.EMPTY;
            }
        }
        else
        {
            // Transfer into the container
            for (int i = 0; i < containerSlots; i++)
            {
                if (inventorySlots.get(i).isItemValid(stack))
                {
                    if (this.mergeItemStack(stack, i, i + 1, false))
                    {
                        tile.setAndUpdateSlots(i);
                    }
                }
            }
        }

        // Required
        if (stack.getCount() == 0)
        {
            slot.putStack(ItemStack.EMPTY);
        }
        else
        {
            slot.putStack(stack);
            slot.onSlotChanged();
        }
        if (stack.getCount() == stackCopy.getCount())
        {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return stackCopy;
    }

    @Override
    protected void addContainerSlots()
    {
        IItemHandler cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null)
        {
            // Forging Slots
            addSlotToContainer(new SlotForgeInput(cap, SLOT_INPUT, 21, 25, tile));
            addSlotToContainer(new SlotOutput(cap, SLOT_OUTPUT, 21, 45));

            // Hammer Slot
            addSlotToContainer(new SlotTileCore(cap, SLOT_HAMMER, 138, 35, tile));

            // Display Slot
            addSlotToContainer(new SlotDisplay(cap, SLOT_DISPLAY, 80, 20));
        }
    }

    private boolean canWork(int amount)
    {
        Slot slotInput = inventorySlots.get(SLOT_INPUT);
        if (slotInput != null)
        {
            ItemStack stack = slotInput.getStack();
            IForgeItem cap = stack.getCapability(CapabilityForgeItem.CAPABILITY, null);

            if (cap != null)
            {
                AnvilRecipe recipe = ModRecipes.ANVIL.getByName(cap.getRecipeName());
                if (recipe == null)
                {
                    return false;
                }
                if (tile.getTier() < recipe.getTier())
                {
                    TinkersForging.getLog().info("Tier is {}, Requires {}.", recipe.getTier(), tile.getTier());
                    player.sendMessage(new TextComponentString("" + TextFormatting.RED).appendSibling(new TextComponentTranslation(MOD_ID + ".tooltip.tier_too_low")));
                    return false;
                }
            }
        }

        Slot slot = inventorySlots.get(SLOT_HAMMER);
        if (slot != null)
        {
            ItemStack stack = slot.getStack();
            if (!stack.isEmpty())
            {
                stack.damageItem(amount, player);
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
        player.sendMessage(new TextComponentString("" + TextFormatting.RED).appendSibling(new TextComponentTranslation(MOD_ID + ".tooltip.no_hammer")));
        return false;
    }
}