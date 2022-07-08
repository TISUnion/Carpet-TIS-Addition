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
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/Entity;checkDespawn()V"
			)
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
