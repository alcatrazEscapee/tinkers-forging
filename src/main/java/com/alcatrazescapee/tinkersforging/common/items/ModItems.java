/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.items;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import com.alcatrazescapee.alcatrazcore.util.RegistryHelper;
import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.util.ItemType;
import com.alcatrazescapee.tinkersforging.util.material.MaterialRegistry;
import com.alcatrazescapee.tinkersforging.util.material.MaterialType;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;
import static com.alcatrazescapee.tinkersforging.client.ModCreativeTabs.TAB_ITEMS;

@GameRegistry.ObjectHolder(value = MOD_ID)
public final class ModItems
{
    public static void preInit()
    {
        RegistryHelper r = RegistryHelper.get(MOD_ID);

        for (MaterialType material : MaterialRegistry.getAllMaterials())
        {
            Item.ToolMaterial toolMaterial = material.getToolMaterial();

            r.registerItem(new ItemHammer(material, toolMaterial), "hammer/" + material.getName());
            r.registerItem(new ItemToolHead(ItemType.HAMMER_HEAD, material), ItemType.HAMMER_HEAD.name() + "/" + material.getName());

            if (!Loader.isModLoaded("tconstruct") || !ModConfig.GENERAL.useTinkersConstruct)
            {
                for (ItemType type : ItemType.tools())
                {
                    r.registerItem(new ItemToolHead(type, material), type.name() + "/" + material.getName());
                }
            }

            if (Loader.isModLoaded("notreepunching") && ModConfig.GENERAL.enableNoTreePunchingCompat && MaterialRegistry.isNTPMaterial(material))
            {
                for (ItemType type : ItemType.ntpTools())
                {
                    r.registerItem(new ItemToolHead(type, material), type.name() + "/" + material.getName());
                }
            }
        }

        r.registerItem(new ItemHammer(Item.ToolMaterial.WOOD), "hammer/wood", TAB_ITEMS);
        r.registerItem(new ItemHammer(Item.ToolMaterial.STONE), "hammer/stone", TAB_ITEMS);
        r.registerItem(new ItemHammer(Item.ToolMaterial.DIAMOND), "hammer/diamond", TAB_ITEMS);
    }

    public static void init()
    {
        // Add hammer creative tabs
        for (ItemHammer item : ItemHammer.getAll())
        {
            if (item.getMaterial() == null || item.getMaterial().isEnabled())
                item.setCreativeTab(TAB_ITEMS);
        }

        // Add tool part creative tabs
        for (ItemToolHead item : ItemToolHead.getAll())
        {
            if (item.getMaterial().isEnabled())
            {
                item.setCreativeTab(TAB_ITEMS);
                // Doing this here is not the best practice but it works
                item.setTranslationKey(MOD_ID + ":" + item.getType().name().toLowerCase());
            }
        }

        // Add charcoal ore dict
        OreDictionary.registerOre("charcoal", new ItemStack(Items.COAL, 1, 1));
    }
}
