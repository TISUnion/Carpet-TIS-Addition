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

package carpettisaddition.mixins.carpet.hooks;

import carpet.CarpetServer;
import carpettisaddition.CarpetTISAdditionServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CarpetServer.class)
public abstract class CarpetServerMixin
{
	/**
	 * We need to steal the MinecraftServer object fast enough, and {@link carpet.CarpetExtension#onServerLoaded} is too late
	 * <p>
	 * Reason: We need to access our {@link CarpetTISAdditionServer#minecraft_server} in those rule validator logics
	 * when carpet invokes the {@link carpet.settings.SettingsManager#attachServer} in {@link CarpetServer#onServerLoaded}
	 */
	@Inject(method = "onServerLoaded", at = @At("HEAD"), remap = false)
	private static void stealMinecraftServerObjectFast$TISCM(MinecraftServer server, CallbackInfo ci)
	{
		CarpetTISAdditionServer.minecraft_server = server;
	}
}
