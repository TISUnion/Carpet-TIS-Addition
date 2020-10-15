package carpettisaddition.logging.loggers;

import carpet.utils.Messenger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.utils.Util;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.BaseText;


public class ItemLogger extends EntityLogger<ItemEntity>
{
	private static final ItemLogger instance = new ItemLogger();

	public ItemLogger()
	{
		super("item");
	}

	public static ItemLogger getInstance()
	{
		return instance;
	}

	public static void onItemDie(ItemEntity item, DamageSource source, float amount)
	{
		if (ExtensionLoggerRegistry.__item)
		{
			instance.__onEntityDie(item, source, amount);
		}
	}
	public static void onItemDespawn(ItemEntity item)
	{
		if (ExtensionLoggerRegistry.__item)
		{
			instance.__onEntityDespawn(item);
		}
	}

	@Override
	protected BaseText getNameText(ItemEntity item)
	{
		BaseText text = super.getNameText(item);
		text.append("(").append(Util.getTranslatedName(item.getStack().getTranslationKey())).append(")");
		return text;
	}

	@Override
	protected BaseText getNameTextHoverText(ItemEntity item)
	{
		return Messenger.s(String.format("%s: %d", tr("Item stack size"), item.getStack().getCount()));
	}
}
