package carpettisaddition.mixins.rule.hopperNoItemCost.compact.lithium;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.hopperNoItemCost.HopperNoItemCostHelper;
import me.jellysquid.mods.lithium.common.hopper.LithiumStackList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(LithiumStackList.class)
public abstract class LithiumStackListMixin extends DefaultedList<ItemStack>
{
	protected LithiumStackListMixin(List<ItemStack> delegate, @Nullable ItemStack initialElement)
	{
		super(delegate, initialElement);
	}

	@SuppressWarnings("ConstantConditions")
	@Intrinsic
	@NotNull
	@Override
	public ItemStack get(int index)
	{
		ItemStack itemStack = super.get(index);
		if (CarpetTISAdditionSettings.hopperNoItemCost)
		{
			// if it's during a no-item-cost hopper inserting, make sure the result is read-only
			// looks dangerous tho, but it works
			if ((LithiumStackList)(Object)this == HopperNoItemCostHelper.currentHopperInvList.get())
			{
				itemStack = itemStack.copy();
			}
		}
		return itemStack;
	}
}
