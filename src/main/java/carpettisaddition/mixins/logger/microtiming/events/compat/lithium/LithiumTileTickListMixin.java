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

package carpettisaddition.mixins.logger.microtiming.events.compat.lithium;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import me.jellysquid.mods.lithium.common.world.scheduler.LithiumServerTickScheduler;
import me.jellysquid.mods.lithium.common.world.scheduler.TickEntry;
import net.minecraft.server.world.ServerTickScheduler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = "<1.18"),
		@Condition(ModIds.lithium)
})
@Mixin(LithiumServerTickScheduler.class)
public abstract class LithiumTileTickListMixin<T> extends ServerTickScheduler<T>
{
	@Shadow(remap = false) @Final private ServerWorld world;

	private boolean scheduleSuccess;

	public LithiumTileTickListMixin(
			ServerWorld world, Predicate<T> invalidObjPredicate, Function<T, Identifier> idToName,
			//#if MC < 11600
			Function<Identifier, T> nameToId,
			//#endif
			Consumer<ScheduledTick<T>> scheduledTickConsumer
	)
	{
		super(
				world, invalidObjPredicate, idToName,
				//#if MC < 11600
				nameToId,
				//#endif
				scheduledTickConsumer
		);
	}

	@Inject(method = "schedule", at = @At("HEAD"))
	private void startScheduleTileTickEvent(CallbackInfo ci)
	{
		this.scheduleSuccess = false;
	}

	@Inject(
			//#if MC >= 11700
			//$$ method = "scheduleTick",
			//$$ at = @At(
			//$$ 		value = "INVOKE_ASSIGN",
			//$$ 		target = "Lit/unimi/dsi/fastutil/objects/ObjectOpenHashSet;add(Ljava/lang/Object;)Z",
			//$$ 		ordinal = 0,
			//$$ 		remap = false
			//$$ ),
			//#else
			method = "addScheduledTick",
			at = @At(
					value = "FIELD",
					target = "Lme/jellysquid/mods/lithium/common/world/scheduler/TickEntry;scheduled:Z",
					ordinal = 0
			),
			//#endif
			locals = LocalCapture.CAPTURE_FAILHARD,
			remap = false
	)
	private void checkScheduledState(
			//#if MC >= 11700
			//$$ BlockPos pos, Object object, long time, TickPriority priority, CallbackInfo ci, TickEntry<T> tick, boolean added
			//#else
			ScheduledTick<T> tick, CallbackInfo ci, TickEntry<T> entry
			//#endif
	)
	{
		this.scheduleSuccess =
				//#if MC >= 11700
				//$$ added;
				//#else
				!entry.scheduled;
				//#endif
	}

	@Inject(method = "schedule", at = @At("RETURN"))
	private void endScheduleTileTickEvent(BlockPos pos, T object, int delay, TickPriority priority, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onScheduleTileTickEvent(this.world, object, pos, delay, priority, this.scheduleSuccess);
	}
}
