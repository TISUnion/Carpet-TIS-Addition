package carpettisaddition.commands.lifetime.filter;

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerUtil;
import carpettisaddition.mixins.command.lifetime.filter.EntitySelectorAccessor;
import carpettisaddition.translations.TranslatableBase;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Maps;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Predicate;

public class EntityFilterManager extends TranslatableBase
{
	private static final EntityFilterManager INSTANCE = new EntityFilterManager();
	private static final Predicate<Entity> DEFAULT_FILTER = entity -> true;

	// key null is for global filter
	private final Map<EntityType<?>, Predicate<Entity>> entityFilter = Maps.newLinkedHashMap();

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

	/**
	 * Test right before recording entity spawning
	 * So the entity should be fully initialized
	 */
	public boolean test(Entity entity)
	{
		// global filter, then specific filter
		return this.getFilter(null).test(entity) && this.getFilter(entity.getType()).test(entity);
	}

	public void setEntityFilter(ServerCommandSource source, @Nullable EntityType<?> entityType, @Nullable EntitySelector entitySelector)
	{
		BaseText typeName = entityType != null ? (BaseText)entityType.getName() : tr("global");
		if (entitySelector != null)
		{
			if (!entitySelector.includesNonPlayers() || ((EntitySelectorAccessor)entitySelector).getPlayerName() != null)
			{
				Messenger.tell(source, tr("unsupported.0"));
				Messenger.tell(source, tr("unsupported.1"));
			}
			else
			{
				EntityFilter entityFilter = new EntityFilter(source, entitySelector);
				this.entityFilter.put(entityType, entityFilter);
				Messenger.tell(source, tr(
						"filter_set",
						typeName,
						entityFilter.toText()
				));
			}
		}
		else
		{
			this.entityFilter.remove(entityType);
			Messenger.tell(source, tr(
					"filter_removed",
					typeName
			));
		}
	}

	public BaseText getEntityFilterText(@Nullable EntityType<?> entityType)
	{
		Predicate<Entity> entityPredicate = this.getFilter(entityType);
		return entityPredicate instanceof EntityFilter ? ((EntityFilter)entityPredicate).toText() : tr("none");
	}

	public BaseText getEntityTypeText(@Nullable EntityType<?> entityType)
	{
		return entityType != null ? (BaseText)entityType.getName() : tr("global");
	}

	public void displayFilter(ServerCommandSource source, @Nullable EntityType<?> entityType)
	{
		Messenger.tell(source, tr(
				"display",
				this.getEntityTypeText(entityType),
				this.getEntityFilterText(entityType)
		));
	}

	public int displayAllFilters(ServerCommandSource source)
	{
		Messenger.tell(source, tr("display_total", this.entityFilter.size()));
		this.entityFilter.keySet().forEach(entityType -> Messenger.tell(
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
								new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format(
										"/%s filter %s clear",
										LifeTimeTracker.getInstance().getCommandPrefix(),
										entityType != null ? LifeTimeTrackerUtil.getEntityTypeDescriptor(entityType) : tr("global")
								))
						)
				)
		));
		return 1;
	}
}
