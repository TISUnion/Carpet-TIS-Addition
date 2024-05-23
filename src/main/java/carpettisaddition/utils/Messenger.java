/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.utils;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.mixins.translations.StyleAccessor;
import carpettisaddition.mixins.translations.TranslatableTextAccessor;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.state.property.Property;
import net.minecraft.text.*;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

//#if MC >= 12005
//$$ import net.minecraft.registry.entry.RegistryEntry;
//#endif

//#if MC >= 11600 && MC < 11900
//$$ import net.minecraft.util.Util;
//#endif

//#if MC >= 11600
//$$ import carpettisaddition.mixins.utils.DyeColorAccessor;
//$$ import net.minecraft.text.TextColor;
//#endif

//#if MC < 11500
//$$ import carpettisaddition.mixins.carpet.access.MessengerInvoker;
//#endif

public class Messenger
{
	private static final Translator translator = new Translator("util");

	/*
	 * ----------------------------
	 *    Text Factories - Utils
	 * ----------------------------
	 */

	/**
	 * MC 1.19 +- compatibility
	 * Get the object of the text object that indicates variable kind of text type
	 */
	public static
	//#if MC >= 11900
	//$$ TextContent
	//#else
	BaseText
	//#endif
	getTextContent(BaseText text)
	{
		//#if MC >= 11900
		//$$ return text.getContent();
		//#else
		return text;
		//#endif
	}

	/*
	 * ----------------------------
	 *    Text Factories - Basic
	 * ----------------------------
	 */

	// Compound Text in carpet style
	public static BaseText c(Object ... fields)
	{
		return
				//#if MC >= 11900
				//$$ (MutableText)
				//#endif
				carpet.utils.Messenger.c(fields);
	}

	// Simple Text
	public static BaseText s(Object text)
	{
		return
				//#if MC >= 11900
				//$$ Text.literal
				//#else
				new LiteralText
				//#endif
						(text.toString());
	}

	// Simple Text with carpet style
	public static BaseText s(Object text, String carpetStyle)
	{
		return formatting(s(text), carpetStyle);
	}

	// Simple Text with formatting
	public static BaseText s(Object text, Formatting ...textFormattings)
	{
		return formatting(s(text), textFormattings);
	}

	public static BaseText newLine()
	{
		return s("\n");
	}

	private static final ImmutableMap<DyeColor, Consumer<BaseText>> DYE_COLOR_APPLIER = Util.make(() -> {
		Map<DyeColor, Formatting> map = Maps.newHashMap();
		map.put(DyeColor.WHITE, Formatting.WHITE);
		map.put(DyeColor.LIGHT_GRAY, Formatting.GRAY);
		map.put(DyeColor.GRAY, Formatting.DARK_GRAY);
		map.put(DyeColor.BLACK, Formatting.BLACK);
		map.put(DyeColor.RED, Formatting.RED);
		map.put(DyeColor.YELLOW, Formatting.YELLOW);
		map.put(DyeColor.LIME, Formatting.GREEN);
		map.put(DyeColor.GREEN, Formatting.DARK_GREEN);
		map.put(DyeColor.CYAN, Formatting.DARK_AQUA);
		map.put(DyeColor.LIGHT_BLUE, Formatting.AQUA);
		map.put(DyeColor.BLUE, Formatting.DARK_BLUE);
		map.put(DyeColor.PURPLE, Formatting.DARK_PURPLE);
		map.put(DyeColor.MAGENTA, Formatting.LIGHT_PURPLE);

		//#if MC < 11600
		map.put(DyeColor.BROWN, Formatting.DARK_RED);
		map.put(DyeColor.PINK, Formatting.RED);
		map.put(DyeColor.ORANGE, Formatting.GOLD);
		//#endif

		ImmutableMap.Builder<DyeColor, Consumer<BaseText>> builder = new ImmutableMap.Builder<>();
		map.forEach((dyeColor, fmt) -> builder.put(dyeColor, text -> formatting(text, fmt)));
		//#if MC >= 11600
		//$$ Arrays.stream(DyeColor.values())
		//$$ 		.filter(dyeColor -> !map.containsKey(dyeColor))
		//$$ 		.forEach(dyeColor -> builder.put(dyeColor, text -> {
		//$$ 				TextColor color = TextColor.fromRgb(((DyeColorAccessor)(Object)dyeColor).getSignColor$TISCM());
		//$$ 				text.setStyle(text.getStyle().withColor(color));
		//$$ 		}));
		//#endif
		return builder.build();
	});

