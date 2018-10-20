/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.util;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.alcatrazcore.inventory.crafting.InventoryCraftingEmpty;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeRule;

import static com.alcatrazescapee.tinkersforging.util.forge.ForgeRule.*;

public enum ItemType
{
    PICKAXE_HEAD(PUNCH_LAST, BEND_NOT_LAST, DRAW_NOT_LAST),
    AXE_HEAD(PUNCH_LAST, HIT_SECOND_LAST, UPSET_THIRD_LAST),
    HOE_HEAD(PUNCH_LAST, HIT_NOT_LAST, BEND_NOT_LAST),
    SWORD_BLADE(HIT_LAST, BEND_SECOND_LAST, BEND_THIRD_LAST),
    SHOVEL_HEAD(PUNCH_NOT_LAST, HIT_NOT_LAST);

    private ForgeRule[] rules;

    ItemType(ForgeRule... rules)
    {
        this.rules = rules;
    }

    @Nonnull
    public ForgeRule[] getRules()
    {
        return rules;
    }

    public void setCraftingPattern(InventoryCraftingEmpty tempCrafting, ItemStack stack)
    {
        switch (this)
        {
            case PICKAXE_HEAD:
                tempCrafting.setInventorySlotContents(0, stack);
                tempCrafting.setInventorySlotContents(1, stack);
                tempCrafting.setInventorySlotContents(2, stack);
            case AXE_HEAD:
                tempCrafting.setInventorySlotContents(1, stack);
                tempCrafting.setInventorySlotContents(2, stack);
                tempCrafting.setInventorySlotContents(5, stack);
                // todo: more?
        }
    }
}
