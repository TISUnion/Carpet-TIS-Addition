package carpettisaddition.mixins.rule.largeBarrel;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import net.minecraft.state.AbstractState;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractState.class)
public abstract class AbstractStateMixin
{
	@SuppressWarnings("unchecked")
	@Inject(method = "get", at = @At("TAIL"), cancellable = true)
	private <T extends Comparable<T>> void tweaksGetStateResultForLargeBarrel(CallbackInfoReturnable<T> cir)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			if (LargeBarrelHelper.applyAxisOnlyDirectionTesting.get())
			{
				if (cir.getReturnValue() instanceof Direction)
				{
					Direction direction = (Direction) cir.getReturnValue();
					cir.setReturnValue((T)Direction.from(direction.getAxis(), Direction.AxisDirection.NEGATIVE));
				}
			}
		}
	}
}