	public static BaseText colored(BaseText text, DyeColor value)
	{
		// TODO: make microtiming utils use this too
		Consumer<BaseText> consumer = DYE_COLOR_APPLIER.get(value);
		if (consumer != null)
		{
			consumer.accept(text);
		}
		return text;
	}

	public static BaseText colored(BaseText text, Object value)
	{
		Formatting color = null;
		if (Boolean.TRUE.equals(value))
		{
			color = Formatting.GREEN;
		}
		else if (Boolean.FALSE.equals(value))
		{
			color = Formatting.RED;
		}
		if (value instanceof Number)
		{
			color = Formatting.GOLD;
		}
		if (color != null)
		{
			formatting(text, color);
		}
		return text;
	}

	public static BaseText colored(Object value)
	{
		return colored(s(value), value);
	}

	public static BaseText property(Property<?> property, Object value)
	{
		return colored(s(TextUtil.property(property, value)), value);
	}

	// Translation Text
	public static BaseText tr(String key, Object ... args)
	{
		return
				//#if MC >= 11900
				//$$ Text.translatable
				//#else
				new TranslatableText
				//#endif
						(key, args);
	}

	// Fancy text
	// A copy will be made to make sure the original displayText will not be modified
	// TODO: yeets style
	public static BaseText fancy(@Nullable String carpetStyle, @NotNull BaseText displayText, @Nullable BaseText hoverText, @Nullable ClickEvent clickEvent)
	{
		BaseText text = copy(displayText);
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

	public static BaseText fancy(BaseText displayText, BaseText hoverText, ClickEvent clickEvent)
	{
		return fancy(null, displayText, hoverText, clickEvent);
	}

	public static BaseText join(BaseText joiner, Iterable<BaseText> items)
	{
		BaseText text = s("");
		boolean first = true;
		for (BaseText item : items)
		{
			if (!first)
			{
				text.append(joiner);
			}
			first = false;
			text.append(item);
		}
		return text;
	}

	public static BaseText join(BaseText joiner, BaseText... items)
	{
		return join(joiner, Arrays.asList(items));
	}

	public static BaseText format(String formatter, Object... args)
	{
		TranslatableTextAccessor dummy =
				(TranslatableTextAccessor)(
						tr(formatter, args)
						//#if MC >= 11900
						//$$ .getContent()
						//#endif
				);
		try
		{
			//#if MC >= 11800
			//$$ List<StringVisitable> segments = Lists.newArrayList();
			//$$ dummy.invokeForEachPart(formatter, segments::add);
			//#else
			dummy.getTranslations().clear();
			dummy.invokeSetTranslation(formatter);
			//#endif

			return Messenger.c(
					//#if MC >= 11600
					//#if MC >= 11800
					//$$ segments.
					//#else
					//$$ dummy.getTranslations().
					//#endif
					//$$ 		stream().map(stringVisitable -> {
					//$$ 			if (stringVisitable instanceof
										//#if MC >= 11900
										//$$ Text
										//#else
										//$$ BaseText
										//#endif
					//$$ 			)
					//$$ 			{
									//#if MC >= 11900
									//$$ return (Text)stringVisitable;
									//#else
									//$$ return (BaseText)stringVisitable;
									//#endif
					//$$ 			}
					//$$ 			return Messenger.s(stringVisitable.getString());
					//$$ 		}).toArray()
					//#else
					dummy.getTranslations().toArray(new Object[0])
					//#endif
			);
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

	public static BaseText bool(boolean value)
	{
		return s(String.valueOf(value), value ? Formatting.GREEN : Formatting.RED);
	}

	private static BaseText getTeleportHint(BaseText dest)
	{
		return translator.tr("teleport_hint", dest);
	}

	private static BaseText __coord(String style, @Nullable DimensionWrapper dim, String posStr, String command)
	{
		BaseText hoverText = Messenger.s("");
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

	private static BaseText __vector(String style, String displayText, String detailedText)
	{
		return fancy(style, Messenger.s(displayText), Messenger.s(detailedText), new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, detailedText));
	}
	public static BaseText vector(String style, Vec3d vec) {return __vector(style, TextUtil.vector(vec), TextUtil.vector(vec, 6));}
	public static BaseText vector(Vec3d vec) {return vector(null, vec);}

	public static BaseText entityType(EntityType<?> entityType)
	{
		return (BaseText)entityType.getName();
	}
	public static BaseText entityType(Entity entity)
	{
		return entityType(entity.getType());
	}

	public static BaseText entity(String style, Entity entity)
	{
		BaseText entityBaseName = entityType(entity);
		BaseText entityDisplayName = (BaseText)entity.getName();
		BaseText hoverText = Messenger.c(
				translator.tr("entity_type", entityBaseName, s(EntityType.getId(entity.getType()).toString())), newLine(),
				getTeleportHint(entityDisplayName)
		);
		return fancy(style, entityDisplayName, hoverText, new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(entity)));
	}

	public static BaseText entity(Entity entity)
	{
		return entity(null, entity);
	}

	public static BaseText attribute(EntityAttribute attribute)
	{
		return tr(
				//#if MC >= 11600
				//$$ attribute.getTranslationKey()
				//#else
				"attribute.name." + attribute.getId()
				//#endif
		);
	}
	//#if MC >= 12005
	//$$ public static MutableText attribute(RegistryEntry<EntityAttribute> attribute)
	//$$ {
	//$$ 	return attribute(attribute.value());
	//$$ }
	//#endif

	private static final ImmutableMap<DimensionWrapper, BaseText> DIMENSION_NAME = ImmutableMap.of(
			DimensionWrapper.OVERWORLD, tr(
					//#if MC >= 11900
					//$$ "flat_world_preset.minecraft.overworld"
					//#else
					"createWorld.customize.preset.overworld"
					//#endif
			),
			DimensionWrapper.THE_NETHER, tr("advancements.nether.root.title"),
			DimensionWrapper.THE_END, tr("advancements.end.root.title")
	);

	public static BaseText dimension(DimensionWrapper dim)
	{
		BaseText dimText = DIMENSION_NAME.get(dim);
		return dimText != null ? copy(dimText) : Messenger.s(dim.getIdentifierString());
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
		return hover(tr(block.getTranslationKey()), s(TextUtil.block(block)));
	}

	public static BaseText block(BlockState blockState)
	{
		List<BaseText> hovers = Lists.newArrayList();
		hovers.add(s(TextUtil.block(blockState.getBlock())));
		for (Property<?> property: blockState.getProperties())
		{
			hovers.add(Messenger.c(
					Messenger.s(property.getName()),
					"g : ",
					property(property, blockState.get(property))
			));
		}
		return fancy(
				block(blockState.getBlock()),
				join(s("\n"), hovers.toArray(new BaseText[0])),
				new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.block(blockState))
		);
	}

	public static BaseText fluid(Fluid fluid)
	{
		return hover(block(fluid.getDefaultState().getBlockState().getBlock()), s(IdentifierUtil.id(fluid).toString()));
	}

	public static BaseText fluid(FluidState fluid)
	{
		return fluid(fluid.getFluid());
	}

	public static BaseText blockEntity(BlockEntity blockEntity)
	{
		Identifier id = IdentifierUtil.id(blockEntity.getType());
		return s(id != null ?
				id.toString() : // vanilla block entity
				blockEntity.getClass().getSimpleName()  // modded block entity, assuming the class name is not obfuscated
		);
	}

	public static BaseText item(Item item)
	{
		return tr(item.getTranslationKey());
	}

	public static BaseText color(DyeColor color)
	{
		return translator.tr("color." + color.getName().toLowerCase());
	}

	/*
	 * --------------------
	 *    Text Modifiers
	 * --------------------
	 */

	public static BaseText hover(BaseText text, HoverEvent hoverEvent)
	{
		//#if MC >= 11600
		//$$ style(text, text.getStyle().withHoverEvent(hoverEvent));
		//#else
		text.getStyle().setHoverEvent(hoverEvent);
		//#endif
		return text;
	}

	public static BaseText hover(BaseText text, BaseText hoverText)
	{
		return hover(text, new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
	}

	public static BaseText click(BaseText text, ClickEvent clickEvent)
	{
		//#if MC >= 11600
		//$$ style(text, text.getStyle().withClickEvent(clickEvent));
		//#else
		text.getStyle().setClickEvent(clickEvent);
		//#endif
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
		StyleAccessor parsedStyle = (StyleAccessor)parseCarpetStyle(carpetStyle);

		//#if MC >= 11600
		//$$ textStyle = textStyle.withColor(parsedStyle.getColorField());
		//$$ textStyle = textStyle.withBold(parsedStyle.getBoldField());
		//$$ textStyle = textStyle.withItalic(parsedStyle.getItalicField());
		//$$ ((StyleAccessor)textStyle).setUnderlinedField(parsedStyle.getUnderlineField());
		//$$ ((StyleAccessor)textStyle).setStrikethroughField(parsedStyle.getStrikethroughField());
		//$$ ((StyleAccessor)textStyle).setObfuscatedField(parsedStyle.getObfuscatedField());
		//#else
		textStyle.setColor(parsedStyle.getColorField());
		textStyle.setBold(parsedStyle.getBoldField());
		textStyle.setItalic(parsedStyle.getItalicField());
		textStyle.setUnderline(parsedStyle.getUnderlineField());
		textStyle.setStrikethrough(parsedStyle.getStrikethroughField());
		textStyle.setObfuscated(parsedStyle.getObfuscatedField());
		//#endif

		return style(text, textStyle);
	}

	public static BaseText style(BaseText text, Style style)
	{
		text.setStyle(style);
		return text;
	}

	public static BaseText copy(BaseText text)
	{
		BaseText copied;

		//#if MC >= 11900
		//$$ copied = text.copy();
		//#elseif MC >= 11600
		//$$ copied = (BaseText)text.shallowCopy();
		//#else
		copied = (BaseText)text.deepCopy();
		//#endif

		// mc1.16+ doesn't make a copy of args of a TranslatableText,
		// so we need to copy that by ourselves
		//#if MC >= 11600
		//$$ if (getTextContent(copied) instanceof TranslatableText)
		//$$ {
		//$$ 	TranslatableText translatableText = (TranslatableText)getTextContent(copied);
		//$$ 	Object[] args = translatableText.getArgs().clone();
		//$$ 	for (int i = 0; i < args.length; i++)
		//$$ 	{
		//$$ 		if (args[i] instanceof BaseText)
		//$$ 		{
		//$$ 			args[i] = copy((BaseText)args[i]);
		//$$ 		}
		//$$ 	}
		//$$ 	((TranslatableTextAccessor)translatableText).setArgs(args);
		//$$ }
		//#endif

		return copied;
	}

	/*
	 * ------------------
	 *    Text Senders
	 * ------------------
	 */

	private static void __tell(ServerCommandSource source, BaseText text, boolean broadcastToOps)
	{
		// translation logic is handled in carpettisaddition.mixins.translations.ServerPlayerEntityMixin
		source.sendFeedback(
				//#if MC >= 12000
				//$$ () ->
				//#endif
				text, broadcastToOps
		);
	}

	public static void tell(ServerCommandSource source, BaseText text, boolean broadcastToOps)
	{
		__tell(source, text, broadcastToOps);
	}
	public static void tell(PlayerEntity player, BaseText text, boolean broadcastToOps)
	{
		tell(player.getCommandSource(), text, broadcastToOps);
	}
	public static void tell(ServerCommandSource source, BaseText text)
	{
		tell(source, text, false);
	}
	public static void tell(PlayerEntity player, BaseText text)
	{
		tell(player, text, false);
	}
	public static void tell(ServerCommandSource source, Iterable<BaseText> texts, boolean broadcastToOps)
	{
		texts.forEach(text -> tell(source, text, broadcastToOps));
	}
	public static void tell(PlayerEntity player, Iterable<BaseText> texts, boolean broadcastToOps)
	{
		texts.forEach(text -> tell(player, text, broadcastToOps));
	}
	public static void tell(ServerCommandSource source, Iterable<BaseText> texts)
	{
		tell(source, texts, false);
	}
	public static void tell(PlayerEntity player, Iterable<BaseText> texts)
	{
		tell(player, texts, false);
	}

	public static void reminder(PlayerEntity player, BaseText text)
	{
		// translation logic is handled in carpettisaddition.mixins.translations.ServerPlayerEntityMixin
		//#if MC >= 11600
		//$$ player.sendMessage
		//#else
		player.addChatMessage
		//#endif
				(text, true);
	}

	public static void sendToConsole(BaseText text)
	{
		if (CarpetTISAdditionServer.minecraft_server != null)
		{
			//#if MC >= 11900
			//$$ CarpetTISAdditionServer.minecraft_server.sendMessage(text);
			//#elseif MC >= 11600
			//$$ CarpetTISAdditionServer.minecraft_server.sendSystemMessage(text, Util.NIL_UUID);
			//#else
			CarpetTISAdditionServer.minecraft_server.sendMessage(text);
			//#endif
		}
	}

	public static void broadcast(BaseText text)
	{
		sendToConsole(text);
		if (CarpetTISAdditionServer.minecraft_server != null)
		{
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
		//#if MC >= 11500
		return carpet.utils.Messenger.parseStyle(style);
		//#else
		//$$ return MessengerInvoker.call_applyStyleToTextComponent(Messenger.s(""), style).getStyle();
		//#endif
	}

	// some language doesn't use space char to divide word
	// so here comes the compatibility
	public static BaseText getSpaceText()
	{
		return translator.tr("language.space");
	}
}
