/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.items;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.alcatrazcore.item.ItemCore;
import com.alcatrazescapee.alcatrazcore.util.collections.EnumTable;
import com.alcatrazescapee.tinkersforging.util.Metal;
import com.alcatrazescapee.tinkersforging.util.material.ItemType;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;

@ParametersAreNonnullByDefault
public class ItemToolHead extends ItemCore
{
    private static final EnumTable<ItemType, Metal, ItemToolHead> TABLE = new EnumTable<>(ItemType.class, Metal.class);

    @Nullable
    public static ItemToolHead get(ItemType type, Metal metal)
    {
        return TABLE.get(type, metal);
    }

    @Nonnull
    public static Set<ItemToolHead> getAll()
    {
        return TABLE.values();
    }

    @Nonnull
    public static ItemStack get(ItemType type, Metal metal, int amount)
    {
        ItemToolHead item = get(type, metal);
        return item == null ? ItemStack.EMPTY : new ItemStack(item, amount);
    }

    private final Metal metal;
    private final ItemType type;

    public ItemToolHead(ItemType type, Metal metal)
    {
        this.metal = metal;
        this.type = type;
        TABLE.put(type, metal, this);
    }

    @Nonnull
    public Metal getMetal()
    {
        return metal;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel()
    {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(MOD_ID + ":" + type.name().toLowerCase()));
    }
}
