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

package carpettisaddition.mixins.logger.wanderingTrader;

import carpettisaddition.logging.loggers.wanderingTrader.WanderingTraderLogger;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WanderingTraderSpawner.class)
public abstract class WanderingTraderManagerMixin
{
	@Inject(
			method = "spawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/npc/WanderingTrader;setDespawnDelay(I)V"
			)
	)
	private void logWanderingTraderSpawning(CallbackInfoReturnable<Boolean> cir, @Local Player playerEntity, @Local WanderingTrader wanderingTraderEntity)
	{
		WanderingTraderLogger.getInstance().onWanderingTraderSpawn(playerEntity, wanderingTraderEntity);
	}
}
