package carpettisaddition.mixins.command.lifetime.spawning.mobthrow;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobThrowSpawningReason;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.DolphinEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net.minecraft.entity.passive.DolphinEntity$PlayWithItemsGoal")
public abstract class DolphinEntityPlayWithItemsGoalMixin
{
	@SuppressWarnings("target")
	@Shadow @Final
	DolphinEntity field_6757;

	@ModifyVariable(
			method = "spitOutItem",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			)
	)
	private ItemEntity onDolphinThrowItemEntityLifeTimeTracker(ItemEntity itemEntity)
	{
		((LifetimeTrackerTarget)itemEntity).recordSpawning(new MobThrowSpawningReason(this.field_6757.getType()));
		return itemEntity;
	}
}
