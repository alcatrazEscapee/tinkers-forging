package com.alcatrazescapee.tinkersforging.integration;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;

public class TwilightIntegration
{
    public static void init()
    {
        // This is because twilight forest for some dumb reason doesn't register its Tinker's material with the same name as its ore dictionary name.
        ItemStack stack = CoreHelpers.getStackByRegistryName("twilightforest:fiery_ingot", OreDictionary.WILDCARD_VALUE);
        if (!stack.isEmpty())
        {
            OreDictionary.registerOre("ingotFierymetal", stack);
        }
    }
}
