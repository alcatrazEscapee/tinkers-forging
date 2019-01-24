/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.items;

import com.alcatrazescapee.alcatrazcore.item.ItemCore;
import com.alcatrazescapee.tinkersforging.util.ItemType;
import com.alcatrazescapee.tinkersforging.util.material.MaterialType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;

@ParametersAreNonnullByDefault
public class ItemToolHead extends ItemCore
{
    private static final Map<ItemType, Map<MaterialType, ItemToolHead>> TABLE = new HashMap<>();
    private final MaterialType material;

    public ItemToolHead(ItemType type, MaterialType material) {
        this.material = material;
        this.type = type;

        if (!TABLE.containsKey(type)) {
            TABLE.put(type, new HashMap<>());
        }
        TABLE.get(type).put(material, this);
    }

    @Nullable
    public static ItemToolHead get(ItemType type, MaterialType material)
    {
        return TABLE.containsKey(type) ? TABLE.get(type).get(material) : null;
    }

    @Nonnull
    public static Collection<ItemToolHead> getAll()
    {
        return TABLE.values().stream().map(Map::values).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private final ItemType type;

    @Nonnull
    public static ItemStack get(ItemType type, MaterialType material, int amount)
    {
        ItemToolHead item = get(type, material);
        return item == null ? ItemStack.EMPTY : new ItemStack(item, amount);
    }

    @Nonnull
    public MaterialType getMaterial()
    {
        return material;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel()
    {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(MOD_ID + ":" + type.name().toLowerCase()));
    }
}
