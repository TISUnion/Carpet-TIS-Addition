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

package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash.yeet;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.ExceptionCatchLocation;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionException;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionYeeter;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ReentrantBlockableEventLoop.class)
public abstract class ReentrantThreadExecutorMixin<R extends Runnable>
{
	 /**
	  * The exception caught in {@link net.minecraft.util.thread.BlockableEventLoop#doRunTask} will re-throw if:
	  * - The Minecraft version is a snapshot / pre-release, or whatever non-stable versions that makes {@link net.minecraft.SharedConstants#CRASH_EAGERLY} true
	  * - (MC >= 1.21.2-rc1) The exception is marked as {@link net.minecraft.util.thread.BlockableEventLoop#isNonRecoverable}
	  * - It's actually not an {@link Exception}, but an {@link Error} (e.g. {@link OutOfMemoryError}) or whatever else. This try-catch only catches {@link Exception}
	  * Whatever, here's the outer crash yeeter for it
	 */
	@SuppressWarnings("ConstantValue")
	@WrapOperation(
			method = "doRunTask",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/thread/BlockableEventLoop;doRunTask(Ljava/lang/Runnable;)V"
			)
	)
	private void yeetUpdateSuppressionCrash_implForThreadExecutorTaskExecuting(ReentrantBlockableEventLoop<R> obj, R task, Operation<Void> original)
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash && (Object)this instanceof MinecraftServer)
		{
			try
			{
				original.call(obj, task);
			}
			catch (Throwable throwable)
			{
				Optional<UpdateSuppressionException> opt = UpdateSuppressionYeeter.extractInCauses(throwable);
				if (opt.isPresent())
				{
					opt.get().getSuppressionContext().report(ExceptionCatchLocation.SERVER_ASYNC_TASK);
				}
				else
				{
					throw throwable;
				}
			}
		}
		else
		{
			// vanilla
			original.call(obj, task);
		}
	}
}
