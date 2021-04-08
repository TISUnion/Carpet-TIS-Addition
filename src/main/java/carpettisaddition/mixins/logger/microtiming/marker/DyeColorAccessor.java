package carpettisaddition.mixins.logger.microtiming.marker;

import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DyeColor.class)
public interface DyeColorAccessor
{
	@Accessor("signColor")
	int getTextColor();
}
