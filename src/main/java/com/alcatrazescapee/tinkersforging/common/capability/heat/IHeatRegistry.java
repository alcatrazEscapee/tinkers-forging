package com.alcatrazescapee.tinkersforging.common.capability.heat;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;

import static com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem.DEFAULT_MELT_TEMPERATURE;
import static com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem.DEFAULT_WORK_TEMPERATURE;

public interface IHeatRegistry extends Predicate<ItemStack>
{
    float getMeltTemp();

    float getWorkTemp();

    class Impl implements IHeatRegistry
    {
        @Override
        public float getMeltTemp()
        {
            return DEFAULT_MELT_TEMPERATURE;
        }

        @Override
        public float getWorkTemp()
        {
            return DEFAULT_WORK_TEMPERATURE;
        }

        @Override
        public boolean test(ItemStack itemStack)
        {
            return true;
        }
    }
}
