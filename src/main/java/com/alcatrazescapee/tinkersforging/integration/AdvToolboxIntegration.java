package com.alcatrazescapee.tinkersforging.integration;

import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import api.materials.HeadMaterial;
import api.materials.Materials;
import com.alcatrazescapee.tinkersforging.common.recipe.AnvilRecipe;
import com.alcatrazescapee.tinkersforging.common.recipe.ModRecipes;
import com.alcatrazescapee.tinkersforging.util.ItemType;
import com.alcatrazescapee.tinkersforging.util.material.MaterialRegistry;
import com.alcatrazescapee.tinkersforging.util.material.MaterialType;
import toolbox.common.items.parts.ItemToolHead;

import static com.alcatrazescapee.alcatrazcore.util.OreDictionaryHelper.UPPER_UNDERSCORE_TO_LOWER_CAMEL;

public final class AdvToolboxIntegration
{
    @Optional.Method(modid = "toolbox")
    public static void addAllMaterials()
    {
        for (Map.Entry<String, HeadMaterial> entry : Materials.head_registry.entrySet())
        {
            HeadMaterial mat = entry.getValue();
            String name = mat.getName();
            MaterialType tfMaterial = MaterialRegistry.getMaterial(name);
            if (tfMaterial != null)
            {
                MaterialRegistry.addToolboxMaterial(tfMaterial);
            }
        }
    }

    @Optional.Method(modid = "toolbox")
    public static void addRecipes()
    {
        for (MaterialType material : MaterialRegistry.getAllMaterials())
        {
            for (ItemType type : ItemType.advToolbox())
            {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("toolbox", type.name().substring(4).toLowerCase()));
                if (item instanceof ItemToolHead)
                {
                    ItemToolHead toolPart = (ItemToolHead) item;
                    Integer meta = getMetaFromBadlyDesignedMap(toolPart.meta_map, m -> m.getName().equals(material.getName()));
                    if (meta == null) continue;
                    ItemStack output = new ItemStack(toolPart, 1, meta);

                    String inputOre = UPPER_UNDERSCORE_TO_LOWER_CAMEL.convert("INGOT_" + material.getName());

                    if (!output.isEmpty() && inputOre != null && OreDictionary.doesOreNameExist(inputOre))
                        ModRecipes.ANVIL.add(new AnvilRecipe(output, inputOre, type.getAmount(), material.getTier(), type.getRules()));
                }
            }
        }
    }

    @Nullable
    private static <K, V> K getMetaFromBadlyDesignedMap(Map<K, V> map, Predicate<V> valueTest)
    {
        for (Map.Entry<K, V> entry : map.entrySet())
        {
            if (valueTest.test(entry.getValue()))
            {
                return entry.getKey();
            }
        }
        return null;
    }
}
