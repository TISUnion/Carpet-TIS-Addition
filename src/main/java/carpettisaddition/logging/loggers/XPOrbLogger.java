package carpettisaddition.logging.loggers;

import carpet.utils.Messenger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.BaseText;


public class XPOrbLogger extends EntityLogger<ExperienceOrbEntity>
{
	private static final XPOrbLogger instance = new XPOrbLogger();

	public XPOrbLogger()
	{
		super("xporb");
	}

	public static XPOrbLogger getInstance()
	{
		return instance;
	}

	public static void onXPOrbDie(ExperienceOrbEntity xp, DamageSource source, float amount)
	{
		if (ExtensionLoggerRegistry.__xporb)
		{
			instance.__onEntityDie(xp, source, amount);
		}
	}
	public static void onXPOrbDespawn(ExperienceOrbEntity xp)
	{
		if (ExtensionLoggerRegistry.__xporb)
		{
			instance.__onEntityDespawn(xp);
		}
	}

	@Override
	protected BaseText getNameTextHoverText(ExperienceOrbEntity xp)
	{
		return Messenger.s(String.format("%s: %d", tr("XP amount"), xp.getExperienceAmount()));
	}
}
