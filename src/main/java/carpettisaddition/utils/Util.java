package carpettisaddition.utils;

import carpet.utils.Messenger;
import carpet.utils.Translations;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;


public class Util
{
	public static BaseText getCoordinateText(String style, Vec3d pos, RegistryKey<DimensionType> dim)
	{
		String posText = String.format("[%.1f, %.1f, %.1f]", pos.getX(), pos.getY(), pos.getZ());
		String command = String.format("/execute in %s run tp %f %f %f", dim.toString(), pos.getX(), pos.getY(), pos.getZ());
		LiteralText hoverText = new LiteralText("");
		hoverText.append(String.format(Translations.tr("util.teleportHint", "Click to teleport to %s"), posText));
		hoverText.append("\n");
		hoverText.append(String.format(Translations.tr("util.teleportHintDimension", "Dimension: "), posText));
		hoverText.append(getDimensionNameText(dim));
		LiteralText text = new LiteralText(posText);
		text.setStyle(Messenger.parseStyle(style));
		text.setStyle(text.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
		text.setStyle(text.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)));
		return text;
	}

	public static BaseText getDimensionNameText(RegistryKey<DimensionType> dim)
	{
		if (dim == DimensionType.OVERWORLD_REGISTRY_KEY)
		{
			return getTranslatedName("createWorld.customize.preset.overworld");
		}
		else if (dim == DimensionType.THE_NETHER_REGISTRY_KEY)
		{
			return getTranslatedName("advancements.nether.root.title");
		}
		else if (dim == DimensionType.THE_END_REGISTRY_KEY)
		{
			return getTranslatedName("advancements.end.root.title");
		}
		return getTranslatedName(dim.getValue().getPath());
	}

	public static TranslatableText getTranslatedName(String key, Formatting color, Object... args)
	{
		TranslatableText text = new TranslatableText(key, args);
		if (color != null)
		{
			text.getStyle().withColor(color);
		}
		return text;
	}
	public static TranslatableText getTranslatedName(String key, Object... args)
	{
		return getTranslatedName(key, null, args);
	}
}
