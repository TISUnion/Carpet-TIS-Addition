package carpettisaddition.mixins.translations;

import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Style.class)
public interface StyleAccessor
{
	@Accessor("bold")
	Boolean getBoldField();

	@Accessor("italic")
	Boolean getItalicField();

	@Accessor("underlined")
	Boolean getUnderlineField();

	@Mutable
	@Accessor("underlined")
	void setUnderlinedField(Boolean value);

	@Accessor("strikethrough")
	Boolean getStrikethroughField();

	@Mutable
	@Accessor("strikethrough")
	void setStrikethroughField(Boolean value);

	@Accessor("obfuscated")
	Boolean getObfuscatedField();

	@Mutable
	@Accessor("obfuscated")
	void setObfuscatedField(Boolean value);

	@Accessor("color")
	TextColor getColorField();

	@Accessor("hoverEvent")
	HoverEvent getHoverEventField();
}
