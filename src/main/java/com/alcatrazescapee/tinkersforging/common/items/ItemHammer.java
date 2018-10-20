/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.items;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.alcatrazcore.item.tool.ItemToolCore;
import com.alcatrazescapee.tinkersforging.util.Metal;

@ParametersAreNonnullByDefault
public class ItemHammer extends ItemToolCore
{
    private static final Map<Metal, ItemHammer> MAP = new HashMap<>();

    @Nullable
    public static ItemHammer get(Metal metal)
    {
        return MAP.get(metal);
    }

    @Nonnull
    public static ItemStack get(Metal metal, int amount)
    {
        ItemHammer item = get(metal);
        return item != null ? new ItemStack(item, amount) : ItemStack.EMPTY;
    }

    private final Metal metal;

    public ItemHammer(Metal metal, ToolMaterial material)
    {
        super(material, 2.0f, -3.0f);

        this.metal = metal;
        MAP.put(metal, this);
    }

    public Metal getMetal()
    {
        return metal;
    }
}
