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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity
{
	protected MobEntityMixin(EntityType<? extends LivingEntity> type, World world)
	{
		super(type, world);
	}

	//#if MC >= 11500
	@Inject(
			method = "checkDespawn",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/entity/mob/MobEntity;discard()V",
					//#else
					target = "Lnet/minecraft/entity/mob/MobEntity;remove()V",
					//#endif
					ordinal = 0
			)
	)
	private void onDifficultyDespawnLifeTimeTracker(CallbackInfo ci)
	{
		((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.DESPAWN_DIFFICULTY);
	}
	//#endif

	@Inject(
			method = "checkDespawn",
			// slice for optifine reeee
			// optifine will inserts shits after getClosestPlayer
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/entity/Entity;squaredDistanceTo(Lnet/minecraft/entity/Entity;)D"
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/entity/mob/MobEntity;discard()V",
					//#else
					target = "Lnet/minecraft/entity/mob/MobEntity;remove()V",
					//#endif
					ordinal = 0
			)
	)
	private void onImmediatelyDespawnLifeTimeTracker(CallbackInfo ci)
	{
		((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.DESPAWN_IMMEDIATELY);
	}

	@Inject(
			method = "checkDespawn",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/entity/Entity;squaredDistanceTo(Lnet/minecraft/entity/Entity;)D"
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/entity/mob/MobEntity;discard()V",
					//#else
					target = "Lnet/minecraft/entity/mob/MobEntity;remove()V",
					//#endif
					ordinal = 1
			)
	)
	private void onRandomlyDespawnLifeTimeTracker(CallbackInfo ci)
	{
		((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.DESPAWN_RANDOMLY);
	}

	@Inject(method = "setPersistent", at = @At("HEAD"))
	private void onEntityPersistentLifeTimeTracker(CallbackInfo ci)
	{
		((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.PERSISTENT);
	}

	@Inject(
			//#if MC >= 11600
			//$$ method = "equipLootStack",
			//#else
			method = "loot",
			//#endif
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/entity/mob/MobEntity;persistent:Z"
			)
	)
	private void onEntityPersistent2LifeTimeTracker(CallbackInfo ci)
	{
		((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.PERSISTENT);
	}

	@Inject(
			method = "loot",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/entity/ItemEntity;discard()V"
					//#else
					target = "Lnet/minecraft/entity/ItemEntity;remove()V"
					//#endif
			)
	)
	private void onItemPickUpLifeTimeTracker(ItemEntity item, CallbackInfo ci)
	{
		((LifetimeTrackerTarget)item).recordRemoval(new MobPickupRemovalReason(this.getType()));
	}
}
