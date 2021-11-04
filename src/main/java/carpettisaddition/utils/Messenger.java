package carpettisaddition.utils;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.mixins.carpet.MessengerInvoker;
import carpettisaddition.translations.TISAdditionTranslations;
import carpettisaddition.translations.Translator;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.BiConsumer;

public class Messenger
{
	private static final Translator translator = new Translator("util");

	/*
	 * ----------------------------
	 *    Text Factories - Basic
	 * ----------------------------
	 */

	// Compound Text
	public static BaseText c(Object ... fields)
	{
		return carpet.utils.Messenger.c(fields);
	}

	// Simple Text
	public static BaseText s(String text)
	{
		return new LiteralText(text);
	}

	// Simple Text with carpet style
	public static BaseText s(String text, String carpetStyle)
	{
		return formatting(s(text), carpetStyle);
	}

	// Simple Text with formatting
	public static BaseText s(String text, Formatting textFormatting)
	{
		return formatting(s(text), textFormatting);
	}

	public static BaseText newLine()
	{
		return s("\n");
	}

	// Translation Text
	public static BaseText tr(String key, Object ... args)
	{
		return new TranslatableText(key, args);
	}

	// Fancy text
	// TODO: yeets style
	public static BaseText fancy(String style, BaseText displayText, BaseText hoverText, ClickEvent clickEvent)
	{
		BaseText text = copy(displayText);
		if (style != null)
		{
			text.setStyle(parseCarpetStyle(style));
		}
		if (hoverText != null)
		{
			hover(text, hoverText);
		}
		if (clickEvent != null)
		{
			click(text, clickEvent);
		}
		return text;
	}

	public static BaseText fancy(BaseText displayText, BaseText hoverText, ClickEvent clickEvent)
	{
		return fancy(null, displayText, hoverText, clickEvent);
	}

	public static BaseText join(BaseText joiner, BaseText... items)
	{
		BaseText text = s("");
		for (int i = 0; i < items.length; i++)
		{
			if (i > 0)
			{
				text.append(joiner.copy());
			}
			text.append(items[i]);
		}
		return text;
	}

	/*
	 * -------------------------------
	 *    Text Factories - Advanced
	 * -------------------------------
	 */

	private static BaseText getTeleportHint()
	{
		return translator.tr("teleport_hint");
	}

	private static BaseText __coord(String style, @Nullable DimensionWrapper dim, String posText, String command)
	{
		BaseText hoverText = Messenger.s("");
		hoverText.append(getTeleportHint());
		hoverText.append(Messenger.s(" "));
		hoverText.append(posText);
		if (dim != null)
		{
			hoverText.append("\n");
			hoverText.append(translator.tr("teleport_hint.dimension"));
			hoverText.append(": ");
			hoverText.append(dimension(dim));
		}
		return fancy(style, Messenger.s(posText), hoverText, new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
	}

	public static BaseText coord(String style, Vec3d pos, DimensionWrapper dim) {return __coord(style, dim, TextUtil.coord(pos), TextUtil.tp(pos, dim));}
	public static BaseText coord(String style, Vec3i pos, DimensionWrapper dim) {return __coord(style, dim, TextUtil.coord(pos), TextUtil.tp(pos, dim));}
	public static BaseText coord(String style, ChunkPos pos, DimensionWrapper dim) {return __coord(style, dim, TextUtil.coord(pos), TextUtil.tp(pos, dim));}
	public static BaseText coord(String style, Vec3d pos) {return __coord(style, null, TextUtil.coord(pos), TextUtil.tp(pos));}
	public static BaseText coord(String style, Vec3i pos) {return __coord(style, null, TextUtil.coord(pos), TextUtil.tp(pos));}
	public static BaseText coord(String style, ChunkPos pos) {return __coord(style, null, TextUtil.coord(pos), TextUtil.tp(pos));}
	public static BaseText coord(Vec3d pos, DimensionWrapper dim) {return coord(null, pos, dim);}
	public static BaseText coord(Vec3i pos, DimensionWrapper dim) {return coord(null, pos, dim);}
	public static BaseText coord(ChunkPos pos, DimensionWrapper dim) {return coord(null, pos, dim);}
	public static BaseText coord(Vec3d pos) {return coord(null, pos);}
	public static BaseText coord(Vec3i pos) {return coord(null, pos);}
	public static BaseText coord(ChunkPos pos) {return coord(null, pos);}

	public static BaseText entity(String style, Entity entity)
	{
		BaseText entityBaseName = copy((BaseText)entity.getType().getName());
		BaseText entityDisplayName = copy((BaseText)entity.getName());
		BaseText hoverText = Messenger.c(
				translator.tr("entity_type"), "w : ",
				entityBaseName,
				"w \n" + getTeleportHint(),
				getSpaceText(),
				entityDisplayName
		);
		return fancy(style, entityDisplayName, hoverText, new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(entity)));
	}

	public static BaseText attribute(EntityAttribute attribute)
	{
		return tr("attribute.name." + attribute.getId());
	}

	private static final ImmutableMap<DimensionWrapper, BaseText> DIMENSION_NAME = ImmutableMap.of(
			DimensionWrapper.OVERWORLD, new TranslatableText("createWorld.customize.preset.overworld"),
			DimensionWrapper.THE_NETHER, new TranslatableText("advancements.nether.root.title"),
			DimensionWrapper.THE_END, new TranslatableText("advancements.end.root.title")
	);

