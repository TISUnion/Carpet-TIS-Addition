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

package carpettisaddition.commands.lifetime;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.lifetime.filter.EntityFilterManager;
import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.interfaces.ServerWorldWithLifeTimeTracker;
import carpettisaddition.commands.lifetime.removal.RemovalReason;
import carpettisaddition.commands.lifetime.spawning.SpawningReason;
import carpettisaddition.utils.EntityUtils;
import carpettisaddition.utils.GameUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class LifetimeTrackerTargetImpl implements LifetimeTrackerTarget
{
	private final Entity entity;
	private final LifeTimeWorldTracker tracker;
	private final int trackId;
	private final boolean doLifeTimeTracking;

	private boolean recordedSpawning;
	private long spawnTime;
	private Vec3 spawningPos;

	private boolean recordedRemoval;
	private long removalTime;
	private Vec3 removalPos;

	public LifetimeTrackerTargetImpl(Entity entity)
	{
		this.entity = entity;
		Level world = EntityUtils.getEntityWorld(entity);

		this.recordedSpawning = false;
		this.recordedRemoval = false;
		if (world instanceof ServerLevel)
		{
			// In case the entity is loaded when the world is being constructed
			// Not sure if it's possible in vanilla, at least it happens in #29
			LifeTimeWorldTracker tracker = ((ServerWorldWithLifeTimeTracker)world).getLifeTimeWorldTracker();
			if (tracker != null)
			{
				// do track
				this.tracker = tracker;
				this.trackId = LifeTimeTracker.getInstance().getCurrentTrackId();
				this.doLifeTimeTracking = LifeTimeTracker.getInstance().willTrackEntity(entity, false);
				return;
			}
		}
		// dont track
		this.tracker = null;
		this.trackId = -1;
		this.doLifeTimeTracking = false;
	}

	private void reset()
	{
		if (this.doLifeTimeTracking)
		{
			this.recordedSpawning = false;
			this.recordedRemoval = false;
		}
	}

	public boolean isActivated()
	{
		return this.doLifeTimeTracking;
	}

	@Override
	public int getTrackId()
	{
		return this.trackId;
	}

	@Override
	public long getLifeTime()
	{
		if (this.recordedSpawning && this.recordedRemoval)
		{
			return this.removalTime - this.spawnTime;
		}
		CarpetTISAdditionServer.LOGGER.warn("Try to query lifetime of {} without recording spawning or removal {} {}", this.entity, this.recordedSpawning, this.recordedRemoval);
		return -1;
	}

	@Override
	public Vec3 getSpawningPosition()
	{
		return Objects.requireNonNull(this.spawningPos, "Spawning is not recorded");
	}

	@Override
	public Vec3 getRemovalPosition()
	{
		return Objects.requireNonNull(this.removalPos, "Removal is not recorded");
	}

	@Override
	public void recordSpawning(SpawningReason reason)
	{
		if (!this.doLifeTimeTracking)
		{
			return;
		}

		// already finished lifetime recording, now do another recording
		if (this.recordedSpawning && this.recordedRemoval && reason.getSpawningType().isDoubleRecordSupported())
		{
			this.reset();
		}
		if (!this.recordedSpawning)
		{
			if (!EntityFilterManager.getInstance().test(this.entity))
			{
				return;
			}
			if (
					CarpetTISAdditionSettings.lifeTimeTrackerConsidersMobcap &&
					this.entity instanceof Mob &&
					!GameUtils.countsTowardsMobcap(this.entity)
			)
			{
				return;
			}
			this.recordedSpawning = true;
			this.spawningPos = this.entity.position();
			this.spawnTime = this.tracker.getSpawnStageCounter();
			this.tracker.onEntitySpawn(this.entity, reason);
		}
	}

	@Override
	public void recordRemoval(RemovalReason reason)
	{
		if (this.doLifeTimeTracking && this.recordedSpawning && this.spawningPos != null && !this.recordedRemoval)
		{
			if (!reason.getRemovalType().isValid(this.entity))
			{
				return;
			}
			this.recordedRemoval = true;
			this.removalPos = this.entity.position();
			this.removalTime = this.tracker.getSpawnStageCounter();
			this.tracker.onEntityRemove(this.entity, reason);
		}
	}
}
