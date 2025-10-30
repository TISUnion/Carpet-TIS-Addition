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

package carpettisaddition.mixins.logger.microtiming.tickstages.raid;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.world.entity.raid.Raids;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 12105
//$$ import com.llamalad7.mixinextras.sugar.Local;
//#else
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
//#endif

@Mixin(Raids.class)
public abstract class RaidManagerMixin
{
	//#if MC < 12105
	@Shadow @Final private ServerLevel level;
	//#endif

	@Inject(method = "tick", at = @At("HEAD"))
	private void enterStageRaid(
			CallbackInfo ci
			//#if MC >= 12105
			//$$ , @Local ServerWorld world
			//#endif
	)
	{
		//#if MC < 12105
		ServerLevel world = this.level;
		//#endif
		MicroTimingLoggerManager.setTickStage(world, TickStage.RAID);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void exitStageRaid(
			CallbackInfo ci
			//#if MC >= 12105
			//$$ , @Local ServerWorld world
			//#endif
	)
	{
		//#if MC < 12105
		ServerLevel world = this.level;
		//#endif
		MicroTimingLoggerManager.setTickStage(world, TickStage.UNKNOWN);
	}
}
