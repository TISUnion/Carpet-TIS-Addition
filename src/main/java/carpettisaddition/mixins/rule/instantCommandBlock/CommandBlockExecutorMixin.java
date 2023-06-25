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

package carpettisaddition.mixins.rule.instantCommandBlock;

import carpettisaddition.helpers.rule.instantCommandBlock.ICommandBlockExecutor;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.CommandBlockExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandBlockExecutor.class)
public abstract class CommandBlockExecutorMixin implements ICommandBlockExecutor
{
	private boolean	ignoreWorldTimeCheck = false;

	@Override
	public void setIgnoreWorldTimeCheck(boolean ignoreWorldTimeCheck)
	{
		this.ignoreWorldTimeCheck = ignoreWorldTimeCheck;
	}

	@ModifyExpressionValue(
			method = "execute",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/CommandBlockExecutor;lastExecution:J",
					ordinal = 0
			)
	)
	private long dontCheckLastExecutionTimeIfItsInstant(long lastExecution)
	{
		if (this.ignoreWorldTimeCheck)
		{
			lastExecution = -1;
		}
		return lastExecution;
	}
}
