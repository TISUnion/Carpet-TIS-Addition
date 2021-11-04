package carpettisaddition.commands.lifetime;

import carpettisaddition.commands.lifetime.filter.EntityFilterManager;
import carpettisaddition.commands.lifetime.removal.RemovalReason;
import carpettisaddition.commands.lifetime.spawning.SpawningReason;
import carpettisaddition.commands.lifetime.trackeddata.BasicTrackedData;
import carpettisaddition.commands.lifetime.trackeddata.ExperienceOrbTrackedData;
import carpettisaddition.commands.lifetime.trackeddata.ItemTrackedData;
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerUtil;
import carpettisaddition.commands.lifetime.utils.SpecificDetailMode;
import carpettisaddition.translations.TranslatableBase;
import carpettisaddition.utils.DimensionWrapper;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.Formatting;

import java.util.*;

public class LifeTimeWorldTracker extends TranslatableBase
{
	private final ServerWorld world;
	private final Map<EntityType<?>, BasicTrackedData> dataMap = Maps.newHashMap();
	// a counter which accumulates when spawning stage occurs
	// it's used to determine life time
	private long spawnStageCounter;

	public LifeTimeWorldTracker(ServerWorld world)
	{
		super(LifeTimeTracker.getInstance().getTranslator());
		this.world = world;
	}

	public Map<EntityType<?>, BasicTrackedData> getDataMap()
	{
		return this.dataMap;
	}

	public void initTracker()
	{
		this.dataMap.clear();
	}

	private Optional<BasicTrackedData> getTrackedData(Entity entity)
	{
		if (LifeTimeTracker.getInstance().willTrackEntity(entity))
		{
			return Optional.of(this.dataMap.computeIfAbsent(entity.getType(), (e -> {
				if (entity instanceof ItemEntity)
				{
					return new ItemTrackedData();
				}
				if (entity instanceof ExperienceOrbEntity)
				{
					return new ExperienceOrbTrackedData();
				}
				return new BasicTrackedData();
			})));
		}
		return Optional.empty();
	}

	public void onEntitySpawn(Entity entity, SpawningReason reason)
	{
		this.getTrackedData(entity).ifPresent(data -> data.updateSpawning(entity, reason));
	}

	public void onEntityRemove(Entity entity, RemovalReason reason)
	{
		this.getTrackedData(entity).ifPresent(data -> data.updateRemoval(entity, reason));
	}

	public void increaseSpawnStageCounter()
	{
		this.spawnStageCounter++;
	}

	public long getSpawnStageCounter()
	{
		return this.spawnStageCounter;
	}

	private List<BaseText> addIfEmpty(List<BaseText> list, BaseText text)
	{
		if (list.isEmpty())
		{
			list.add(text);
		}
		return list;
	}

	protected int print(ServerCommandSource source, long ticks, EntityType<?> specificType, SpecificDetailMode detailMode)
	{
		// existence check
		BasicTrackedData specificData = this.dataMap.get(specificType);
		if (this.dataMap.isEmpty() || (specificType != null && specificData == null) || !LifeTimeTracker.getInstance().isTracking())
		{
			return 0;
		}

		// dimension name header
		// Overworld (minecraft:overworld)
		List<BaseText> result = Lists.newArrayList();
		result.add(Messenger.s(" "));
		result.add(Messenger.c(
				Messenger.formatting(Messenger.dimension(DimensionWrapper.of(this.world)), Formatting.BOLD, Formatting.GOLD),
				String.format("g  (%s)", DimensionWrapper.of(this.world).getIdentifier())
		));

		if (specificType == null)
		{
			this.printAll(ticks, result);
		}
		else
		{
			this.printSpecific(ticks, specificType, specificData, detailMode, result);
		}
		Messenger.tell(source, result);
		return 1;
	}

