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

package carpettisaddition.mixins.command.lifetime.removal.conversion;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.MobConversionRemovalReason;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(Zombie.class)
public abstract class ZombieEntityMixin extends Monster
{
	protected ZombieEntityMixin(EntityType<? extends Monster> type, Level world)
	{
		super(type, world);
	}

	@ModifyArg(
			method = "convertTo",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
			)
	)
	private Entity lifetimeTracker_recordRemoval_conversion_zombieToVariant(Entity zombieVariant)
	{
		((LifetimeTrackerTarget)this).recordRemoval$TISCM(new MobConversionRemovalReason(zombieVariant.getType()));
		return zombieVariant;
	}

	@Inject(
			method = "killed",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/npc/Villager;remove()V"
			)
	)
	private void lifetimeTracker_recordRemoval_conversion_zombieInfection(
			LivingEntity other, CallbackInfo ci,
			@Local Villager villagerEntity,
			@Local ZombieVillager zombieVillagerEntity
	)
	{
		((LifetimeTrackerTarget)villagerEntity).recordRemoval$TISCM(new MobConversionRemovalReason(zombieVillagerEntity.getType()));
	}
}
