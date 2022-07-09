package carpettisaddition.logging.loggers.entity;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.utils.Messenger;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.text.BaseText;

//#if MC >= 11700
//$$ import carpettisaddition.mixins.logger.xporb.ExperienceOrbEntityAccessor;
//#endif

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
		//#if MC >= 11700
		//$$ int amount = xp.getExperienceAmount();
		//$$ int count = ((ExperienceOrbEntityAccessor)xp).getPickingCount();
		//$$ long total = (long)amount * count;
		//$$ String amountStr = String.format("w : %dxp * %d = %d", amount, count, total);
		//#else
		String amountStr = String.format("w : %d", xp.getExperienceAmount());
		//#endif

		return Messenger.c(tr("xp_amount"), amountStr);
	}

	@Override
	protected boolean getAcceleratorBoolean()
	{
		return TISAdditionLoggerRegistry.__xporb;
	}
}
