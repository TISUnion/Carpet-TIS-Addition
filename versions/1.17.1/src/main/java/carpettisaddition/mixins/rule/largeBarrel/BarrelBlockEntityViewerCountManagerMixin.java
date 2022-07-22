package carpettisaddition.mixins.rule.largeBarrel;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"))
@Mixin(targets = "net.minecraft.block.entity.BarrelBlockEntity$1")
public abstract class BarrelBlockEntityViewerCountManagerMixin
{
	@Shadow @Final BarrelBlockEntity field_27208;  // BarrelBlockEntity.this

	@Inject(
			method = "isPlayerViewing(Lnet/minecraft/entity/player/PlayerEntity;)Z",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/screen/GenericContainerScreenHandler;getInventory()Lnet/minecraft/inventory/Inventory;"
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true
	)
	private void correctLargeBarrelLogic(PlayerEntity player, CallbackInfoReturnable<Boolean> cir, Inventory inventory)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			//  reference: the lambda ViewerCountManager subclass inside ChestBlockEntity
			boolean openingLargeBarrel = inventory instanceof DoubleInventory && ((DoubleInventory)inventory).isPart(this.field_27208);
			if (openingLargeBarrel)
			{
				cir.setReturnValue(true);
			}
		}
	}
}