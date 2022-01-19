package carpettisaddition.logging.loggers.entity;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.utils.Messenger;
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
		return Messenger.c(tr("xp_amount"), String.format("w : %d", xp.getExperienceAmount()));
	}

	@Override
	protected boolean getAcceleratorBoolean()
	{
		return TISAdditionLoggerRegistry.__xporb;
	}
}
