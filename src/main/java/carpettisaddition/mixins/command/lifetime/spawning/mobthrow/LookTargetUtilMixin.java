package carpettisaddition.mixins.command.lifetime.spawning.mobthrow;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobThrowSpawningReason;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//#if MC >= 11600
//$$ import net.minecraft.util.math.Vec3d;
//#endif

@Mixin(LookTargetUtil.class)
public abstract class LookTargetUtilMixin
{
	@ModifyVariable(
			method = "give",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			)
	)
	private static ItemEntity onThrowItemEntityLifeTimeTracker(
			ItemEntity itemEntity, LivingEntity entity, ItemStack stack,
			//#if MC >= 11600
			//$$ Vec3d targetLocation
			//#else
			LivingEntity target
			//#endif
	)
	{
		((LifetimeTrackerTarget) itemEntity).recordSpawning(new MobThrowSpawningReason(entity.getType()));
		return itemEntity;
	}
}
