package carpettisaddition.logging.loggers.entity;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.utils.Messenger;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.MutableText;


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
	protected MutableText getNameText(ItemEntity item)
	{
		MutableText text = super.getNameText(item);
		text.append("(").append(Messenger.tr(item.getStack().getTranslationKey())).append(")");
		return text;
	}

	@Override
	protected MutableText getNameTextHoverText(ItemEntity item)
	{
		return Messenger.c(tr("item_stack_size"), String.format("w : %d", item.getStack().getCount()));
	}

	@Override
	protected boolean getAcceleratorBoolean()
	{
		return TISAdditionLoggerRegistry.__item;
	}
}
