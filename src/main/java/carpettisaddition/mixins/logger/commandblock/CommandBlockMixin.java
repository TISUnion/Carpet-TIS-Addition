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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 12102
//$$ import net.minecraft.server.world.ServerWorld;
//#endif

@Mixin(CommandBlock.class)
public abstract class CommandBlockMixin
{
	@Inject(
			method = "execute",
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.21.11
					//$$ target = "Lnet/minecraft/world/CommandBlockExecutor;execute(Lnet/minecraft/server/world/ServerWorld;)Z",
					//#else
					target = "Lnet/minecraft/world/level/BaseCommandBlock;performCommand(Lnet/minecraft/world/level/Level;)Z",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	private void onCommandBlockExecutedCommandBlockLogger(
			BlockState state,
			//#if MC >= 12102
			//$$ ServerWorld world,
			//#else
			Level world,
			//#endif
			BlockPos pos, BaseCommandBlock executor, boolean hasCommand,
			CallbackInfo ci
	)
	{
		CommandBlockLogger.getInstance().onCommandBlockActivated(world, pos, state, executor);
	}
}
