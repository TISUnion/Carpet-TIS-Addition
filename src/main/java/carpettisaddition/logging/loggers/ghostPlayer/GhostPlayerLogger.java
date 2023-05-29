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

package carpettisaddition.logging.loggers.ghostPlayer;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingAccess;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import carpettisaddition.utils.deobfuscator.StackTracePrinter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class GhostPlayerLogger extends AbstractLogger
{
	public static final String NAME = "ghostPlayer";
	private static final GhostPlayerLogger INSTANCE = new GhostPlayerLogger();

	private GhostPlayerLogger()
	{
		super(NAME, false);
	}

	public static GhostPlayerLogger getInstance()
	{
		return INSTANCE;
	}

	@Nullable
	@Override
	public String[] getSuggestedLoggingOption()
	{
		return new String[]{"me", "all", "me,no_subchunk", "all,no_subchunk"};
	}

	private void logFormat(Type type, World world, PlayerEntity entity, String formatter, Object... args)
	{
		BaseText message = Messenger.format(
				"%s %s (%s) %s %s %s %s %s %s",
				Messenger.hover(
						Messenger.c("g [", Messenger.s(NAME, Formatting.AQUA), "g ]"),
						Messenger.s(NAME + " logger")
				),
				Messenger.entity(entity), entity.getEntityId(),
				Messenger.format(formatter, args),
				Messenger.c("g @"),
				Messenger.hover(Messenger.dimension(DimensionWrapper.of(world)), Messenger.s("GameTime: " + world.getTime())),
				MicroTimingAccess.getTickPhase().toText("y"),
				StackTracePrinter.create().ignore(GhostPlayerLogger.class).deobfuscate().toSymbolText()
		);
		this.log((option, player) -> {
			List<String> options = Arrays.asList(option.split(MULTI_OPTION_SEP_REG));
			boolean ok = options.contains("me") ? player.getUuid().equals(entity.getUuid()) : options.contains("all");
			ok &= !(type == Type.SUBCHUNK_OP && options.contains("no_subchunk"));
			return ok ? new BaseText[]{message} : null;
		});
	}

	// ============================= hooks =============================
	// all hooks should be checked using GhostPlayerLogger#isEnabled()
	// to ensure that they only get triggered when necessary

	public static boolean isEnabled()
	{
		return !CarpetTISAdditionSettings.loggerGhostPlayer.equals("false") && TISAdditionLoggerRegistry.__ghostPlayer;
	}

	public void onWorldRemoveEntity(World world, PlayerEntity entity, String actionName)
	{
		if (!isEnabled() || world.isClient())
		{
			return;
		}
		this.logFormat(Type.WORLD_REMOVE, world, entity, actionName);
	}

	public void onChunkUnloadPlayer(WorldChunk chunk, PlayerEntity entity)
	{
		if (!isEnabled() || chunk.getWorld().isClient())
		{
			return;
		}
		this.logFormat(
				Type.CHUNK_UNLOAD,
				chunk.getWorld(), entity,
				"chunk unload -> unloadEntities %s",
				Messenger.coord(chunk.getPos(), DimensionWrapper.of(chunk.getWorld()))
		);
	}

	public void onChunkSectionAddOrRemovePlayer(WorldChunk chunk, PlayerEntity entity, int y, String actionName)
	{
		if (!isEnabled() || chunk.getWorld().isClient())
		{
			return;
		}
		ChunkPos chunkPos = chunk.getPos();
		this.logFormat(
		        Type.SUBCHUNK_OP,
		        chunk.getWorld(), entity,
	            "subchunk %s %s",
		        Messenger.fancy(
						Messenger.format("[%s, %s, %s]", chunkPos.x, y, chunkPos.z),
				        Messenger.s("Chunk section coordinate"),
				        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/tp %d %d %d", chunkPos.x * 16 + 8, y * 16 + 8, chunkPos.z * 16 + 8))
		        ),
		        actionName
        );
	}

	private enum Type
	{
		WORLD_REMOVE,
		CHUNK_UNLOAD,
		SUBCHUNK_OP
	}
}
