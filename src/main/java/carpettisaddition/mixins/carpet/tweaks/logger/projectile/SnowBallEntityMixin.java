package carpettisaddition.mixins.carpet.tweaks.logger.projectile;

import carpet.logging.LoggerRegistry;
import carpettisaddition.helpers.carpet.tweaks.logger.projectile.VisualizeTrajectoryHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SnowballEntity.class)
public abstract class SnowBallEntityMixin extends ThrownItemEntity
{
	public SnowBallEntityMixin(EntityType<? extends ThrownItemEntity> type, World world)
	{
		super(type, world);
	}

	@Intrinsic
	@Override
	public void tick()
	{
		if (VisualizeTrajectoryHelper.isVisualizeProjectile(this))
		{
			if (LoggerRegistry.getLogger("projectiles").hasOnlineSubscribers())
			{
				if (this.getVelocity().lengthSquared() > 0)
				{
					this.velocityDirty = true;
				}
				this.setVelocity(Vec3d.ZERO);
				VisualizeTrajectoryHelper.addVisualizer(this);
			}
			else
			{
				this.remove();
			}
		}
		else
		{
			super.tick();
		}
	}

	@Intrinsic
	@Override
	public boolean damage(DamageSource source, float amount)
	{
		if (source.getAttacker() instanceof ServerPlayerEntity && VisualizeTrajectoryHelper.isVisualizeProjectile(this))
		{
			VisualizeTrajectoryHelper.onVisualizerReacted((ServerPlayerEntity)source.getAttacker(), this);
		}
		return super.damage(source, amount);
	}
}
