package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;

import java.util.List;

public class BlockReplaceEvent extends SetBlockStateEventBase
{
	private final Block oldBlock;
	private final Block newBlock;

	public BlockReplaceEvent(EventType eventType, Block oldBlock, Block newBlock, Boolean returnValue,  int flags)
	{
		super(eventType, "block_replace", oldBlock, returnValue, flags);
		this.oldBlock = oldBlock;
		this.newBlock = newBlock;
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		BaseText titleText = Messenger.fancy(
				Messenger.formatting(tr("block_replace"), COLOR_ACTION),
				this.getFlagsText(),
				null
		);
		BaseText infoText = Messenger.c(
				Messenger.block(this.oldBlock),
				"g ->",
				Messenger.block(this.newBlock)
		);
		if (this.getEventType() != EventType.ACTION_END)
		{
			list.add(Messenger.c(titleText, "g : ", infoText));
		}
		else
		{
			list.add(Messenger.fancy(
					"w",
					Messenger.c(titleText, Messenger.getSpaceText(), Messenger.formatting(tr("finished"), COLOR_RESULT)),
					infoText,
					null
			));
		}
		if (this.returnValue != null)
		{
			list.add("w  ");
			list.add(MicroTimingUtil.getSuccessText(this.returnValue, true));
		}
		return Messenger.c(list.toArray(new Object[0]));
	}
}
