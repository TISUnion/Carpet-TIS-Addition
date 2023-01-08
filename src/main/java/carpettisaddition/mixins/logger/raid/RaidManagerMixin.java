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
import net.minecraft.entity.raid.Raid;
import net.minecraft.entity.raid.RaidManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;


@Mixin(RaidManager.class)
public abstract class RaidManagerMixin
{
	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							//#if MC >= 11600
							//$$ target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"
							//#else
							target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$RuleKey;)Z"
							//#endif
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/village/raid/Raid;invalidate()V",
					//#else
					target = "Lnet/minecraft/entity/raid/Raid;invalidate()V",
					//#endif
					ordinal = 0
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void onInvalidatedByGamerule(CallbackInfo ci, Iterator<Raid> iterator, Raid raid)
	{
		((IRaid)raid).onRaidInvalidated(RaidLogger.InvalidateReason.GAMERULE_DISABLE);
	}
}
