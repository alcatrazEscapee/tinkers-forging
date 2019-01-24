package com.alcatrazescapee.tinkersforging.util.material;

import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.alcatrazcore.util.OreDictionaryHelper;
import com.alcatrazescapee.tinkersforging.common.capability.heat.IHeatRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.EnumHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BooleanSupplier;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;

@ParametersAreNonnullByDefault
public class MaterialType implements IHeatRegistry
{
    private static final Item.ToolMaterial[] TOOL_MATERIALS = {
            EnumHelper.addToolMaterial(MOD_ID + ":tier_0", 0, 120, 1.0f, 0.5f, 2),
            EnumHelper.addToolMaterial(MOD_ID + ":tier_1", 1, 250, 3.0f, 1.0f, 2),
            EnumHelper.addToolMaterial(MOD_ID + ":tier_2", 2, 600, 6.0f, 1.5f, 2),
            EnumHelper.addToolMaterial(MOD_ID + ":tier_3", 3, 1400, 8.0f, 2.0f, 2),
            EnumHelper.addToolMaterial(MOD_ID + ":tier_4", 4, 1200, 12.0f, 2.5f, 2),
            EnumHelper.addToolMaterial(MOD_ID + ":tier_5", 5, 1700, 10.0f, 3.0f, 2)
    };

    private final int color;
    private final int tier;
    private final float workTemp;
    private final float meltTemp;
    private final String name;
    private final String oreName;
    private final Item.ToolMaterial toolMaterial;
    private final BooleanSupplier precondition;
    private boolean enabled;

    public MaterialType(String name, int color, int tier, float workTemp, float meltTemp)
    {
        this(name, null, color, tier, workTemp, meltTemp);
    }

    public MaterialType(String name, @Nullable Item.ToolMaterial toolMaterial, int color, int tier, float workTemp, float meltTemp)
    {
        this.name = name;
        this.oreName = OreDictionaryHelper.UPPER_UNDERSCORE_TO_LOWER_CAMEL.convert("INGOT_" + name);

        // noinspection ConstantConditions
        this.precondition = () -> CoreHelpers.doesOreHaveStack(oreName);
        this.enabled = false;

        this.toolMaterial = toolMaterial;
        this.color = color;
        this.tier = MathHelper.clamp(tier, 0, 5);
        this.workTemp = MathHelper.clamp(100, workTemp, 1400);
        this.meltTemp = Math.max(workTemp + 100, meltTemp);
    }

    public MaterialType(String name, String oreName, BooleanSupplier precondition, @Nullable Item.ToolMaterial toolMaterial, int color, int tier, float workTemp, float meltTemp)
    {
        this.name = name;
        this.oreName = oreName;

        this.precondition = precondition;
        this.enabled = false;

        this.toolMaterial = toolMaterial;
        this.color = color;
        this.tier = MathHelper.clamp(tier, 0, 5);
        this.workTemp = workTemp;
        this.meltTemp = meltTemp;
    }

    @Nonnull
    public String getName()
    {
        return name;
    }

    public boolean isEnabled()
    {
        if (enabled)
        {
            return true;
        }
        enabled = precondition.getAsBoolean();
        return enabled;
    }

    @Override
    public boolean test(ItemStack stack) {
        return CoreHelpers.doesStackMatchOre(stack, oreName);
    }

    public void setEnabled()
    {
        this.enabled = true;
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public Item.ToolMaterial getToolMaterial()
    {
        return toolMaterial != null ? toolMaterial : TOOL_MATERIALS[tier];
    }

    public int getColor()
    {
        return color;
    }

    public int getTier()
    {
        return tier;
    }

    @Override
    public float getWorkTemp() {
        return workTemp;
    }

    @Override
    public float getMeltTemp() {
        return meltTemp;
    }
}
