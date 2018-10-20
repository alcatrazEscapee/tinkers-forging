/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import com.alcatrazescapee.alcatrazcore.tile.ITileFields;
import com.alcatrazescapee.alcatrazcore.tile.TileInventory;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.alcatrazcore.util.collections.ImmutablePair;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem;
import com.alcatrazescapee.tinkersforging.common.capability.IForgeItem;
import com.alcatrazescapee.tinkersforging.common.recipe.AnvilRecipe;
import com.alcatrazescapee.tinkersforging.common.recipe.ModRecipes;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeRule;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeStep;
import com.alcatrazescapee.tinkersforging.util.forge.ForgeSteps;

@ParametersAreNonnullByDefault
public class TileTinkersAnvil extends TileInventory implements ITileFields
{
    public static final int FIELD_PROGRESS = 0;
    public static final int FIELD_TARGET = 1;
    public static final int FIELD_LAST_STEP = 2;
    public static final int FIELD_SECOND_STEP = 3;
    public static final int FIELD_THIRD_STEP = 4;
    public static final int FIELD_FIRST_RULE = 5;
    public static final int FIELD_SECOND_RULE = 6;
    public static final int FIELD_THIRD_RULE = 7;

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int SLOT_WELD_1 = 2;
    public static final int SLOT_WELD_2 = 3;
    public static final int SLOT_WELD_OUTPUT = 4;
    public static final int SLOT_DISPLAY = 0;

    private ItemStackHandler displayInventory;
    private AnvilRecipe cachedAnvilRecipe;
    private ForgeSteps steps;
    private ForgeRule[] rules;
    private int workingProgress = 0; // Min = 0, Max = 150. If it goes over / under you lose the input
    private int workingTarget = 0;

    public TileTinkersAnvil()
    {
        super(5);

        steps = new ForgeSteps();
        rules = new ForgeRule[3];
        displayInventory = new ItemStackHandler(1);
    }

