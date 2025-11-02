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

package carpettisaddition.mixins.logger.commandblock;

import carpettisaddition.logging.loggers.commandblock.CommandBlockLogger;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecartCommandBlock.class)
public abstract class CommandBlockMinecartEntityMixin
{
	@Inject(
			method = "activateMinecart",
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.21.11
					//$$ target = "Lnet/minecraft/world/CommandBlockExecutor;execute(Lnet/minecraft/server/level/ServerLevel;)Z",
					//#else
					target = "Lnet/minecraft/world/level/BaseCommandBlock;performCommand(Lnet/minecraft/world/level/Level;)Z",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	private void onCommandBlockMinecartExecutedCommandBlockLogger(CallbackInfo ci)
	{
		CommandBlockLogger.getInstance().onCommandBlockMinecartActivated((MinecartCommandBlock)(Object)this);
	}
}
