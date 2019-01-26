/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.items;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.alcatrazcore.item.tool.ItemToolCore;
import com.alcatrazescapee.alcatrazcore.util.OreDictionaryHelper;
import com.alcatrazescapee.tinkersforging.util.material.MaterialType;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;

@ParametersAreNonnullByDefault
public class ItemHammer extends ItemToolCore
{
    private static final Map<MaterialType, ItemHammer> MAP = new HashMap<>();
    private final MaterialType material;

    public ItemHammer(MaterialType material, ToolMaterial toolMaterial)
    {
        super(toolMaterial, 2.0f, -3.0f);

        this.efficiency = Math.min(efficiency - 2.0f, 1.0f);
        addToolClass(ToolClass.PICKAXE);

        this.material = material;
        MAP.put(material, this);
        //noinspection ConstantConditions
        setCreativeTab(null);

        OreDictionaryHelper.register(this, "hammer");
    }

    @Nonnull
    public static Collection<ItemHammer> getAll() {
        return MAP.values();
    }

    public ItemHammer(ToolMaterial material)
    {
        super(material, 2.0f, -3.0f);

        this.efficiency = Math.min(efficiency - 2.0f, 1.0f);
        addToolClass(ToolClass.PICKAXE);

        this.material = null;
        //noinspection ConstantConditions
        setCreativeTab(null);

        OreDictionaryHelper.register(this, "hammer");
    }

    @Nullable
    public static ItemHammer get(MaterialType material) {
        return MAP.get(material);
    }

    @Nonnull
    public static ItemStack get(MaterialType material, int amount) {
        ItemHammer item = get(material);
        return item == null ? ItemStack.EMPTY : new ItemStack(item, amount);
    }

    @Nullable
    public MaterialType getMaterial()
    {
        return material;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel()
    {
        if (material != null)
        {
            ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(MOD_ID + ":hammer"));
        }
        else
        {
            super.registerModel();
        }
    }
}
