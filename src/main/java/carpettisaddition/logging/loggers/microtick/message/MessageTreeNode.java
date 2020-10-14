package carpettisaddition.logging.loggers.microtick.message;

import com.google.common.collect.Lists;

import java.util.List;

public class MessageTreeNode
{
	private final List<MessageTreeNode> children = Lists.newArrayList();
	private final MessageTreeNode parent;
	private final MicroTickMessage message;
	private MicroTickMessage quitMessage;
	private final int depth;

	public MessageTreeNode(MessageTreeNode parent, MicroTickMessage message)
	{
		this.parent = parent;
		this.message = message;
		if (this.parent != null)
		{
			this.parent.addChild(this);
			this.depth = this.parent.depth + 1;
		}
		else
		{
			this.depth = 0;
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

	public List<ArrangedMessage> toList()
	{
		List<ArrangedMessage> list = Lists.newArrayList();
		boolean isLeaf = this.children.isEmpty();
		if (this.message.event.isImportant() || !isLeaf)
		{
			list.add(new ArrangedMessage(this.message, this.depth));
		}
		if (!isLeaf)
		{
			for (MessageTreeNode child : this.children)
			{
				list.addAll(child.toList());
			}
			if (this.quitMessage != null)
			{
				list.add(new ArrangedMessage(this.quitMessage, this.depth));
			}
		}
		return list;
	}
}
