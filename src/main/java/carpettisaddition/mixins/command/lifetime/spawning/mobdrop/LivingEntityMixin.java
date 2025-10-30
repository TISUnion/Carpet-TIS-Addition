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

package carpettisaddition.mixins.command.lifetime.spawning.mobdrop;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobDropSpawningReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC >= 11700
//$$ import carpettisaddition.commands.lifetime.utils.LifetimeMixinUtil;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	public LivingEntityMixin(EntityType<?> type, Level world)
	{
		super(type, world);
	}

	// wither rose thing
	@ModifyArg(
			//#if MC >= 11500
			method = "createWitherRose",
			//#else
			//$$ method = "onDeath",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
			)
	)
	private Entity lifetimeTracker_recordSpawning_mobDrop_livingEntityDeathDrop_item(Entity itemEntity)
	{
		((LifetimeTrackerTarget)itemEntity).recordSpawning(new MobDropSpawningReason(this.getType()));
		return itemEntity;
	}

	//#if MC >= 11700
	//$$ @Inject(method = "dropXp", at = @At("HEAD"))
	//$$ private void lifetimeTracker_recordSpawning_mobDrop_livingEntityDeathDrop_xpOrbHook(CallbackInfo ci)
	//$$ {
	//$$ 	LifetimeMixinUtil.xpOrbSpawningReason.set(new MobDropSpawningReason(this.getType()));
	//$$ }
	//#else
	@ModifyArg(
			//#if MC >= 11500
			method = "dropExperience",
			//#else
			//$$ method = "updatePostDeath",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
			),
			index = 0
	)
	private Entity lifetimeTracker_recordSpawning_mobDrop_livingEntityDeathDrop_xpOrb(Entity entity)
	{
		((LifetimeTrackerTarget)entity).recordSpawning(new MobDropSpawningReason(this.getType()));
		return entity;
	}
	//#endif
}
