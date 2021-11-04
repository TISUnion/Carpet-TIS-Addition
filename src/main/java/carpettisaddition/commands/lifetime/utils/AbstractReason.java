package carpettisaddition.commands.lifetime.utils;

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.translations.TranslatableBase;
import net.minecraft.text.BaseText;

public abstract class AbstractReason extends TranslatableBase
{
	public AbstractReason(String reasonType)
	{
		super(LifeTimeTracker.getInstance().getTranslator().getDerivedTranslator(reasonType));
	}

	public abstract BaseText toText();
}
