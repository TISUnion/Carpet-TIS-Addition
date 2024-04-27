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

package carpettisaddition.mixins.rule.keepMobInLazyChunks;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.15"))
@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	/**
	 * Do the despawn check iff. the keepMobInLazyChunks == false
	 * The skipped despawn check will be executed in the injection of {@link MobEntityMixin}
	 */
	@WrapOperation(
			//#if MC >= 11700
			method = "method_31420",  // lambda method in method ServerWorld#tick
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/Entity;checkDespawn()V",
					remap = true
			),
			remap = false
			//#else
			//$$ method = "tick",
			//$$ at = @At(
			//$$ 		value = "INVOKE",
			//$$ 		target = "Lnet/minecraft/entity/Entity;checkDespawn()V"
			//$$ )
			//#endif
	)
	private void keepMobInLazyChunks_optionalCheckDespawn(Entity entity, Operation<Void> original)
	{
		if (!CarpetTISAdditionSettings.keepMobInLazyChunks)
		{
			original.call(entity);
		}
	}
}
