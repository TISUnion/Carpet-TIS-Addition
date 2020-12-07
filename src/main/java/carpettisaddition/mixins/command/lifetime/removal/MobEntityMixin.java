package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin
{
	@Shadow private boolean persistent;

	@Inject(
			method = "checkDespawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/mob/MobEntity;discard()V",
					ordinal = 0
			)
	)
	private void onDifficultyDespawnLifeTimeTracker(CallbackInfo ci)
	{
		((IEntity)this).recordRemoval(LiteralRemovalReason.DESPAWN_DIFFICULTY);
	}

	@Inject(
			method = "checkDespawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/mob/MobEntity;discard()V",
					ordinal = 1
			)
	)
	private void onImmediatelyDespawnLifeTimeTracker(CallbackInfo ci)
	{
		((IEntity)this).recordRemoval(LiteralRemovalReason.DESPAWN_IMMEDIATELY);
	}

	@Inject(
			method = "checkDespawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/mob/MobEntity;discard()V",
					ordinal = 2
			)
	)
	private void onRandomlyDespawnLifeTimeTracker(CallbackInfo ci)
	{
		((IEntity)this).recordRemoval(LiteralRemovalReason.DESPAWN_RANDOMLY);
	}

	@Inject(method = "setPersistent", at = @At("HEAD"))
	private void onEntityPersistentLifeTimeTracker(CallbackInfo ci)
	{
		((IEntity)this).recordRemoval(LiteralRemovalReason.PERSISTENT);
	}

	@Inject(
			method = "equipLootStack",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/entity/mob/MobEntity;persistent:Z"
			)
	)
	private void onEntityPersistent2LifeTimeTracker(CallbackInfo ci)
	{
		((IEntity)this).recordRemoval(LiteralRemovalReason.PERSISTENT);
	}
}
