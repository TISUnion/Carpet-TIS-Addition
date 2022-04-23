package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import net.minecraft.entity.Entity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.Vec3d;

public class EntitySubStage extends AbstractSubStage
{
	private final Entity entity;
	private final int order;
	private final Vec3d pos;

	public EntitySubStage(Entity entity, int order)
	{
		this.entity = entity;
		this.order = order;
		this.pos = entity.getPos();
	}

	@Override
	public MutableText toText()
	{
		return Messenger.c(
				MicroTimingLoggerManager.tr("common.entity"), "w : ", this.entity.getDisplayName(), "w \n",
				MicroTimingLoggerManager.tr("common.type"), "w : ", this.entity.getType().getName(), "w \n",
				MicroTimingLoggerManager.tr("common.order"), String.format("w : %d\n", this.order),
				MicroTimingLoggerManager.tr("common.position"), String.format("w : %s", TextUtil.coord(this.pos))
		);
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(this.entity));
	}
}
