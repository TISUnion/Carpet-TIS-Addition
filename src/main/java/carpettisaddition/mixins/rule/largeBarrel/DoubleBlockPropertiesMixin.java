package carpettisaddition.mixins.rule.largeBarrel;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DoubleBlockProperties.class)
public abstract class DoubleBlockPropertiesMixin
{
	@Inject(
			method = "toPropertySource",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;"
			)
	)
	private static <S extends BlockEntity> void tweaksGetStateResultForLargeBarrelPre(CallbackInfoReturnable<DoubleBlockProperties.PropertySource<S>> cir)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			if (LargeBarrelHelper.gettingLargeBarrelPropertySource.get())
			{
				LargeBarrelHelper.applyAxisOnlyDirectionTesting.set(true);
			}
		}
	}

	@Inject(
			method = "toPropertySource",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;",
					shift = At.Shift.AFTER
			)
	)
	private static <S extends BlockEntity> void tweaksGetStateResultForLargeBarrelPost(CallbackInfoReturnable<DoubleBlockProperties.PropertySource<S>> cir)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			if (LargeBarrelHelper.gettingLargeBarrelPropertySource.get())
			{
				LargeBarrelHelper.applyAxisOnlyDirectionTesting.set(false);
			}
		}
	}
}
