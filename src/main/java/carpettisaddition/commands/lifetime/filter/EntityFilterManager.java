package carpettisaddition.commands.lifetime.filter;

import carpet.utils.Messenger;
import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.mixins.command.lifetime.filter.EntitySelectorAccessor;
import carpettisaddition.translations.TranslatableBase;
import com.google.common.collect.Maps;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Predicate;

public class EntityFilterManager extends TranslatableBase
{
	private static final EntityFilterManager INSTANCE = new EntityFilterManager();
	private static final Predicate<Entity> DEFAULT_FILTER = entity -> true;

	// key null is for global filter
	private final Map<EntityType<?>, Predicate<Entity>> entityFilter = Maps.newHashMap();

	public EntityFilterManager()
	{
		super(LifeTimeTracker.getInstance().getTranslator().getDerivedTranslator("filter"));
	}

	public static EntityFilterManager getInstance()
	{
		return INSTANCE;
	}

	public Predicate<Entity> getFilter(@Nullable EntityType<?> entityType)
	{
		return this.entityFilter.getOrDefault(entityType, DEFAULT_FILTER);
	}

	public boolean test(Entity entity)
	{
		// global filter, then specific filter
		return this.getFilter(null).test(entity) && this.getFilter(entity.getType()).test(entity);
	}

	public void setEntityFilter(ServerCommandSource source, @Nullable EntityType<?> entityType, @Nullable EntitySelector entitySelector)
	{
		BaseText typeName = entityType != null ? (BaseText)entityType.getName() : Messenger.s(this.tr(""));
		if (entitySelector != null)
		{
			if (!entitySelector.includesNonPlayers() || ((EntitySelectorAccessor)entitySelector).getPlayerName() != null)
			{
				Messenger.m(source, Messenger.s(this.tr("unsupported.0", "Unsupported entity filter")));
				Messenger.m(source, Messenger.s(this.tr("unsupported.1", "Please enter a @e style entity selector")));
			}
			else
			{
				EntityFilter entityFilter = new EntityFilter(source, entitySelector);
				this.entityFilter.put(entityType, entityFilter);
				Messenger.m(source, this.advTr(
						"set", "Entity filter of %1$s set to %2$s",
						typeName,
						entityFilter.toText()
				));
			}
		}
		else
		{
			this.entityFilter.put(entityType, DEFAULT_FILTER);
			Messenger.m(source, this.advTr(
					"removed", "Entity filter of %1$s removed",
					typeName
			));
		}
	}

	public void displayFilter(ServerCommandSource source, @NotNull EntityType<?> entityType)
	{
		Predicate<Entity> entityPredicate = this.getFilter(entityType);
		BaseText filterText = entityPredicate instanceof EntityFilter ? ((EntityFilter)entityPredicate).toText() : Messenger.s(this.tr("None"));
		Messenger.m(source, this.advTr(
				"display", "Entity filter of %1$s is %2$s",
				entityType.getName(),
				filterText
		));
	}
}
