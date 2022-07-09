package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11900
//$$ import net.minecraft.entity.InventoryOwner;
//#else
import carpettisaddition.utils.compat.DummyClass;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
@Mixin(
		//#if MC >= 11900
		//$$ InventoryOwner.class
		//#else
		DummyClass.class
		//#endif
)
public interface InventoryOwnerMixin
{
	// OH NO, Mixin cannot inject into interface default method
	// TODO: GO FIX THIS InventoryOwner picking up item entity as item's removal reason
//	@Inject(
//			method = "pickUpItem",
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