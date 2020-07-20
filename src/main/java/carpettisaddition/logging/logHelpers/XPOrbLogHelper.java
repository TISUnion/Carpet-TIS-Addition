package carpettisaddition.logging.logHelpers;

import carpet.utils.Messenger;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.BaseText;


public class XPOrbLogHelper extends EntityLogHelper<ExperienceOrbEntity>
{
	public static XPOrbLogHelper inst = new XPOrbLogHelper();

	public XPOrbLogHelper()
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
