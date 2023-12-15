/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.logging.loggers.microtiming.message;

import carpettisaddition.CarpetTISAdditionServer;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class MessageTreeNode
{
	private final List<MessageTreeNode> children = Lists.newArrayList();
	private final MessageTreeNode parent;
	private final MicroTimingMessage entryMessage;
	private MicroTimingMessage quitMessage;
	private boolean flushed;

	public MessageTreeNode(MessageTreeNode parent, MicroTimingMessage message)
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

	public void setQuitMessage(MicroTimingMessage message)
	{
		this.quitMessage = message;
		if (this.entryMessage.getEvent().getClass() != this.quitMessage.getEvent().getClass())
		{
			CarpetTISAdditionServer.LOGGER.warn(String.format("Trying to merge %s with %s", childName(this.entryMessage.getEvent()), childName(this.quitMessage.getEvent())));
		}
	}

	private void addChild(MessageTreeNode child)
	{
		this.children.add(child);
	}

	public List<IndentedMessage> flush()
	{
		// Manual stack 100% Stack Overflow proof
		Deque<StackElement> stack = Queues.newArrayDeque();
		StackElement rootElement = new StackElement(this, 0, null);
		stack.push(rootElement);
		while (!stack.isEmpty())
		{
			StackElement e = stack.pop();
			if (e.iterator == null)
			{
				if (e.node.flushed)
				{
					throw new IllegalStateException(e.node.getClass().getName() + " can only flush once");
				}
				e.node.flushed = true;
				e.iterator = e.node.children.iterator();
			}
			if (e.iterator.hasNext())  // iterating children
			{
				MessageTreeNode child = e.iterator.next();
				stack.push(e);
				stack.push(new StackElement(child, e.depth + 1, e));
			}
			else  // iteration finished
			{
				boolean showEntryMessage = e.node.entryMessage.getEvent().isImportant() || e.childWithMessageCount > 0;
				boolean mergeMessage = showEntryMessage && e.node.quitMessage != null && e.childWithMessageCount <= 1;
				boolean showQuitMessage = e.node.quitMessage != null && showEntryMessage && !mergeMessage;
				if (mergeMessage)
				{
					e.node.entryMessage.mergeQuitMessage(e.node.quitMessage);
				}
				if (showEntryMessage)
				{
					e.messageList.add(new IndentedMessage(e.node.entryMessage, e.depth));
				}
				e.messageList.addAll(e.childMessageList);
				if (showQuitMessage)
				{
					e.messageList.add(new IndentedMessage(e.node.quitMessage, e.depth));
				}
				// Backtracking towards parent node to update parent's data
				StackElement parent = e.parentStackElement;
				if (parent != null)
				{
					parent.childMessageList.addAll(e.messageList);
					if (!e.messageList.isEmpty())
					{
						parent.childWithMessageCount++;
					}
				}
			}
		}
		return rootElement.messageList;
	}

	private static class StackElement
	{
		public MessageTreeNode node;
		public int depth;
		public StackElement parentStackElement;
		public List<IndentedMessage> messageList;
		public List<IndentedMessage> childMessageList;
		public Iterator<MessageTreeNode> iterator;
		public int childWithMessageCount;

		private StackElement(MessageTreeNode node, int depth, StackElement parentStackElement)
		{
			this.node = node;
			this.depth = depth;
			this.parentStackElement = parentStackElement;
			this.messageList = Lists.newArrayList();
			this.childMessageList = Lists.newArrayList();
			this.iterator = null;
			this.childWithMessageCount = 0;
		}
	}

	/*
	 * ----------------
	 *   Debug things
	 * ----------------
	 */

	private static String childName(Object o)
	{
		String[] list = o.toString().split("\\.");
		return list[list.length - 1];
	}
	private static String ms(MicroTimingMessage msg)
	{
		return childName(msg.getEvent()) + " " + childName(msg.getEvent().getEventType()) + " " + childName(msg.getMessageType());
	}
	private void printTree(int depth)
	{
		StringBuilder prefix = new StringBuilder();
		for (int i = 0; i < depth; i++) prefix.append("  ");

		System.err.println(prefix + ms(this.entryMessage));
		for (MessageTreeNode child : this.children) child.printTree(depth + 1);
		System.err.println(prefix + (this.quitMessage != null ? ms(this.quitMessage) : null));
	}
	public void printTree()
	{
		try
		{
			this.printTree(0);
		}
		catch (Exception e)
		{
			//noinspection CallToPrintStackTrace
			e.printStackTrace();
		}
	}
}
