package carpettisaddition.logging.loggers.microtick.message;

import com.google.common.collect.Lists;
import net.minecraft.text.BaseText;

import java.util.List;

public class MessageTreeNode
{
	private final List<MessageTreeNode> children = Lists.newArrayList();
	private final MessageTreeNode parent;
	private final MicroTickMessage message;
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

	public MicroTickMessage getMessage()
	{
		return this.message;
	}

	public BaseText getMessageText()
	{
		return this.message.toText(this.depth);
	}

	private void addChild(MessageTreeNode child)
	{
		this.children.add(child);
	}

	public void trim()
	{
		// TODO
	}

	public List<MessageTreeNode> toList()
	{
		List<MessageTreeNode> list = Lists.newArrayList(this);
		for (MessageTreeNode child : this.children)
		{
			list.addAll(child.toList());
		}
		return list;
	}
}
