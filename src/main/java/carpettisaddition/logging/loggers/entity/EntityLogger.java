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

package carpettisaddition.logging.loggers.entity;

import carpet.logging.LoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import carpettisaddition.utils.deobfuscator.StackTracePrinter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public abstract class EntityLogger<T extends Entity> extends AbstractLogger
{
	private static final EntityLogger<Entity> translator = new EntityLogger<Entity>("entity"){
		@Override
		protected boolean getAcceleratorBoolean()
		{
			throw new RuntimeException();
		}
	};

	public EntityLogger(String loggerName)
	{
		super(loggerName, false);
	}

	@Override
	public String getDefaultLoggingOption()
	{
		return LoggingType.DIE.getName();
	}

	@Override
	public String[] getSuggestedLoggingOption()
	{
		return LoggingType.LOGGING_SUGGESTIONS;
	}

	protected BaseText getNameText(T entity)
	{
		return Messenger.tr(entity.getType().getTranslationKey());
	}

	protected BaseText getNameTextHoverText(T entity)
	{
		return null;
	}

	protected abstract boolean getAcceleratorBoolean();

	private BaseText getNameTextRich(T entity)
	{
		BaseText text = getNameText(entity);
		BaseText hoverText = getNameTextHoverText(entity);
		if (hoverText != null)
		{
			Messenger.hover(text, hoverText);
		}
		return text;
	}

	// e.g. "[12000] " for gt 12000, note the space at the end
	private BaseText getWorldTimeText(World world)
	{
		return Messenger.s(String.format("[%s] ", world.getTime()), "g");
	}

	private void onLoggingEvent(T entity, LoggingType loggingType, Supplier<BaseText[]> supplier)
	{
		if (this.getAcceleratorBoolean() && entity.world != null && !entity.world.isClient())
		{
			LoggerRegistry.getLogger(this.getName()).log((option) -> loggingType.isContainedIn(option) ? supplier.get() : null);
		}
	}

	public void onEntityCreated(T entity)
	{
		this.onLoggingEvent(entity, LoggingType.CREATE, () -> new BaseText[]{Messenger.c(
				this.getWorldTimeText(entity.world),
				translator.tr("created", getNameTextRich(entity)),
				"g  @ ",
				Messenger.coord("w", entity.getPos(), DimensionWrapper.of(entity)),
				"w  ",
				StackTracePrinter.makeSymbol(this.getClass())
		)});
	}

	public void onEntityDespawn(T entity)
	{
		this.onLoggingEvent(entity, LoggingType.DESPAWN, () -> new BaseText[]{Messenger.c(
				this.getWorldTimeText(entity.world),
				translator.tr("despawned", getNameTextRich(entity)),
				"g  @ ",
				Messenger.coord("w", entity.getPos(), DimensionWrapper.of(entity))
		)});
	}

	public void onEntityDied(T entity, DamageSource source, float amount)
	{
		this.onLoggingEvent(entity, LoggingType.DIE, () -> new BaseText[]{Messenger.c(
				this.getWorldTimeText(entity.world),
				Messenger.fancy(
						null,
						Messenger.tr(
								"death.attack." + source.name,
								Messenger.formatting(getNameTextRich(entity), Formatting.WHITE)
						),
						Messenger.c(
								translator.tr("damage_amount", "Damage amount"),
								String.format("w : %.1f", amount)
						),
						null
				),
				"g  @ ",
				Messenger.coord("w", entity.getPos(), DimensionWrapper.of(entity))
		)});
	}

	public enum LoggingType
	{
		DESPAWN,
		DIE,
		CREATE;

		public static final	String[] LOGGING_SUGGESTIONS;

		public String getName()
		{
			return this.name().toLowerCase();
		}

		public boolean isContainedIn(String option)
		{
			return Arrays.asList(option.split(MULTI_OPTION_SEP_REG)).contains(this.getName());
		}

		static
		{
			List<String> list = Arrays.stream(LoggingType.values()).map(LoggingType::getName).collect(Collectors.toList());
			list.add(createCompoundOption(list));
			LOGGING_SUGGESTIONS = list.toArray(new String[0]);
		}
	}
}
