package carpettisaddition.mixins.rule.minecartTakePassengerMinVelocity;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin
{
	@ModifyConstant(
			method = "tick",
			// cannot use our 0.1D constant value here, since 0.1D * 0.1D != 0.01D
			constant = @Constant(doubleValue = 0.01D),
			require = 0
	)
	private double minecartTakePassengerMinVelocity(double squaredThreshold)
	{
		if (CarpetTISAdditionSettings.minecartTakePassengerMinVelocity != CarpetTISAdditionSettings.VANILLA_MINECART_TAKE_PASSENGER_MIN_VELOCITY)
		{
			// since the operator is <, but what we want is <=, here comes a Math.nextDown trick
			squaredThreshold = Math.nextDown(CarpetTISAdditionSettings.minecartTakePassengerMinVelocity);
		}
		return squaredThreshold;
	}
}
