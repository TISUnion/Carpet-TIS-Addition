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

package carpettisaddition.mixins.logger.mobcapsLocal;

import carpet.logging.HUDController;
import carpettisaddition.logging.TISAdditionHUDController;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.mobcapsLocal.MobcapsLocalLogger;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(HUDController.class)
public abstract class HUDControllerMixin
{
	@Inject(
			method = "update_hud",
			at = @At(
					value = "FIELD",
					target = "Lcarpet/logging/LoggerRegistry;__counter:Z",
					remap = false
			),
			remap = false
	)
	private static void updateMobCapsLocalLogger(MinecraftServer packetData, List<ServerPlayerEntity> packet, CallbackInfo ci)
	{
		TISAdditionHUDController.doHudLogging(TISAdditionLoggerRegistry.__mobcapsLocal, MobcapsLocalLogger.NAME, MobcapsLocalLogger.getInstance());
	}
}