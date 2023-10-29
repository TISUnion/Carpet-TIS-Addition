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

package carpettisaddition.mixins.logger.phantom;

import carpettisaddition.logging.loggers.phantom.PhantomLogger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.gen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//#if MC >= 12000
//$$ import net.minecraft.server.network.ServerPlayerEntity;
//#endif

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin
{
	private PlayerEntity currentPlayer$TISCM$phantomLogger = null;

	@ModifyVariable(
			method = "spawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"
			)
	)
	//#if MC >= 12000
	//$$ private ServerPlayerEntity recordsCurrentPlayer$phantomLogger(ServerPlayerEntity player)
	//#else
	private PlayerEntity recordsCurrentPlayer$phantomLogger(PlayerEntity player)
	//#endif
	{
		this.currentPlayer$TISCM$phantomLogger = player;
		return player;
	}

	@ModifyVariable(
			method = "spawn",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/entity/EntityType;PHANTOM:Lnet/minecraft/entity/EntityType;"
			),
			ordinal = 1
	)
	private int logPlayerSpawningPhantoms(int amount)
	{
		if (this.currentPlayer$TISCM$phantomLogger != null)
		{
			PhantomLogger.getInstance().onPhantomSpawn(this.currentPlayer$TISCM$phantomLogger, amount);
			this.currentPlayer$TISCM$phantomLogger = null;
		}
		return amount;
	}
}
