package carpettisaddition.mixins.carpet.tweaks.logger.explosion;

import carpettisaddition.helpers.carpet.tweaks.logger.explosion.ITntEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * priority 2000 to make sure it injects after carpet's modifyTNTAngle injection
 */
@Mixin(value = TntEntity.class, priority = 2000)
public abstract class TntEntityMixin extends Entity implements ITntEntity
{
	private Vec3d initializedVelocity;
	private Vec3d initializedPosition;

	public TntEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	@Inject(
			method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/entity/LivingEntity;)V",
			at = @At("TAIL")
	)
	private void recordInitializedAngle(CallbackInfo ci)
	{
		this.initializedVelocity = this.getVelocity();
		this.initializedPosition = this.getPos();
	}

	@Override
	public boolean dataRecorded()
	{
		return this.getInitializedVelocity() != null && this.getInitializedPosition() != null;
	}

	@Override
	public Vec3d getInitializedVelocity()
	{
		return this.initializedVelocity;
	}

	@Override
	public Vec3d getInitializedPosition()
	{
		return this.initializedPosition;
	}
}
