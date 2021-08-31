package carpettisaddition.utils;

import carpet.utils.Messenger;
import carpettisaddition.translations.Translator;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class TextUtil
{
	private static final Translator translator = new Translator("util");

	private static final Translator miscTranslator = new Translator("misc");

	public static Translator getMiscTranslator()
	{
		return miscTranslator;
	}

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

	private static final ImmutableMap<RegistryKey<World>, BaseText> DIMENSION_NAME = ImmutableMap.of(
			World.OVERWORLD, new TranslatableText("createWorld.customize.preset.overworld"),
			World.NETHER, new TranslatableText("advancements.nether.root.title"),
			World.END, new TranslatableText("advancements.end.root.title")
	);

	private static String getTeleportHint()
	{
		return translator.tr("teleport_hint", "Click to teleport to");
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

	public static Style parseCarpetStyle(String style)
	{
		return Messenger.parseStyle(style);
	}

	public static BaseText getFancyText(String style, BaseText displayText, BaseText hoverText, ClickEvent clickEvent)
	{
		BaseText text = copyText(displayText);
		if (style != null)
		{
			text.setStyle(parseCarpetStyle(style));
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
		hoverText.append(translator.tr("teleport_hint.dimension", "Dimension"));
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
		BaseText entityBaseName = copyText((BaseText)entity.getType().getName());
		BaseText entityDisplayName = copyText((BaseText)entity.getName());
		BaseText hoverText = Messenger.c(
				String.format("w %s: ", translator.tr("entity_type", "Entity type")),
				entityBaseName,
				"w \n" + getTeleportHint(),
				getSpaceText(),
				entityDisplayName
		);
		return getFancyText(style, entityDisplayName, hoverText, new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, getTeleportCommand(entity)));
	}

	public static BaseText getAttributeText(EntityAttribute attribute)
	{
		return new TranslatableText(attribute.getTranslationKey());
	}

	public static BaseText getDimensionNameText(RegistryKey<World> dim)
	{
		return copyText(DIMENSION_NAME.getOrDefault(dim, Messenger.s(dim.getValue().toString())));
	}

	public static String dimensionToString(RegistryKey<World> dimensionType)
	{
		return dimensionType.getValue().getPath();
	}

	public static BaseText getColoredDimensionSymbol(RegistryKey<World> dimensionType)
	{
		if (dimensionType == World.OVERWORLD)
		{
			return Messenger.s("O", "e");  // DARK_GREEN
		}
		if (dimensionType == World.NETHER)
		{
			return Messenger.s("N", "n");  // DARK_RED
		}
		if (dimensionType == World.END)
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
		return attachHoverText(new TranslatableText(block.getTranslationKey()), Messenger.s(Registry.BLOCK.getId(block).toString()));
	}

	public static BaseText getFluidName(Fluid fluid)
	{
		Identifier id = Registry.FLUID.getId(fluid);
		BaseText nameText;
		if (fluid.isIn(FluidTags.WATER))
		{
			nameText = getBlockName(Blocks.WATER);
		}
		else if (fluid.isIn(FluidTags.LAVA))
		{
			nameText = getBlockName(Blocks.LAVA);
		}
		else
		{
			nameText = Messenger.s(id.getPath());
		}
		return attachHoverText(nameText, Messenger.s(id.toString()));
	}

	// some language doesn't use space char to divide word
	// so here comes the compatibility
	public static String getSpace()
	{
		return translator.tr("language.space", " ");
	}

	public static BaseText getSpaceText()
	{
		return Messenger.s(getSpace());
	}
}
