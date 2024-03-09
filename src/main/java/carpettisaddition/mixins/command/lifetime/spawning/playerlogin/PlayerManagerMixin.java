/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.command.lifetime.spawning.playerlogin;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin
{
	@ModifyReturnValue(
			method = "method_18241",  // lambda method in onPlayerConnect
			at = @At("TAIL")
	)
	private static Entity lifetimeTracker_recordSpawning_playerLogin(Entity entity)
	{
		if (entity != null)
		{
			((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.PLAYER_LOGIN);
		}
		return entity;
	}
}
