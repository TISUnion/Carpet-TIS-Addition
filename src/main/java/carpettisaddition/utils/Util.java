package carpettisaddition.utils;

import carpet.utils.Messenger;
import carpet.utils.Translations;
import carpettisaddition.CarpetTISAdditionServer;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Map;


public class Util
{
	private static final String teleportHint = Translations.tr("util.teleport_hint", "Click to teleport to");
	private static final Map<DimensionType, BaseText> DIMENSION_NAME = Maps.newHashMap();
	static
	{
		DIMENSION_NAME.put(DimensionType.OVERWORLD, new TranslatableText("createWorld.customize.preset.overworld"));
		DIMENSION_NAME.put(DimensionType.THE_NETHER, new TranslatableText("advancements.nether.root.title"));
		DIMENSION_NAME.put(DimensionType.THE_END, new TranslatableText("advancements.end.root.title"));
	}

	public static String getTeleportCommand(Vec3d pos, RegistryKey<World> dim)
	{
		return String.format("/execute in %s run tp %f %f %f", dim.getValue(), pos.getX(), pos.getY(), pos.getZ());
	}
	public static String getTeleportCommand(Vec3i pos, RegistryKey<World> dim)
	{
		return String.format("/execute in %s run tp %d %d %d", dim.getValue(), pos.getX(), pos.getY(), pos.getZ());
	}
	public static String getTeleportCommand(Entity entity)
	{
		String uuid = entity.getUuid().toString();
		return String.format("/execute at %1$s run tp %1$s", uuid);
	}

	public static BaseText getFancyText(String style, BaseText displayText, BaseText hoverText, ClickEvent clickEvent)
	{
		BaseText text = (BaseText)displayText.copy();
		if (style != null)
		{
			text.setStyle(Messenger.parseStyle(style));
		}
		text.setStyle(text.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)));
		if (clickEvent != null)
		{
			text.setStyle(text.getStyle().withClickEvent(clickEvent));
		}
		return text;
	}
	private static BaseText __getCoordinateText(String style, RegistryKey<World> dim, String posText, String command)
	{
		BaseText hoverText = Messenger.s("");
		hoverText.append(String.format("%s %s\n", teleportHint, posText));
		hoverText.append(Translations.tr("util.teleport_hint_dimension", "Dimension: "));
		hoverText.append(getDimensionNameText(dim));
		return getFancyText(style, Messenger.s(posText), hoverText, new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
	}
	public static BaseText getCoordinateText(String style, Vec3d pos, RegistryKey<World> dim)
	{
		String posText = String.format("[%.1f, %.1f, %.1f]", pos.getX(), pos.getY(), pos.getZ());
		return __getCoordinateText(style, dim, posText, getTeleportCommand(pos, dim));
	}
	public static BaseText getCoordinateText(String style, Vec3i pos, RegistryKey<World> dim)
	{
		String posText = String.format("[%d, %d, %d]", pos.getX(), pos.getY(), pos.getZ());
		return __getCoordinateText(style, dim, posText, getTeleportCommand(pos, dim));
	}
	public static BaseText getEntityText(String style, Entity entity)
	{
		BaseText entityName = (BaseText)entity.getType().getName().copy();
		BaseText hoverText = Messenger.c(String.format("w %s ", teleportHint), entityName);
		return getFancyText(style, entityName, hoverText, new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, getTeleportCommand(entity)));
	}

	public static BaseText getDimensionNameText(RegistryKey<World> dim)
	{
		return (BaseText)DIMENSION_NAME.getOrDefault(dim, Messenger.s(dim.toString())).deepCopy();
	}

	public static TranslatableText getTranslatedName(String key, Formatting color, Object... args)
	{
		TranslatableText text = new TranslatableText(key, args);
		if (color != null)
		{
			text.setStyle(text.getStyle().withColor(color));
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

	// some language doesn't use space char to divide word
	// so here comes the compatibility
	public static String getSpace()
	{
		return Translations.tr("language_tool.space", " ");
	}
	public static BaseText getSpaceText()
	{
		return Messenger.s(getSpace());
	}
}
