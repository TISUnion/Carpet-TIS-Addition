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

package carpettisaddition.logging.loggers.microtiming.tickphase;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.AbstractSubStage;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.BaseComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class TickPhase
{
	public final TickStage mainStage;
	@Nullable
	public final String stageDetail;
	@Nullable
	public final AbstractSubStage subStage;
	@Nullable
	public final DimensionWrapper dimensionType;

	private TickPhase(TickStage mainStage, @Nullable String stageDetail, @Nullable AbstractSubStage subStage, @Nullable DimensionWrapper dimensionType)
	{
		this.mainStage = mainStage;
		this.stageDetail = stageDetail;
		this.subStage = subStage;
		this.dimensionType = dimensionType;
	}

	public TickPhase(TickStage mainStage, @Nullable DimensionWrapper dimensionType)
	{
		this(mainStage, null, null, dimensionType);
	}

	public TickPhase withMainStage(TickStage mainStage)
	{
		return new TickPhase(mainStage, null, null, this.dimensionType);
	}

	public TickPhase withDetailed(@Nullable String stageDetail)
	{
		return new TickPhase(this.mainStage, stageDetail, this.subStage, this.dimensionType);
	}

	public TickPhase withSubStage(@Nullable AbstractSubStage subStage)
	{
		return new TickPhase(this.mainStage, this.stageDetail, subStage, this.dimensionType);
	}

	private static BaseComponent tr(String key, Object... args)
	{
		return MicroTimingLoggerManager.TRANSLATOR.tr(key, args);
	}

	public BaseComponent toText(@Nullable String carpetStyle)
	{
		List<Object> stageText = Lists.newArrayList();
		stageText.add(this.mainStage.toText());
		if (this.stageDetail != null)
		{
			stageText.add(Messenger.s("."));
			BaseComponent detailText;
			try
			{
				detailText = Messenger.s(String.valueOf(Integer.parseInt(this.stageDetail)));
			}
			catch (NumberFormatException e)
			{
				detailText = tr("stage_detail." + this.stageDetail.toLowerCase());
			}
			stageText.add(detailText);
		}
		List<Object> hoverTextList = Lists.newArrayList();
		hoverTextList.add(this.subStage != null ? Messenger.c(this.subStage.toText(), "w \n"): Messenger.s(""));
		hoverTextList.add(tr("common.dimension"));
		hoverTextList.add(Messenger.s(": "));
		hoverTextList.add(this.mainStage.isInsideWorld() ? Messenger.dimension(this.dimensionType) : Messenger.s("N/A"));
		return Messenger.fancy(
				carpetStyle,
				Messenger.c(stageText.toArray(new Object[0])),
				Messenger.c(hoverTextList.toArray(new Object[0])),
				this.subStage != null ? this.subStage.getClickEvent() : null
		);
	}

	public BaseComponent toText()
	{
		return this.toText(null);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TickPhase tickPhase = (TickPhase) o;
		return mainStage == tickPhase.mainStage && Objects.equals(stageDetail, tickPhase.stageDetail) && Objects.equals(subStage, tickPhase.subStage) && Objects.equals(dimensionType, tickPhase.dimensionType);
	}
}