	private void printAll(long ticks, List<BaseText> result)
	{
		// sorted by spawn count
		// will being sorting by avg life time better?
		this.dataMap.entrySet().stream().
				sorted(Collections.reverseOrder(Comparator.comparingLong(a -> a.getValue().getSpawningCount()))).
				forEach((entry) -> {
					EntityType<?> entityType = entry.getKey();
					BasicTrackedData data = entry.getValue();
					List<BaseText> spawningReasons = data.getSpawningReasonsTexts(ticks, true);
					List<BaseText> removalReasons = data.getRemovalReasonsTexts(ticks, true);
					String currentCommandBase = String.format("/%s %s", LifeTimeTracker.getInstance().getCommandPrefix(), LifeTimeTrackerUtil.getEntityTypeDescriptor(entityType));
					// [Creeper] S/R: 21/8, L: 145/145/145.00 (gt)
					result.add(Messenger.c(
							"g - [",
							Messenger.fancy(
									null,
									(BaseText)entityType.getName(),
									Messenger.c(
											tr("filter_info_header"), "w : ",
											EntityFilterManager.getInstance().getEntityFilterText(entityType),
											"g  / [G] ",
											EntityFilterManager.getInstance().getEntityFilterText(null),
											"w \n",
											tr("detail_hint")
									),
									new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, currentCommandBase)
							),
							"g ] ",
							Messenger.fancy(
									null,
									Messenger.c("e S", "g /", "r R"),
									Messenger.c(
											Messenger.formatting(tr("spawn_count"), "e"),
											"g  / ",
											Messenger.formatting(tr("removal_count"), "r")
									),
									null
							),
							"g : ",
							Messenger.fancy(
									null,
									Messenger.c("e " + data.getSpawningCount()),
									Messenger.c(
											data.getSpawningCountText(ticks),
											"w " + (spawningReasons.isEmpty() ? "" : "\n"),
											Messenger.c(spawningReasons.toArray(new Object[0]))
									),
									new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("%s %s", currentCommandBase, SpecificDetailMode.SPAWNING))
							),
							"g /",
							Messenger.fancy(
									null,
									Messenger.c("r " + data.getRemovalCount()),
									Messenger.c(
											data.getRemovalCountText(ticks),
											"w " + (removalReasons.isEmpty() ? "" : "\n"),
											Messenger.c(removalReasons.toArray(new Object[0]))
									),
									new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("%s %s", currentCommandBase, SpecificDetailMode.REMOVAL))
							),
							"g , ",
							Messenger.fancy(
									null,
									Messenger.c(
											"q L", "g : ",
											data.lifeTimeStatistic.getCompressedResult(true)
									),
									Messenger.c(
											Messenger.formatting(tr("life_time_overview"), "q"), "w \n",
											data.lifeTimeStatistic.getResult("", true)
									),
									new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("%s %s", currentCommandBase, SpecificDetailMode.LIFE_TIME))
							)
					));
				});
	}

	private void printSpecific(long ticks, EntityType<?> specificType, BasicTrackedData specificData, SpecificDetailMode detailMode, List<BaseText> result)
	{
		result.add(Messenger.c(
				Messenger.formatting(Messenger.c(tr("filter_info_header"), Messenger.s(": ")), "c"),
				EntityFilterManager.getInstance().getEntityFilterText(specificType),
				"g  / ",
				Messenger.fancy("g", Messenger.s("[G]"), EntityFilterManager.getInstance().tr("global"), null),
				"g  ",
				EntityFilterManager.getInstance().getEntityFilterText(null)
		));
		boolean showLifeTime = detailMode == null || detailMode == SpecificDetailMode.LIFE_TIME;
		boolean showSpawning = detailMode == null || detailMode == SpecificDetailMode.SPAWNING;
		boolean showRemoval = detailMode == null || detailMode == SpecificDetailMode.REMOVAL;
		if (showSpawning)
		{
			result.add(specificData.getSpawningCountText(ticks));
		}
		if (showRemoval)
		{
			result.add(specificData.getRemovalCountText(ticks));
		}
		if (showLifeTime)
		{
			result.add(Messenger.fancy(
					"q",
					tr("life_time_overview"),
					tr("life_time_explain"),
					null
			));
			result.add(specificData.lifeTimeStatistic.getResult("", false));
		}
		if (showSpawning)
		{
			result.add(Messenger.formatting(tr("reasons_for_spawning"), "e"));
			result.addAll(this.addIfEmpty(specificData.getSpawningReasonsTexts(ticks, false), Messenger.s("  N/A", "g")));
		}
		if (showRemoval)
		{
			result.add(Messenger.formatting(tr("reasons_for_removal"), "r"));
			result.addAll(this.addIfEmpty(specificData.getRemovalReasonsTexts(ticks, false), Messenger.s("  N/A", "g")));
		}
	}
}
