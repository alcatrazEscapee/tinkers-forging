/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

import com.alcatrazescapee.tinkersforging.ModConfig;

import static com.alcatrazescapee.tinkersforging.ModConstants.MOD_ID;

public final class ModMaterials
{
    public static final Item.ToolMaterial COPPER = EnumHelper.addToolMaterial(MOD_ID + ":copper", ModConfig.TOOLS.tierCopper, 250, 2.0f, 1.0f, 2);
    public static final Item.ToolMaterial TIN = EnumHelper.addToolMaterial(MOD_ID + ":tin", ModConfig.TOOLS.tierTin, 120, 1.0f, 0.5f, 2);
    public static final Item.ToolMaterial BRONZE = EnumHelper.addToolMaterial(MOD_ID + ":bronze", ModConfig.TOOLS.tierBronze, 400, 4.0f, 1.5f, 2);
    public static final Item.ToolMaterial STEEL = EnumHelper.addToolMaterial(MOD_ID + ":steel", ModConfig.TOOLS.tierSteel, 1400, 8.0f, 2.5f, 5);
    public static final Item.ToolMaterial LEAD = EnumHelper.addToolMaterial(MOD_ID + ":lead", ModConfig.TOOLS.tierLead, 250, 2.0f, 1.0f, 1);
    public static final Item.ToolMaterial SILVER = EnumHelper.addToolMaterial(MOD_ID + ":silver", ModConfig.TOOLS.tierSilver, 400, 4.0f, 1.5f, 1);
    public static final Item.ToolMaterial ALUMINIUM = EnumHelper.addToolMaterial(MOD_ID + ":aluminium", ModConfig.TOOLS.tierAluminium, 250, 3.0f, 0.5f, 3);
    public static final Item.ToolMaterial ALUMINIUM_BRASS = EnumHelper.addToolMaterial(MOD_ID + ":aluminium_brass", ModConfig.TOOLS.tierAluminiumBrass, 400, 6.0f, 1.5f, 9);
    public static final Item.ToolMaterial ARDITE = EnumHelper.addToolMaterial(MOD_ID + ":ardite", ModConfig.TOOLS.tierArdite, 1800, 8.0f, 3.0f, 8);
    public static final Item.ToolMaterial COBALT = EnumHelper.addToolMaterial(MOD_ID + ":cobalt", ModConfig.TOOLS.tierCobalt, 1200, 12.0f, 1.0f, 8);
    public static final Item.ToolMaterial MANYULLYN = EnumHelper.addToolMaterial(MOD_ID + ":manyullyn", ModConfig.TOOLS.tierManyullyn, 1700, 10.0f, 2.5f, 6);




}
