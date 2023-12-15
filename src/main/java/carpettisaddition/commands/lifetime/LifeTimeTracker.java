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

package carpettisaddition.commands.lifetime;

import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.commands.AbstractTracker;
import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.interfaces.ServerWorldWithLifeTimeTracker;
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerContext;
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerUtil;
import carpettisaddition.commands.lifetime.utils.SpecificDetailMode;
import carpettisaddition.utils.Messenger;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class LifeTimeTracker extends AbstractTracker
{
	private static boolean attachedServer = false;
	private static final LifeTimeTracker INSTANCE = new LifeTimeTracker();

	private int currentTrackId = 0;

	private final Map<ServerWorld, LifeTimeWorldTracker> trackers = new Reference2ObjectArrayMap<>();

	public LifeTimeTracker()
	{
		super("LifeTime");
	}

	public static LifeTimeTracker getInstance()
	{
		return INSTANCE;
	}

	public LifeTimeWorldTracker getTracker(World world)
	{
		return world instanceof ServerWorld ? this.trackers.get(world) : null;
	}

	public static void attachServer(MinecraftServer minecraftServer)
	{
		attachedServer = true;
		INSTANCE.trackers.clear();
		for (ServerWorld world : minecraftServer.getWorlds())
		{
			INSTANCE.trackers.put(world, ((ServerWorldWithLifeTimeTracker)world).getLifeTimeWorldTracker());
		}
	}

	public static void detachServer()
	{
		attachedServer = false;
		INSTANCE.stop();
	}

	public static boolean isActivated()
	{
		return attachedServer && INSTANCE.isTracking();
	}

	public boolean willTrackEntity(Entity entity, boolean checkTrackId)
	{
		return isActivated() &&
				(!checkTrackId || ((LifetimeTrackerTarget)entity).getTrackId() == this.getCurrentTrackId()) &&
				LifeTimeTrackerUtil.isTrackedEntityClass(entity);
	}

	public Stream<String> getAvailableEntityType()
	{
		if (!isActivated())
		{
			return Stream.empty();
		}
		return this.trackers.values().stream().
				flatMap(
						tracker -> tracker.getDataMap().keySet().
						stream().map(LifeTimeTrackerUtil::getEntityTypeDescriptor)
				).
				distinct();
	}

	public int getCurrentTrackId()
	{
		return this.currentTrackId;
	}

	@Override
	protected void initTracker()
	{
		this.currentTrackId++;
		this.trackers.values().forEach(LifeTimeWorldTracker::initTracker);
	}

	@Override
	protected void printTrackingResult(ServerCommandSource source, boolean realtime)
	{
		LifeTimeTrackerContext.commandSource.set(source);
		try
		{
			long ticks = this.sendTrackedTime(source, realtime);
			int count = this.trackers.values().stream().
					mapToInt(tracker -> tracker.print(source, ticks, null, null)).
					sum();
			if (count == 0)
			{
				Messenger.tell(source, tr("no_result"));
			}
		}
		catch (Exception e)
		{
			CarpetTISAdditionMod.LOGGER.error("Lifetime tracker report failed", e);
		}
	}

	public void sendUnknownEntity(ServerCommandSource source, String entityTypeString)
	{
		Messenger.tell(source, Messenger.formatting(tr("unknown_entity_type", entityTypeString), "r"));
	}

	private void printTrackingResultSpecificImpl(ServerCommandSource source, String entityTypeString, String detailModeString, boolean realtime)
	{
		LifeTimeTrackerContext.commandSource.set(source);

		Optional<EntityType<?>> entityTypeOptional = LifeTimeTrackerUtil.getEntityTypeFromName(entityTypeString);
		if (entityTypeOptional.isPresent())
		{
			SpecificDetailMode detailMode = null;
			if (detailModeString != null)
			{
				try
				{
					detailMode = SpecificDetailMode.fromString(detailModeString);
				}
				catch (IllegalArgumentException e)
				{
					Messenger.tell(source, Messenger.formatting(tr("invalid_detail", detailModeString), "r"));
					return;
				}
			}

			long ticks = this.sendTrackedTime(source, realtime);
			EntityType<?> entityType = entityTypeOptional.get();
			Messenger.tell(source, tr("specific_result", Messenger.entityType(entityType)));
			SpecificDetailMode finalDetailMode = detailMode;
			int count = this.trackers.values().stream().
					mapToInt(tracker -> tracker.print(source, ticks, entityType, finalDetailMode)).
					sum();
			if (count == 0)
			{
				Messenger.tell(source, tr("no_result"));
			}
		}
		else
		{
			this.sendUnknownEntity(source, entityTypeString);
		}
	}

	public int printTrackingResultSpecific(ServerCommandSource source, String entityTypeString, String detailModeString, boolean realtime)
	{
		return this.doWhenTracking(source, () -> this.printTrackingResultSpecificImpl(source, entityTypeString, detailModeString, realtime));
	}


	protected int showHelp(ServerCommandSource source)
	{
		BaseText docLink = Messenger.formatting(tr("help.doc_link"), "t");
		Messenger.tell(source, Messenger.join(
				Messenger.s("\n"),
				Messenger.formatting(this.getTranslatedNameFull(), "wb"),
				tr("help.doc_summary"),
				tr(
						"help.complete_doc_hint",
						Messenger.fancy(
								null,
								Messenger.formatting(tr("help.here"), "ut"),
								docLink,
								new ClickEvent(ClickEvent.Action.OPEN_URL, docLink.getString())
						)
				)
		));
		return 1;
	}
}
