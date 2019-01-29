package com.alcatrazescapee.tinkersforging.integration;

import net.minecraftforge.fml.common.Optional;

import com.alcatrazescapee.tinkersforging.util.material.MaterialRegistry;
import com.alcatrazescapee.tinkersforging.util.material.MaterialType;

public final class AdvToolboxIntegration
{
    private static final String[] ITEMS = {"pickaxe_head", "axe_head", "shovel_head", "hoe_head", "handpick_head", "hammer_head", "sword_blade", "sword_crossguard", "dagger_blade", "mace_head"};

    @Optional.Method(modid = "toolbox")
    public static void addAllMaterials()
    {
        // todo: stuff here
    }

    @Optional.Method(modid = "toolbox")
    public static void addRecipes()
    {
        for (MaterialType material : MaterialRegistry.getAllMaterials())
        {
            if (MaterialRegistry.isToolboxMaterial(material))
            {
                // todo: stuff here
            }
        }
    }
}
