package carpettisaddition.mixins.rule.tileTickLimit;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.world.ServerTickScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = ServerTickScheduler.class, priority = 998)
public abstract class ServerTickSchedulerMixin<T>
{
	@ModifyConstant(
			method = "tick",
			constant = @Constant(intValue = 65536),
			require = 2
	)
	private static int modifyTileTickLimit(int value)
	{
		return CarpetTISAdditionSettings.tileTickLimit;
	}
}
