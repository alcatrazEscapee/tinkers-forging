package com.alcatrazescapee.tinkersforging.common.items;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import com.alcatrazescapee.tinkersforging.common.blocks.BlockTinkersAnvil;
import com.alcatrazescapee.tinkersforging.util.material.MaterialType;

@ParametersAreNonnullByDefault
public class ItemBlockTinkersAnvil extends ItemBlock
{
    private final MaterialType material;

    public ItemBlockTinkersAnvil(Block block)
    {
        super(block);

        this.material = ((BlockTinkersAnvil) block).getMaterial();
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public String getItemStackDisplayName(ItemStack stack)
    {
        String translationKey = getTranslationKey() + ".name";
        String materialKey = material.getTranslationKey();
        return I18n.translateToLocalFormatted(translationKey, I18n.translateToLocal(materialKey));
    }
}
