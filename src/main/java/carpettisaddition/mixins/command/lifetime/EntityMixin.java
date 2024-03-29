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

package carpettisaddition.mixins.command.lifetime;

import carpettisaddition.commands.lifetime.LifetimeTrackerTargetImpl;
import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.RemovalReason;
import carpettisaddition.commands.lifetime.spawning.SpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements LifetimeTrackerTarget
{
	private LifetimeTrackerTargetImpl lifeTimeTrackerHook$TISCM;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onConstructLifeTimeTracker(CallbackInfo ci)
	{
		this.lifeTimeTrackerHook$TISCM = new LifetimeTrackerTargetImpl((Entity)(Object)this);
	}

	@Override
	public int getTrackId()
	{
		return this.lifeTimeTrackerHook$TISCM.getTrackId();
	}

	@Override
	public long getLifeTime()
	{
		return this.lifeTimeTrackerHook$TISCM.getLifeTime();
	}

	@Override
	public Vec3d getSpawningPosition()
	{
		return this.lifeTimeTrackerHook$TISCM.getSpawningPosition();
	}

	@Override
	public Vec3d getRemovalPosition()
	{
		return this.lifeTimeTrackerHook$TISCM.getRemovalPosition();
	}

	@Override
	public void recordSpawning(SpawningReason reason)
	{
		this.lifeTimeTrackerHook$TISCM.recordSpawning(reason);
	}

	@Override
	public void recordRemoval(RemovalReason reason)
	{
		this.lifeTimeTrackerHook$TISCM.recordRemoval(reason);
	}
}
