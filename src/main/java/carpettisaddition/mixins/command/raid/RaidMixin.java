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

package carpettisaddition.mixins.command.raid;

import carpettisaddition.commands.raid.RaidTracker;
import carpettisaddition.commands.raid.RaidWithIdAndWorld;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raid.class)
public abstract class RaidMixin implements RaidWithIdAndWorld
{
	@Inject(
			//#if MC >= 12105
			//$$ method = "<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/Difficulty;)V",
			//#else
			method = "<init>(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V",
			//#endif
			at = @At(value = "RETURN")
	)
	private void raidCommand_onConstruct(CallbackInfo ci)
	{
		RaidTracker.getInstance().trackRaidGenerated((Raid)(Object)this);
	}

	@Inject(
			method = "joinRaid",
			at = @At(value = "RETURN")
	)
	private void raidCommand_onAddedRaider(CallbackInfo ci, @Local(argsOnly = true) Raider raider, @Local(argsOnly = true) boolean existing)
	{
		if (!existing)
		{
			RaidTracker.getInstance().trackNewRaider(raider);
		}
	}
}
