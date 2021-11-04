package carpettisaddition.mixins.translations;

import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Style.class)
public interface StyleAccessor
{
	@Accessor("bold")
	Boolean getBoldField();

	@Accessor("italic")
	Boolean getItalicField();

	@Accessor("underline")
	Boolean getUnderlineField();

	@Accessor("strikethrough")
	Boolean getStrikethroughField();

	@Accessor("obfuscated")
	Boolean getObfuscatedField();

	@Accessor("color")
	Formatting getColorField();

	@Accessor("hoverEvent")
	HoverEvent getHoverEventField();
}
