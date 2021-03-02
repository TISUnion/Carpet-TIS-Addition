package carpettisaddition.mixins.rule.turtleEggTrampledDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.ai.goal.StepAndDestroyBlockGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StepAndDestroyBlockGoal.class)
public abstract class StepAndDestroyBlockGoalMixin
{
	@Shadow private int counter;

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/entity/ai/goal/StepAndDestroyBlockGoal;tickStepping(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;)V"
					)
			),
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/entity/ai/goal/StepAndDestroyBlockGoal;counter:I"
			)
	)
	private void dontBreakTheEgg(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.turtleEggTrampledDisabled)
		{
			if (this.counter > 60)
			{
				this.counter -= 30;
			}
		}
	}
}
