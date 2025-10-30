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

package carpettisaddition.logging.loggers.movement;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingAccess;
import carpettisaddition.utils.EntityUtils;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.WorldUtils;
import carpettisaddition.utils.compat.DimensionWrapper;
import carpettisaddition.utils.entityfilter.EntityFilter;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MovementLogger extends AbstractLogger
{
	public static final String NAME = "movement";
	private static final String NON_ZERO_HEADER = "non_zero:";
	private static final MovementLogger INSTANCE = new MovementLogger();

	private MovementLogger()
	{
		super(NAME, false);
	}

	public static MovementLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public @Nullable String[] getSuggestedLoggingOption()
	{
		return new String[]{NON_ZERO_HEADER + "@a[distance=..10]", "@s", NON_ZERO_HEADER + "@e[type=creeper,distance=..5]", "Steve"};
	}

	public static boolean isLoggerActivated()
	{
		return TISAdditionLoggerRegistry.__movement;
	}

	public void create(Entity entity, MoverType movementType, Vec3 originalMovement)
	{
		if (isLoggerActivated() && EntityUtils.getEntityWorld(entity) instanceof ServerLevel)
		{
			MovementLoggerTarget target = (MovementLoggerTarget)entity;
			if (!target.getMovementTracker().isPresent())
			{
				target.setMovementTracker(new Tracker(entity, movementType, originalMovement));
			}
		}
	}

	public void finalize(Entity entity)
	{
		if (isLoggerActivated())
		{
			MovementLoggerTarget target = (MovementLoggerTarget) entity;
			target.getMovementTracker().ifPresent(Tracker::report);
			target.setMovementTracker(null);
		}
	}

	public class Tracker
	{
		private static final double MIN_DIFFERENCE = 1e-12;

		private final Entity entity;
		private final Vec3 originalPos;
		private final ServerLevel world;
		private final MoverType movementType;
		private final Vec3 originalMovement;
		private Vec3 currentMovement;
		private final List<ModificationRecord> modifications = Lists.newArrayList();

		public Tracker(Entity entity, MoverType movementType, Vec3 originalMovement)
		{
			this.entity = entity;
			this.originalPos = entity.position();
			this.world = (ServerLevel)EntityUtils.getEntityWorld(entity);
			this.movementType = movementType;
			this.originalMovement = originalMovement;
			this.currentMovement = originalMovement;
		}

		public void recordModification(MovementModification modification, Vec3 newMovement)
		{
			if (this.currentMovement.subtract(newMovement).length() >= MIN_DIFFERENCE)
			{
				this.modifications.add(new ModificationRecord(this.currentMovement, newMovement, modification));
				this.currentMovement = newMovement;
			}
		}

		@SuppressWarnings("ConstantConditions")
		public boolean shouldReportFor(Player player, String option)
		{
			if (option.isEmpty())
			{
				// by default (option string is empty), log the subscriber itself only
				option = player.getGameProfile().getName();
			}

			boolean ok = true;
			if (option.startsWith(NON_ZERO_HEADER))
			{
				ok &= this.currentMovement.lengthSqr() > 0;
				option = option.substring(NON_ZERO_HEADER.length());
			}
			ok &= EntityFilter.createOptional(player, option).
					map(filter -> filter.test(this.entity)).
					orElse(false);
			return ok;
		}

		public void report()
		{
			MovementLogger.this.log((option, player) -> {
				if (!this.shouldReportFor(player, option))
				{
					return null;
				}

				BaseComponent entityName = Messenger.entity("b", this.entity);
				List<BaseComponent> lines = Lists.newArrayList();

				lines.add(Messenger.s(""));
				lines.add(Messenger.c(
						tr("header", entityName, Messenger.vector("y", this.originalMovement)),
						"g  @ ",
						Messenger.coord("g", this.originalPos, DimensionWrapper.of(this.world))
				));
				lines.add(tr(
						"header_details",
						Messenger.formatting(tr("movement_type." + this.movementType.name().toLowerCase(), ""), "c"),
						Messenger.c(
								Messenger.s(WorldUtils.getWorldTime(this.world), "q"),
								"w  ",
								MicroTimingAccess.getTickPhase(this.world).toText("q")
						)
				));

				for (ModificationRecord record : this.modifications)
				{
					BaseComponent delta = Messenger.vector(record.newMovement.subtract(record.oldMovement));
					lines.add(Messenger.c(
							"g  - ",
							Messenger.vector("y", record.oldMovement),
							Messenger.fancy(
									"g",
									Messenger.s(" -> "),
									Messenger.c(tr("delta"), "w : ", delta),
									delta.getStyle().getClickEvent()
							),
							Messenger.vector("d", record.newMovement),
							tr("due_to", record.reason.toText())
					));
				}

				lines.add(Messenger.c(
						tr("footer", entityName, Messenger.vector("d", this.currentMovement)),
						"g  @ ",
						Messenger.coord("g", this.entity.position(), DimensionWrapper.of(this.world))
				));

				return lines.toArray(new BaseComponent[0]);
			});
		}
	}

	private static class ModificationRecord
	{
		public final MovementModification reason;
		public final Vec3 oldMovement;
		public final Vec3 newMovement;

		public ModificationRecord(Vec3 oldMovement, Vec3 newMovement, MovementModification reason)
		{
			this.reason = reason;
			this.oldMovement = oldMovement;
			this.newMovement = newMovement;
		}
	}
}
