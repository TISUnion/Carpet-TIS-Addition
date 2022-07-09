package carpettisaddition.mixins.command.lifetime.spawning.mobdrop;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobDropSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC >= 11700
//$$ import carpettisaddition.commands.lifetime.utils.LifetimeMixinUtil;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	public LivingEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	// wither rose thing
	@ModifyArg(
			//#if MC >= 11500
			method = "onKilledBy",
			//#else
			//$$ method = "onDeath",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			)
	)
	private Entity onMobDroppedItemLifeTimeTracker(Entity itemEntity)
	{
		((LifetimeTrackerTarget)itemEntity).recordSpawning(new MobDropSpawningReason(this.getType()));
		return itemEntity;
	}

	//#if MC >= 11700
	//$$ @Inject(method = "dropXp", at = @At("HEAD"))
	//$$ private void onMobDroppedXpLifeTimeTrackerPre(CallbackInfo ci)
	//$$ {
	//$$ 	LifetimeMixinUtil.xpOrbSpawningReason.set(new MobDropSpawningReason(this.getType()));
	//$$ }
	//#else
	@ModifyArg(
			//#if MC >= 11500
			method = "dropXp",
			//#else
			//$$ method = "updatePostDeath",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			),
			index = 0
	)
	private Entity onMobDroppedXpLifeTimeTracker(Entity entity)
	{
		((LifetimeTrackerTarget)entity).recordSpawning(new MobDropSpawningReason(this.getType()));
		return entity;
	}
	//#endif
}
