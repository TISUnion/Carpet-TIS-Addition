package carpettisaddition.mixins.rule.farmlandTrampledDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FarmlandBlock.class)
public abstract class FarmLandBlockMixin
{
	@Redirect(
			method = "onLandedUpon",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/World;isClient:Z"
			)
	)
	private boolean farmlandTrampledDisabled(World world)
	{
		if (CarpetTISAdditionSettings.farmlandTrampledDisabled)
		{
			// world.isClient returning true -> if predicate fails -> farmland protected
			return true;
		}
		return world.isClient;
	}
}
