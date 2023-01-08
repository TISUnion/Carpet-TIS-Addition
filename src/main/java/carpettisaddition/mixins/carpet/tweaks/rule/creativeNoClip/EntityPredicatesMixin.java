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

package carpettisaddition.mixins.carpet.tweaks.rule.creativeNoClip;

//#if MC >= 11500
import carpet.CarpetSettings;
//#else
//$$ import carpettisaddition.utils.compat.carpet.CarpetSettings;
//#endif

import carpettisaddition.helpers.carpet.tweaks.rule.creativeNoClip.CreativeNoClipHelper;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntityPredicates;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPredicates.class)
public abstract class EntityPredicatesMixin
{
	/**
	 * The lambda method with the declaration of {@link EntityPredicates#EXCEPT_SPECTATOR}
	 */
	@Dynamic
	@Inject(
			//#if MC >= 11700
			//$$ method = "method_24517",
			//#else
			method = "method_5907",
			//#endif
			at = @At("TAIL"),
			remap = false,
			cancellable = true
	)
	private static void creativeNoClipEnhancement(Entity entity, CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetSettings.creativeNoClip && CreativeNoClipHelper.ignoreNoClipPlayersFlag.get())
		{
			cir.setReturnValue(cir.getReturnValue() && !CreativeNoClipHelper.isNoClipPlayer(entity));
		}
	}
}
