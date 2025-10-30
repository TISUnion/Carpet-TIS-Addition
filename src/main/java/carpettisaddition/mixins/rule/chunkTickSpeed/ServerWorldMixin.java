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

package carpettisaddition.mixins.rule.chunkTickSpeed;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin
{
	@Shadow public abstract void tickChunk(LevelChunk chunk, int randomTickSpeed);

	private int depth;

	@Inject(method = "tickChunk", at = @At("HEAD"), cancellable = true)
	void checkIfCancelAndInitDepth(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.chunkTickSpeed == 0)
		{
			ci.cancel();
			return;
		}
		this.depth++;
	}

	@Inject(method = "tickChunk", at = @At("RETURN"))
	void tickMultipleTimes(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci)
	{
		if (this.depth == 1)
		{
			// in case rule gets changed during the chunk tick stage
			int ruleValue = CarpetTISAdditionSettings.chunkTickSpeed;
			for (int i = 0; i < ruleValue - 1; i++)
			{
				this.tickChunk(chunk, randomTickSpeed);
			}
		}
		this.depth--;
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerLevel;getChunkSource()Lnet/minecraft/server/level/ServerChunkCache;"
			)
	)
	void resetTickChunkDepth(BooleanSupplier shouldKeepTicking, CallbackInfo ci)
	{
		// just in case whatever happened
		this.depth = 0;
	}
}
