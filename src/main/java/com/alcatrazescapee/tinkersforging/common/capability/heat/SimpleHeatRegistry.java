package com.alcatrazescapee.tinkersforging.common.capability.heat;

import net.minecraft.item.ItemStack;

import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.alcatrazcore.util.OreDictionaryHelper;
import com.alcatrazescapee.tinkersforging.util.Metal;

public class SimpleHeatRegistry implements IHeatRegistry
{
    private final String oreName;
    private final float workTemp;
    private final float meltTemp;

    public SimpleHeatRegistry(Metal metal)
    {
        this.oreName = OreDictionaryHelper.UPPER_UNDERSCORE_TO_LOWER_CAMEL.convert("INGOT_" + metal.name());
        this.workTemp = metal.getWorkTemp();
        this.meltTemp = metal.getMeltTemp();
    }

    @Override
    public boolean test(ItemStack stack)
    {
        return CoreHelpers.doesStackMatchOre(stack, oreName);
    }

    @Override
    public float getMeltTemp()
    {
        return meltTemp;
    }

    @Override
    public float getWorkTemp()
    {
        return workTemp;
    }
}
