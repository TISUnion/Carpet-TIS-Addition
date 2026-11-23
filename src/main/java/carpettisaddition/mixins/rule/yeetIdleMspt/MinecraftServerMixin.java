/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.yeetIdleMspt;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC >= 1.20 && MC < 1.20.3
//$$ import carpet.fakes.MinecraftServerInterface;
//#elseif MC < 1.20
import carpet.helpers.TickSpeed;
//#endif

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
	@ModifyArg(
			//#if MC >= 1.21.11
			//$$ method = "processPacketsAndTick",
			//#elseif MC >= 1.20.3
			//$$ method = "runServer",
			//#else
			// it's hard to mixin into `carpet$modifiedRunLoop`, we choose it as the alternate
			method = "tickServer",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.20.3
					//$$ target = "Lnet/minecraft/server/MinecraftServer;tickServer(Ljava/util/function/BooleanSupplier;)V"
					//#else
					target = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V"
					//#endif
			)
	)
	private BooleanSupplier yeetIdleMspt_thereNoRemainingTime(BooleanSupplier shouldKeepTicking)
	{
		if (!CarpetTISAdditionSettings.yeetIdleMspt)
		{
			return shouldKeepTicking;
		}
		//#if MC < 1.20.3
		// Match Carpet's warp check even when the final warp tick has already been counted.
		//#if MC >= 1.20
		//$$ if (((MinecraftServerInterface)this).getTickRateManager().isInWarpSpeed())
		//#else
		if (TickSpeed.time_warp_start_time != 0)
		//#endif
		{
			return shouldKeepTicking;
		}
		//#endif
		return () -> false;
	}
}
