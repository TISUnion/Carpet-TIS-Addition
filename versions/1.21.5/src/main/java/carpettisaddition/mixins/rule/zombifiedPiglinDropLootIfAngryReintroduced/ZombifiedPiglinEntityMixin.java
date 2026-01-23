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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombifiedPiglin.class)
public abstract class ZombifiedPiglinEntityMixin extends Zombie implements NeutralMob
{
	public ZombifiedPiglinEntityMixin(EntityType<? extends Zombie> entityType, Level world)
	{
		super(entityType, world);
	}

	@Unique
	private static final int ZPDIAR_PLAYER_HURT_EXPERIENCE_TIME = 100;

	@Inject(
			method = "customServerAiStep",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/monster/Zombie;customServerAiStep(Lnet/minecraft/server/level/ServerLevel;)V"
			)
	)
	private void zombifiedPiglinDropLootIfAngryReintroduced_updatePlayerHitTimer(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.zombifiedPiglinDropLootIfAngryReintroduced)
		{
			if (this.isAngry())
			{
				this.lastHurtByPlayerMemoryTime = ZPDIAR_PLAYER_HURT_EXPERIENCE_TIME;
			}
		}
	}

	@Inject(
			method = "setTarget",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/monster/Zombie;setTarget(Lnet/minecraft/world/entity/LivingEntity;)V"
			)
	)
	private void zombifiedPiglinDropLootIfAngryReintroduced_setAttackingIfTargetIsPlayer(LivingEntity target, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.zombifiedPiglinDropLootIfAngryReintroduced)
		{
			if (target instanceof Player player)
			{
				this.setLastHurtByPlayer(player, ZPDIAR_PLAYER_HURT_EXPERIENCE_TIME);
			}
		}
	}
}
