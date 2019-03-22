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
    PICKAXE_HEAD(3, PUNCH_LAST, BEND_NOT_LAST, DRAW_NOT_LAST),
    AXE_HEAD(3, PUNCH_LAST, HIT_SECOND_LAST, UPSET_THIRD_LAST),
    HOE_HEAD(2, PUNCH_LAST, HIT_NOT_LAST, BEND_NOT_LAST),
    SWORD_BLADE(2, HIT_LAST, BEND_SECOND_LAST, BEND_THIRD_LAST),
    SHOVEL_HEAD(1, PUNCH_NOT_LAST, HIT_NOT_LAST),
    // Tinkers Forging Tool Heads
    HAMMER_HEAD(5, PUNCH_LAST, SHRINK_NOT_LAST),
    // Armor
    HELMET(5, BEND_LAST, BEND_SECOND_LAST, HIT_THIRD_LAST),
    CHESTPLATE(8, HIT_LAST, BEND_ANY, UPSET_ANY),
    LEGGINGS(7, HIT_ANY, BEND_ANY, DRAW_ANY),
    BOOTS(4, BEND_LAST, BEND_SECOND_LAST, SHRINK_THIRD_LAST),
    // Tinkers Construct Tool Heads
    TC_PICK_HEAD(2, PUNCH_LAST, BEND_NOT_LAST, DRAW_NOT_LAST),
    TC_AXE_HEAD(2, PUNCH_LAST, HIT_SECOND_LAST, UPSET_THIRD_LAST),
    TC_HOE_HEAD(2, PUNCH_LAST, HIT_NOT_LAST, BEND_NOT_LAST),
    TC_SWORD_BLADE(2, HIT_LAST, BEND_SECOND_LAST, BEND_THIRD_LAST),
    TC_SHOVEL_HEAD(2, PUNCH_NOT_LAST, HIT_NOT_LAST),
    TC_KNIFE_BLADE(2, HIT_LAST, PUNCH_SECOND_LAST, PUNCH_THIRD_LAST),
    TC_BROAD_AXE_HEAD(8, DRAW_LAST, HIT_NOT_LAST, UPSET_NOT_LAST),
    TC_LARGE_SWORD_BLADE(8, DRAW_LAST, UPSET_SECOND_LAST, HIT_THIRD_LAST),
    TC_HAMMER_HEAD(8, SHRINK_LAST, HIT_SECOND_LAST, HIT_THIRD_LAST),
    TC_EXCAVATOR_HEAD(8, PUNCH_ANY, SHRINK_ANY, DRAW_THIRD_LAST),
    TC_SCYTHE_HEAD(8, HIT_LAST, PUNCH_NOT_LAST, SHRINK_NOT_LAST),
    TC_PAN_HEAD(3, HIT_LAST, DRAW_SECOND_LAST, HIT_THIRD_LAST),
    TC_SIGN_HEAD(3, HIT_LAST, SHRINK_SECOND_LAST, HIT_THIRD_LAST),
    TC_KAMA_HEAD(2, PUNCH_LAST, BEND_NOT_LAST),
    // Tinkers Construct Tool Parts
    TC_TOOL_ROD(1, HIT_LAST, DRAW_ANY),
    TC_TOUGH_TOOL_ROD(3, HIT_LAST, HIT_SECOND_LAST, DRAW_ANY),
    TC_BINDING(1, HIT_LAST, HIT_SECOND_LAST),
    TC_TOUGH_BINDING(3, PUNCH_LAST, PUNCH_SECOND_LAST, UPSET_ANY),
    TC_WIDE_GUARD(1, PUNCH_LAST, HIT_NOT_LAST, BEND_NOT_LAST),
    TC_HAND_GUARD(1, PUNCH_LAST, HIT_NOT_LAST, BEND_NOT_LAST),
    TC_CROSS_GUARD(1, PUNCH_LAST, HIT_NOT_LAST, BEND_NOT_LAST),
    TC_LARGE_PLATE(8, HIT_LAST, HIT_SECOND_LAST, HIT_THIRD_LAST),
    // Construct's Armory Tool Parts
    CA_HELMET_CORE(4, BEND_LAST, BEND_SECOND_LAST, HIT_THIRD_LAST),
    CA_ARMOR_TRIM(1, HIT_LAST, HIT_SECOND_LAST),
    CA_ARMOR_PLATE(3, HIT_LAST, HIT_SECOND_LAST, HIT_THIRD_LAST),
    CA_CHEST_CORE(6, HIT_LAST, BEND_ANY, UPSET_ANY),
    CA_LEGGINGS_CORE(5, HIT_ANY, BEND_ANY, DRAW_ANY),
    CA_BOOTS_CORE(4, BEND_LAST, BEND_SECOND_LAST, SHRINK_THIRD_LAST),
    // No Tree Punching
    NTP_KNIFE(1, HIT_LAST, DRAW_NOT_LAST),
    NTP_MATTOCK(4, HIT_LAST, BEND_SECOND_LAST, UPSET_THIRD_LAST),
    NTP_SAW(3, PUNCH_LAST, HIT_SECOND_LAST, SHRINK_THIRD_LAST),
    // Adventurer's Toolbox
    ADV_PICKAXE_HEAD(3, PUNCH_LAST, BEND_NOT_LAST, DRAW_NOT_LAST),
    ADV_SHOVEL_HEAD(1, PUNCH_NOT_LAST, HIT_NOT_LAST),
    ADV_AXE_HEAD(3, PUNCH_LAST, HIT_SECOND_LAST, UPSET_THIRD_LAST),
    ADV_HOE_HEAD(2, PUNCH_LAST, HIT_NOT_LAST, BEND_NOT_LAST),
    ADV_HANDPICK_HEAD(2, PUNCH_LAST, BEND_NOT_LAST, HIT_NOT_LAST),
    ADV_HAMMER_HEAD(8, SHRINK_LAST, HIT_SECOND_LAST, HIT_THIRD_LAST),
    ADV_SWORD_BLADE(1, HIT_LAST, BEND_SECOND_LAST, BEND_THIRD_LAST),
    ADV_SWORD_CROSSGUARD(1, HIT_LAST, HIT_SECOND_LAST, BEND_THIRD_LAST),
    ADV_DAGGER_BLADE(1, BEND_LAST, BEND_NOT_LAST),
    ADV_MACE_HEAD(4, HIT_LAST, HIT_SECOND_LAST, SHRINK_THIRD_LAST);

    private static final ItemType[] allNewToolTypes = {AXE_HEAD, PICKAXE_HEAD, SHOVEL_HEAD, HOE_HEAD, SWORD_BLADE};
    private static final ItemType[] allArmorTypes = {HELMET, CHESTPLATE, LEGGINGS, BOOTS};
    private static final ItemType[] allTiConTypes = {TC_PICK_HEAD, TC_AXE_HEAD, TC_HOE_HEAD, TC_SWORD_BLADE, TC_SHOVEL_HEAD, TC_KNIFE_BLADE, TC_BROAD_AXE_HEAD, TC_LARGE_SWORD_BLADE, TC_HAMMER_HEAD, TC_EXCAVATOR_HEAD, TC_SCYTHE_HEAD, TC_PAN_HEAD, TC_SIGN_HEAD, TC_KAMA_HEAD, TC_TOOL_ROD, TC_TOUGH_TOOL_ROD, TC_BINDING, TC_TOUGH_BINDING, TC_WIDE_GUARD, TC_HAND_GUARD, TC_CROSS_GUARD, TC_LARGE_PLATE};
    private static final ItemType[] allConArmTypes = {CA_HELMET_CORE, CA_ARMOR_TRIM, CA_ARMOR_PLATE, CA_CHEST_CORE, CA_LEGGINGS_CORE, CA_BOOTS_CORE};
    private static final ItemType[] allNTPTypes = {NTP_KNIFE, NTP_MATTOCK, NTP_SAW};
    private static final ItemType[] allAdvToolboxTypes = {ADV_PICKAXE_HEAD, ADV_SHOVEL_HEAD, ADV_AXE_HEAD, ADV_HOE_HEAD, ADV_HANDPICK_HEAD, ADV_HAMMER_HEAD, ADV_SWORD_BLADE, ADV_SWORD_CROSSGUARD, ADV_DAGGER_BLADE, ADV_MACE_HEAD};

    public static ItemType[] tools()
    {
        return allNewToolTypes;
    }

    public static ItemType[] armors()
    {
        return allArmorTypes;
    }

    public static ItemType[] tinkersParts()
    {
        return allTiConTypes;
    }

    public static ItemType[] constructArmors()
    {
        return allConArmTypes;
    }

    public static ItemType[] ntpTools()
    {
        return allNTPTypes;
    }

    public static ItemType[] advToolbox()
    {
        return allAdvToolboxTypes;
    }

    private final int amount;
    private final ForgeRule[] rules;

    ItemType(int amount, ForgeRule... rules)
    {
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
}
