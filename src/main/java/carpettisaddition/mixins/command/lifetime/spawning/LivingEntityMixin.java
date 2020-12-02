package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.commands.lifetime.spawning.MobDropSpawningReason;
import carpettisaddition.commands.lifetime.utils.ExperienceOrbEntityFlags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	public LivingEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	// wither rose thing
	@Inject(
			method = "onKilledBy",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void onMobDroppedItemLifeTimeTracker(LivingEntity adversary, CallbackInfo ci, boolean bl, ItemEntity itemEntity)
	{
		((IEntity)itemEntity).recordSpawning(new MobDropSpawningReason(this.getType()));
	}

	@Inject(method = "dropXp", at = @At("HEAD"))
	private void onMobDroppedXpLifeTimeTrackerPre(CallbackInfo ci)
	{
		ExperienceOrbEntityFlags.recordSpawning = true;
	}

	@Inject(method = "dropXp", at = @At("TAIL"))
	private void onMobDroppedXpLifeTimeTrackerPost(CallbackInfo ci)
	{
		ExperienceOrbEntityFlags.recordSpawning = false;
	}
}
