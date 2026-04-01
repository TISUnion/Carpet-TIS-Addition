/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash.mark.entityIdISE;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionYeeter;
import carpettisaddition.utils.EntityUtils;
import carpettisaddition.utils.PositionUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin
{
	@ModifyExpressionValue(
			method = "addEntity",
			at = @At(
					value = "NEW",
					target = "(Ljava/lang/String;)Ljava/lang/IllegalStateException;"
			)
	)
	private IllegalStateException yeetUpdateSuppressionCrash_wrapEntityIdIllegalStateSuppression(
			IllegalStateException throwable,
			@Local(argsOnly = true) Entity entity
	)
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash && throwable instanceof IllegalStateException)
		{
			// TODO: better location info for entity?
			throwable = (IllegalStateException)UpdateSuppressionYeeter.tryReplaceWithWrapper(
					throwable,
					EntityUtils.getEntityWorld(entity),
					PositionUtils.flooredBlockPos(entity.position())
			);
		}
		return throwable;
	}
}
