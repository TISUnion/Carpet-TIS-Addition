package carpettisaddition.logging.logHelpers;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.utils.Util;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.BaseText;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class itemLoggerHelper extends AbstractLoggerHelper
{
	static
	{
		loggerName = "itemLogger";
	}

	public static void onItemDespawn(ItemEntity item)
	{
		LoggerRegistry.getLogger("item").log((option) ->
		{
			if (!Arrays.asList(option.split(",")).contains(LoggingType.DESPAWN))
			{
				return null;
			}
			String dimensionName = item.world.dimension.getType().toString();
			return new BaseText[]{Messenger.c(
					String.format("g [%s] ", item.world.getTime()),
					Util.getTranslatedName(item.getStack().getItem().getTranslationKey()),
					String.format("r  %s", tr("despawned")),
					"g  @ ",
					Util.getCoordinateText("w", item.getPos(), item.world.getDimension())
			)};
		});
	}
	public static void onItemDie(ItemEntity item, DamageSource source, float amount)
	{
		LoggerRegistry.getLogger("item").log((option) ->
		{
			if (!Arrays.asList(option.split(",")).contains(LoggingType.DIE))
			{
				return null;
			}
			TranslatableText itemName = Util.getTranslatedName(item.getStack().getItem().getTranslationKey());
			itemName.getStyle().setColor(Formatting.WHITE);
			TranslatableText deathMessage = Util.getTranslatedName("death.attack." + source.name, itemName);
			deathMessage.getStyle()
					.setColor(Formatting.RED)
					.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Messenger.s(String.format("%s: %.1f", tr("damage_amount", "Damage amount"), amount))));
			return new BaseText[]{Messenger.c(
					String.format("g [%s] ", item.world.getTime()),
					deathMessage,
					"g  @ ",
					Util.getCoordinateText("w", item.getPos(), item.world.getDimension())
			)};
		});
	}

	public static class LoggingType
	{
		public static final String DESPAWN = "despawn";
		public static final String DIE = "die";
		public static final List<String> typeList;
		public static final	String[] loggingSuggest;

		static
		{
			typeList = Lists.newArrayList();
			for (Field field : LoggingType.class.getFields())
			{
				if (field.getType() == String.class)
				{
					try
					{
						typeList.add((String) field.get(null));
					}
					catch (IllegalAccessException e)
					{
						throw new IllegalStateException(e);
					}
				}
			}
			List<String> list = Lists.newArrayList(typeList);
			list.add(Joiner.on(",").join(typeList));
			loggingSuggest = list.toArray(new String[0]);
		}
	}
}
