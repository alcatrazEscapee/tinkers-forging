/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

import static com.alcatrazescapee.tinkersforging.ModConstants.MOD_ID;

public final class ModMaterials
{
    public static final Item.ToolMaterial TOOL_TIER_0 = EnumHelper.addToolMaterial(MOD_ID + ":tier_0", 0, 120, 1.0f, 0.5f, 2);
    public static final Item.ToolMaterial TOOL_TIER_1 = EnumHelper.addToolMaterial(MOD_ID + ":tier_1", 1, 250, 3.0f, 1.0f, 2);
    public static final Item.ToolMaterial TOOL_TIER_2 = EnumHelper.addToolMaterial(MOD_ID + ":tier_2", 2, 600, 6.0f, 1.5f, 2);
    public static final Item.ToolMaterial TOOL_TIER_3 = EnumHelper.addToolMaterial(MOD_ID + ":tier_3", 3, 1400, 8.0f, 2.0f, 2);
    public static final Item.ToolMaterial TOOL_TIER_4 = EnumHelper.addToolMaterial(MOD_ID + ":tier_4", 4, 1200, 12.0f, 2.5f, 2);
    public static final Item.ToolMaterial TOOL_TIER_5 = EnumHelper.addToolMaterial(MOD_ID + ":tier_5", 5, 1700, 10.0f, 3.0f, 2);
}
