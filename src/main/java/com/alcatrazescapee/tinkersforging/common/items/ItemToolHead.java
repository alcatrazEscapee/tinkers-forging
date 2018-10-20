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

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.alcatrazcore.item.ItemCore;
import com.alcatrazescapee.alcatrazcore.util.collections.EnumTable;
import com.alcatrazescapee.tinkersforging.util.ItemType;
import com.alcatrazescapee.tinkersforging.util.Metal;

@ParametersAreNonnullByDefault
public class ItemToolHead extends ItemCore
{
    private static final EnumTable<ItemType, Metal, ItemToolHead> TABLE = new EnumTable<>(ItemType.class, Metal.class);

    @Nullable
    public static ItemToolHead get(ItemType type, Metal metal)
    {
        return TABLE.get(type, metal);
    }

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

    private final ItemType type;
    private final Metal metal;

    public ItemToolHead(ItemType type, Metal metal)
    {
        this.type = type;
        this.metal = metal;
        TABLE.put(type, metal, this);
    }

    public ItemType getType()
    {
        return type;
    }

    public Metal getMetal()
    {
        return metal;
    }
}
