package carpettisaddition.commands.lifetime.trackeddata;

import carpettisaddition.mixins.command.lifetime.data.ExperienceOrbEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.text.BaseText;

public class ExperienceOrbTrackedData extends ExtraCountTrackedData
{
	@Override
	protected long getExtraCount(Entity entity)
	{
		return entity instanceof ExperienceOrbEntity ? (long)((ExperienceOrbEntity)entity).getExperienceAmount() * ((ExperienceOrbEntityAccessor)entity).getPickingCount() : 0L;
	}

	@Override
	protected BaseText getCountDisplayText()
	{
		return tr("experience_amount");
	}
}
