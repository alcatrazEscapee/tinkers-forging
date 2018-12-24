/*
 * Part of the Tinkers Forging Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.tinkersforging.common.tile;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.alcatrazcore.tile.ITileFields;
import com.alcatrazescapee.alcatrazcore.tile.TileInventory;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.alcatrazcore.util.collections.ImmutablePair;
import com.alcatrazescapee.tinkersforging.ModConfig;
import com.alcatrazescapee.tinkersforging.TinkersForging;
import com.alcatrazescapee.tinkersforging.common.blocks.BlockTinkersAnvil;
import com.alcatrazescapee.tinkersforging.common.capability.CapabilityForgeItem;
import com.alcatrazescapee.tinkersforging.common.capability.IForgeItem;
import com.alcatrazescapee.tinkersforging.common.network.PacketAnvilRecipeUpdate;
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
    public static final int SLOT_HAMMER = 2;
    public static final int SLOT_DISPLAY = 3;

    private AnvilRecipe cachedAnvilRecipe;
    private ForgeSteps steps;
    private ForgeRule[] rules;
    private int workingProgress = 0; // Min = 0, Max = 150. If it goes over / under you lose the input
    private int workingTarget = 0;

    public TileTinkersAnvil()
    {
        super(4);

        steps = new ForgeSteps();
        rules = new ForgeRule[3];
    }

    public AnvilRecipe getRecipe()
    {
        // Called on server
        return cachedAnvilRecipe;
    }

    public void setRecipe(@Nullable AnvilRecipe recipe)
    {
        cachedAnvilRecipe = recipe;

        // update recipe-based fields (both sides)
        // note on client the recipe is only a shallow copy of the actual recipe (it has no input paramaters)
        if (recipe != null)
        {
            ItemStack stack = inventory.getStackInSlot(SLOT_INPUT);
            IForgeItem cap = stack.getCapability(CapabilityForgeItem.CAPABILITY, null);
            if (cap != null)
            {
                cap.setRecipe(cachedAnvilRecipe);
            }

            inventory.setStackInSlot(SLOT_DISPLAY, cachedAnvilRecipe.getOutput());
        }
        else
        {
            inventory.setStackInSlot(SLOT_DISPLAY, ItemStack.EMPTY);
        }
    }

    @Override
    public void setAndUpdateSlots(int slot)
    {
        super.setAndUpdateSlots(slot);

        if (world.isRemote)
            return;

        ItemStack stack = inventory.getStackInSlot(SLOT_INPUT);
        IForgeItem cap = stack.getCapability(CapabilityForgeItem.CAPABILITY, null);

        if (cap != null)
        {
            if (cachedAnvilRecipe == null || !cachedAnvilRecipe.test(stack))
            {
                // no current recipe or recipe exists but doesn't match input
                // in both cases, reset the recipe based off the stack info
                updateRecipe(ModRecipes.ANVIL.getByName(cap.getRecipeName()));
                if (cachedAnvilRecipe == null)
                {
                    // for some reason the stack has an invalid recipe name
                    updateRecipe(ModRecipes.ANVIL.get(stack));
                    if (cachedAnvilRecipe != null)
                    {
                        cap.setRecipe(cachedAnvilRecipe);
                    }
                }
                if (cachedAnvilRecipe == null)
                {
                    // no current recipe
                    resetFields();
                    cap.reset();
                    return;
                }
            }

            // at this point, the recipe is valid, but may have changed
            // update server side fields
            workingProgress = cap.getWork();
            steps = cap.getSteps().copy();

            workingTarget = cachedAnvilRecipe.getWorkingTarget(world.getSeed());
            rules = cachedAnvilRecipe.getRules();

            cap.setRecipe(cachedAnvilRecipe);

            // update display inventory
            if (cachedAnvilRecipe != null)
            {
                inventory.setStackInSlot(SLOT_DISPLAY, cachedAnvilRecipe.getOutput());
            }
            else
            {
                inventory.setStackInSlot(SLOT_DISPLAY, ItemStack.EMPTY);
            }
        }
        else
        {
            // cap was null, most likely if the slot was empty
            resetFields();
            updateRecipe(null);
            inventory.setStackInSlot(SLOT_DISPLAY, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        switch (slot)
        {
            case SLOT_INPUT:
                return stack.hasCapability(CapabilityForgeItem.CAPABILITY, null);
            case SLOT_HAMMER:
                return CoreHelpers.doesStackMatchOre(stack, "hammer");
            default:
                return false;
        }
    }

    @Override
    public void onBreakBlock()
    {
        // -1 is to skip the display slot
        for (int i = 0; i < inventory.getSlots() - 1; ++i)
        {
            if (i == SLOT_INPUT)
            {
                ItemStack input = inventory.getStackInSlot(i);
                CapabilityForgeItem.clearStackCheckRecipe(input);
                CoreHelpers.dropItemInWorld(world, pos, input);
            }
            else
            {
                CoreHelpers.dropItemInWorld(world, pos, inventory.getStackInSlot(i));
            }
        }
    }

    public int getTier()
    {
        return ((BlockTinkersAnvil) world.getBlockState(pos).getBlock()).getTier();
    }

    public void cycleForgeRecipe(boolean isForwards)
    {
        // This is only called server side
        if (cachedAnvilRecipe != null)
        {
            ItemStack stack = inventory.getStackInSlot(SLOT_INPUT);
            if (isForwards)
                cachedAnvilRecipe = ModRecipes.ANVIL.getNext(cachedAnvilRecipe, stack);
            else
                cachedAnvilRecipe = ModRecipes.ANVIL.getPrevious(cachedAnvilRecipe, stack);
            setAndUpdateSlots(0);
        }
    }

    public void addStep(@Nullable ForgeStep step)
    {
        // This is only called on server
        ItemStack input = inventory.getStackInSlot(SLOT_INPUT);
        IForgeItem cap = input.getCapability(CapabilityForgeItem.CAPABILITY, null);

        if (cap != null)
        {
            // Add step to stack + tile
            cap.addStep(step);
            steps = cap.getSteps().copy();
            if (step != null)
            {
                workingProgress += step.getStepAmount();
            }

            // Handle possible recipe completion
            if (cachedAnvilRecipe != null)
            {
                int targetRange = ModConfig.BALANCE.forgeTargetRange + (5 - cachedAnvilRecipe.getTier()) * ModConfig.BALANCE.forgeTierRangeMod;
                if (Math.abs(workingProgress - workingTarget) <= targetRange && cachedAnvilRecipe.stepsMatch(steps))
                {
                    ItemStack output = inventory.getStackInSlot(SLOT_OUTPUT);
                    ItemStack newInput = cachedAnvilRecipe.consumeInput(input);
                    if (!newInput.isEmpty())
                    {
                        // Reset the capability data
                        IForgeItem newCap = newInput.getCapability(CapabilityForgeItem.CAPABILITY, null);
                        if (newCap != null)
                        {
                            newCap.reset();
                        }
                    }

                    // Consume input + produce output / throw it in the world
                    inventory.setStackInSlot(SLOT_INPUT, newInput);
                    ImmutablePair<ItemStack, ItemStack> result = CoreHelpers.mergeStacksWithResult(output, cachedAnvilRecipe.getOutput());
                    inventory.setStackInSlot(SLOT_OUTPUT, result.getKey());
                    if (!result.getValue().isEmpty())
                    {
                        CoreHelpers.dropItemInWorld(world, pos, result.getValue());
                    }

                    world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);

                    // Reset forge stuff
                    resetFields();
                    setRecipe(null);
                    inventory.setStackInSlot(SLOT_DISPLAY, ItemStack.EMPTY);
                }
                else if (workingProgress < 0 || workingProgress >= 150)
                {
                    // Consume input, produce no output
                    ItemStack newInput = cachedAnvilRecipe.consumeInput(input);
                    if (!newInput.isEmpty())
                    {
                        // Reset the capability data
                        IForgeItem newCap = newInput.getCapability(CapabilityForgeItem.CAPABILITY, null);
                        if (newCap != null)
                        {
                            newCap.reset();
                        }
                    }
                    inventory.setStackInSlot(SLOT_INPUT, newInput);
                    world.playSound(null, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f);
                }
            }

            // update recipe
            setAndUpdateSlots(0);
        }
    }

    @Override
    public void onLoad()
    {
        setAndUpdateSlots(0);
    }

    @SideOnly(Side.CLIENT)
    public ForgeSteps getSteps()
    {
        return steps;
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

    private void updateRecipe(@Nullable AnvilRecipe recipe)
    {
        // Called on server
        setRecipe(recipe);
        TinkersForging.getNetwork().sendToDimension(new PacketAnvilRecipeUpdate(this), world.provider.getDimension());
    }

    private void resetFields()
    {
        if (!world.isRemote)
        {
            workingProgress = 0;
            workingTarget = 0;
            steps.reset();
            rules = new ForgeRule[3];
        }
    }
}
