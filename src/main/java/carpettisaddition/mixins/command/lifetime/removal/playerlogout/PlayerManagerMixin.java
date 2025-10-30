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

package carpettisaddition.mixins.command.lifetime.removal.playerlogout;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 11700
//$$ import org.spongepowered.asm.mixin.injection.ModifyVariable;
//#else
import org.spongepowered.asm.mixin.injection.ModifyArg;
//#endif

@Mixin(PlayerList.class)
public abstract class PlayerManagerMixin
{
	//#if MC >= 11700
	//$$ @ModifyVariable(
	//$$ 		method = "method_31441",  // lambda method in remove
	//$$ 		at = @At("HEAD"),
	//$$ 		argsOnly = true
	//$$ )
	//$$ private static
	//#else
	@ModifyArg(
			method = "remove",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerLevel;despawn(Lnet/minecraft/world/entity/Entity;)V"
			),
			require = 2,
			allow = 2
	)
	private
	//#endif
	Entity lifetimeTracker_recordRemoval_playerLogout(Entity entity)
	{
		((LifetimeTrackerTarget)entity).recordRemoval(LiteralRemovalReason.PLAYER_LOGOUT);
		return entity;
	}
}
