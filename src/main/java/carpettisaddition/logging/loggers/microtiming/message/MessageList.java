package carpettisaddition.logging.loggers.microtiming.message;

import com.google.common.collect.Lists;

import java.util.List;

public class MessageList
{
	private final List<MessageTreeNode> messageTrees = Lists.newArrayList();
	private MessageTreeNode currentNode;

	public synchronized void clear()
	{
		this.messageTrees.clear();
		this.currentNode = null;
	}

	public synchronized boolean isEmpty()
	{
		return this.messageTrees.isEmpty();
	}

	public synchronized List<IndentedMessage> flush()
	{
		List<IndentedMessage> list = Lists.newArrayList();
		for (MessageTreeNode tree : this.messageTrees)
		{
			// tree.printTree();
			list.addAll(tree.flush());
		}
		this.clear();
		return list;
	}

	public synchronized void addMessageAndIndent(MicroTimingMessage message)
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

	public synchronized void addMessageAndUnIndent(MicroTimingMessage message)
	{
		if (this.currentNode != null)
		{
			this.currentNode.setQuitMessage(message);
			this.currentNode = this.currentNode.getParent();
		}
	}
}
