package carpettisaddition.mixins.command.lifetime;

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.commands.lifetime.interfaces.IServerWorld;
import carpettisaddition.commands.lifetime.removal.RemovalReason;
import carpettisaddition.commands.lifetime.spawning.SpawningReason;
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerUtil;
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
public abstract class EntityMixin implements IEntity
{
	@Shadow public World world;

	@Shadow public abstract Vec3d getPos();

	@Shadow public abstract EntityType<?> getType();

	private long spawnTime;
	private boolean doLifeTimeTracking;
	private boolean recordedSpawning;
	private boolean recordedRemoval;
	private Vec3d spawnPos;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onConstructLifeTimeTracker(CallbackInfo ci)
	{
		this.doLifeTimeTracking = false;
		this.recordedSpawning = false;
		this.recordedRemoval = false;
		if (this.world instanceof ServerWorld)
		{
			this.spawnTime = ((IServerWorld)this.world).getLifeTimeWorldTracker().getSpawnStageCounter();
			this.doLifeTimeTracking = LifeTimeTracker.isActivated() && LifeTimeTrackerUtil.isTrackedEntity((Entity) (Object) this);
		}
	}

	@Override
	public long getLifeTime()
	{
		return ((IServerWorld)this.world).getLifeTimeWorldTracker().getSpawnStageCounter() - this.spawnTime;
	}

	@Override
	public Vec3d getSpawnPosition()
	{
		return this.spawnPos;
	}

	@Override
	public void recordSpawning(SpawningReason reason)
	{
		if (this.doLifeTimeTracking && !this.recordedSpawning)
		{
			//noinspection ConstantConditions
			if ((Entity)(Object)this instanceof MobEntity && !GameUtil.countsTowardsMobcap((Entity)(Object)this))
			{
				return;
			}
			this.recordedSpawning = true;
			this.spawnPos = this.getPos();
			((IServerWorld)this.world).getLifeTimeWorldTracker().onEntitySpawn((Entity)(Object)this, reason);
		}
	}

	@Override
	public void recordRemoval(RemovalReason reason)
	{
		if (this.doLifeTimeTracking && this.recordedSpawning && this.spawnPos != null && !this.recordedRemoval)
		{
			this.recordedRemoval = true;
			((IServerWorld)this.world).getLifeTimeWorldTracker().onEntityRemove((Entity)(Object)this, reason);
		}
	}
}
