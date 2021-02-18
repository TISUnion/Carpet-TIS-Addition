package carpettisaddition.commands.lifetime.trackeddata;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;

public class ExperienceOrbTrackedData extends ExtraCountTrackedData
{
	@Override
	protected long getExtraCount(Entity entity)
	{
		return entity instanceof ExperienceOrbEntity ? ((ExperienceOrbEntity)entity).getExperienceAmount() : 0L;
	}

	@Override
	protected String getCountDisplayString()
	{
		return this.tr("Experience Amount");
	}
}