	public static BaseText dimension(DimensionWrapper dim)
	{
		return copy(DIMENSION_NAME.getOrDefault(dim, Messenger.s(dim.toString())));
	}

	public static BaseText getColoredDimensionSymbol(DimensionWrapper dimensionType)
	{
		if (dimensionType.equals(DimensionWrapper.OVERWORLD))
		{
			return s("O", Formatting.DARK_GREEN);  // DARK_GREEN
		}
		if (dimensionType.equals(DimensionWrapper.THE_NETHER))
		{
			return s("N", Formatting.DARK_RED);  // DARK_RED
		}
		if (dimensionType.equals(DimensionWrapper.THE_END))
		{
			return s("E", Formatting.DARK_PURPLE);  // DARK_PURPLE
		}
		return s(dimensionType.getIdentifierString().toUpperCase().substring(0, 1));
	}

	public static BaseText block(Block block)
	{
		return hover(new TranslatableText(block.getTranslationKey()), s(Registry.BLOCK.getId(block).toString()));
	}

	public static BaseText fluid(Fluid fluid)
	{
		Identifier id = Registry.FLUID.getId(fluid);
		return hover(block(fluid.getDefaultState().getBlockState().getBlock()), s(id.toString()));
	}

	/*
	 * --------------------
	 *    Text Modifiers
	 * --------------------
	 */

	public static BaseText hover(BaseText text, HoverEvent hoverEvent)
	{
		text.getStyle().setHoverEvent(hoverEvent);
		return text;
	}

	public static BaseText hover(BaseText text, BaseText hoverText)
	{
		return hover(text, new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
	}

	public static BaseText click(BaseText text, ClickEvent clickEvent)
	{
		text.getStyle().setClickEvent(clickEvent);
		return text;
	}

	public static BaseText formatting(BaseText text, Formatting... formattings)
	{
		text.formatted(formattings);
		return text;
	}

	public static BaseText formatting(BaseText text, String carpetStyle)
	{
		Style textStyle = text.getStyle();
		Style parsedStyle = parseCarpetStyle(carpetStyle);
		textStyle.setColor(parsedStyle.getColor());
		textStyle.setBold(parsedStyle.isBold());
		textStyle.setItalic(parsedStyle.isItalic());
		textStyle.setUnderline(parsedStyle.isUnderlined());
		textStyle.setStrikethrough(parsedStyle.isStrikethrough());
		textStyle.setObfuscated(parsedStyle.isObfuscated());
		return style(text, textStyle);
	}

	public static BaseText style(BaseText text, Style style)
	{
		text.setStyle(style);
		return text;
	}

	public static BaseText copy(BaseText text)
	{
		return (BaseText)text.deepCopy();
	}

	/*
	 * ------------------
	 *    Text Senders
	 * ------------------
	 */

	private static void __tell(ServerCommandSource source, BaseText text)
	{
		Entity entity = source.getEntity();
		text = entity instanceof ServerPlayerEntity ?
				TISAdditionTranslations.translate(text, (ServerPlayerEntity)entity) :
				TISAdditionTranslations.translate(text);
		source.sendFeedback(text, false);
	}

	private static void __reminder(PlayerEntity player, BaseText text)
	{
		text = player instanceof ServerPlayerEntity ?
				TISAdditionTranslations.translate(text, (ServerPlayerEntity)player) :
				TISAdditionTranslations.translate(text);
		player.addChatMessage(text, true);
	}

	private static <T> void messageSender(T target, BiConsumer<T, BaseText> consumer, Object... texts)
	{
		if (texts.length == 1)
		{
			if (texts[0] instanceof BaseText)
			{
				consumer.accept(target, (BaseText)texts[0]);
			}
			else if (texts[0] instanceof Collection)
			{
				((Collection<?>) texts[0]).forEach(text -> consumer.accept(target, (BaseText)text));
			}
		}
		else
		{
			consumer.accept(target, c(texts));
		}
	}

	public static void tell(ServerCommandSource source, Object... texts)
	{
		messageSender(source, Messenger::__tell, texts);
	}

	public static void tell(PlayerEntity player, Object... text)
	{
		tell(player.getCommandSource(), text);
	}

	public static void reminder(PlayerEntity player, Object... texts)
	{
		messageSender(player, Messenger::__reminder, texts);
	}

	public static void broadcast(BaseText text)
	{
		if (CarpetTISAdditionServer.minecraft_server != null)
		{
			CarpetTISAdditionServer.minecraft_server.sendMessage(text);
			CarpetTISAdditionServer.minecraft_server.getPlayerManager().getPlayerList().forEach(player -> tell(player, text));
		}
	}


	/*
	 * ----------
	 *    Misc
	 * ----------
	 */

	@SuppressWarnings("ConstantConditions")
	public static Style parseCarpetStyle(String style)
	{
		return MessengerInvoker.call_applyStyleToTextComponent(Messenger.s(""), style).getStyle();
	}

	// some language doesn't use space char to divide word
	// so here comes the compatibility
	public static BaseText getSpaceText()
	{
		return translator.tr("language.space");
	}
}
