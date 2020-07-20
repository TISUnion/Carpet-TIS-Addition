package carpettisaddition.utils;

import carpet.utils.Messenger;
import carpet.utils.Translations;
import carpettisaddition.CarpetTISAdditionServer;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;


public class Util
{
	public static String getTeleportCommand(Vec3d pos, Dimension dim)
	{
		return String.format("/execute in %s run tp %f %f %f", dim.getType().toString(), pos.getX(), pos.getY(), pos.getZ());
	}
	public static String getTeleportCommand(Vec3i pos, Dimension dim)
	{
		return String.format("/execute in %s run tp %d %d %d", dim.getType().toString(), pos.getX(), pos.getY(), pos.getZ());
	}

	private static BaseText __getCoordinateText(String style, Dimension dim, String posText, String command)
	{
		LiteralText hoverText = new LiteralText("");
		hoverText.append(String.format(Translations.tr("util.teleport_hint", "Click to teleport to %s"), posText));
		hoverText.append("\n");
		hoverText.append(String.format(Translations.tr("util.teleport_hint_dimension", "Dimension: %s"), posText));
		hoverText.append(getDimensionNameText(dim.getType()));
		LiteralText text = new LiteralText(posText);
		text.setStyle(Messenger.parseStyle(style));
		text.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		text.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
		return text;
	}
	public static BaseText getCoordinateText(String style, Vec3d pos, Dimension dim)
	{
		String posText = String.format("[%.1f, %.1f, %.1f]", pos.getX(), pos.getY(), pos.getZ());
		return __getCoordinateText(style, dim, posText, getTeleportCommand(pos, dim));
	}
	public static BaseText getCoordinateText(String style, Vec3i pos, Dimension dim)
	{
		String posText = String.format("[%d, %d, %d]", pos.getX(), pos.getY(), pos.getZ());
		return __getCoordinateText(style, dim, posText, getTeleportCommand(pos, dim));
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
		}
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

	public static long getGameTime()
	{
		return CarpetTISAdditionServer.minecraft_server.getWorld(DimensionType.OVERWORLD).getTime();
	}

	public static String ratePerHour(int rate, long ticks)
	{
		return String.format("%d, (%.1f/h)", rate, (double)rate / ticks * (20 * 60 * 60));
	}
}
