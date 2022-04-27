package carpettisaddition.mixins.command.lifetime.removal;

import net.minecraft.entity.InventoryOwner;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InventoryOwner.class)
public interface InventoryOwnerMixin
{
	// OH NO, Mixin cannot inject into interface default method
	// TODO: GO FIX THIS InventoryOwner picking up item entity as item's removal reason
//	@Inject(
//			method = "method_43544",
//			at = @At(
//					value = "INVOKE",
//					target = "Lnet/minecraft/entity/ItemEntity;discard()V"
//			)
//	)
//	static void onInventoryOwnerPickUpItemLifeTimeTracker(MobEntity mobEntity, InventoryOwner inventoryOwner, ItemEntity itemEntity, CallbackInfo ci)
//	{
//		((LifetimeTrackerTarget)itemEntity).recordRemoval(new MobPickupRemovalReason(mobEntity.getType()));
//	}
}
