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

package carpettisaddition.commands.lifetime.filter;

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerUtil;
import carpettisaddition.mixins.utils.entityfilter.EntitySelectorAccessor;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.entityfilter.EntityFilter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class EntityFilterManager extends TranslationContext
{
	private static final EntityFilterManager INSTANCE = new EntityFilterManager();
	private static final Predicate<Entity> DEFAULT_FILTER = entity -> true;

	// key null is for global filter
	private final Map<EntityType<?>, Predicate<Entity>> entityFilters = Maps.newLinkedHashMap();

	public EntityFilterManager()
	{
		super(LifeTimeTracker.getInstance().getTranslator().getDerivedTranslator("filter"));
	}

	public static EntityFilterManager getInstance()
	{
		return INSTANCE;
	}

	@NotNull
	public Predicate<Entity> getFilter(@Nullable EntityType<?> entityType)
	{
		return this.entityFilters.getOrDefault(entityType, DEFAULT_FILTER);
	}

	/**
	 * Test right before recording entity spawning
	 * So the entity should be fully initialized
	 */
	public boolean test(Entity entity)
	{
		// global filter, then specific filter
		return this.getFilter(null).test(entity) && this.getFilter(entity.getType()).test(entity);
	}

	public void setEntityFilter(CommandSourceStack source, @Nullable EntityType<?> entityType, @Nullable List<EntitySelector> selectors)
	{
		BaseComponent typeName = this.getEntityTypeText(entityType);
		if (selectors != null && !selectors.isEmpty())
		{
			List<EntityFilter> entityFilters = Lists.newArrayList();
			for (int i = 0; i < selectors.size(); i++)
			{
				EntitySelector entitySelector = selectors.get(i);
				if (!entitySelector.includesEntities() || ((EntitySelectorAccessor)entitySelector).getPlayerName() != null)
				{
					Messenger.tell(source, tr("unsupported.0", i + 1));
					Messenger.tell(source, tr("unsupported.1"));
					return;
				}
				entityFilters.add(new EntityFilter(source, entitySelector));
			}

			LifetimeEntityFilter entityFilter = new LifetimeEntityFilter(entityFilters);
			this.entityFilters.put(entityType, entityFilter);
			Messenger.tell(source, tr(
					"filter_set",
					typeName,
					entityFilter.toText()
			));
		}
		else
		{
			this.entityFilters.remove(entityType);
			Messenger.tell(source, tr(
					"filter_removed",
					typeName
			));
		}
	}

	public BaseComponent getEntityFilterText(@Nullable EntityType<?> entityType)
	{
		Predicate<Entity> entityPredicate = this.getFilter(entityType);
		if (entityPredicate == DEFAULT_FILTER)
		{
			return tr("none");
		}
		else if (entityPredicate instanceof EntityFilter)
		{
			return ((EntityFilter)entityPredicate).toText();
		}
		else if (entityPredicate instanceof LifetimeEntityFilter)
		{
			return ((LifetimeEntityFilter)entityPredicate).toText();
		}
		// unexpected predicate type
		return Messenger.hover(
				Messenger.s("?", ChatFormatting.RED),
				Messenger.s(entityPredicate.getClass().getSimpleName())
		);
	}

	public BaseComponent getEntityTypeText(@Nullable EntityType<?> entityType)
	{
		return entityType != null ? Messenger.entityType(entityType) : tr("global");
	}

	public void displayFilter(CommandSourceStack source, @Nullable EntityType<?> entityType)
	{
		Messenger.tell(source, tr(
				"display",
				this.getEntityTypeText(entityType),
				this.getEntityFilterText(entityType)
		));
	}

	public int displayAllFilters(CommandSourceStack source)
	{
		Messenger.tell(source, tr("display_total", this.entityFilters.size()));
		this.entityFilters.keySet().forEach(entityType -> Messenger.tell(
				source,
				Messenger.c(
						"f - ",
						this.getEntityTypeText(entityType),
						"g : ",
						this.getEntityFilterText(entityType),
						"w  ",
						Messenger.fancy(
								null,
								Messenger.s("[×]", "r"),
								tr("click_to_clear"),
								Messenger.ClickEvents.runCommand(String.format(
										"/%s filter %s clear",
										LifeTimeTracker.getInstance().getCommandPrefix(),
										entityType != null ? LifeTimeTrackerUtil.getEntityTypeDescriptor(entityType) : "global"
								))
						)
				)
		));
		return 1;
	}
}
