package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin
{
	@Inject(
			method = "extract(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/entity/ItemEntity;)Z",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ItemEntity;remove()V"
			)
	)
	private static void onHopperCollectedItemLifeTimeTracker(Inventory inventory, ItemEntity itemEntity, CallbackInfoReturnable<Boolean> cir)
	{
		if (!itemEntity.removed)
		{
			((LifetimeTrackerTarget)itemEntity).recordRemoval(LiteralRemovalReason.HOPPER);
		}
	}
}
