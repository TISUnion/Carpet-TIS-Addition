/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.mixins.rule.dispensersFireDragonBreath;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.dispensersFireDragonBreath.FireDragonBreathDispenserBehaviour;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 26.4
//$$ import net.minecraft.core.dispenser.DispenseBehavior;
//$$ import java.util.List;
//#else
import net.minecraft.core.dispenser.DispenseItemBehavior;
//#endif

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin
{
	@Inject(
			//#if MC >= 26.4
			//$$ method = "getDispenseMethods",
			//#else
			method = "getDispenseMethod",
			//#endif
			at = @At("HEAD"),
			cancellable = true
	)
	//#if MC >= 26.4
	//$$ private void registerNewBehaviors_dispensersFireDragonBreath(CallbackInfoReturnable<List<DispenseBehavior>> cir, @Local(argsOnly = true) ItemStack stack)
	//#else
	private void registerNewBehaviors_dispensersFireDragonBreath(CallbackInfoReturnable<DispenseItemBehavior> cir, @Local(argsOnly = true) ItemStack stack)
	//#endif
	{
		if (CarpetTISAdditionSettings.dispensersFireDragonBreath)
		{
			Item item = stack.getItem();
			if (item == Items.DRAGON_BREATH)
			{
				//#if MC >= 26.4
				//$$ cir.setReturnValue(List.of(FireDragonBreathDispenserBehaviour.INSTANCE));
				//#else
				cir.setReturnValue(FireDragonBreathDispenserBehaviour.INSTANCE);
				//#endif
			}
		}
	}
}
