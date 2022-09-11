package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionException;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.crash.CrashException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
{
	@Inject(
			//#if MC >= 11500
			method = "playerTick",
			//#else
			//$$ method = "method_14226",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;"
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true
	)
	private void yeetUpdateSuppressionCrash_implForPlayerEntityTicking(CallbackInfo ci, Throwable throwable)
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash)
		{
			UpdateSuppressionException updateSuppressionException = null;
			if (throwable instanceof CrashException && throwable.getCause() instanceof UpdateSuppressionException)
			{
				updateSuppressionException = (UpdateSuppressionException)throwable.getCause();
			}
			if (throwable instanceof UpdateSuppressionException)
			{
				updateSuppressionException = (UpdateSuppressionException)throwable;
			}
			if (updateSuppressionException != null)
			{
				UpdateSuppressionException.report(updateSuppressionException);
				ci.cancel();
			}
		}
	}
}
