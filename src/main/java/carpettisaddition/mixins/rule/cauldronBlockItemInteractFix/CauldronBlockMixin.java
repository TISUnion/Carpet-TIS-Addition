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

package carpettisaddition.mixins.rule.cauldronBlockItemInteractFix;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.level.block.CauldronBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11500
import net.minecraft.world.InteractionResult;
//#else
//$$ import org.spongepowered.asm.mixin.injection.Slice;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.17"))
@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin
{
	@Inject(
			method = "use",
			//#if MC < 11500
			//$$ slice = @Slice(
			//$$ 		from = @At(
			//$$ 				value = "FIELD",
			//$$ 				target = "Lnet/minecraft/stats/Stats;CLEAN_SHULKER_BOX:Lnet/minecraft/resources/ResourceLocation;"
			//$$ 		)
			//$$ ),
			//#endif
			at = @At(
					//#if MC >= 11500
					value = "FIELD",
					target = "Lnet/minecraft/world/InteractionResult;CONSUME:Lnet/minecraft/world/InteractionResult;"
					//#else
					//$$ value = "RETURN",
					//$$ ordinal = 0
					//#endif
			),
			cancellable = true
	)
	private void cauldronBlockItemInteractFix(
			//#if MC >= 11500
			CallbackInfoReturnable<InteractionResult> cir
			//#else
			//$$ CallbackInfoReturnable<Boolean> cir
			//#endif
	)
	{
		if (CarpetTISAdditionSettings.cauldronBlockItemInteractFix)
		{
			cir.setReturnValue(
					//#if MC >= 11500
					InteractionResult.PASS
					//#else
					//$$ false
					//#endif
			);
		}
	}
}
