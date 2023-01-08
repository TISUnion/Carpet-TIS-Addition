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
			this.messageTrees.add(this.currentNode);
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
