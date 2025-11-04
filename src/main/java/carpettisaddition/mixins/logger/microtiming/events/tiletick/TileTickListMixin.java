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

package carpettisaddition.mixins.logger.microtiming.events.tiletick;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.world.level.ServerTickList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.TickNextTickData;
import net.minecraft.world.level.TickPriority;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ServerTickList.class)
public abstract class TileTickListMixin<T>
{
	@Shadow @Final private ServerLevel level;
	@Shadow @Final private Set<TickNextTickData<T>> tickNextTickSet;

	@Inject(method = "scheduleTick", at = @At("HEAD"))
	private void startScheduleTileTickEvent(CallbackInfo ci, @Share("oldListSize") LocalIntRef oldListSize)
	{
		oldListSize.set(this.tickNextTickSet.size());
	}

	@Inject(method = "scheduleTick", at = @At("RETURN"))
	private void endScheduleTileTickEvent(BlockPos pos, T object, int delay, TickPriority priority, CallbackInfo ci, @Share("oldListSize") LocalIntRef oldListSize)
	{
		MicroTimingLoggerManager.onScheduleTileTickEvent(this.level, object, pos, delay, priority, this.tickNextTickSet.size() > oldListSize.get());
	}
}
