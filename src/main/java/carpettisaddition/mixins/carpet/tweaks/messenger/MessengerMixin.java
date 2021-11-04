package carpettisaddition.mixins.carpet.tweaks.messenger;

import carpet.utils.Messenger;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings({"ConstantConditions", "DefaultAnnotationParam"})
@Mixin(Messenger.class)
public abstract class MessengerMixin
{

	@Redirect(
			method = "parseStyle",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/text/Style;setItalic(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
					remap = true
			),
			remap = false
	)
	private static Style doIfCheckPlease_setItalic(Style style, Boolean value)
	{
		return value ? style.setItalic(value) : style;
	}

	@Redirect(
			method = "parseStyle",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/text/Style;setStrikethrough(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
					remap = true
			),
			remap = false
	)
	private static Style doIfCheckPlease_setStrikethrough(Style style, Boolean value)
	{
		return value ? style.setStrikethrough(value) : style;
	}

	@Redirect(
			method = "parseStyle",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/text/Style;setUnderline(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
					remap = true
			),
			remap = false
	)
	private static Style doIfCheckPlease_setUnderline(Style style, Boolean value)
	{
		return value ? style.setUnderline(value) : style;
	}

	@Redirect(
			method = "parseStyle",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/text/Style;setBold(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
					remap = true
			),
			remap = false
	)
	private static Style doIfCheckPlease_setBold(Style style, Boolean value)
	{
		return value ? style.setBold(value) : style;
	}

	@Redirect(
			method = "parseStyle",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/text/Style;setObfuscated(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
					remap = true
			),
			remap = false
	)
	private static Style doIfCheckPlease_setObfuscated(Style style, Boolean value)
	{
		return value ? style.setObfuscated(value) : style;
	}

//	@Redirect(
//			method = "parseStyle",
//			at = @At(
//					value = "INVOKE",
//					target = "Lnet/minecraft/text/Style;setColor(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;",
//					remap = true
//			),
//			remap = false
//	)
//	private static Style noSettingColor(Style style, Formatting color)
//	{
//		return style;
//	}
}