    @Override
    public void setAndUpdateSlots(int slot)
    {
        updateForgeRecipe(false);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        switch (slot)
        {
            case SLOT_INPUT:
                return stack.hasCapability(CapabilityForgeItem.CAPABILITY, null);
            case SLOT_WELD_1:
            case SLOT_WELD_2:
                return true; // todo: check if has a welding recipe
            default:
                return false;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        displayInventory.deserializeNBT(nbt.getCompoundTag("displayInv"));

        super.readFromNBT(nbt);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setTag("displayInv", displayInventory.serializeNBT());

        return super.writeToNBT(nbt);
    }

    @Nullable
    public AnvilRecipe getCurrentForgeRecipe()
    {
        return cachedAnvilRecipe;
    }

    public void cycleForgeRecipe(boolean isForwards)
    {
        if (cachedAnvilRecipe != null)
        {
            ItemStack stack = inventory.getStackInSlot(SLOT_INPUT);
            if (isForwards)
                cachedAnvilRecipe = ModRecipes.ANVIL.getNext(cachedAnvilRecipe, stack);
            else
                cachedAnvilRecipe = ModRecipes.ANVIL.getPrevious(cachedAnvilRecipe, stack);
        }
        updateForgeRecipe(true);
    }

    public void addStep(@Nullable ForgeStep step)
    {
        steps.addStep(step);
        markDirty();
        if (step != null && cachedAnvilRecipe != null)
        {
            ItemStack input = inventory.getStackInSlot(SLOT_INPUT);
            IForgeItem cap = input.getCapability(CapabilityForgeItem.CAPABILITY, null);
            if (cap != null)
            {
                cap.addStep(step);
                input.setTagInfo(CapabilityForgeItem.KEY.toString(), cap.serializeNBT());
            }
            workingProgress += step.getStepAmount();


            if (workingProgress == workingTarget)
            {
                if (cachedAnvilRecipe.stepsMatch(steps))
                {
                    ItemStack output = inventory.getStackInSlot(SLOT_OUTPUT);

                    inventory.setStackInSlot(SLOT_INPUT, cachedAnvilRecipe.consumeInput(input));
                    ImmutablePair<ItemStack, ItemStack> result = CoreHelpers.mergeStacksWithResult(output, cachedAnvilRecipe.getOutput());
                    inventory.setStackInSlot(SLOT_OUTPUT, result.getKey());
                    if (!result.getValue().isEmpty())
                    {
                        CoreHelpers.dropItemInWorld(world, pos, result.getValue());
                    }

                    updateForgeRecipe(true);
                    world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
                }
            }

            if (workingProgress < 0 || workingProgress >= 150)
            {
                // Consume input, produce no output, reset recipe
                inventory.setStackInSlot(SLOT_INPUT, cachedAnvilRecipe.consumeInput(input));

                updateForgeRecipe(true);
                world.playSound(null, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
        }
    }

    @Override
    public void onLoad()
    {
        updateForgeRecipe(false);
    }

    @SideOnly(Side.CLIENT)
    public ForgeSteps getSteps()
    {
        return steps;
    }

    public IItemHandler getDisplayInventory()
    {
        return displayInventory;
    }

    @Override
    public int getFieldCount()
    {
        return 8;
    }

    @Override
    public int getField(int ID)
    {
        switch (ID)
        {
            case FIELD_PROGRESS:
                return workingProgress;
            case FIELD_TARGET:
                return workingTarget;
            case FIELD_LAST_STEP:
            case FIELD_SECOND_STEP:
            case FIELD_THIRD_STEP:
                return steps.getStepByID(ID);
            case FIELD_FIRST_RULE:
            case FIELD_SECOND_RULE:
            case FIELD_THIRD_RULE:
                if (ID - FIELD_FIRST_RULE >= rules.length)
                    return -1;
                return ForgeRule.getID(rules[ID - FIELD_FIRST_RULE]);
            default:
                TinkersForging.getLog().warn("Invalid field id {}", ID);
                return 0;
        }
    }

    @Override
    public void setField(int ID, int value)
    {
        switch (ID)
        {
            case FIELD_PROGRESS:
                workingProgress = value;
                break;
            case FIELD_TARGET:
                workingTarget = value;
                break;
            case FIELD_LAST_STEP:
            case FIELD_SECOND_STEP:
            case FIELD_THIRD_STEP:
                steps.setStep(ID, value);
                break;
            case FIELD_FIRST_RULE:
            case FIELD_SECOND_RULE:
            case FIELD_THIRD_RULE:
                rules[ID - FIELD_FIRST_RULE] = ForgeRule.valueOf(value);
                break;
            default:
                TinkersForging.getLog().warn("Invalid field id {}", ID);
        }
    }

    private void updateForgeRecipe(boolean shouldReset)
    {
        ItemStack stack = inventory.getStackInSlot(SLOT_INPUT);
        IForgeItem cap = stack.getCapability(CapabilityForgeItem.CAPABILITY, null);

        // Try cached forge recipe, if it exists
        if (cachedAnvilRecipe != null && cachedAnvilRecipe.test(stack))
        {
            // Current recipe is valid
            if (shouldReset)
            {
                resetForgeRecipe(cachedAnvilRecipe);

                if (cap != null)
                {
                    workingProgress = cap.getWork();
                    steps = cap.getSteps();
                    cap.setRecipe(cachedAnvilRecipe);
                    stack.setTagInfo(CapabilityForgeItem.KEY.toString(), cap.serializeNBT());
                }

            }
        }
        else
        {
            // Need to find a new recipe anyway, since cache is null
            // If this is null it will reset, since no recipe was found
            AnvilRecipe recipe = null;
            if (cap != null)
            {
                recipe = ModRecipes.ANVIL.getByName(stack, cap.getRecipeName());
                workingProgress = cap.getWork();
            }
            if (recipe == null)
            {
                recipe = ModRecipes.ANVIL.get(stack);
                workingProgress = 0;
            }
            resetForgeRecipe(recipe);
        }
    }

    private void resetForgeRecipe(@Nullable AnvilRecipe recipe)
    {
        if (recipe != null)
        {
            displayInventory.setStackInSlot(SLOT_DISPLAY, recipe.getOutput());
        }
        else
        {
            displayInventory.setStackInSlot(SLOT_DISPLAY, ItemStack.EMPTY);
        }

        if (!world.isRemote)
        {
            steps.reset();
            cachedAnvilRecipe = recipe;

            if (cachedAnvilRecipe != null)
            {
                workingTarget = cachedAnvilRecipe.getWorkingTarget(world.getSeed());
                rules = cachedAnvilRecipe.getRules();
            }
            else
            {
                workingTarget = 0;
                rules = new ForgeRule[3];
            }
        }
    }
}
