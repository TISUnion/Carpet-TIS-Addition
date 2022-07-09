package carpettisaddition.mixins.translations;

import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC >= 11600
//$$ import net.minecraft.text.TextColor;
//$$ import org.spongepowered.asm.mixin.Mutable;
//#else
import net.minecraft.util.Formatting;
//#endif

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
	//#if MC >= 11600
	//$$ TextColor getColorField();
	//#else
	//#disable-remap
	Formatting getColorField();
	//#enable-remap
	//#endif

	@Accessor("hoverEvent")
	HoverEvent getHoverEventField();

	//#if MC >= 11600
	//$$ @Mutable
	//$$ @Accessor("underlined")
	//$$ void setUnderlinedField(Boolean value);
 //$$
	//$$ @Mutable
	//$$ @Accessor("strikethrough")
	//$$ void setStrikethroughField(Boolean value);
 //$$
	//$$ @Mutable
	//$$ @Accessor("obfuscated")
	//$$ void setObfuscatedField(Boolean value);
	//#endif
}
