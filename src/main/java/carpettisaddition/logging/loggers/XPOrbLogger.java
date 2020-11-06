package carpettisaddition.logging.loggers;

import carpet.utils.Messenger;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.text.BaseText;


public class XPOrbLogger extends EntityLogger<ExperienceOrbEntity>
{
	private static final XPOrbLogger INSTANCE = new XPOrbLogger();

	public XPOrbLogger()
	{
		super("xporb");
	}

	public static XPOrbLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	protected BaseText getNameTextHoverText(ExperienceOrbEntity xp)
	{
		return Messenger.s(String.format("%s: %d", tr("XP amount"), xp.getExperienceAmount()));
	}
}
