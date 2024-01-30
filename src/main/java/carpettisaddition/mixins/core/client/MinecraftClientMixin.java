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

package carpettisaddition.mixins.core.client;

import carpettisaddition.CarpetTISAdditionClient;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin
{
	@Inject(method = "tick", at = @At("RETURN"))
	private void clientTickHook$TISCM(CallbackInfo ci)
	{
		CarpetTISAdditionClient.getInstance().onClientTick((MinecraftClient)(Object)this);
	}

	@Inject(
			//#if MC >= 12005
			//$$ method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;Z)V",
			//#else
			method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;clear()V"
			)
	)
	private void onClientDisconnected(CallbackInfo ci)
	{
		CarpetTISAdditionClient.getInstance().onClientDisconnected((MinecraftClient)(Object)this);
	}
}
