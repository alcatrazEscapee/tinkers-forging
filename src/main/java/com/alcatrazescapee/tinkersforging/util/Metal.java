/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.util;

import java.awt.*;
import java.util.EnumSet;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;

import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.common.ModMaterials;

@ParametersAreNonnullByDefault
public enum Metal
{
    // Vanilla Materials
    IRON(new Color(255, 255, 255), Item.ToolMaterial.IRON, true, 1350f, 1600f),
    GOLD(new Color(255, 248, 53), Item.ToolMaterial.GOLD, false, 700f, 1100f),
    // Common Modded Materials
    COPPER("ingotCopper", new Color(207, 134, 101), 600f, 1000f),
    TIN("ingotTin", new Color(120, 143, 149), 150f, 300f),
    BRONZE("ingotBronze", new Color(184, 115, 51), 700f, 950f),
    STEEL("ingotSteel", new Color(128, 128, 128), 950f, 1350f),
    LEAD("ingotLead", new Color(101, 82, 127), 200f, 350f),
    SILVER("ingotSilver", new Color(239, 246, 255), 700f, 950f),
    ALUMINIUM("ingotAluminium", new Color(224, 224, 224), 450f, 700f),
    ELECTRUM("ingotElectrum", new Color(255, 241, 94), 650f, 900f),
    // Tinkers Construct Materials
    ARDITE("ingotArdite", new Color(220, 84, 43), 1350f, 2100f),
    COBALT("ingotCobalt", new Color(35, 118, 221), 1350f, 1600f),
    MANYULLYN("ingotManyullyn", new Color(113, 65, 172), 1350f, 2100f),
    PIGIRON(() -> Loader.isModLoaded("tconstruct") && CoreHelpers.doesOreHaveStack("ingotPigiron"), new Color(254, 188, 188), 400f, 250f),
    // Base Metals
    BRASS("ingotBrass", new Color(227, 174, 31), 650f, 900f),
    MITHRIL("ingotMithril", new Color(230, 250, 240), 1250f, 3200f),
    INVAR("ingotInvar", new Color(160, 173, 189), 1050f, 1450f);

    private static final EnumSet<Metal> NTP_METALS = EnumSet.of(IRON, GOLD, COPPER, TIN, BRONZE, STEEL);

    private final int color;
    private final Item.ToolMaterial material;
    private final BooleanSupplier precondition;
    private final boolean isTinkersMetal;
    private final float workTemp;
    private final float meltTemp;

    Metal(String oreName, Color color, float work, float melt)
    {
        this(() -> CoreHelpers.doesOreHaveStack(oreName), color, null, true, work, melt);
    }

    Metal(BooleanSupplier precondition, Color color, float work, float melt)
    {
        this(precondition, color, null, true, work, melt);
    }

    Metal(Color color, @Nullable Item.ToolMaterial material, boolean isTinkersMetal, float work, float melt)
    {
        this(() -> true, color, material, isTinkersMetal, work, melt);
    }

    Metal(BooleanSupplier precondition, Color color, @Nullable Item.ToolMaterial material, boolean isTinkersMetal, float work, float melt)
    {
        this.precondition = precondition;
        this.color = color.getRGB();
        this.material = material;
        this.isTinkersMetal = isTinkersMetal;
        this.workTemp = work;
        this.meltTemp = melt;
    }

    public int getColor()
    {
        return color;
    }

    public float getMeltTemp()
    {
        return meltTemp;
    }

    public float getWorkTemp()
    {
        return workTemp;
    }

    @Nullable
    public Item.ToolMaterial getMaterial()
    {
        if (material != null) return material;
        switch (getTier())
        {
            case 1:
                return ModMaterials.TOOL_TIER_1;
            case 2:
                return ModMaterials.TOOL_TIER_2;
            case 3:
                return ModMaterials.TOOL_TIER_3;
            case 4:
                return ModMaterials.TOOL_TIER_4;
            case 5:
                return ModMaterials.TOOL_TIER_5;
            default:
                return ModMaterials.TOOL_TIER_0;
        }
    }

    public boolean isEnabled()
    {
        return precondition.getAsBoolean();
    }

    public boolean isTinkersMetal()
    {
        return isTinkersMetal;
    }

    public boolean isNTPMetal()
    {
        return NTP_METALS.contains(this);
    }

    public int getTier()
    {
        switch (this)
        {
            case IRON:
            case GOLD:
                return material != null ? material.getHarvestLevel() : 1;
            case COPPER:
                return ModConfig.MATERIALS.tierCopper;
            case TIN:
                return ModConfig.MATERIALS.tierTin;
            case BRONZE:
                return ModConfig.MATERIALS.tierBronze;
            case STEEL:
                return ModConfig.MATERIALS.tierSteel;
            case SILVER:
                return ModConfig.MATERIALS.tierSilver;
            case LEAD:
                return ModConfig.MATERIALS.tierLead;
            case ALUMINIUM:
                return ModConfig.MATERIALS.tierAluminium;
            case COBALT:
                return ModConfig.MATERIALS.tierCobalt;
            case ARDITE:
                return ModConfig.MATERIALS.tierArdite;
            case MANYULLYN:
                return ModConfig.MATERIALS.tierManyullyn;
            case BRASS:
                return ModConfig.MATERIALS.tierBrass;
            case INVAR:
                return ModConfig.MATERIALS.tierInvar;
            case MITHRIL:
                return ModConfig.MATERIALS.tierMithril;
            case PIGIRON:
                return ModConfig.MATERIALS.tierPigiron;
            case ELECTRUM:
                return ModConfig.MATERIALS.tierElectrum;
            default:
                TinkersForging.getLog().warn("Missing tier config check!");
                return 0;
        }
    }
}
