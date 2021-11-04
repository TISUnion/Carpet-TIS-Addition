package carpettisaddition.mixins.translations;

import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Style.class)
public interface StyleAccessor
{
	@Accessor("hoverEvent")
	HoverEvent getHoverEventField();
}
