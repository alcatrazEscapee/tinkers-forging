/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.integration.guide;

import java.awt.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.BookBinder;
import amerifrance.guideapi.category.CategoryItemStack;
import com.alcatrazescapee.tinkersforging.common.blocks.ModBlocks;
import com.alcatrazescapee.tinkersforging.common.items.ModItems;
import com.alcatrazescapee.tinkersforging.integration.guide.utils.CategoryBuilder;
import com.alcatrazescapee.tinkersforging.integration.guide.utils.ILazyLoader;

import static com.alcatrazescapee.tinkersforging.ModConstants.MOD_ID;
import static com.alcatrazescapee.tinkersforging.client.ModCreativeTabs.TAB_ITEMS;

@GuideBook
@SuppressWarnings("unused")
public class GuideIntegration implements IGuideBook
{
    private Book book;

    @Nullable
    @Override
    public Book buildBook()
    {
        return book = new BookBinder(new ResourceLocation(MOD_ID, "guide_book"))
                .addCategory(new CategoryBuilder(new CategoryItemStack(MOD_ID + ".guide.category.intro", new ItemStack(ModBlocks.IRON_ANVIL)).withKeyBase("intro"))
                        .addEntry(MOD_ID + ".guide.intro.overview")
                        .addPage(MOD_ID + ".guide.intro.page.1")
                        .build())
                .addCategory(new CategoryBuilder(new CategoryItemStack(MOD_ID + ".guide.category.hammer", new ItemStack(ModItems.IRON_HAMMER)).withKeyBase("hammer"))
                        .addEntry(MOD_ID + ".guide.hammer.overview")
                        .addPage(MOD_ID + ".guide.hammer.page.1")
                        .build())
                .setGuideTitle(MOD_ID + ".guide.title")
                .setHeader(MOD_ID + ".guide.title")
                .setItemName(MOD_ID + ".guide.item_name")
                .setAuthor("AlcatrazEscapee")
                .setColor(new Color(120, 20, 20))
                .setCreativeTab(TAB_ITEMS)
                .build();
    }

    @Nonnull
    @Override
    public IRecipe getRecipe(@Nonnull ItemStack bookStack)
    {
        // This needs to use an alternate mod id otherwise it gets 'dangerous alternative prefix' warnings
        ResourceLocation loc = new ResourceLocation("guideapi", MOD_ID + "_guide_book");
        return new ShapedOreRecipe(loc, bookStack, " S ", "SRS", " S ", 'S', Items.BOOK, 'R', "nuggetIron").setRegistryName(loc);
    }

    @Override
    public void handlePost(@Nonnull ItemStack bookStack)
    {
        ILazyLoader.init(book);
    }
}
