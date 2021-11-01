package carpettisaddition.mixins.command.lifetime;

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.lifetime.LifeTimeWorldTracker;
import carpettisaddition.commands.lifetime.filter.EntityFilterManager;
import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.interfaces.ServerWorldWithLifeTimeTracker;
import carpettisaddition.commands.lifetime.removal.RemovalReason;
import carpettisaddition.commands.lifetime.spawning.SpawningReason;
import carpettisaddition.utils.GameUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements LifetimeTrackerTarget
{
	@Shadow public World world;

	@Shadow public abstract Vec3d getPos();

	@Shadow public abstract EntityType<?> getType();

	private long spawnTime;
	private boolean doLifeTimeTracking;
	private boolean recordedSpawning;
	private boolean recordedRemoval;
	private Vec3d spawningPos;
	private Vec3d removalPos;
	private int trackId;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onConstructLifeTimeTracker(CallbackInfo ci)
	{
		this.recordedSpawning = false;
		this.recordedRemoval = false;
		if (this.world instanceof ServerWorld)
		{
			// In case the entity is loaded when the world is being constructed
			// Not sure if it's possible in vanilla, at least it happens in #29
			LifeTimeWorldTracker tracker = ((ServerWorldWithLifeTimeTracker)this.world).getLifeTimeWorldTracker();
			if (tracker != null)
			{
				// do track
				this.spawnTime = tracker.getSpawnStageCounter();
				this.trackId = LifeTimeTracker.getInstance().getCurrentTrackId();
				this.doLifeTimeTracking = LifeTimeTracker.getInstance().willTrackEntity((Entity)(Object)this);
				return;
			}
		}
		// dont track
		this.trackId = -1;
		this.doLifeTimeTracking = false;
	}

	@Override
	public int getTrackId()
	{
		return this.trackId;
	}

	@Override
	public long getLifeTime()
	{
		return ((ServerWorldWithLifeTimeTracker)this.world).getLifeTimeWorldTracker().getSpawnStageCounter() - this.spawnTime;
	}

	@Override
	public Vec3d getSpawningPosition()
	{
		return this.spawningPos;
	}

	@Override
	public Vec3d getRemovalPosition()
	{
		return this.removalPos;
	}

	@Override
	public void recordSpawning(SpawningReason reason)
	{
		if (this.doLifeTimeTracking && !this.recordedSpawning && EntityFilterManager.getInstance().test((Entity)(Object)this))
		{
			//noinspection ConstantConditions
			if ((Entity)(Object)this instanceof MobEntity && !GameUtil.countsTowardsMobcap((Entity)(Object)this))
			{
				return;
			}
			this.recordedSpawning = true;
			this.spawningPos = this.getPos();
			((ServerWorldWithLifeTimeTracker)this.world).getLifeTimeWorldTracker().onEntitySpawn((Entity)(Object)this, reason);
		}
	}

	@Override
	public void recordRemoval(RemovalReason reason)
	{
		if (this.doLifeTimeTracking && this.recordedSpawning && this.spawningPos != null && !this.recordedRemoval)
		{
			this.recordedRemoval = true;
			this.removalPos = this.getPos();
			((ServerWorldWithLifeTimeTracker)this.world).getLifeTimeWorldTracker().onEntityRemove((Entity)(Object)this, reason);
		}
	}
}
