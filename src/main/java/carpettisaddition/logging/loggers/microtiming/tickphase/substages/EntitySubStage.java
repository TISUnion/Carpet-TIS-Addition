package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.utils.TextUtil;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
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
	public BaseText toText()
	{
		return Messenger.c(
				String.format("w %s: ", MicroTimingLoggerManager.tr("Entity")),
				this.entity.getDisplayName(),
				String.format("w \n%s: ", MicroTimingLoggerManager.tr("Type")),
				this.entity.getType().getName(),
				String.format("w \n%s: %d", MicroTimingLoggerManager.tr("Order"), this.order),
				String.format("w \n%s: [%.2f, %.2f, %.2f]", MicroTimingLoggerManager.tr("Position"), this.pos.getX(), this.pos.getY(), this.pos.getZ())
		);
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.getTeleportCommand(this.entity));
	}
}
