/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.client;

import javax.annotation.Nonnull;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.tinkersforging.common.items.ItemToolHead;
import com.alcatrazescapee.tinkersforging.util.ItemType;
import com.alcatrazescapee.tinkersforging.util.Metal;

import static com.alcatrazescapee.tinkersforging.ModConstants.MOD_ID;

public final class ModCreativeTabs
{
    public static final CreativeTabs TAB_ITEMS = new CreativeTabs(MOD_ID)
    {
        @SideOnly(Side.CLIENT)
        @Override
        @Nonnull
        public ItemStack createIcon()
        {
            return ItemToolHead.get(ItemType.PICKAXE_HEAD, Metal.IRON, 1);
        }
    };
}
