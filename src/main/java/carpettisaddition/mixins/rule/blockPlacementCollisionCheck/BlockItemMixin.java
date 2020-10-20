package carpettisaddition.mixins.rule.blockPlacementIgnoreEntity;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin
{
	@Shadow protected abstract boolean checkStatePlacement();

	@Inject(method = "canPlace", at = @At(value = "HEAD"), cancellable = true)
	void skipCollisionCheck(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetTISAdditionSettings.blockPlacementIgnoreEntity)
		{
			PlayerEntity player = context.getPlayer();
			if (player != null && player.isCreative())
			{
				// partially vanilla copy
				cir.setReturnValue(!this.checkStatePlacement() || state.canPlaceAt(context.getWorld(), context.getBlockPos()));
				cir.cancel();
			}
		}
	}
}
