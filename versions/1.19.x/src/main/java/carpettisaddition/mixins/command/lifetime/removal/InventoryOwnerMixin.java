package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.MobPickupRemovalReason;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
@Mixin(InventoryOwner.class)
public interface InventoryOwnerMixin
{
	// private static requires java9+
	@Inject(
			method = "pickUpItem",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ItemEntity;discard()V"
			)
	)
	private static void onInventoryOwnerPickUpItemLifeTimeTracker(MobEntity mobEntity, InventoryOwner inventoryOwner, ItemEntity itemEntity, CallbackInfo ci)
	{
		((LifetimeTrackerTarget)itemEntity).recordRemoval(new MobPickupRemovalReason(mobEntity.getType()));
	}
}