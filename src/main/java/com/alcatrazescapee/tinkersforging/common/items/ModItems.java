/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import com.alcatrazescapee.alcatrazcore.util.RegistryHelper;
import com.alcatrazescapee.tinkersforging.util.ItemType;
import com.alcatrazescapee.tinkersforging.util.Metal;

import static com.alcatrazescapee.tinkersforging.ModConstants.MOD_ID;
import static com.alcatrazescapee.tinkersforging.client.ModCreativeTabs.TAB_ITEMS;

@GameRegistry.ObjectHolder(value = MOD_ID)
public final class ModItems
{
    public static void preInit()
    {
        RegistryHelper r = RegistryHelper.get(MOD_ID);

        for (Metal metal : Metal.values())
        {
            // todo: check if TiCon is enabled
            for (ItemType type : ItemType.values())
            {
                r.registerItem(new ItemToolHead(type, metal), type.name() + "/" + metal.name(), TAB_ITEMS);
            }

            // todo: sheets?
            // todo: double sheets?

            // todo: hammers + materials
            if (metal.getMaterial() != null)
            {
                r.registerItem(new ItemHammer(metal, metal.getMaterial()), "tinkers_hammer/" + metal.name(), TAB_ITEMS);
            }
        }
    }

    public static void init()
    {
        OreDictionary.registerOre("charcoal", new ItemStack(Items.COAL, 1, 1));
    }
}
