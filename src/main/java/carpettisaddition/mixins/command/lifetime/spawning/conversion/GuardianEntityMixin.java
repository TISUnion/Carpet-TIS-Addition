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

package carpettisaddition.mixins.command.lifetime.spawning.conversion;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobConversionSpawningReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Carpet's renewableSponges mixin uses default priority 1000.
 * We need to mixin into its merged method, so here comes priority 2000.
 */
@Mixin(value = Guardian.class, priority = 2000)
public abstract class GuardianEntityMixin extends Monster
{
	protected GuardianEntityMixin(EntityType<? extends Monster> type, Level world)
	{
		super(type, world);
	}

	@Dynamic("Added by Carpet's renewableSponges mixin")
	@SuppressWarnings("target")
	@ModifyArg(
			//#if MC >= 1.16
			//$$ method = "thunderHit(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LightningBolt;)V",
			//#else
			method = "thunderHit(Lnet/minecraft/world/entity/global/LightningBolt;)V",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
			)
	)
	private Entity lifetimeTracker_recordSpawning_conversion_elderGuardianFromGuardian(Entity elderGuardian)
	{
		((LifetimeTrackerTarget)elderGuardian).recordSpawning$TISCM(new MobConversionSpawningReason(this.getType()));
		return elderGuardian;
	}
}
