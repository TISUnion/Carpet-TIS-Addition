package carpettisaddition.logging.loggers;

import carpet.utils.Messenger;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.BaseText;


public class XPOrbLogger extends EntityLogger<ExperienceOrbEntity>
{
	public static XPOrbLogger inst = new XPOrbLogger();

	public XPOrbLogger()
	{
		super("xporb");
	}

	public static void onXPOrbDie(ExperienceOrbEntity xp, DamageSource source, float amount)
	{
		inst.__onEntityDie(xp, source, amount);
	}
	public static void onXPOrbDespawn(ExperienceOrbEntity xp)
	{
		inst.__onEntityDespawn(xp);
	}

	@Override
	protected BaseText getNameTextHoverText(ExperienceOrbEntity xp)
	{
		return Messenger.s(String.format("%s: %d", tr("XP amount"), xp.getExperienceAmount()));
	}
}
