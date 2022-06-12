package carpettisaddition.mixins.carpet.tweaks.rule.creativeNoClip;

import carpettisaddition.helpers.carpet.tweaks.rule.creativeNoClip.CreativeNoClipHelper;
import carpettisaddition.utils.compat.CarpetSettings;
import net.minecraft.block.TripwireBlock;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(TripwireBlock.class)
public abstract class TripwireBlockMixin
{
	@ModifyVariable(
			method = "updatePowered",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;isEmpty()Z"
			)
	)
	private List<? extends Entity> dontDetectCreativeNoClipPlayers(List<? extends Entity> entities)
	{
		if (CarpetSettings.creativeNoClip)
		{
			entities.removeIf(CreativeNoClipHelper::isNoClipPlayer);
		}
		return entities;
	}
}
