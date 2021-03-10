package carpettisaddition.logging.loggers.entity;

import carpet.utils.Messenger;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.utils.TextUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.BaseText;


public class ItemLogger extends EntityLogger<ItemEntity>
{
	private static final ItemLogger INSTANCE = new ItemLogger();

	public ItemLogger()
	{
		super("item");
	}

	public static ItemLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	protected BaseText getNameText(ItemEntity item)
	{
		BaseText text = super.getNameText(item);
		text.append("(").append(TextUtil.getTranslatedName(item.getStack().getTranslationKey())).append(")");
		return text;
	}

	@Override
	protected BaseText getNameTextHoverText(ItemEntity item)
	{
		return Messenger.s(String.format("%s: %d", tr("Item stack size"), item.getStack().getCount()));
	}

	@Override
	protected boolean getAcceleratorBoolean()
	{
		return TISAdditionLoggerRegistry.__item;
	}
}
