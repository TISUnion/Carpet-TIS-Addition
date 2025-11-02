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

package carpettisaddition.mixins.logger.raid;

import carpettisaddition.logging.loggers.raid.IRaid;
import carpettisaddition.logging.loggers.raid.RaidLogger;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 12105
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(Raids.class)
public abstract class RaidManagerMixin
{
	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							//#if MC >= 11600
							//$$ target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"
							//#else
							target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"
							//#endif
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/entity/raid/Raid;stop()V",
					//#else
					target = "Lnet/minecraft/world/entity/raid/Raid;stop()V",
					//#endif
					ordinal = 0
			)
	)
	private void raidLogger_onInvalidatedByGameRule(CallbackInfo ci, @Local Raid raid)
	{
		((IRaid)raid).onRaidInvalidated$TISCM(RaidLogger.InvalidateReason.GAMERULE_DISABLE);
	}

	//#if MC >= 12105
	//$$ /**
	//$$  * Call `onRaidCreated()` after {@link carpettisaddition.commands.raid.RaidWithIdAndWorld#setRaidId$TISCM} and {@link carpettisaddition.commands.raid.RaidWithIdAndWorld#setRaidWorld$TISCM} was called
	//$$  * in {@link carpettisaddition.mixins.command.raid.RaidManagerMixin#raidCommand_recordRaidFields},
	//$$  * to make sure `getRaidId$TISCM()` and `getRaidWorld$TISCM()` can be used
	//$$  */
	//$$ @Inject(
	//$$ 		method = "startRaid",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;put(ILjava/lang/Object;)Ljava/lang/Object;",
	//$$ 				shift = At.Shift.AFTER,
	//$$ 				remap = false
	//$$ 		)
	//$$ )
	//$$ private void raidLogger_onRaidAddToManager(CallbackInfoReturnable<Raid> cir, @Local Raid raid)
	//$$ {
	//$$ 	RaidLogger.getInstance().onRaidCreated(raid);
	//$$ }
	//#endif
}
