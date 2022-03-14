package carpettisaddition.mixins.command.lifetime.spawning.mobdrop;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobDropSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemUsage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemUsage.class)
public abstract class ItemUsageMixin
{
	/**
	 * Currently only used in onItemEntityDestroyed for bundle item and shulker box item
	 */
	@ModifyArg(
			method = "method_33265",  // lambda method in spawnItemContents
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z",
					remap = true
			),
			remap = false
	)
	private static Entity recordItemEntitySpawnedByItemEntityDestroyedLifeTimeTracker(Entity itemEntity)
	{
		((LifetimeTrackerTarget)itemEntity).recordSpawning(new MobDropSpawningReason(EntityType.ITEM));
		return itemEntity;
	}
}
