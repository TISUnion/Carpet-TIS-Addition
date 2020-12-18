package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import carpettisaddition.commands.lifetime.removal.MobPickupRemovalReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin extends Entity
{
	public ExperienceOrbEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "intValue=6000"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ExperienceOrbEntity;remove()V"
			)
	)
	private void onDespawnLifeTimeTracker(CallbackInfo ci)
	{
		((IEntity)this).recordRemoval(LiteralRemovalReason.DESPAWN_TIMEOUT);
	}

	@Inject(
			method = "onPlayerCollision",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ExperienceOrbEntity;remove()V"
			)
	)
	private void onPickupLifeTimeTracker(PlayerEntity player, CallbackInfo ci)
	{
		((IEntity)this).recordRemoval(new MobPickupRemovalReason(player.getType()));
	}
}
