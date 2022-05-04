package carpettisaddition.utils;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.mixins.translations.StyleAccessor;
import carpettisaddition.mixins.translations.TranslatableTextAccessor;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.state.property.Property;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Messenger
{
	private static final Translator translator = new Translator("util");

	/*
	 * ----------------------------
	 *    Text Factories - Basic
	 * ----------------------------
	 */

	// Compound Text in carpet style
	public static MutableText c(Object ... fields)
	{
		return (MutableText)carpet.utils.Messenger.c(fields);
	}

	// Simple Text
	public static MutableText s(Object text)
	{
		return Text.literal(text.toString());
	}

	// Simple Text with carpet style
	public static MutableText s(String text, String carpetStyle)
	{
		return formatting(s(text), carpetStyle);
	}

	// Simple Text with formatting
	public static MutableText s(String text, Formatting textFormatting)
	{
		return formatting(s(text), textFormatting);
	}

	public static MutableText newLine()
	{
		return s("\n");
	}

	// Translation Text
	public static MutableText tr(String key, Object ... args)
	{
		return Text.translatable(key, args);
	}

	// Fancy text
	// TODO: yeets style
	public static MutableText fancy(String carpetStyle, MutableText displayText, MutableText hoverText, ClickEvent clickEvent)
	{
		MutableText text = copy(displayText);
		if (carpetStyle != null)
		{
			text.setStyle(parseCarpetStyle(carpetStyle));
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

	public static MutableText fancy(MutableText displayText, MutableText hoverText, ClickEvent clickEvent)
	{
		return fancy(null, displayText, hoverText, clickEvent);
	}

	public static MutableText join(MutableText joiner, MutableText... items)
	{
		MutableText text = s("");
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

	public static MutableText format(String formatter, Object... args)
	{
		TranslatableTextAccessor dummy = (TranslatableTextAccessor)(Messenger.tr(formatter, args).getContent());
		try
		{
			List<StringVisitable> segments = Lists.newArrayList();
			dummy.invokeForEachPart(formatter, segments::add);
			return Messenger.c(segments.stream().map(stringVisitable -> {
				if (stringVisitable instanceof Text)
				{
					return (Text)stringVisitable;
				}
				return Messenger.s(stringVisitable.getString());
			}).toArray());
		}
		catch (TranslationException e)
		{
			throw new IllegalArgumentException(formatter);
		}
	}

	/*
	 * -------------------------------
	 *    Text Factories - Advanced
	 * -------------------------------
	 */

	private static MutableText getTeleportHint(Text dest)
	{
		return translator.tr("teleport_hint", dest);
	}

	private static MutableText __coord(String style, @Nullable DimensionWrapper dim, String posStr, String command)
	{
		MutableText hoverText = Messenger.s("");
		hoverText.append(getTeleportHint(Messenger.s(posStr)));
		if (dim != null)
		{
			hoverText.append("\n");
			hoverText.append(translator.tr("teleport_hint.dimension"));
			hoverText.append(": ");
			hoverText.append(dimension(dim));
		}
		return fancy(style, Messenger.s(posStr), hoverText, new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
	}

	public static MutableText coord(String style, Vec3d pos, DimensionWrapper dim) {return __coord(style, dim, TextUtil.coord(pos), TextUtil.tp(pos, dim));}
	public static MutableText coord(String style, Vec3i pos, DimensionWrapper dim) {return __coord(style, dim, TextUtil.coord(pos), TextUtil.tp(pos, dim));}
	public static MutableText coord(String style, ChunkPos pos, DimensionWrapper dim) {return __coord(style, dim, TextUtil.coord(pos), TextUtil.tp(pos, dim));}
	public static MutableText coord(String style, Vec3d pos) {return __coord(style, null, TextUtil.coord(pos), TextUtil.tp(pos));}
	public static MutableText coord(String style, Vec3i pos) {return __coord(style, null, TextUtil.coord(pos), TextUtil.tp(pos));}
	public static MutableText coord(String style, ChunkPos pos) {return __coord(style, null, TextUtil.coord(pos), TextUtil.tp(pos));}
	public static MutableText coord(Vec3d pos, DimensionWrapper dim) {return coord(null, pos, dim);}
	public static MutableText coord(Vec3i pos, DimensionWrapper dim) {return coord(null, pos, dim);}
	public static MutableText coord(ChunkPos pos, DimensionWrapper dim) {return coord(null, pos, dim);}
	public static MutableText coord(Vec3d pos) {return coord(null, pos);}
	public static MutableText coord(Vec3i pos) {return coord(null, pos);}
	public static MutableText coord(ChunkPos pos) {return coord(null, pos);}

	public static MutableText entity(String style, Entity entity)
	{
		MutableText entityBaseName = copy(entity.getType().getName());
		MutableText entityDisplayName = copy(entity.getName());
		MutableText hoverText = Messenger.c(
				translator.tr("entity_type", entityBaseName, s(EntityType.getId(entity.getType()).toString())), newLine(),
				getTeleportHint(entityDisplayName)
		);
		return fancy(style, entityDisplayName, hoverText, new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(entity)));
	}

	public static MutableText entity(Entity entity)
	{
		return entity(null, entity);
	}

	public static MutableText attribute(EntityAttribute attribute)
	{
		return tr(attribute.getTranslationKey());
	}

	private static final ImmutableMap<DimensionWrapper, MutableText> DIMENSION_NAME = ImmutableMap.of(
			DimensionWrapper.OVERWORLD, tr("flat_world_preset.minecraft.overworld"),
			DimensionWrapper.THE_NETHER, tr("advancements.nether.root.title"),
			DimensionWrapper.THE_END, tr("advancements.end.root.title")
	);

	public static MutableText dimension(DimensionWrapper dim)
	{
		MutableText dimText = DIMENSION_NAME.get(dim);
		return dimText != null ? copy(dimText) : Messenger.s(dim.getIdentifierString());
	}

	public static MutableText getColoredDimensionSymbol(DimensionWrapper dimensionType)
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

	public static MutableText block(Block block)
	{
		return hover(tr(block.getTranslationKey()), s(IdentifierUtil.id(block).toString()));
	}

	public static MutableText block(BlockState blockState)
	{
		List<String> hovers = Lists.newArrayList();
		hovers.add(IdentifierUtil.id(blockState.getBlock()).toString());
		for (Property<?> property: blockState.getProperties())
		{
			hovers.add(String.format("%s=%s", property.getName(), blockState.get(property)));
		}
		return hover(block(blockState.getBlock()), s(Joiner.on('\n').join(hovers)));
	}

	public static MutableText fluid(Fluid fluid)
	{
		return hover(block(fluid.getDefaultState().getBlockState().getBlock()), s(IdentifierUtil.id(fluid).toString()));
	}

	public static MutableText fluid(FluidState fluid)
	{
		return fluid(fluid.getFluid());
	}

	public static MutableText blockEntity(BlockEntity blockEntity)
	{
		Identifier id = IdentifierUtil.id(blockEntity.getType());
		return s(id != null ? id.toString() : blockEntity.getClass().getSimpleName());
	}

	/*
	 * --------------------
	 *    Text Modifiers
	 * --------------------
	 */

	public static MutableText hover(MutableText text, HoverEvent hoverEvent)
	{
		style(text, text.getStyle().withHoverEvent(hoverEvent));
		return text;
	}

	public static MutableText hover(MutableText text, MutableText hoverText)
	{
		return hover(text, new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
	}

	public static MutableText click(MutableText text, ClickEvent clickEvent)
	{
		style(text, text.getStyle().withClickEvent(clickEvent));
		return text;
	}

	public static MutableText formatting(MutableText text, Formatting... formattings)
	{
		text.formatted(formattings);
		return text;
	}

	public static MutableText formatting(MutableText text, String carpetStyle)
	{
		Style textStyle = text.getStyle();
		StyleAccessor parsedStyle = (StyleAccessor)parseCarpetStyle(carpetStyle);
		textStyle = textStyle.withColor(parsedStyle.getColorField());
		textStyle = textStyle.withBold(parsedStyle.getBoldField());
		textStyle = textStyle.withItalic(parsedStyle.getItalicField());
		((StyleAccessor)textStyle).setUnderlinedField(parsedStyle.getUnderlineField());
		((StyleAccessor)textStyle).setStrikethroughField(parsedStyle.getStrikethroughField());
		((StyleAccessor)textStyle).setObfuscatedField(parsedStyle.getObfuscatedField());
		return style(text, textStyle);
	}

	public static MutableText style(MutableText text, Style style)
	{
		text.setStyle(style);
		return text;
	}

	public static MutableText copy(Text text)
	{
		return text.shallowCopy();
	}

	/*
	 * ------------------
	 *    Text Senders
	 * ------------------
	 */

	private static void __tell(ServerCommandSource source, MutableText text, boolean broadcastToOps)
	{
		// translation logic is handled in carpettisaddition.mixins.translations.ServerPlayerEntityMixin
		source.sendFeedback(text, broadcastToOps);
	}

	public static void tell(ServerCommandSource source, MutableText text, boolean broadcastToOps)
	{
		__tell(source, text, broadcastToOps);
	}
	public static void tell(PlayerEntity player, MutableText text, boolean broadcastToOps)
	{
		tell(player.getCommandSource(), text, broadcastToOps);
	}
	public static void tell(ServerCommandSource source, MutableText text)
	{
		tell(source, text, false);
	}
	public static void tell(PlayerEntity player, MutableText text)
	{
		tell(player, text, false);
	}
	public static void tell(ServerCommandSource source, Iterable<MutableText> texts, boolean broadcastToOps)
	{
		texts.forEach(text -> tell(source, text, broadcastToOps));
	}
	public static void tell(PlayerEntity player, Iterable<MutableText> texts, boolean broadcastToOps)
	{
		texts.forEach(text -> tell(player, text, broadcastToOps));
	}
	public static void tell(ServerCommandSource source, Iterable<MutableText> texts)
	{
		tell(source, texts, false);
	}
	public static void tell(PlayerEntity player, Iterable<MutableText> texts)
	{
		tell(player, texts, false);
	}

	public static void reminder(PlayerEntity player, MutableText text)
	{
		// translation logic is handled in carpettisaddition.mixins.translations.ServerPlayerEntityMixin
		player.sendMessage(text, true);
	}

	public static void broadcast(MutableText text)
	{
		if (CarpetTISAdditionServer.minecraft_server != null)
		{
			CarpetTISAdditionServer.minecraft_server.method_43496(text);
			CarpetTISAdditionServer.minecraft_server.getPlayerManager().getPlayerList().forEach(player -> tell(player, text));
		}
	}


	/*
	 * ----------
	 *    Misc
	 * ----------
	 */

	public static Style parseCarpetStyle(String style)
	{
		return carpet.utils.Messenger.parseStyle(style);
	}

	// some language doesn't use space char to divide word
	// so here comes the compatibility
	public static MutableText getSpaceText()
	{
		return translator.tr("language.space");
	}
}
