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

package carpettisaddition.logging.loggers.raid;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.entity.raid.Raid;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;


public class RaidLogger extends AbstractLogger
{
	public static final String NAME = "raid";
	private static final RaidLogger instance = new RaidLogger();

	public RaidLogger()
	{
		super(NAME, true);
	}

	public static RaidLogger getInstance()
	{
		return instance;
	}

	public void onRaidCreated(Raid raid)
	{
		if (!TISAdditionLoggerRegistry.__raid)
		{
			return;
		}
		this.log(() -> {
			return new BaseText[]{Messenger.c(
					tr("created", raid.getRaidId()),
					"g  @ ",
					Messenger.coord("w", raid.getCenter(), DimensionWrapper.of(raid.getWorld()))
			)};
		});
	}

	public void onRaidInvalidated(Raid raid, InvalidateReason reason)
	{
		if (!TISAdditionLoggerRegistry.__raid)
		{
			return;
		}
		this.log(() -> {
			return new BaseText[]{Messenger.c(
					tr("invalidated", raid.getRaidId(), reason.toText())
			)};
		});
	}

	public void onBadOmenLevelIncreased(Raid raid, int badOmenLevel)
	{
		if (!TISAdditionLoggerRegistry.__raid)
		{
			return;
		}
		this.log(() -> {
			return new BaseText[]{Messenger.c(
					tr("bad_omen_level_increased", raid.getRaidId(), badOmenLevel)
			)};
		});
	}

	public void onCenterMoved(Raid raid, BlockPos pos)
	{
		if (!TISAdditionLoggerRegistry.__raid)
		{
			return;
		}
		this.log(() -> {
			return new BaseText[]{Messenger.c(
					tr("center_moved", raid.getRaidId(), Messenger.coord(pos, DimensionWrapper.of(raid.getWorld())))
			)};
		});
	}

	public enum InvalidateReason
	{
		DIFFICULTY_PEACEFUL,
		GAMERULE_DISABLE,
		POI_REMOVED_BEFORE_SPAWN,
		TIME_OUT,
		RAIDER_CANNOT_SPAWN,
		RAID_VICTORY,
		RAID_DEFEAT;

		private static final Translator TRANSLATOR = getInstance().getTranslator().getDerivedTranslator("raid_invalidate_reason");

		public String getName()
		{
			return this.name().toLowerCase();
		}

		public BaseText toText()
		{
			return TRANSLATOR.tr(getName());
		}
	}
}
