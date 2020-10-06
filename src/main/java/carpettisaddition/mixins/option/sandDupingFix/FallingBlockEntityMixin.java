package carpettisaddition.mixins.option.sandDupingFix;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity
{
	public FallingBlockEntityMixin(EntityType<? extends FallingBlockEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					shift = At.Shift.AFTER,
					target = "Lnet/minecraft/entity/FallingBlockEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"
			),
			cancellable = true
	)
	private void afterMove(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.sandDupingFix)
		{
			// one-line code to fix
			// thank mojang for not fixing
			if (this.removed)
			{
				ci.cancel();
			}
		}
	}
}
