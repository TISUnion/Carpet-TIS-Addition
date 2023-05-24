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

package carpettisaddition.mixins.carpet.tweaks.freezeActionPackTicking;

import carpet.helpers.EntityPlayerActionPack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 12000
//$$ import carpet.fakes.LevelInterface;
//$$ import carpettisaddition.CarpetTISAdditionServer;
//#else
import carpet.helpers.TickSpeed;
//#endif

@Mixin(EntityPlayerActionPack.class)
public abstract class EntityPlayerActionPackMixin
{
	@Shadow @Final private ServerPlayerEntity player;

	@Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true, remap = false)
	private void stopUpdatingWhenTickFrozen(CallbackInfo ci)
	{
		if (!
				//#if MC >= 12000
				//$$ ((LevelInterface)this.player.getServerWorld()).tickRateManager().runsNormally()
				//#else
				TickSpeed.process_entities
				//#endif
		)
		{
			ci.cancel();
		}
	}
}
