package carpettisaddition.mixins.rule.tileTickLimit;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	@ModifyConstant(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=tickPending"
					),
					to = @At(
							value = "CONSTANT",
							args = "stringValue=raid"
					)
			),
			constant = @Constant(intValue = 65536),
			require = 2
	)
	private static int modifyTileTickLimit(int value)
	{
		return CarpetTISAdditionSettings.tileTickLimit;
	}
}
