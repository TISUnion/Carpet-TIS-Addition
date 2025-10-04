/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.zombifiedPiglinDropLootIfAngryReintroduced;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombifiedPiglinEntity.class)
public class ZombifiedPiglinEntityMixin extends ZombieEntity
{
	public ZombifiedPiglinEntityMixin(EntityType<? extends ZombieEntity> entityType, World world)
	{
		super(entityType, world);
	}

	private static final int ZPDIAR_PLAYER_HURT_EXPERIENCE_TIME = 100;

	@Inject(
			method = "mobTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/mob/ZombieEntity;mobTick(Lnet/minecraft/server/world/ServerWorld;)V"
			)
	)
	private void zombifiedPiglinDropLootIfAngryReintroduced_updatePlayerHitTimer(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.zombifiedPiglinDropLootIfAngryReintroduced)
		{
			this.playerHitTimer = ZPDIAR_PLAYER_HURT_EXPERIENCE_TIME;
		}
	}

	@Inject(
			method = "setTarget",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/mob/ZombieEntity;setTarget(Lnet/minecraft/entity/LivingEntity;)V"
			)
	)
	private void zombifiedPiglinDropLootIfAngryReintroduced_setAttackingIfTargetIsPlayer(LivingEntity target, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.zombifiedPiglinDropLootIfAngryReintroduced)
		{
			if (target instanceof PlayerEntity player)
			{
				this.setAttacking(player, ZPDIAR_PLAYER_HURT_EXPERIENCE_TIME);
			}
		}
	}
}
