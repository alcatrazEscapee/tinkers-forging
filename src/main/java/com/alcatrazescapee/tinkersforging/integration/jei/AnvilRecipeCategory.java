/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import com.alcatrazescapee.tinkersforging.common.recipe.AnvilRecipe;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeRule;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_ID;
import static com.alcatrazescapee.tinkersforging.TinkersForging.MOD_NAME;
import static com.alcatrazescapee.tinkersforging.integration.jei.JEIIntegration.ANVIL_UID;

@ParametersAreNonnullByDefault
public class AnvilRecipeCategory implements IRecipeCategory<AnvilRecipeCategory.Wrapper>
{
    private static final String TRANSLATION_KEY = MOD_ID + ".jei.category.anvil";
    private static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation(MOD_ID, "textures/jei/anvil.png");

    private final IDrawable background;
    private final IDrawable icon;

    AnvilRecipeCategory(IGuiHelper guiHelper)
    {
        background = guiHelper.createDrawable(BACKGROUND_LOCATION, 0, 0, 135, 50);
        icon = guiHelper.createDrawable(BACKGROUND_LOCATION, 215, 0, 16, 16);
    }

    @Override
    @Nonnull
    public String getUid()
    {
        return ANVIL_UID;
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public String getTitle()
    {
        return I18n.format(TRANSLATION_KEY);
    }

    @Override
    @Nonnull
    public String getModName()
    {
        return MOD_NAME;
    }

    @Override
    @Nonnull
    public IDrawable getBackground()
    {
        return background;
    }

    @Nullable
    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients)
    {
        int index = 0;

        recipeLayout.getItemStacks().init(index, true, 0, 7);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(ItemStack.class).get(index));

        index++;
        recipeLayout.getItemStacks().init(index, true, 117, 17);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(ItemStack.class).get(index));

        index++;
        recipeLayout.getItemStacks().init(index, false, 59, 2);
        recipeLayout.getItemStacks().set(index, ingredients.getOutputs(ItemStack.class).get(0));
    }

    public static class Wrapper implements IRecipeWrapper
    {
        private final List<List<ItemStack>> inputLists;
        private final ItemStack output;
        private final ForgeRule[] rules;
        private final IDrawable[] ruleDrawables;

        public Wrapper(AnvilRecipe recipe)
        {
            inputLists = new ArrayList<>();
            inputLists.add(recipe.getInput().getStacks());
            inputLists.add(OreDictionary.getOres("hammer"));

            output = recipe.getOutput();
            rules = recipe.getRules();
            ruleDrawables = new IDrawable[rules.length * 2];

            for (int i = 0; i < rules.length; i++)
            {
                ForgeRule rule = rules[i];
                if (rule != null)
                {
                    // The rule icon
                    ruleDrawables[i] = JEIIntegration.guiHelper.createDrawable(BACKGROUND_LOCATION, rule.getIconU() - 41, rule.getIconV(), 14, 14);
                    // The color / border
                    ruleDrawables[i + rules.length] = JEIIntegration.guiHelper.createDrawable(BACKGROUND_LOCATION, rule.getOutlineU() + 18 - 41, rule.getOutlineV(), 18, 24);
                }
            }
        }

        @Override
        public void getIngredients(@Nonnull IIngredients ingredients)
        {
            ingredients.setInputLists(ItemStack.class, inputLists);
            ingredients.setOutput(ItemStack.class, output);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
        {
            for (int i = 0; i < rules.length; i++)
            {
                ruleDrawables[i].draw(minecraft, 39 + 22 * i, 27);
                ruleDrawables[i + rules.length].draw(minecraft, 37 + 22 * i, 25);
            }
        }

        @Override
        @Nonnull
        public List<String> getTooltipStrings(int mouseX, int mouseY)
        {
            for (int i = 0; i < rules.length; i++)
            {
                if (mouseX >= 37 && mouseY >= 25 && mouseX < 37 + 22 * i + 18 && mouseY < 25 + 24)
                {
                    ForgeRule rule = rules[i];
                    if (rule != null)
                    {
                        return Collections.singletonList(I18n.format(MOD_ID + ".tooltip." + rule.name().toLowerCase()));
                    }
                }
            }
            return IRecipeWrapper.super.getTooltipStrings(mouseX, mouseY);
        }
    }
}
