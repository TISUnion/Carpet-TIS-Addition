package carpettisaddition.logging.loggers.microtick.message;

import com.google.common.collect.Lists;

import java.util.List;

public class MessageTreeNode
{
	private final List<MessageTreeNode> children = Lists.newArrayList();
	private final MessageTreeNode parent;
	private final MicroTickMessage entryMessage;
	private MicroTickMessage quitMessage;
	private boolean flushed;

	public MessageTreeNode(MessageTreeNode parent, MicroTickMessage message)
	{
		this.parent = parent;
		this.entryMessage = message;
		this.flushed = false;
		if (this.parent != null)
		{
			this.parent.addChild(this);
		}
	}

	public MessageTreeNode getParent()
	{
		return this.parent;
	}

	public void setQuitMessage(MicroTickMessage message)
	{
		this.quitMessage = message;
	}

	private void addChild(MessageTreeNode child)
	{
		this.children.add(child);
	}

	private List<IndentedMessage> flush(int depth)
	{
		if (this.flushed)
		{
			throw new IllegalStateException(this.getClass().getName() + " can only flush once");
		}
		this.flushed = true;

		List<IndentedMessage> childrenMessageList = Lists.newArrayList();
		int childWithMessageCount = 0;
		for (MessageTreeNode child : this.children)
		{
			List<IndentedMessage> childMessage = child.flush(depth + 1);
			childrenMessageList.addAll(childMessage);
			if (!childMessage.isEmpty())
			{
				childWithMessageCount++;
			}
		}

		boolean showEntryMessage = this.entryMessage.getEvent().isImportant() || childWithMessageCount > 0;
		boolean mergeMessage = showEntryMessage && this.quitMessage != null && childWithMessageCount <= 1;
		boolean showQuitMessage = this.quitMessage != null && showEntryMessage && !mergeMessage;
		List<IndentedMessage> list = Lists.newArrayList();

		if (mergeMessage)
		{
			this.entryMessage.mergeQuiteMessage(this.quitMessage);
		}
		if (showEntryMessage)
		{
			list.add(new IndentedMessage(this.entryMessage, depth));
		}
		list.addAll(childrenMessageList);
		if (showQuitMessage)
		{
			list.add(new IndentedMessage(this.quitMessage, depth));
		}
		return list;
	}

	public List<IndentedMessage> flush()
	{
		return this.flush(0);
	}
}
