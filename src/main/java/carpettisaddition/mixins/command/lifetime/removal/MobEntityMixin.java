package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import carpettisaddition.commands.lifetime.removal.MobPickupRemovalReason;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity
{
	protected MobEntityMixin(EntityType<? extends LivingEntity> type, World world)
	{
		super(type, world);
	}

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
			// slice for optifine reeee
			// optifine will inserts shits after getClosestPlayer
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/entity/Entity;squaredDistanceTo(Lnet/minecraft/entity/Entity;)D"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/mob/MobEntity;discard()V",
					ordinal = 0
			)
	)
	private void onImmediatelyDespawnLifeTimeTracker(CallbackInfo ci)
	{
		((IEntity)this).recordRemoval(LiteralRemovalReason.DESPAWN_IMMEDIATELY);
	}

	@Inject(
			method = "checkDespawn",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/entity/Entity;squaredDistanceTo(Lnet/minecraft/entity/Entity;)D"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/mob/MobEntity;discard()V",
					ordinal = 1
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

	@Inject(
			method = "loot",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ItemEntity;discard()V"
			)
	)
	private void onItemPickUpLifeTimeTracker(ItemEntity item, CallbackInfo ci)
	{
		((IEntity)item).recordRemoval(new MobPickupRemovalReason(this.getType()));
	}
}
