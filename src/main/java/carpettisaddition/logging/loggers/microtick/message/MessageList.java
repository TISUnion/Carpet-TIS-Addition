package carpettisaddition.logging.loggers.microtick.message;

import com.google.common.collect.Lists;

import java.util.List;

public class MessageList
{
	private final List<MessageTreeNode> messageTrees = Lists.newArrayList();
	private MessageTreeNode currentNode;
	private boolean flushed;

	public MessageList()
	{
		this.flushed = false;
	}

	public void clear()
	{
		this.messageTrees.clear();
		this.currentNode = null;
		this.flushed = false;
	}

	public boolean isEmpty()
	{
		return this.messageTrees.isEmpty();
	}

	public List<IndentedMessage> flush()
	{
		if (this.flushed)
		{
			throw new IllegalStateException(this.getClass().getName() + " can only flush once");
		}
		this.flushed = true;
		List<IndentedMessage> list = Lists.newArrayList();
		for (MessageTreeNode tree : this.messageTrees)
		{
			list.addAll(tree.flush());
		}
		this.clear();
		return list;
	}

	public void addMessageAndIndent(MicroTickMessage message)
	{
		this.currentNode = new MessageTreeNode(this.currentNode, message);
		if (currentNode.getParent() == null)
		{
			this.messageTrees.add(currentNode);
		}
		if (message.getMessageType() == MessageType.ATOM)
		{
			this.currentNode = this.currentNode.getParent();
		}
	}

	public void addMessageAndUnIndent(MicroTickMessage message)
	{
		if (this.currentNode != null)
		{
			this.currentNode.setQuitMessage(message);
			this.currentNode = this.currentNode.getParent();
		}
	}
}
