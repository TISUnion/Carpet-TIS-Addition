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

package carpettisaddition.mixins.logger.microtiming.tickstages.entity;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.interfaces.WorldWithEntityTickingOrder;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.EntitySubStage;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(World.class)
public abstract class WorldMixin implements WorldWithEntityTickingOrder
{
	private int entityOrderCounter;

	public void setEntityOrderCounter(int value)
	{
		this.entityOrderCounter = value;
	}

	@Inject(method = "tickEntity", at = @At("HEAD"))
	private void beforeTickEntity(Consumer<Entity> consumer, Entity entity, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setSubTickStage((World)(Object)this, new EntitySubStage(entity, this.entityOrderCounter++));
	}

	@Inject(method = "tickEntity", at = @At("RETURN"))
	private void afterTickEntity(Consumer<Entity> consumer, Entity entity, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setSubTickStage((World)(Object)this, null);
	}
}
