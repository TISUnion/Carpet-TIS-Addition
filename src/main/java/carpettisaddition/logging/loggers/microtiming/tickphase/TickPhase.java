package carpettisaddition.logging.loggers.microtiming.tickphase;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.AbstractSubStage;
import carpettisaddition.logging.loggers.microtiming.utils.ToTextAble;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import net.minecraft.text.BaseText;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager.TRANSLATOR;

public class TickPhase implements ToTextAble
{
	public final TickStage mainStage;
	@Nullable
	public final String stageDetail;
	@Nullable
	public final AbstractSubStage subStage;
	@Nullable
	public final DimensionType dimensionType;

	private TickPhase(TickStage mainStage, @Nullable String stageDetail, @Nullable AbstractSubStage subStage, @Nullable DimensionType dimensionType)
	{
		this.mainStage = mainStage;
		this.stageDetail = stageDetail;
		this.subStage = subStage;
		this.dimensionType = dimensionType;
	}

	public TickPhase(TickStage mainStage, @Nullable DimensionType dimensionType)
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

	public BaseText toText()
	{
		List<Object> stageText = Lists.newArrayList();
		stageText.add("y  " + this.mainStage.tr());
		if (this.stageDetail != null)
		{
			stageText.add("y ." + TRANSLATOR.tr("stage_detail." + this.stageDetail, this.stageDetail));
		}
		List<Object> hoverTextList = Lists.newArrayList();
		hoverTextList.add(this.subStage != null ? Messenger.c(this.subStage.toText(), "w \n"): Messenger.s(""));
		hoverTextList.add(String.format("w %s: ", TRANSLATOR.tr("Dimension")));
		hoverTextList.add(this.mainStage.isInsideWorld() ? TextUtil.getDimensionNameText(this.dimensionType) : "w N/A");
		return TextUtil.getFancyText(
				null,
				Messenger.c(stageText.toArray(new Object[0])),
				Messenger.c(hoverTextList.toArray(new Object[0])),
				this.subStage != null ? this.subStage.getClickEvent() : null
		);
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
