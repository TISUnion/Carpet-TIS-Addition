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
import net.minecraft.server.ServerTickRateManager;
import net.minecraft.world.TickRateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerTickRateManager.class)
public abstract class ServerTickRateManagerMixin extends TickRateManager implements DeepFreezableServerTickRateManager
{
	@Unique
	private boolean deeplyFrozen$TISCM = false;

	@Override
	public boolean isDeeplyFrozen$TISCM()
	{
		return this.isFrozen() && this.deeplyFrozen$TISCM;
	}

	@Override
	public void setDeeplyFrozen$TISCM(boolean deeplyFrozen)
	{
		this.deeplyFrozen$TISCM = deeplyFrozen;
	}

	@Inject(method = "setFrozen", at = @At("HEAD"))
	private void tickFreezeDeepCommand_clearDeepFrozenStateIfThatExists(boolean frozen, CallbackInfo ci)
	{
		if (!frozen)
		{
			this.deeplyFrozen$TISCM = false;
		}
	}
}
