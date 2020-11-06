package carpettisaddition.logging.loggers;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.utils.Util;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.BaseText;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;


public abstract class EntityLogger<T extends Entity> extends AbstractLogger
{
	protected final String loggerName;

	private static final EntityLogger<Entity> translator = new EntityLogger<Entity>("entity"){};

	public EntityLogger(String loggerName)
	{
		super(loggerName);
		this.loggerName = loggerName;
	}

	protected BaseText getNameText(T entity)
	{
		return Util.getTranslatedName(entity.getType().getTranslationKey());
	}

	protected BaseText getNameTextHoverText(T entity)
	{
		return null;
	}

	private BaseText getNameTextRich(T entity)
	{
		BaseText text = getNameText(entity);
		BaseText hoverText = getNameTextHoverText(entity);
		if (hoverText != null)
		{
			text.setStyle(text.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)));
		}
		return text;
	}

	public void onEntityDespawn(T entity)
	{
		LoggerRegistry.getLogger(this.loggerName).log((option) ->
		{
			if (!Arrays.asList(option.split(",")).contains(LoggingType.DESPAWN))
			{
				return null;
			}
			return new BaseText[]{Messenger.c(
					String.format("g [%s] ", entity.world.getTime()),
					getNameTextRich(entity),
					String.format("r %s", translator.tr(" despawned")),
					"g  @ ",
					Util.getCoordinateText("w", entity.getPos(), entity.world.getRegistryKey())
			)};
		});
	}

	public void onEntityDied(T entity, DamageSource source, float amount)
	{
		LoggerRegistry.getLogger(this.loggerName).log((option) ->
		{
			if (!Arrays.asList(option.split(MULTI_OPTION_SEP_REG)).contains(LoggingType.DIE))
			{
				return null;
			}
			BaseText itemName = getNameTextRich(entity);
			itemName.setStyle(itemName.getStyle().withColor(Formatting.WHITE));
			TranslatableText deathMessage = Util.getTranslatedName("death.attack." + source.name, itemName);
			deathMessage.setStyle(deathMessage.getStyle().withColor(Formatting.RED));
			deathMessage.setStyle(deathMessage.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Messenger.s(String.format("%s: %.1f", translator.tr("damage_amount", "Damage amount"), amount)))));
			return new BaseText[]{Messenger.c(
					String.format("g [%s] ", entity.world.getTime()),
					deathMessage,
					"g  @ ",
					Util.getCoordinateText("w", entity.getPos(), entity.world.getRegistryKey())
			)};
		});
	}

	public Logger getStandardLogger()
	{
		return ExtensionLoggerRegistry.standardLogger(this.loggerName, EntityLogger.LoggingType.DIE, EntityLogger.LoggingType.loggingSuggest);
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
