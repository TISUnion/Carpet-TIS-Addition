package carpettisaddition.logging.loggers;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.utils.Util;
import com.google.common.base.Joiner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.BaseText;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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
			text.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
		}
		return text;
	}

	public void onEntityDespawn(T entity)
	{
		LoggerRegistry.getLogger(this.loggerName).log((option) ->
		{
			if (!LoggingType.DESPAWN.isContainedIn(option))
			{
				return null;
			}
			return new BaseText[]{Messenger.c(
					String.format("g [%s] ", entity.world.getTime()),
					getNameTextRich(entity),
					String.format("r %s", translator.tr(" despawned")),
					"g  @ ",
					Util.getCoordinateText("w", entity.getPos(), entity.world.getDimension())
			)};
		});
	}

	public void onEntityDied(T entity, DamageSource source, float amount)
	{
		LoggerRegistry.getLogger(this.loggerName).log((option) ->
		{
			if (!LoggingType.DIE.isContainedIn(option))
			{
				return null;
			}
			BaseText itemName = getNameTextRich(entity);
			itemName.setStyle(itemName.getStyle().setColor(Formatting.WHITE));
			TranslatableText deathMessage = Util.getTranslatedName("death.attack." + source.name, itemName);
			deathMessage.setStyle(deathMessage.getStyle().setColor(Formatting.RED));
			deathMessage.setStyle(deathMessage.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Messenger.s(String.format("%s: %.1f", translator.tr("damage_amount", "Damage amount"), amount)))));
			return new BaseText[]{Messenger.c(
					String.format("g [%s] ", entity.world.getTime()),
					deathMessage,
					"g  @ ",
					Util.getCoordinateText("w", entity.getPos(), entity.world.getDimension())
			)};
		});
	}

	public Logger getStandardLogger()
	{
		return ExtensionLoggerRegistry.standardLogger(this.loggerName, LoggingType.DIE.getName(), LoggingType.LOGGING_SUGGESTIONS);
	}

	public enum LoggingType
	{
		DESPAWN("despawn"),
		DIE("die");
		private final String name;
		public static final	String[] LOGGING_SUGGESTIONS;

		LoggingType(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}

		public boolean isContainedIn(String option)
		{
			return Arrays.asList(option.split(MULTI_OPTION_SEP_REG)).contains(this.getName());
		}

		static
		{
			List<String> list = Arrays.stream(LoggingType.values()).map(LoggingType::getName).collect(Collectors.toList());
			list.add(Joiner.on(",").join(list));
			LOGGING_SUGGESTIONS = list.toArray(new String[0]);
		}
	}
}
