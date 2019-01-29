package com.alcatrazescapee.tinkersforging.util.material;

import java.awt.*;
import java.util.*;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;

import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.integration.AdvToolboxIntegration;
import com.alcatrazescapee.tinkersforging.integration.TinkersIntegration;

@ParametersAreNonnullByDefault
public final class MaterialRegistry
{
    private static final Map<String, MaterialType> MATERIALS = new HashMap<>();
    private static final Set<String> TINKERS_MATERIALS = new HashSet<>();
    private static final Set<String> NTP_MATERIALS = new HashSet<>();
    private static final Set<String> TOOLBOX_MATERIALS = new HashSet<>();
    private static final MaterialType NULL = new MaterialType("null", "null", () -> false, null, 0, 0, 0, 0);

    public static void preInit()
    {
        // Vanilla Materials
        addMaterial(new MaterialType("iron", Item.ToolMaterial.IRON, new Color(255, 255, 255).getRGB(), 2, 1350f, 1600f));
        addMaterial(new MaterialType("gold", Item.ToolMaterial.GOLD, new Color(255, 248, 53).getRGB(), 1, 700f, 1100f));

        // Common Modded Materials
        addMaterial(new MaterialType("copper", new Color(207, 134, 101).getRGB(), 1, 600f, 1000f));
        addMaterial(new MaterialType("tin", new Color(120, 143, 149).getRGB(), 1, 150f, 300f));
        addMaterial(new MaterialType("bronze", new Color(184, 115, 51).getRGB(), 2, 700f, 950f));
        addMaterial(new MaterialType("steel", new Color(128, 128, 128).getRGB(), 3, 950f, 1350f));
        addMaterial(new MaterialType("lead", new Color(101, 82, 127).getRGB(), 1, 200f, 350f));
        addMaterial(new MaterialType("silver", new Color(239, 246, 255).getRGB(), 1, 700f, 950f));
        addMaterial(new MaterialType("aluminium", new Color(224, 224, 224).getRGB(), 1, 450f, 700f));

        // NTP Compat Materials
        NTP_MATERIALS.addAll(Arrays.asList("iron", "gold", "tin", "copper", "bronze", "steel"));

        // Adventurer's Toolbox Materials
        if (Loader.isModLoaded("toolbox"))
        {
            AdvToolboxIntegration.addAllMaterials();
        }

        // Tinker's Construct Materials
        if (Loader.isModLoaded("tconstruct"))
        {
            TinkersIntegration.addAllMaterials();
        }

        // Force Enable materials
        for (String s : ModConfig.GENERAL.forceEnabledMetals)
        {
            if (MATERIALS.containsKey(s))
            {
                MATERIALS.get(s).setEnabled();
            }
        }
    }

    public static void addMaterial(MaterialType material)
    {
        if (MATERIALS.containsKey(material.getName()))
        {
            TinkersForging.getLog().warn("Material {} was overriden!", material.getName());
        }
        MATERIALS.put(material.getName(), material);
    }

    public static void addTinkersMaterial(MaterialType material)
    {
        if (MATERIALS.containsKey(material.getName()))
        {
            TINKERS_MATERIALS.add(material.getName());
        }
    }

    public static void addToolboxMaterial(MaterialType material)
    {
        if (MATERIALS.containsKey(material.getName()))
        {
            TOOLBOX_MATERIALS.add(material.getName());
        }
    }

    @Nonnull
    public static Collection<MaterialType> getAllMaterials()
    {
        return MATERIALS.values();
    }

    @Nonnull
    public static MaterialType getMaterial(String name)
    {
        return MATERIALS.getOrDefault(name, NULL);
    }

    public static boolean isTinkersMaterial(MaterialType material)
    {
        return TINKERS_MATERIALS.contains(material.getName());
    }

    public static boolean isNTPMaterial(MaterialType material)
    {
        return NTP_MATERIALS.contains(material.getName());
    }

    public static boolean isToolboxMaterial(MaterialType material)
    {
        return TOOLBOX_MATERIALS.contains(material.getName());
    }
}
