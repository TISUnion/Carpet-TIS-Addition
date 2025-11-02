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
import carpettisaddition.mixins.utils.messenger.StyleAccessor;
import carpettisaddition.mixins.utils.messenger.TranslatableTextAccessor;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.Item;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.DyeColor;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Vec3i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

//#if MC >= 12105
//$$ import java.net.URI;
//#endif

//#if MC >= 12005
//$$ import net.minecraft.core.Holder;
//#endif

//#if MC >= 11900
//$$ import net.minecraft.network.chat.contents.TranslatableContents;
//$$ import net.minecraft.network.chat.contents.TranslatableFormatException;
//#endif

//#if MC >= 11600 && MC < 11900
//$$ import net.minecraft.Util;
//#endif

//#if MC >= 11600
//$$ import carpettisaddition.mixins.utils.DyeColorAccessor;
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
	//$$ ComponentContents
	//#else
	BaseComponent
	//#endif
	getTextContent(BaseComponent text)
	{
		//#if MC >= 11900
		//$$ return text.getContents();
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
	public static BaseComponent c(Object ... fields)
	{
		return
				//#if MC >= 11900
				//$$ (MutableComponent)
				//#endif
				carpet.utils.Messenger.c(fields);
	}

	// Simple Text
	public static BaseComponent s(Object text)
	{
		return
				//#if MC >= 11900
				//$$ Component.literal
				//#else
				new TextComponent
				//#endif
						(text.toString());
	}

	// Simple Text with carpet style
	public static BaseComponent s(Object text, String carpetStyle)
	{
		return formatting(s(text), carpetStyle);
	}

	// Simple Text with formatting
	public static BaseComponent s(Object text, ChatFormatting ...textFormattings)
	{
		return formatting(s(text), textFormattings);
	}

	public static BaseComponent newLine()
	{
		return s("\n");
	}

	private static final ImmutableMap<DyeColor, Consumer<BaseComponent>> DYE_COLOR_APPLIER = Util.make(() -> {
		Map<DyeColor, ChatFormatting> map = Maps.newHashMap();
		map.put(DyeColor.WHITE, ChatFormatting.WHITE);
		map.put(DyeColor.LIGHT_GRAY, ChatFormatting.GRAY);
		map.put(DyeColor.GRAY, ChatFormatting.DARK_GRAY);
		map.put(DyeColor.BLACK, ChatFormatting.BLACK);
		map.put(DyeColor.RED, ChatFormatting.RED);
		map.put(DyeColor.YELLOW, ChatFormatting.YELLOW);
		map.put(DyeColor.LIME, ChatFormatting.GREEN);
		map.put(DyeColor.GREEN, ChatFormatting.DARK_GREEN);
		map.put(DyeColor.CYAN, ChatFormatting.DARK_AQUA);
		map.put(DyeColor.LIGHT_BLUE, ChatFormatting.AQUA);
		map.put(DyeColor.BLUE, ChatFormatting.DARK_BLUE);
		map.put(DyeColor.PURPLE, ChatFormatting.DARK_PURPLE);
		map.put(DyeColor.MAGENTA, ChatFormatting.LIGHT_PURPLE);

		//#if MC < 11600
		map.put(DyeColor.BROWN, ChatFormatting.DARK_RED);
		map.put(DyeColor.PINK, ChatFormatting.RED);
		map.put(DyeColor.ORANGE, ChatFormatting.GOLD);
		//#endif

		ImmutableMap.Builder<DyeColor, Consumer<BaseComponent>> builder = new ImmutableMap.Builder<>();
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

	public static BaseComponent colored(BaseComponent text, DyeColor value)
	{
		// TODO: make microtiming utils use this too
		Consumer<BaseComponent> consumer = DYE_COLOR_APPLIER.get(value);
		if (consumer != null)
		{
			consumer.accept(text);
		}
		return text;
	}

	public static BaseComponent colored(BaseComponent text, Object value)
	{
		ChatFormatting color = null;
		if (Boolean.TRUE.equals(value))
		{
			color = ChatFormatting.GREEN;
		}
		else if (Boolean.FALSE.equals(value))
		{
			color = ChatFormatting.RED;
		}
		if (value instanceof Number)
		{
			color = ChatFormatting.GOLD;
		}
		if (color != null)
		{
			formatting(text, color);
		}
		return text;
	}

	public static BaseComponent colored(Object value)
	{
		return colored(s(value), value);
	}

	public static BaseComponent property(Property<?> property, Object value)
	{
		return colored(s(TextUtils.property(property, value)), value);
	}

	// Translation Text
	public static BaseComponent tr(String key, Object ... args)
	{
		return
				//#if MC >= 11900
				//$$ Component.translatable
				//#else
				new TranslatableComponent
				//#endif
						(key, args);
	}

	// Fancy text
	// A copy will be made to make sure the original displayText will not be modified
	// TODO: yeets style
	public static BaseComponent fancy(@Nullable String carpetStyle, @NotNull BaseComponent displayText, @Nullable BaseComponent hoverText, @Nullable ClickEvent clickEvent)
	{
		BaseComponent text = copy(displayText);
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

	public static BaseComponent fancy(BaseComponent displayText, BaseComponent hoverText, ClickEvent clickEvent)
	{
		return fancy(null, displayText, hoverText, clickEvent);
	}

	public static BaseComponent join(BaseComponent joiner, Iterable<BaseComponent> items)
	{
		BaseComponent text = s("");
		boolean first = true;
		for (BaseComponent item : items)
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

	public static BaseComponent join(BaseComponent joiner, BaseComponent... items)
	{
		return join(joiner, Arrays.asList(items));
	}

	public static BaseComponent joinLines(Iterable<BaseComponent> items)
	{
		return join(newLine(), items);
	}

	public static BaseComponent joinLines(BaseComponent... items)
	{
		return join(newLine(), items);
	}

	public static BaseComponent format(String formatter, Object... args)
	{
		TranslatableTextAccessor dummy =
				(TranslatableTextAccessor)(
						tr(formatter, args)
						//#if MC >= 11900
						//$$ .getContents()
						//#endif
				);
		try
		{
			//#if MC >= 11800
			//$$ List<FormattedText> segments = Lists.newArrayList();
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
										//$$ Component
										//#else
										//$$ BaseComponent
										//#endif
					//$$ 			)
					//$$ 			{
									//#if MC >= 11900
									//$$ return (Component)stringVisitable;
									//#else
									//$$ return (BaseComponent)stringVisitable;
									//#endif
					//$$ 			}
					//$$ 			return Messenger.s(stringVisitable.getString());
					//$$ 		}).toArray()
					//#else
					dummy.getTranslations().toArray(new Object[0])
					//#endif
			);
		}
		catch (TranslatableFormatException e)
		{
			throw new IllegalArgumentException(formatter);
		}
	}

	/*
	 * -------------------------------
	 *    Text Factories - Advanced
	 * -------------------------------
	 */

	public static BaseComponent bool(boolean value)
	{
		return s(String.valueOf(value), value ? ChatFormatting.GREEN : ChatFormatting.RED);
	}

	private static BaseComponent getTeleportHint(BaseComponent dest)
	{
		return translator.tr("teleport_hint", dest);
	}

	private static BaseComponent __coord(String style, @Nullable DimensionWrapper dim, String posStr, String command)
	{
		BaseComponent hoverText = Messenger.s("");
		hoverText.append(getTeleportHint(Messenger.s(posStr)));
		if (dim != null)
		{
			hoverText.append("\n");
			hoverText.append(translator.tr("teleport_hint.dimension"));
			hoverText.append(": ");
			hoverText.append(dimension(dim));
		}
		return fancy(style, Messenger.s(posStr), hoverText, Messenger.ClickEvents.suggestCommand(command));
	}

	public static BaseComponent coord(String style, Vec3 pos, DimensionWrapper dim) {return __coord(style, dim, TextUtils.coord(pos), TextUtils.tp(pos, dim));}
	public static BaseComponent coord(String style, Vec3i pos, DimensionWrapper dim) {return __coord(style, dim, TextUtils.coord(pos), TextUtils.tp(pos, dim));}
	public static BaseComponent coord(String style, ChunkPos pos, DimensionWrapper dim) {return __coord(style, dim, TextUtils.coord(pos), TextUtils.tp(pos, dim));}
	public static BaseComponent coord(String style, Vec3 pos) {return __coord(style, null, TextUtils.coord(pos), TextUtils.tp(pos));}
	public static BaseComponent coord(String style, Vec3i pos) {return __coord(style, null, TextUtils.coord(pos), TextUtils.tp(pos));}
	public static BaseComponent coord(String style, ChunkPos pos) {return __coord(style, null, TextUtils.coord(pos), TextUtils.tp(pos));}
	public static BaseComponent coord(Vec3 pos, DimensionWrapper dim) {return coord(null, pos, dim);}
	public static BaseComponent coord(Vec3i pos, DimensionWrapper dim) {return coord(null, pos, dim);}
	public static BaseComponent coord(ChunkPos pos, DimensionWrapper dim) {return coord(null, pos, dim);}
	public static BaseComponent coord(Vec3 pos) {return coord(null, pos);}
	public static BaseComponent coord(Vec3i pos) {return coord(null, pos);}
	public static BaseComponent coord(ChunkPos pos) {return coord(null, pos);}

	private static BaseComponent __vector(String style, String displayText, String detailedText)
	{
		return fancy(style, Messenger.s(displayText), Messenger.s(detailedText), Messenger.ClickEvents.suggestCommand(detailedText));
	}
	public static BaseComponent vector(String style, Vec3 vec) {return __vector(style, TextUtils.vector(vec), TextUtils.vector(vec, 6));}
	public static BaseComponent vector(Vec3 vec) {return vector(null, vec);}

	public static BaseComponent entityType(EntityType<?> entityType)
	{
		return (BaseComponent)entityType.getDescription();
	}
	public static BaseComponent entityType(Entity entity)
	{
		return entityType(entity.getType());
	}

	public static BaseComponent entity(String style, Entity entity)
	{
		BaseComponent entityBaseName = entityType(entity);
		BaseComponent entityDisplayName = (BaseComponent)entity.getName();
		BaseComponent hoverText = Messenger.c(
				translator.tr("entity_type", entityBaseName, s(EntityType.getKey(entity.getType()).toString())), newLine(),
				getTeleportHint(entityDisplayName)
		);
		return fancy(style, entityDisplayName, hoverText, Messenger.ClickEvents.suggestCommand(TextUtils.tp(entity)));
	}

	public static BaseComponent entity(Entity entity)
	{
		return entity(null, entity);
	}

	public static BaseComponent attribute(Attribute attribute)
	{
		return tr(
				//#if MC >= 11600
				//$$ attribute.getDescriptionId()
				//#else
				"attribute.name." + attribute.getName()
				//#endif
		);
	}
	//#if MC >= 12005
	//$$ public static MutableText attribute(Holder<EntityAttribute> attribute)
	//$$ {
	//$$ 	return attribute(attribute.value());
	//$$ }
	//#endif

	private static final ImmutableMap<DimensionWrapper, BaseComponent> DIMENSION_NAME = ImmutableMap.of(
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

	public static BaseComponent dimension(DimensionWrapper dim)
	{
		BaseComponent dimText = DIMENSION_NAME.get(dim);
		return dimText != null ? copy(dimText) : Messenger.s(dim.getIdentifierString());
	}

	public static BaseComponent dimensionColored(BaseComponent text, DimensionWrapper dimensionType)
	{
		return formatting(text, getDimensionColor(dimensionType));
	}
	
	public static ChatFormatting getDimensionColor(DimensionWrapper dimensionType)
	{
		if (dimensionType.equals(DimensionWrapper.OVERWORLD))
		{
			return ChatFormatting.DARK_GREEN;
		}
		else if (dimensionType.equals(DimensionWrapper.THE_NETHER))
		{
			return ChatFormatting.DARK_RED;
		}
		else if (dimensionType.equals(DimensionWrapper.THE_END))
		{
			return ChatFormatting.DARK_PURPLE;
		}
		else
		{
			return ChatFormatting.WHITE;
		}
	}

	public static BaseComponent getColoredDimensionSymbol(DimensionWrapper dimensionType)
	{
		String symbol = "?";
		if (dimensionType.equals(DimensionWrapper.OVERWORLD))
		{
			symbol = "O";
		}
		else if (dimensionType.equals(DimensionWrapper.THE_NETHER))
		{
			symbol = "N";
		}
		else if (dimensionType.equals(DimensionWrapper.THE_END))
		{
			symbol = "E";
		}
		else
		{
			String id = dimensionType.getIdentifierString().toUpperCase();
			if (!id.isEmpty())
			{
				symbol = id.substring(0, 1);
			}
		}
		return s(symbol, getDimensionColor(dimensionType));
	}

	public static BaseComponent block(Block block)
	{
		return hover(tr(block.getDescriptionId()), s(TextUtils.block(block)));
	}

	public static BaseComponent block(BlockState blockState)
	{
		List<BaseComponent> hovers = Lists.newArrayList();
		hovers.add(s(TextUtils.block(blockState.getBlock())));
		for (Property<?> property: blockState.getProperties())
		{
			hovers.add(Messenger.c(
					Messenger.s(property.getName()),
					"g : ",
					property(property, blockState.getValue(property))
			));
		}
		return fancy(
				block(blockState.getBlock()),
				join(s("\n"), hovers.toArray(new BaseComponent[0])),
				Messenger.ClickEvents.suggestCommand(TextUtils.block(blockState))
		);
	}

	public static BaseComponent fluid(Fluid fluid)
	{
		return hover(block(fluid.defaultFluidState().createLegacyBlock().getBlock()), s(IdentifierUtils.id(fluid).toString()));
	}

	public static BaseComponent fluid(FluidState fluid)
	{
		return fluid(fluid.getType());
	}

	public static BaseComponent blockEntity(BlockEntity blockEntity)
	{
		ResourceLocation id = IdentifierUtils.id(blockEntity.getType());
		return s(id != null ?
				id.toString() : // vanilla block entity
				blockEntity.getClass().getSimpleName()  // modded block entity, assuming the class name is not obfuscated
		);
	}

	public static BaseComponent item(Item item)
	{
		return tr(item.getDescriptionId());
	}

	public static BaseComponent color(DyeColor color)
	{
		return translator.tr("color." + color.getName().toLowerCase());
	}

	/*
	 * --------------------
	 *    Text Modifiers
	 * --------------------
	 */

	public static BaseComponent hover(BaseComponent text, HoverEvent hoverEvent)
	{
		//#if MC >= 11600
		//$$ style(text, text.getStyle().withHoverEvent(hoverEvent));
		//#else
		text.getStyle().setHoverEvent(hoverEvent);
		//#endif
		return text;
	}

	public static BaseComponent hover(BaseComponent text, BaseComponent hoverText)
	{
		return hover(text, HoverEvents.showText(hoverText));
	}

	public static BaseComponent click(BaseComponent text, ClickEvent clickEvent)
	{
		//#if MC >= 11600
		//$$ style(text, text.getStyle().withClickEvent(clickEvent));
		//#else
		text.getStyle().setClickEvent(clickEvent);
		//#endif
		return text;
	}

	public static BaseComponent formatting(BaseComponent text, ChatFormatting... formattings)
	{
		text.withStyle(formattings);
		return text;
	}

	@SuppressWarnings("RedundantCast")  // in mc1.21.9+, Style is a final class, so we need to cast it to Object first
	public static BaseComponent formatting(BaseComponent text, String carpetStyle)
	{
		Style textStyle = text.getStyle();
		StyleAccessor parsedStyle = (StyleAccessor)(Object)parseCarpetStyle(carpetStyle);

		//#if MC >= 11600
		//$$ textStyle = textStyle.withColor(parsedStyle.getColor$TISCM());
		//$$ textStyle = textStyle.withBold(parsedStyle.getBold$TISCM());
		//$$ textStyle = textStyle.withItalic(parsedStyle.getItalic$TISCM());
		//$$ //#if MC >= 11700
		//$$ //$$ textStyle = textStyle.withUnderlined(parsedStyle.getUnderline$TISCM());
		//$$ //$$ textStyle = textStyle.withStrikethrough(parsedStyle.getStrikethrough$TISCM());
		//$$ //$$ textStyle = textStyle.withObfuscated(parsedStyle.getObfuscated$TISCM());
		//$$ //#else
		//$$ textStyle = ((StyleExt)(Object)textStyle).withUnderline$TISCM(parsedStyle.getUnderline$TISCM());
		//$$ textStyle = ((StyleExt)(Object)textStyle).withStrikethrough$TISCM(parsedStyle.getStrikethrough$TISCM());
		//$$ textStyle = ((StyleExt)(Object)textStyle).withObfuscated$TISCM(parsedStyle.getObfuscated$TISCM());
		//$$ //#endif
		//#else
		textStyle.setColor(parsedStyle.getColor$TISCM());
		textStyle.setBold(parsedStyle.getBold$TISCM());
		textStyle.setItalic(parsedStyle.getItalic$TISCM());
		textStyle.setUnderlined(parsedStyle.getUnderline$TISCM());
		textStyle.setStrikethrough(parsedStyle.getStrikethrough$TISCM());
		textStyle.setObfuscated(parsedStyle.getObfuscated$TISCM());
		//#endif

		return style(text, textStyle);
	}

	public static BaseComponent style(BaseComponent text, Style style)
	{
		text.setStyle(style);
		return text;
	}

	public static BaseComponent copy(BaseComponent text)
	{
		BaseComponent copied;

		//#if MC >= 11900
		//$$ copied = text.copy();
		//#elseif MC >= 11600
		//$$ copied = (BaseComponent)text.copy();
		//#else
		copied = (BaseComponent)text.deepCopy();
		//#endif

		// mc1.16+ doesn't make a copy of args of a TranslatableComponent,
		// so we need to copy that by ourselves
		//#if MC >= 11600
		//$$ if (getTextContent(copied) instanceof TranslatableComponent)
		//$$ {
		//$$ 	TranslatableComponent translatableText = (TranslatableComponent)getTextContent(copied);
		//$$ 	Object[] args = translatableText.getArgs().clone();
		//$$ 	for (int i = 0; i < args.length; i++)
		//$$ 	{
		//$$ 		if (args[i] instanceof BaseComponent)
		//$$ 		{
		//$$ 			args[i] = copy((BaseComponent)args[i]);
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

	private static void __tell(CommandSourceStack source, BaseComponent text, boolean broadcastToOps)
	{
		if (GameUtils.getOverworld(source.getServer()) == null)
		{
			// Broadcasting to OP requires accessing the SEND_COMMAND_FEEDBACK gamerule,
			// and MinecraftServer#getGameRules requires the overworld to be loaded,
			// otherwise a NPE will raise during the getGameRules() call
			// Here's a simple bruteforce patch for that
			broadcastToOps = false;
		}

		// translation logic is handled in carpettisaddition.mixins.translations.ServerPlayerEntityMixin
		source.sendSuccess(
				//#if MC >= 12000
				//$$ () ->
				//#endif
				text, broadcastToOps
		);
	}

	public static void tell(CommandSourceStack source, BaseComponent text, boolean broadcastToOps)
	{
		__tell(source, text, broadcastToOps);
	}
	public static void tell(Player player, BaseComponent text, boolean broadcastToOps)
	{
		if (player instanceof ServerPlayer)
		{
			ServerPlayer serverPlayer = (ServerPlayer)player;  // for mc1.21.2+, where getCommandSource requires being on the server-side
			tell(serverPlayer.createCommandSourceStack(), text, broadcastToOps);
		}
	}
	public static void tell(CommandSourceStack source, BaseComponent text)
	{
		tell(source, text, false);
	}
	public static void tell(Player player, BaseComponent text)
	{
		tell(player, text, false);
	}
	public static void tell(CommandSourceStack source, Iterable<BaseComponent> texts, boolean broadcastToOps)
	{
		texts.forEach(text -> tell(source, text, broadcastToOps));
	}
	public static void tell(Player player, Iterable<BaseComponent> texts, boolean broadcastToOps)
	{
		texts.forEach(text -> tell(player, text, broadcastToOps));
	}
	public static void tell(CommandSourceStack source, Iterable<BaseComponent> texts)
	{
		tell(source, texts, false);
	}
	public static void tell(Player player, Iterable<BaseComponent> texts)
	{
		tell(player, texts, false);
	}

	public static void reminder(Player player, BaseComponent text)
	{
		// translation logic is handled in carpettisaddition.mixins.translations.ServerPlayerEntityMixin
		player.displayClientMessage(text, true);
	}

	public static void sendToConsole(BaseComponent text)
	{
		if (CarpetTISAdditionServer.minecraft_server != null)
		{
			//#if MC >= 11900
			//$$ CarpetTISAdditionServer.minecraft_server.sendSystemMessage(text);
			//#elseif MC >= 11600
			//$$ CarpetTISAdditionServer.minecraft_server.sendMessage(text, Util.NIL_UUID);
			//#else
			CarpetTISAdditionServer.minecraft_server.sendMessage(text);
			//#endif
		}
	}

	public static void broadcast(BaseComponent text)
	{
		sendToConsole(text);
		if (CarpetTISAdditionServer.minecraft_server != null)
		{
			CarpetTISAdditionServer.minecraft_server.getPlayerList().getPlayers().forEach(player -> tell(player, text));
		}
	}

	/*
	 * -------------------------
	 *    Click / Hover Events
	 * -------------------------
	 */

	public static class ClickEvents
	{
		public static ClickEvent runCommand(String command)
		{
			//#if MC >= 12105
			//$$ return new ClickEvent.RunCommand(command);
			//#else
			return new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
			//#endif
		}

		public static ClickEvent suggestCommand(String command)
		{
			//#if MC >= 12105
			//$$ return new ClickEvent.SuggestCommand(command);
			//#else
			return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command);
			//#endif
		}

		public static ClickEvent openUrl(String url)
		{
			//#if MC >= 12105
			//$$ return new ClickEvent.OpenUrl(URI.create(url));
			//#else
			return new ClickEvent(ClickEvent.Action.OPEN_URL, url);
			//#endif
		}

		//#if MC >= 11500
		public static ClickEvent copyToClipBoard(String value)
		{
			//#if MC >= 12105
			//$$ return new ClickEvent.CopyToClipboard(value);
			//#else
			return new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, value);
			//#endif
		}
		//#endif
	}

	public static class HoverEvents
	{
		public static HoverEvent showText(Component text)
		{
			//#if MC >= 12105
			//$$ return new HoverEvent.ShowText(text);
			//#else
			return new HoverEvent(HoverEvent.Action.SHOW_TEXT, text);
			//#endif
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
	public static BaseComponent getSpaceText()
	{
		return translator.tr("language.space");
	}
}
