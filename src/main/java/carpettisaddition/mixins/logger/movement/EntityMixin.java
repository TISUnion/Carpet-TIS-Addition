package carpettisaddition.mixins.logger.movement;

import carpettisaddition.logging.loggers.movement.MovementLogger;
import carpettisaddition.logging.loggers.movement.MovementLoggerTarget;
import carpettisaddition.logging.loggers.movement.MovementModification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/**
 * Larger priority means being applied later, so other movement cancelling logics can be applied first
 */
@Mixin(value = Entity.class, priority = 2000)
public abstract class EntityMixin implements MovementLoggerTarget
{
	// ============= Interface Implementations =============

	@Nullable
	private MovementLogger.Tracker movementTracker$TCA = null;

	@Override
	public Optional<MovementLogger.Tracker> getMovementTracker()
	{
		return Optional.ofNullable(this.movementTracker$TCA);
	}

	@Override
	public void setMovementTracker(MovementLogger.@Nullable Tracker tracker)
	{
		this.movementTracker$TCA = tracker;
	}

	// ============= Life cycle =============

	@Inject(method = "move", at = @At("HEAD"))
	private void onMovementStart_movementLogger(MovementType type, Vec3d movement, CallbackInfo ci)
	{
		MovementLogger.getInstance().create((Entity)(Object)this, type, movement);
	}

	@Inject(method = "move", at = @At("RETURN"))
	private void onMovementEnd_movementLogger(CallbackInfo ci)
	{
		MovementLogger.getInstance().finalize((Entity)(Object)this);
	}

	// ============= Modifications =============

	@Inject(
			method = "move",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/entity/Entity;adjustMovementForPiston(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;",
					shift = At.Shift.AFTER
			)
	)
	private void onMovementModified_piston(MovementType type, Vec3d movement, CallbackInfo ci)
	{
		this.getMovementTracker().ifPresent(tracker -> tracker.recordModification(MovementModification.PISTON, movement));
	}

	@Inject(
			method = "move",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/entity/Entity;adjustMovementForSneaking(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/MovementType;)Lnet/minecraft/util/math/Vec3d;",
					shift = At.Shift.AFTER
			)
	)
	private void onMovementModified_sneaking(MovementType type, Vec3d movement, CallbackInfo ci)
	{
		this.getMovementTracker().ifPresent(tracker -> tracker.recordModification(MovementModification.SNEAKING, movement));
	}

	@ModifyVariable(
			method = "move",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/entity/Entity;adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;",
					shift = At.Shift.AFTER
			),
			ordinal = 1
	)
	private Vec3d onMovementModified_collision(Vec3d movement)
	{
		this.getMovementTracker().ifPresent(tracker -> tracker.recordModification(MovementModification.COLLISION, movement));
		return movement;
	}
}
