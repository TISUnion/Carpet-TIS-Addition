package carpettisaddition.logging.loggers.microtick.message;

import com.google.common.collect.Lists;

import java.util.List;

public class MessageList
{
	private final List<MessageTreeNode> messageTrees = Lists.newArrayList();
	private MessageTreeNode currentNode;

	public void clear()
	{
		this.messageTrees.clear();
		this.currentNode = null;
	}

	public boolean isEmpty()
	{
		return this.messageTrees.isEmpty();
	}

	public List<MessageTreeNode> toList()
	{
		List<MessageTreeNode> list = Lists.newArrayList();
		for (MessageTreeNode tree : this.messageTrees)
		{
			list.addAll(tree.toList());
		}
		return list;
	}

	public void addMessageAndIndent(MicroTickMessage message)
	{
		this.currentNode = new MessageTreeNode(this.currentNode, message);
		if (message.messageType == MessageType.ATOM)
		{
			this.currentNode = this.currentNode.getParent();
		}
	}

	public void unIndent()
	{
		if (this.currentNode != null)
		{
			this.currentNode = this.currentNode.getParent();
		}
	}
}
