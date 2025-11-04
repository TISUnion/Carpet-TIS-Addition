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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements LifetimeTrackerTarget
{
	@Unique
	private LifetimeTrackerTargetImpl lifeTimeTrackerHook$TISCM;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onConstructLifeTimeTracker(CallbackInfo ci)
	{
		this.lifeTimeTrackerHook$TISCM = new LifetimeTrackerTargetImpl((Entity)(Object)this);
	}

	@Override
	public int getTrackId$TISCM()
	{
		return this.lifeTimeTrackerHook$TISCM.getTrackId$TISCM();
	}

	@Override
	public long getLifeTime$TISCM()
	{
		return this.lifeTimeTrackerHook$TISCM.getLifeTime$TISCM();
	}

	@Override
	public Vec3 getSpawningPosition$TISCM()
	{
		return this.lifeTimeTrackerHook$TISCM.getSpawningPosition$TISCM();
	}

	@Override
	public Vec3 getRemovalPosition$TISCM()
	{
		return this.lifeTimeTrackerHook$TISCM.getRemovalPosition$TISCM();
	}

	@Override
	public void recordSpawning$TISCM(SpawningReason reason)
	{
		this.lifeTimeTrackerHook$TISCM.recordSpawning$TISCM(reason);
	}

	@Override
	public void recordRemoval$TISCM(RemovalReason reason)
	{
		this.lifeTimeTrackerHook$TISCM.recordRemoval$TISCM(reason);
	}
}
