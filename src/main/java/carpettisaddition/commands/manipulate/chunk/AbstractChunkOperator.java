/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.commands.manipulate.chunk;

import carpettisaddition.translations.TranslationContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

abstract class AbstractChunkOperator extends TranslationContext implements AbortableOperation
{
	protected final ServerLevel world;
	protected final List<ChunkPos> chunkPosList;
	protected final CommandSourceStack source;
	protected final AtomicBoolean aborted = new AtomicBoolean(false);

	protected AbstractChunkOperator(Translator translator, List<ChunkPos> chunkPosList, CommandSourceStack source)
	{
		super(translator);
		this.world = source.getLevel();
		this.chunkPosList = chunkPosList;
		this.source = source;
	}

	@Override
	public void abort(CommandSourceStack source)
	{
		this.aborted.set(true);
	}
}
