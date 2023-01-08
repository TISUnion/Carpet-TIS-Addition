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

package carpettisaddition.mixins.carpet.tweaks.command.playerActionEnhanced;

import carpet.helpers.EntityPlayerActionPack;
import carpettisaddition.helpers.carpet.playerActionEnhanced.IEntityPlayerActionPackAction;
import carpettisaddition.helpers.carpet.playerActionEnhanced.PlayerActionPackHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerActionPack.Action.class)
public abstract class EntityPlayerActionPackActionMixin implements IEntityPlayerActionPackAction
{
	@Mutable
	@Shadow(remap = false) @Final public int interval;

	private boolean randomly = false;
	private int randomlyLowerBound;
	private int randomlyUpperBound;

	@Override
	public void setRandomlyRange(int lower, int upper)
	{
		this.randomly = true;
		this.randomlyLowerBound = lower;
		this.randomlyUpperBound = upper;
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "FIELD",
					target = "Lcarpet/helpers/EntityPlayerActionPack$Action;interval:I"
					//#if MC >= 11600
					//$$ , ordinal = 1
					//#endif
			),
			remap = false
	)
	private void changeIntervalRandomly(CallbackInfoReturnable<Boolean> cir)
	{
		if (this.randomly)
		{
			this.interval = PlayerActionPackHelper.getRandomInt(this.randomlyLowerBound, this.randomlyUpperBound);
		}
	}
}
