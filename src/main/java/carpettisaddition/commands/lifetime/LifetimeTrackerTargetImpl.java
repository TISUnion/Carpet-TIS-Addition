package carpettisaddition.commands.lifetime;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.lifetime.filter.EntityFilterManager;
import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.interfaces.ServerWorldWithLifeTimeTracker;
import carpettisaddition.commands.lifetime.removal.RemovalReason;
import carpettisaddition.commands.lifetime.spawning.SpawningReason;
import carpettisaddition.utils.GameUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;

public class LifetimeTrackerTargetImpl implements LifetimeTrackerTarget
{
	private final Entity entity;
	private final LifeTimeWorldTracker tracker;
	private final int trackId;
	private final boolean doLifeTimeTracking;

	private boolean recordedSpawning;
	private long spawnTime;
	private Vec3d spawningPos;

	private boolean recordedRemoval;
	private long removalTime;
	private Vec3d removalPos;

	public LifetimeTrackerTargetImpl(Entity entity)
	{
		this.entity = entity;
		World world = entity.world;

		this.recordedSpawning = false;
		this.recordedRemoval = false;
		if (world instanceof ServerWorld)
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
	public Vec3d getSpawningPosition()
	{
		return Objects.requireNonNull(this.spawningPos, "Spawning is not recorded");
	}

	@Override
	public Vec3d getRemovalPosition()
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
					this.entity instanceof MobEntity &&
					!GameUtil.countsTowardsMobcap(this.entity)
			)
			{
				return;
			}
			this.recordedSpawning = true;
			this.spawningPos = this.entity.getPos();
			this.spawnTime = this.tracker.getSpawnStageCounter();
			this.tracker.onEntitySpawn(this.entity, reason);
		}
	}

	@Override
	public void recordRemoval(RemovalReason reason)
	{
		if (this.doLifeTimeTracking && this.recordedSpawning && this.spawningPos != null && !this.recordedRemoval)
		{
			if (!reason.getRemovalType().isValid())
			{
				return;
			}
			this.recordedRemoval = true;
			this.removalPos = this.entity.getPos();
			this.removalTime = this.tracker.getSpawnStageCounter();
			this.tracker.onEntityRemove(this.entity, reason);
		}
	}
}