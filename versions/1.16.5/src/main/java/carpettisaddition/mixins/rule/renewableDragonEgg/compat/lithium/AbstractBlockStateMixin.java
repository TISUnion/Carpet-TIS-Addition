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

package carpettisaddition.mixins.rule.renewableDragonEgg.compat.lithium;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.16"),
		@Condition(ModIds.lithium)
})
@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin
{
	@Shadow public abstract Block getBlock();

	/**
	 * Lithium block.flatten_states sets up immutable hasRandomTicks cache, and the cache may not match the actual value
	 * since whether dragon egg block has random tick is changeable
	 * So, if the block is dragon egg, use our own carpet setting rule value
	 */
	@Inject(method = "hasRandomTicks", at = @At("HEAD"), cancellable = true)
	private void hasRandomTicksDragonEgg(CallbackInfoReturnable<Boolean> cir)
	{
		if (this.getBlock() == Blocks.DRAGON_EGG)
		{
			cir.setReturnValue(CarpetTISAdditionSettings.renewableDragonEgg);
			cir.cancel();
		}
	}
}