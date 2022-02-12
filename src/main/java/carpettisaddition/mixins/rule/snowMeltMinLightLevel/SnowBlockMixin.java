package carpettisaddition.mixins.rule.snowMeltMinLightLevel;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.SnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SnowBlock.class)
public abstract class SnowBlockMixin
{
	@ModifyConstant(
			method = "randomTick",
			constant = @Constant(intValue = CarpetTISAdditionSettings.VANILLA_SNOW_MELT_MIN_LIGHT_LEVEL - 1),
			require = 0
	)
	private int snowMeltMinLightLevel(int lightLevel)
	{
		if (CarpetTISAdditionSettings.snowMeltMinLightLevel != CarpetTISAdditionSettings.VANILLA_SNOW_MELT_MIN_LIGHT_LEVEL)
		{
			return CarpetTISAdditionSettings.snowMeltMinLightLevel - 1;
		}
		return lightLevel;
	}
}
