/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.util;

import javax.annotation.Nonnull;

import com.alcatrazescapee.tinkersforging.util.forge.ForgeRule;

import static com.alcatrazescapee.tinkersforging.util.forge.ForgeRule.*;

public enum ItemType
{
    // Vanilla Tool Heads
    PICKAXE_HEAD(true, false, true, 3, PUNCH_LAST, BEND_NOT_LAST, DRAW_NOT_LAST),
    AXE_HEAD(true, false, true, 3, PUNCH_LAST, HIT_SECOND_LAST, UPSET_THIRD_LAST),
    HOE_HEAD(true, false, true, 2, PUNCH_LAST, HIT_NOT_LAST, BEND_NOT_LAST),
    SWORD_BLADE(true, false, true, 2, HIT_LAST, BEND_SECOND_LAST, BEND_THIRD_LAST),
    SHOVEL_HEAD(true, false, true, 1, PUNCH_NOT_LAST, HIT_NOT_LAST),
    // Tinkers Forging Tool Heads
    HAMMER_HEAD(true, false, false, 5, PUNCH_LAST, SHRINK_NOT_LAST),
    // Armor
    HELMET(false, true, true, 5, BEND_LAST, BEND_SECOND_LAST, HIT_THIRD_LAST),
    CHESTPLATE(false, true, true, 8, HIT_LAST, BEND_ANY, UPSET_ANY),
    LEGGINGS(false, true, true, 7, HIT_ANY, BEND_ANY, DRAW_ANY),
    BOOTS(false, true, true, 4, BEND_LAST, BEND_SECOND_LAST, SHRINK_THIRD_LAST),
    // Tinkers Construct Tool Heads
    TC_PICK_HEAD(false, false, false, 2, PUNCH_LAST, BEND_NOT_LAST, DRAW_NOT_LAST),
    TC_AXE_HEAD(false, false, false, 2, PUNCH_LAST, HIT_SECOND_LAST, UPSET_THIRD_LAST),
    TC_HOE_HEAD(false, false, false, 2, PUNCH_LAST, HIT_NOT_LAST, BEND_NOT_LAST),
    TC_SWORD_BLADE(false, false, false, 2, HIT_LAST, BEND_SECOND_LAST, BEND_THIRD_LAST),
    TC_SHOVEL_HEAD(false, false, false, 2, PUNCH_NOT_LAST, HIT_NOT_LAST),
    TC_KNIFE_BLADE(false, false, false, 2, HIT_LAST, PUNCH_SECOND_LAST, PUNCH_THIRD_LAST),
    TC_BROAD_AXE_HEAD(false, false, false, 8, DRAW_LAST, HIT_NOT_LAST, UPSET_NOT_LAST),
    TC_LARGE_SWORD_BLADE(false, false, false, 8, DRAW_LAST, UPSET_SECOND_LAST, HIT_THIRD_LAST),
    TC_HAMMER_HEAD(false, false, false, 8, SHRINK_LAST, HIT_SECOND_LAST, HIT_THIRD_LAST),
    TC_EXCAVATOR_HEAD(false, false, false, 8, PUNCH_ANY, SHRINK_ANY, DRAW_THIRD_LAST),
    TC_SCYTHE_HEAD(false, false, false, 8, HIT_LAST, PUNCH_NOT_LAST, SHRINK_NOT_LAST),
    TC_PAN_HEAD(false, false, false, 3, HIT_LAST, DRAW_SECOND_LAST, HIT_THIRD_LAST),
    TC_SIGN_HEAD(false, false, false, 3, HIT_LAST, SHRINK_SECOND_LAST, HIT_THIRD_LAST),
    TC_KAMA_HEAD(false, false, false, 2, PUNCH_LAST, BEND_NOT_LAST),
    // Tinkers Construct Tool Parts
    TC_TOOL_ROD(false, false, false, 1, HIT_LAST, DRAW_ANY),
    TC_TOUGH_TOOL_ROD(false, false, false, 3, HIT_LAST, HIT_SECOND_LAST, DRAW_ANY),
    TC_BINDING(false, false, false, 1, HIT_LAST, HIT_SECOND_LAST),
    TC_TOUGH_BINDING(false, false, false, 3, PUNCH_LAST, PUNCH_SECOND_LAST, UPSET_ANY),
    TC_WIDE_GUARD(false, false, false, 1, PUNCH_LAST, HIT_NOT_LAST, BEND_NOT_LAST),
    TC_HAND_GUARD(false, false, false, 1, PUNCH_LAST, HIT_NOT_LAST, BEND_NOT_LAST),
    TC_CROSS_GUARD(false, false, false, 1, PUNCH_LAST, HIT_NOT_LAST, BEND_NOT_LAST),
    TC_LARGE_PLATE(false, false, false, 8, HIT_LAST, HIT_SECOND_LAST, HIT_THIRD_LAST);

    private final int amount;
    private final ForgeRule[] rules;

    private final boolean isItemType; // Is this used to create new items?
    private final boolean isArmorType; // Is this an armor piece (used for getting the crafting recipe)
    private final boolean isRecipeType; // Is this a crafting recipe item (used for getting crafting recipe)

    ItemType(boolean isItemType, boolean isArmorType, boolean isRecipeType, int amount, ForgeRule... rules)
    {
        this.isItemType = isItemType;
        this.isArmorType = isArmorType;
        this.isRecipeType = isRecipeType;

        this.amount = amount;
        this.rules = rules;
    }

    public int getAmount()
    {
        return amount;
    }

    @Nonnull
    public ForgeRule[] getRules()
    {
        return rules;
    }

    public boolean isArmorType()
    {
        return isArmorType;
    }

    public boolean isItemType()
    {
        return isItemType;
    }

    public boolean isRecipeType()
    {
        return isRecipeType;
    }
}
