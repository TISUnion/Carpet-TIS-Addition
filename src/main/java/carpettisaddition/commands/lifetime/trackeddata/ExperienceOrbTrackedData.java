package carpettisaddition.commands.lifetime.trackeddata;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.text.BaseText;

//#if MC >= 11700
//$$ import carpettisaddition.mixins.command.lifetime.data.ExperienceOrbEntityAccessor;
//#endif

public class ExperienceOrbTrackedData extends ExtraCountTrackedData
{
	@Override
	protected long getExtraCount(Entity entity)
	{
		if (entity instanceof ExperienceOrbEntity)
		{
			return
					((ExperienceOrbEntity)entity).getExperienceAmount()
					//#if MC >= 11700
					//$$ * ((ExperienceOrbEntityAccessor)entity).getPickingCount()
					//#endif
					;
		}
		return 0L;
	}

	@Override
	protected BaseText getCountDisplayText()
	{
		return tr("experience_amount");
	}
}
