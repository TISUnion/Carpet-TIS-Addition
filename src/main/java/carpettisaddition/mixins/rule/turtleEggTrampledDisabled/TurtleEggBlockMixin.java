package carpettisaddition.mixins.rule.turtleEggTrampledDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.TurtleEggBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TurtleEggBlock.class, priority = 2000)
public abstract class TurtleEggBlockMixin
{
	@Inject(
			method = "tryBreakEgg",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/TurtleEggBlock;breakEgg(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"
			),
			cancellable = true
	)
	private void dontBreakTheEgg(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.turtleEggTrampledDisabled)
		{
			ci.cancel();
		}
	}
}
