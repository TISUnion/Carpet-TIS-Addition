package carpettisaddition.utils;

import carpet.utils.Messenger;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;


public class Util
{
	/*
	public static BaseText getCoordinateText(String style, Vec3d pos, Dimension dim)
	{
		String posText = String.format("[%.1f, %.1f, %.1f]", pos.getX(), pos.getY(), pos.getZ());
		String command = String.format("/execute in %s run tp %f %f %f", dim.getType().toString(), pos.getX(), pos.getY(), pos.getZ());
		LiteralText hoverText = new LiteralText("");
		hoverText.append(String.format(Translations.tr("util.teleportHint", "Click to teleport to %s"), posText));
		hoverText.append("\n");
		hoverText.append(String.format(Translations.tr("util.teleportHintDimension", "Dimension: "), posText));
		hoverText.append(getDimensionNameText(dim.getType()));
		LiteralText text = new LiteralText(posText);
		text.setStyle(Messenger.parseStyle(style));
		text.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
		text.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
		return text;
	}

	public static BaseText getDimensionNameText(DimensionType dim)
	{
		if (dim == DimensionType.OVERWORLD)
		{
			return getTranslatedName("createWorld.customize.preset.overworld");
		}
		else if (dim == DimensionType.THE_NETHER)
		{
			return getTranslatedName("advancements.nether.root.title");
		}
		else if (dim == DimensionType.THE_END)
		{
			return getTranslatedName("advancements.end.root.title");
		};
		return null;
	}

	public static TranslatableText getTranslatedName(String key, Formatting color, Object... args)
	{
		TranslatableText text = new TranslatableText(key, args);
		if (color != null)
		{
			text.getStyle().setColor(color);
		}
		return text;
	}
	public static TranslatableText getTranslatedName(String key, Object... args)
	{
		return getTranslatedName(key, null, args);
	}
	 */
}
