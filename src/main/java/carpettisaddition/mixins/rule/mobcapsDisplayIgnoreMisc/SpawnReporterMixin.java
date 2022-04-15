package carpettisaddition.mixins.rule.mobcapsDisplayIgnoreMisc;

import carpet.utils.SpawnReporter;
import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.SpawnGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Arrays;

@Mixin(SpawnReporter.class)
public abstract class SpawnReporterMixin
{
	@Redirect(
			method = "printMobcapsForDimension",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/SpawnGroup;values()[Lnet/minecraft/entity/SpawnGroup;"
			),
			require = 0
	)
	private static SpawnGroup[] mobcapsDisplayIgnoreMisc()
	{
		SpawnGroup[] values = SpawnGroup.values();
		if (CarpetTISAdditionSettings.mobcapsDisplayIgnoreMisc)
		{
			values = Arrays.stream(values).
					filter(entityCategory -> entityCategory != SpawnGroup.MISC).
					toArray(SpawnGroup[]::new);
		}
		return values;
	}
}
