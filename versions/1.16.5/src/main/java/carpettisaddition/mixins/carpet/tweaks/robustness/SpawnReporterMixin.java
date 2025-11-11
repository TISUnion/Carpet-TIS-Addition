/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.mixins.carpet.tweaks.robustness;

import carpet.utils.Messenger;
import carpet.utils.SpawnReporter;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SpawnReporter.class)
public abstract class SpawnReporterMixin
{
	/**
	 * The {@link net.minecraft.server.level.ServerChunkCache#lastSpawnState} will be null
	 * until the {@link net.minecraft.server.level.ServerChunkCache#tickChunks} is called.
	 * <p>
	 * In MC < 1.21.9, it might be not an issue, since even in single player,
	 * no player (even the single player owner) will be added to any carpet logger's
	 * {@link carpet.logging.Logger#subscribedOnlinePlayers} before the first tickChunks call
	 * <p>
	 * But in MC >= 1.21.9, the timing of player placement changed.
	 * Now single player owner will be added to subscribedOnlinePlayers
	 * (called from {@link net.minecraft.server.players.PlayerList#placeNewPlayer} earlier, causing the first
	 * {@link carpet.utils.SpawnReporter#printMobcapsForDimension} call happens before the first tickChunk called,
	 * if rule HUDLoggerUpdateInterval is set to 1, resulting in an NullPointerException being thrown
	 */
	@Inject(
			method = "printMobcapsForDimension",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/NaturalSpawner$SpawnState;getMobCategoryCounts()Lit/unimi/dsi/fastutil/objects/Object2IntMap;"
			),
			cancellable = true
	)
	private static void youForgetTheNullCheck(
			CallbackInfoReturnable<List<Component>> cir,
			@Local List<Component> lst,
			@Local NaturalSpawner.SpawnState lastSpawner
	)
	{
		if (lastSpawner == null)
		{
			lst.add(Messenger.c("g   --UNAVAILABLE--"));
			cir.setReturnValue(lst);
		}
	}
}
