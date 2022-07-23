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
import net.minecraft.item.Item;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.state.property.Property;
import net.minecraft.text.*;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//#if MC >= 11600 && MC < 11900
//$$ import net.minecraft.util.Util;
//#endif

//#if MC < 11500
//$$ import carpettisaddition.mixins.carpet.access.MessengerInvoker;
//#endif

public class Messenger
{
	private static final Translator translator = new Translator("util");

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
	public static BaseText s(Object text, Formatting textFormatting)
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

	public static BaseText join(BaseText joiner, BaseText... items)
	{
		BaseText text = s("");
		for (int i = 0; i < items.length; i++)
		{
			if (i > 0)
			{
				text.append(joiner);
			}
			text.append(items[i]);
		}
		return text;
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
		return hover(tr(block.getTranslationKey()), s(IdentifierUtil.id(block).toString()));
	}

	public static BaseText block(BlockState blockState)
	{
		List<String> hovers = Lists.newArrayList();
		hovers.add(IdentifierUtil.id(blockState.getBlock()).toString());
		for (Property<?> property: blockState.getProperties())
		{
			hovers.add(String.format("%s=%s", property.getName(), blockState.get(property)));
		}
		return hover(block(blockState.getBlock()), s(Joiner.on('\n').join(hovers)));
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
		//#if MC >= 11900
		//$$ return text.copy();
		//#elseif MC >= 11600
		//$$ return (BaseText)text.shallowCopy();
		//#else
		return (BaseText)text.deepCopy();
		//#endif
	}

	/*
	 * ------------------
	 *    Text Senders
	 * ------------------
	 */

	private static void __tell(ServerCommandSource source, BaseText text, boolean broadcastToOps)
	{
		// translation logic is handled in carpettisaddition.mixins.translations.ServerPlayerEntityMixin
		source.sendFeedback(text, broadcastToOps);
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

	public static void broadcast(BaseText text)
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
