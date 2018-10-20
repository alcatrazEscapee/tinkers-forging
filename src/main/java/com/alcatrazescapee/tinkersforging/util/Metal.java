/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.util;

import java.awt.*;
import javax.annotation.Nullable;

import net.minecraft.item.Item;

import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.TinkersForging;

public enum Metal
{
    IRON(new Color(255, 255, 255)),
    GOLD(new Color(243, 234, 83)),
    COPPER(new Color(207, 134, 101)),
    TIN(new Color(160, 173, 179)),
    BRONZE(new Color(184, 115, 51)),
    STEEL(new Color(128, 128, 128)),
    LEAD(new Color(121, 102, 147)),
    SILVER(new Color(239, 246, 255)),
    ALUMINIUM_BRASS(new Color(255, 222, 83)),
    ALUMINIUM(new Color(224, 224, 224)),
    ARDITE(new Color(220, 84, 43)),
    COBALT(new Color(35, 118, 221)),
    MANYULLYN(new Color(113, 65, 172));

    private final int color;
    private final Item.ToolMaterial material;

    Metal(Color color)
    {
        this(color, null);
    }

    Metal(Color color, @Nullable Item.ToolMaterial material)
    {
        this.color = color.getRGB();
        this.material = material;
    }

    public int getColor()
    {
        return color;
    }

    @Nullable
    public Item.ToolMaterial getMaterial()
    {
        return material;
    }

    public int getTier()
    {
        switch (this)
        {
            case IRON:
                return ModConfig.TOOLS.tierIron;
            case GOLD:
                return ModConfig.TOOLS.tierGold;
            case COPPER:
                return ModConfig.TOOLS.tierCopper;
            case TIN:
                return ModConfig.TOOLS.tierTin;
            case BRONZE:
                return ModConfig.TOOLS.tierBronze;
            case STEEL:
                return ModConfig.TOOLS.tierSteel;
            case SILVER:
                return ModConfig.TOOLS.tierSilver;
            case LEAD:
                return ModConfig.TOOLS.tierLead;
            case ALUMINIUM:
                return ModConfig.TOOLS.tierAluminium;
            case ALUMINIUM_BRASS:
                return ModConfig.TOOLS.tierAluminiumBrass;
            case COBALT:
                return ModConfig.TOOLS.tierCobalt;
            case ARDITE:
                return ModConfig.TOOLS.tierArdite;
            case MANYULLYN:
                return ModConfig.TOOLS.tierManyullyn;
            default:
                TinkersForging.getLog().warn("Missing tier config check!");
                return 0;
        }
    }

    public boolean isEnabled()
    {
        switch (this)
        {
            case IRON:
                return ModConfig.COMPAT.enableIron;
            case GOLD:
                return ModConfig.COMPAT.enableGold;
            case COPPER:
                return ModConfig.COMPAT.enableCopper;
            case TIN:
                return ModConfig.COMPAT.enableTin;
            case BRONZE:
                return ModConfig.COMPAT.enableBronze;
            case STEEL:
                return ModConfig.COMPAT.enableSteel;
            case SILVER:
                return ModConfig.COMPAT.enableSilver;
            case LEAD:
                return ModConfig.COMPAT.enableLead;
            case ALUMINIUM:
                return ModConfig.COMPAT.enableAluminium;
            case ALUMINIUM_BRASS:
                return ModConfig.COMPAT.enableAluminiumBrass;
            case COBALT:
                return ModConfig.COMPAT.enableCobalt;
            case ARDITE:
                return ModConfig.COMPAT.enableArdite;
            case MANYULLYN:
                return ModConfig.COMPAT.enableManyullyn;
            default:
                TinkersForging.getLog().warn("Missing enable config check!");
                return false;
        }
    }
}
