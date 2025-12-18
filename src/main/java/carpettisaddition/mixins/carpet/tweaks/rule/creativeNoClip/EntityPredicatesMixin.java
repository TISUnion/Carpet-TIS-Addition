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
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntitySelector.class)
public abstract class EntityPredicatesMixin
{
	/**
	 * The lambda method with the declaration of {@link EntitySelector#NO_SPECTATORS}
	 *
	 * Modify its return value to modify the result of {@link net.minecraft.world.level.EntityGetter#getEntities(net.minecraft.world.entity.Entity, net.minecraft.world.phys.AABB)}
	 */
	@Dynamic
	@ModifyReturnValue(
			//#if MC >= 26.1
			//$$ method = "lambda$static$4",
			//#elseif MC >= 11700
			//$$ method = "method_24517",
			//#else
			method = "method_5907",
			//#endif
			at = @At("TAIL"),
			remap = false
	)
	private static boolean creativeNoClipEnhancementImpl_checkNoClipInEntityPredicatesExceptSpectator(boolean ret, Entity entity)
	{
		if (CarpetSettings.creativeNoClip && CreativeNoClipHelper.exceptSpectatorPredicateIgnoreNoClipPlayers.get())
		{
			// return true: attempt to collide with this entity
			// return false: skip this entity
			if (CreativeNoClipHelper.isNoClipPlayer(entity))
			{
				ret = false;
			}
		}
		return ret;
	}
}
