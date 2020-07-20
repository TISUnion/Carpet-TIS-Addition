package carpettisaddition.utils;

import carpet.utils.Messenger;
import carpet.utils.Translations;
import carpettisaddition.CarpetTISAdditionServer;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.Objects;


public class Util
{
	public static String getTeleportCommand(Vec3d pos, RegistryKey<DimensionType> dim)
	{
		return String.format("/execute in %s run tp %f %f %f", dim.getValue(), pos.getX(), pos.getY(), pos.getZ());
	}
	public static String getTeleportCommand(Vec3i pos, RegistryKey<DimensionType> dim)
	{
		return String.format("/execute in %s run tp %d %d %d", dim.getValue(), pos.getX(), pos.getY(), pos.getZ());
	}

	private static BaseText __getCoordinateText(String style, RegistryKey<DimensionType> dim, String posText, String command)
	{
		LiteralText hoverText = new LiteralText("");
		hoverText.append(String.format(Translations.tr("util.teleport_hint", "Click to teleport to %s"), posText));
		hoverText.append("\n");
		hoverText.append(String.format(Translations.tr("util.teleportHintDimension", "Dimension: "), posText));
		hoverText.append(getDimensionNameText(dim));
		LiteralText text = new LiteralText(posText);
		text.setStyle(Messenger.parseStyle(style));
		text.setStyle(text.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
		text.setStyle(text.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)));
		return text;
	}
	public static BaseText getCoordinateText(String style, Vec3d pos, RegistryKey<DimensionType> dim)
	{
		String posText = String.format("[%.1f, %.1f, %.1f]", pos.getX(), pos.getY(), pos.getZ());
		return __getCoordinateText(style, dim, posText, getTeleportCommand(pos, dim));
	}
	public static BaseText getCoordinateText(String style, Vec3i pos, RegistryKey<DimensionType> dim)
	{
		String posText = String.format("[%d, %d, %d]", pos.getX(), pos.getY(), pos.getZ());
		return __getCoordinateText(style, dim, posText, getTeleportCommand(pos, dim));
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

	public static long getGameTime()
	{
		return Objects.requireNonNull(CarpetTISAdditionServer.minecraft_server.getWorld(World.OVERWORLD)).getTime();
	}

	public static String ratePerHour(int rate, long ticks)
	{
		return String.format("%d, (%.1f/h)", rate, (double)rate / ticks * (20 * 60 * 60));
	}
}
