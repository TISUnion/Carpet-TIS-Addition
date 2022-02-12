package carpettisaddition.mixins.rule.explosionPacketRange;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static carpettisaddition.CarpetTISAdditionSettings.VANILLA_EXPLOSION_PACKET_RANGE;

@Mixin(value = ServerWorld.class, priority = 500)
public abstract class ServerWorldMixin
{
	@ModifyConstant(
			method = "createExplosion",
			constant = @Constant(doubleValue = VANILLA_EXPLOSION_PACKET_RANGE * VANILLA_EXPLOSION_PACKET_RANGE),
			require = 0
	)
	private double modifyExplosionPacketRange(double value)
	{
		double ruleValue = CarpetTISAdditionSettings.explosionPacketRange;
		return ruleValue != VANILLA_EXPLOSION_PACKET_RANGE ? ruleValue * ruleValue : value;
	}
}
