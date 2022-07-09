package carpettisaddition.mixins.rule.keepMobInLazyChunks;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	//#if MC >= 11500
	@Redirect(
			//#if MC >= 11700
			//$$ method = "method_31420",  // lambda method in method ServerWorld#tick
			//$$ at = @At(
			//$$ 		value = "INVOKE",
			//$$ 		target = "Lnet/minecraft/entity/Entity;checkDespawn()V",
			//$$ 		remap = true
			//$$ ),
			//$$ remap = false
			//#else
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/Entity;checkDespawn()V"
			)
			//#endif
	)
	private void keepMobInLazyChunks_optionalCheckDespawn(Entity entity)
	{
		if (!CarpetTISAdditionSettings.keepMobInLazyChunks)
		{
			entity.checkDespawn();
		}
	}
	//#endif
}
