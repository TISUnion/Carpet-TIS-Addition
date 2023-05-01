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

package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionException;
import carpettisaddition.utils.mixin.testers.YeetUpdateSuppressionCrashTester;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.function.BooleanSupplier;

@Restriction(require = @Condition(type = Condition.Type.TESTER, tester = YeetUpdateSuppressionCrashTester.class))
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
	@Redirect(
			method = "tickWorlds",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V"
			)
	)
	private void yeetUpdateSuppressionCrash_implOnTickWorlds(ServerWorld serverWorld, BooleanSupplier shouldKeepTicking)
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash)
		{
			try
			{
				serverWorld.tick(shouldKeepTicking);
			}
			catch (Throwable throwable)
			{
				Optional<UpdateSuppressionException> opt = UpdateSuppressionException.extractInCauses(throwable);
				if (opt.isPresent())
				{
					opt.get().report();
				}
				else
				{
					throw throwable;
				}
			}
		}
		else
		{
			// vanilla
			serverWorld.tick(shouldKeepTicking);
		}
	}
}
