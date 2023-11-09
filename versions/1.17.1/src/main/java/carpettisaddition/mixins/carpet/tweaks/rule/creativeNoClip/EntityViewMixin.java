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

import carpet.CarpetSettings;
import carpettisaddition.helpers.carpet.tweaks.rule.creativeNoClip.CreativeNoClipHelper;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import net.minecraft.world.EntityView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

//#if MC >= 11800
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//#else
import org.spongepowered.asm.mixin.injection.ModifyVariable;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"))
@Mixin(EntityView.class)
public interface EntityViewMixin
{
	// lithium in mc1.16, mc1.17 overwrites the entire method, so it's more simple to just `@ModifyVariable` at HEAD
	//#if MC >= 11800
	//$$ @ModifyArg(
	//$$ 		method = "getEntityCollisions",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/world/EntityView;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"
	//$$ 		)
	//$$ )
	//#else
	@ModifyVariable(method = "getEntityCollisions", at = @At("HEAD"), argsOnly = true)
	//#endif
	private Predicate<Entity> creativeNoClipEnhancementImpl_addNoClipCheckToPredicate(Predicate<Entity> predicate)
	{
		if (CarpetSettings.creativeNoClip && CreativeNoClipHelper.exceptSpectatorPredicateIgnoreNoClipPlayers.get())
		{
			predicate = predicate.and(entity -> {
				// return true: attempt to collide with this entity
				// return false: skip this entity
				return !CreativeNoClipHelper.isNoClipPlayer(entity);
			});
		}
		return predicate;
	}
}
