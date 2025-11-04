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

package carpettisaddition.mixins.logger.microtiming.tickstages.tiletick;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.interfaces.ITileTickListWithServerWorld;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.TileTickSubStage;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ticks.LevelTicks;
import net.minecraft.world.ticks.ScheduledTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(LevelTicks.class)
public abstract class WorldTickScheduler_TileTickDetailMixin
{
	@Unique
	private int tileTickOrderCounter$TISCM;

	@Inject(method = "runCollectedTicks", at = @At("HEAD"))
	private void startExecutingTileTickEvents(CallbackInfo ci)
	{
		this.tileTickOrderCounter$TISCM = 0;
	}

	@Inject(
			method = "runCollectedTicks",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V"
			)
	)
	private void beforeExecuteTileTickEvent(BiConsumer<BlockPos, ?> biConsumer, CallbackInfo ci, @Local ScheduledTick<?> orderedTick)
	{
		ServerLevel serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld$TISCM();
		if (serverWorld != null)
		{
			MicroTimingLoggerManager.setTickStageDetail(serverWorld, String.valueOf(orderedTick.priority().getValue()));
			MicroTimingLoggerManager.setSubTickStage(serverWorld, new TileTickSubStage(serverWorld, orderedTick, this.tileTickOrderCounter$TISCM++));
		}
	}

	@Inject(
			method = "runCollectedTicks",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V",
					shift = At.Shift.AFTER
			)
	)
	private void afterExecuteTileTickEvent(BiConsumer<BlockPos, ?> biConsumer, CallbackInfo ci)
	{
		ServerLevel serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld$TISCM();
		if (serverWorld != null)
		{
			MicroTimingLoggerManager.setTickStageDetail(serverWorld, null);
			MicroTimingLoggerManager.setSubTickStage(serverWorld, null);
		}
	}
}