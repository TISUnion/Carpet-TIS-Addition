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

package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import carpettisaddition.commands.lifetime.removal.MobPickupRemovalReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin extends Entity
{
	public ExperienceOrbEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "intValue=6000"
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/entity/ExperienceOrbEntity;discard()V"
					//#else
					target = "Lnet/minecraft/entity/ExperienceOrbEntity;remove()V"
					//#endif
			)
	)
	private void onDespawnLifeTimeTracker(CallbackInfo ci)
	{
		((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.DESPAWN_TIMEOUT);
	}

	@Inject(
			method = "onPlayerCollision",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/player/PlayerEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V"
			)
	)
	private void onPickupLifeTimeTracker(PlayerEntity player, CallbackInfo ci)
	{
		((LifetimeTrackerTarget)this).recordRemoval(new MobPickupRemovalReason(player.getType()));
	}

	//#if MC >= 11700
	//$$ @Inject(method = "merge", at = @At("HEAD"))
	//$$ private void onMergedLifeTimeTracker(ExperienceOrbEntity other, CallbackInfo ci)
	//$$ {
	//$$ 	int amountBackup = ((ExperienceOrbEntityAccessor)other).getAmount$TISCM();
	//$$ 	((ExperienceOrbEntityAccessor)other).setAmount$TISCM(0);
	//$$ 	((LifetimeTrackerTarget)other).recordRemoval(LiteralRemovalReason.MERGE);
	//$$ 	((ExperienceOrbEntityAccessor)other).setAmount$TISCM(amountBackup);
	//$$ }
	//#endif
}
