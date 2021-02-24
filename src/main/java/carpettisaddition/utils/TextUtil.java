package carpettisaddition.utils;

import carpet.utils.Messenger;
import carpet.utils.Translations;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Objects;

public class TextUtil
{
	// mojang compatibility thing <3
	// these get changed in 1.16 so for easier compatible coding just wrap these methods
	public static BaseText attachHoverEvent(BaseText text, HoverEvent hoverEvent)
	{
		text.setStyle(text.getStyle().withHoverEvent(hoverEvent));
		return text;
	}

	public static BaseText attachHoverText(BaseText text, BaseText hoverText)
	{
		return attachHoverEvent(text, new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
	}

	public static BaseText attachClickEvent(BaseText text, ClickEvent clickEvent)
	{
		text.setStyle(text.getStyle().withClickEvent(clickEvent));
		return text;
	}

	public static BaseText attachFormatting(BaseText text, Formatting... formattings)
	{
		text.formatted(formattings);
		return text;
	}

	public static BaseText copyText(BaseText text)
	{
		return (BaseText)text.shallowCopy();
	}
	// mojang compatibility thing ends

	private static final Map<RegistryKey<World>, BaseText> DIMENSION_NAME = Maps.newHashMap();

	static
	{
		DIMENSION_NAME.put(World.OVERWORLD, new TranslatableText("createWorld.customize.preset.overworld"));
		DIMENSION_NAME.put(World.NETHER, new TranslatableText("advancements.nether.root.title"));
		DIMENSION_NAME.put(World.END, new TranslatableText("advancements.end.root.title"));
	}

	private static String getTeleportHint()
	{
		return Translations.tr("util.teleport_hint", "Click to teleport to");
	}

	public static String getTeleportCommand(Vec3d pos, RegistryKey<World> dim)
	{
		return String.format("/execute in %s run tp %s %s %s", dim.getValue(), pos.getX(), pos.getY(), pos.getZ());
	}

	public static String getTeleportCommand(Vec3d pos)
	{
		return String.format("/tp %s %s %s", pos.getX(), pos.getY(), pos.getZ());
	}

	public static String getTeleportCommand(Vec3i pos)
	{
		return String.format("/tp %d %d %d", pos.getX(), pos.getY(), pos.getZ());
	}

	public static String getTeleportCommand(Vec3i pos, RegistryKey<World> dim)
	{
		return String.format("/execute in %s run tp %d %d %d", dim.getValue(), pos.getX(), pos.getY(), pos.getZ());
	}

	public static String getTeleportCommandPlayer(PlayerEntity player)
	{
		String name = player.getGameProfile().getName();
		return String.format("/tp %s", name);
	}

	public static String getTeleportCommand(Entity entity)
	{
		if (entity instanceof PlayerEntity)
		{
			return getTeleportCommandPlayer((PlayerEntity)entity);
		}
		String uuid = entity.getUuid().toString();
		return String.format("/tp %s", uuid);
	}

	public static BaseText getFancyText(String style, BaseText displayText, BaseText hoverText, ClickEvent clickEvent)
	{
		BaseText text = copyText(displayText);
		if (style != null)
		{
			text.setStyle(Messenger.parseStyle(style));
		}
		if (hoverText != null)
		{
			attachHoverText(text, hoverText);
		}
		if (clickEvent != null)
		{
			attachClickEvent(text, clickEvent);
		}
		return text;
	}

	private static BaseText __getCoordinateText(String style, RegistryKey<World> dim, String posText, String command)
	{
		BaseText hoverText = Messenger.s("");
		hoverText.append(String.format("%s %s\n", getTeleportHint(), posText));
		hoverText.append(Translations.tr("util.teleport_hint.dimension", "Dimension"));
		hoverText.append(": ");
		hoverText.append(getDimensionNameText(dim));
		return getFancyText(style, Messenger.s(posText), hoverText, new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
	}

	public static String getCoordinateString(Vec3d pos)
	{
		return String.format("[%.1f, %.1f, %.1f]", pos.getX(), pos.getY(), pos.getZ());
	}

	public static String getCoordinateString(Vec3i pos)
	{
		return String.format("[%d, %d, %d]", pos.getX(), pos.getY(), pos.getZ());
	}

	public static BaseText getCoordinateText(String style, Vec3d pos, RegistryKey<World> dim)
	{
		return __getCoordinateText(style, dim, getCoordinateString(pos), getTeleportCommand(pos, dim));
	}

	public static BaseText getCoordinateText(String style, Vec3i pos, RegistryKey<World> dim)
	{
		return __getCoordinateText(style, dim, getCoordinateString(pos), getTeleportCommand(pos, dim));
	}

	public static BaseText getEntityText(String style, Entity entity)
	{
		BaseText entityName = copyText((BaseText)entity.getType().getName());
		BaseText hoverText = Messenger.c("w " + getTeleportHint(), getSpaceText(), entityName);
		return getFancyText(style, entityName, hoverText, new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, getTeleportCommand(entity)));
	}

	public static BaseText getAttributeText(EntityAttribute attribute)
	{
		return new TranslatableText(attribute.getTranslationKey());
	}

	public static BaseText getDimensionNameText(RegistryKey<World> dim)
	{
		return copyText(DIMENSION_NAME.getOrDefault(dim, Messenger.s(dim.toString())));
	}

	public static String dimensionToString(DimensionType dimensionType)
	{
		return Objects.requireNonNull(DimensionType.getId(dimensionType)).getPath();
	}

	public static BaseText getColoredDimensionSymbol(DimensionType dimensionType)
	{
		if (dimensionType == DimensionType.OVERWORLD)
		{
			return Messenger.s("O", "e");  // DARK_GREEN
		}
		if (dimensionType == DimensionType.THE_NETHER)
		{
			return Messenger.s("N", "n");  // DARK_RED
		}
		if (dimensionType == DimensionType.THE_END)
		{
			return Messenger.s("E", "p");  // DARK_PURPLE
		}
		return Messenger.s(dimensionToString(dimensionType).toUpperCase().substring(0, 1));
	}

	public static TranslatableText getTranslatedName(String key, Formatting color, Object... args)
	{
		TranslatableText text = new TranslatableText(key, args);
		if (color != null)
		{
			attachFormatting(text, color);
		}
		return text;
	}

	public static TranslatableText getTranslatedName(String key, Object... args)
	{
		return getTranslatedName(key, null, args);
	}

	public static BaseText getBlockName(Block block)
	{
		return TextUtil.attachFormatting(new TranslatableText(block.getTranslationKey()), Formatting.WHITE);
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
