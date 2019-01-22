/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.integration;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Optional;

import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.util.material.MaterialRegistry;
import com.alcatrazescapee.tinkersforging.util.material.MaterialType;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;

public final class TinkersIntegration
{
    @Optional.Method(modid = "tconstruct")
    public static void init()
    {
        updateSharedConfig();
    }

    @Optional.Method(modid = "tconstruct")
    public static void addAllMaterials()
    {
        for (Material material : TinkerRegistry.getAllMaterials())
        {
            if (material.isCastable())
            {
                HeadMaterialStats headStats = material.getStats("head");
                if (headStats != null)
                {
                    float baseTemp = 200 * headStats.harvestLevel + 200;
                    if (material.hasFluid())
                    {
                        baseTemp = material.getFluid().getTemperature();
                    }
                    float meltTemp = Math.max(300, baseTemp * 3.5f - 250);
                    float workTemp = MathHelper.clamp(meltTemp * 0.8f, 150, 1400);
                    TinkersForging.getLog().info("Temps for {} = {}, {}, {}", material.getIdentifier(), baseTemp, meltTemp, workTemp);

                    MaterialType materialTF = new MaterialType(material.getIdentifier(), material.materialTextColor, headStats.harvestLevel, workTemp, meltTemp);
                    materialTF.setEnabled();

                    MaterialRegistry.addMaterial(materialTF);
                    MaterialRegistry.addTinkersMaterial(materialTF);
                }
            }
        }
    }

    @Optional.Method(modid = "tconstruct")
    public static void updateSharedConfig()
    {
        // Hey Tinkers Construct, stop trying to access the tinker's anvil display inventory slot!
        Config.craftingStationBlacklist.add(MOD_ID + ":tinkers_anvil");
    }
}
