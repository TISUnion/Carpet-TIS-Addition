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

package carpettisaddition.mixins.rule.lightUpdates;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.world.ServerLightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerLightingProvider.class, priority = 1500)
public abstract class ServerLightingProviderMixin
{
	private final ThreadLocal<Boolean> enqueueNotImportant = ThreadLocal.withInitial(() -> false);

	/**
	 * Regular light updates can be discard safely without potentially blocking the server
	 */
	@Inject(method = "checkBlock", at = @At("HEAD"))
	private void thisEnqueueIsNotImportant(CallbackInfo ci)
	{
		this.enqueueNotImportant.set(true);
	}

	@Inject(
			//#if MC >= 11500
			method = "enqueue(IILjava/util/function/IntSupplier;Lnet/minecraft/server/world/ServerLightingProvider$Stage;Ljava/lang/Runnable;)V",
			//#else
			//$$ method = "enqueue(IILjava/util/function/IntSupplier;Lnet/minecraft/server/world/ServerLightingProvider$class_3901;Ljava/lang/Runnable;)V",
			//#endif
			at = @At(value = "HEAD"),
			cancellable = true
	)
	private void onEnqueueingLightUpdateTask(CallbackInfo ci)
	{
		CarpetTISAdditionSettings.LightUpdateOptions rule = CarpetTISAdditionSettings.lightUpdates;
		if (rule.shouldEnqueueLightTask())
		{
			return;
		}
		if (this.enqueueNotImportant.get() || rule == CarpetTISAdditionSettings.LightUpdateOptions.OFF)
		{
			ci.cancel();
		}
		this.enqueueNotImportant.set(false);
	}

	/**
	 * Lighting suppression? Just make an endless loop here
	 */
	@Inject(method = "runTasks", at = @At(value = "HEAD"))
	private void onExecutingLightUpdates(CallbackInfo ci)
	{
		while (!CarpetTISAdditionSettings.lightUpdates.shouldExecuteLightTask())
		{
			Thread.yield();
		}
	}
}
