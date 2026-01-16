/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.tickFreezeDeepCommand;

import carpettisaddition.helpers.rule.tickFreezeDeepCommand.DeepFreezableServerTickRateManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTickRateManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
	@Shadow @Final
	private ServerTickRateManager tickRateManager;

	/**
	 * If we don't do this, MinecraftServer will hang forever at the removeTicketsOnClosing/deactivateTicketsOnClosing loop
	 * inside {@link MinecraftServer#stopServer}
	 */
	@Inject(method = "stopServer", at = @At("HEAD"))
	private void tickFreezeDeepCommand_disableDeepFreezeOnServerStop(CallbackInfo ci)
	{
		if (this.tickRateManager instanceof DeepFreezableServerTickRateManager)
		{
			((DeepFreezableServerTickRateManager)this.tickRateManager).setDeeplyFrozen$TISCM(false);
		}
	}
}
