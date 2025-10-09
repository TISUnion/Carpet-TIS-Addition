/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.commands.lifetime.recorder;

import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.commands.lifetime.LifeTimeCommand;
import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.lifetime.utils.AbstractReason;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.CommandUtils;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LifetimeRecorder extends TranslationContext
{
	private static final LifetimeRecorder INSTANCE = new LifetimeRecorder();

	private LifetimeRecorderConfig config;
	private final Random random = new Random();
	private final Object lock = new Object();
	private RecordWriterThread recordWriterThread = null;
	private RecorderState recorderState = RecorderState.STOPPED;

	private enum RecorderState
	{
		STOPPED, RUNNING, PAUSED,
	}

	private LifetimeRecorder()
	{
		super(LifeTimeTracker.getInstance().getTranslator().getDerivedTranslator("recorder"));
		this.config = LifetimeRecorderConfig.load();
	}

	public static LifetimeRecorder getInstance()
	{
		return INSTANCE;
	}

	public boolean hasPermission(ServerCommandSource source)
	{
		if (this.config.consoleOrSinglePlayerOwnerOnly)
		{
			if (!(CommandUtils.isConsoleCommandSource(source) || CommandUtils.isSinglePlayerOwner(source)))
			{
				return false;
			}
		}
		return CommandUtils.hasPermissionLevel(source, this.config.requiredPermissionLevel);
	}

	public void addRecord(Entity entity, AbstractReason reason)
	{
		RecordWriterThread rwt = this.recordWriterThread;
		if (rwt == null || !this.config.enabled)
		{
			return;
		}

		if (this.config.maxOutputRecordCount > 0 && rwt.getRecordWritten() >= this.config.maxOutputRecordCount)
		{
			return;
		}
		if (this.config.maxOutputFileBytes > 0 && rwt.getBytesWritten() >= this.config.maxOutputFileBytes)
		{
			return;
		}
		if (this.config.sampleRate < 1 && this.random.nextFloat() < this.config.sampleRate)
		{
			return;
		}

		Record record = Record.create(entity, Record.EventType.fromReason(reason), reason.getRecordId(), reason.getRecordData());
		rwt.write(record);
	}

	// capped text func
	private BaseText ctf(String key, long current, long limit, Function<Long, BaseText> fmt)
	{
		BaseText text = tr(
				key, fmt.apply(current),
				limit > 0 ? tr("status.capped", fmt.apply(limit), String.format("%.1f%%", 100.0 * current / limit)) : tr("status.unlimited")
		);
		if (limit > 0 && current >= limit)
		{
			Messenger.formatting(text, Formatting.YELLOW);
		}
		return text;
	}

	public int showStatus(ServerCommandSource source)
	{
		RecordWriterThread rwt;
		BaseText st1, st2;
		synchronized (this.lock)
		{
			rwt = this.recordWriterThread;
			st1 = this.config.enabled ?
					Messenger.formatting(tr("status.enabled"), Formatting.GREEN) :
					Messenger.formatting(tr("status.disabled"), Formatting.RED);
			st2 = Util.make(() -> {
				switch (this.recorderState)
				{
					case RUNNING:
						return Messenger.formatting(tr("status.running"), Formatting.GREEN);
					case PAUSED:
						return Messenger.formatting(tr("status.paused"), Formatting.GOLD);
					case STOPPED:
						return Messenger.formatting(tr("status.stopped"), Formatting.RED);
					default:
						throw new AssertionError();
				}
			});
		}

		Messenger.tell(source, Messenger.s(" "));
		Messenger.tell(source, Messenger.formatting(tr("status.title"), Formatting.BOLD));
		Messenger.tell(source, tr("status.status", st1, st2));
		if (!this.hasPermission(source))
		{
			return 0;
		}

		if (rwt != null)
		{
			Messenger.tell(source, tr("status.output_file", Messenger.hover(Messenger.s(rwt.getOutputFile().getFileName(), Formatting.DARK_AQUA), Messenger.s(rwt.getOutputFile().toString()))));
		}
		Messenger.tell(source, ctf("status.record_written", rwt != null ? rwt.getRecordWritten() : 0, this.config.maxOutputRecordCount, n -> Messenger.s(n, Formatting.YELLOW)));
		Messenger.tell(source, ctf("status.bytes_written", rwt != null ? rwt.getBytesWritten() : 0, this.config.maxOutputFileBytes, n -> Messenger.s(TextUtils.byteSizeSi(n), Formatting.GREEN)));
		Messenger.tell(source, tr("status.sample_rate", Messenger.s(String.format("%.1f%%", this.config.sampleRate * 100), this.config.sampleRate < 1 ? Formatting.GOLD : Formatting.DARK_GREEN)));
		return 0;
	}

	public int reloadConfig(ServerCommandSource source)
	{
		LifetimeRecorderConfig newConfig;
		try
		{
			newConfig = LifetimeRecorderConfig.loadNoCheck();
		}
		catch (IOException e)
		{
			CarpetTISAdditionServer.LOGGER.error("Failed to load lifetime recorder config file", e);
			Messenger.tell(source, Messenger.formatting(tr("reload_config_failed", e), Formatting.RED));
			return 0;
		}

		this.config = newConfig;
		Messenger.tell(source, tr("reload_config_ok"));

		if (!this.config.enabled)
		{
			this.stop(source);
		}
		return 1;
	}

	public int enableRecording(ServerCommandSource source)
	{
		if (this.config.enabled)
		{
			Messenger.tell(source, tr("already_enabled"));
			return 0;
		}

		this.config.enabled = true;
		this.config.save();
		Messenger.tell(source, tr("enabled"));

		if (LifeTimeTracker.isActivated())
		{
			String cmd = String.format("/%s tracking restart", LifeTimeCommand.NAME);
			Messenger.tell(source, Messenger.fancy(
					tr("need_rerun_to_start"),
					Messenger.s(cmd),
					Messenger.ClickEvents.suggestCommand(cmd)
			));
		}

		return 1;
	}

	public int disableRecording(ServerCommandSource source)
	{
		if (!this.config.enabled)
		{
			Messenger.tell(source, tr("already_disabled"));
			return 0;
		}

		this.config.enabled = false;
		this.config.save();
		Messenger.tell(source, tr("disabled"));

		this.stop(source);
		return 1;
	}

	public void start(@NotNull ServerCommandSource source, int currentTrackId)
	{
		this.stop(source);

		if (!this.config.enabled)
		{
			return;
		}

		if (this.config.maxTotalOutputFileBytes > 0)
		{
			Optional<Pair<Integer, Long>> st = this.getOutputFilesStats();
			CarpetTISAdditionMod.LOGGER.debug("output file size stats: {}", st);
			if (st.isPresent())
			{
				int count = st.get().getFirst();
				if (count >= this.config.maxTotalOutputFileCount)
				{
					Messenger.tell(source, Messenger.formatting(tr("total_output_count_too_many", count, this.config.maxTotalOutputFileCount), Formatting.RED));
					return;
				}
				long sizeTotal = st.get().getSecond();
				if (sizeTotal >= this.config.maxTotalOutputFileBytes)
				{
					Messenger.tell(source, Messenger.formatting(tr("total_output_size_too_large", TextUtils.byteSizeSi(sizeTotal), TextUtils.byteSizeSi(this.config.maxTotalOutputFileBytes)), Formatting.RED));
					return;
				}
			}
			else
			{
				Messenger.tell(source, Messenger.formatting(tr("total_output_size_unknown"), Formatting.RED));
				return;
			}
		}

		String fileName = String.format("rec_%s_%d.jsonl", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), currentTrackId);
		Path outputFile = Paths.get(this.config.outputDirectory, fileName);

		RecordWriterThread newRwt = new RecordWriterThread(
				outputFile, this.config.maxOutputRecordCount, this.config.maxOutputFileBytes,
				(rwt) -> {
					synchronized (this.lock)
					{
						if (this.recordWriterThread == rwt)
						{
							this.recorderState = RecorderState.PAUSED;
						}
					}
				}
		);
		synchronized (this.lock)
		{
			this.recordWriterThread = newRwt;
			this.recorderState = RecorderState.RUNNING;
		}

		Messenger.tell(source, tr("started", Messenger.s(fileName, Formatting.DARK_AQUA)));
	}

	public boolean stop(@Nullable ServerCommandSource source)
	{
		RecordWriterThread rwt = this.recordWriterThread;
		synchronized (this.lock)
		{
			this.recordWriterThread = null;
			this.recorderState = RecorderState.STOPPED;
		}

		if (rwt == null)
		{
			return false;
		}
		rwt.close();

		if (source != null)
		{
			Messenger.tell(source, tr(
					"stopped",
					Messenger.s(rwt.getRecordWritten(), Formatting.YELLOW),
					Messenger.s(TextUtils.byteSizeSi(rwt.getBytesWritten()), Formatting.GREEN)
			));
		}
		return true;
	}

	// count,sizeSum
	private Optional<Pair<Integer, Long>> getOutputFilesStats()
	{
		Path path = Paths.get(this.config.outputDirectory);
		try (Stream<Path> fileStream = Files.list(path))
		{
			List<Path> files = fileStream.
					filter(f -> f.getFileName().toString().endsWith(".jsonl") && Files.isRegularFile(f)).
					collect(Collectors.toList());
			long total = 0;
			for (Path file : files)
			{
				total += Files.size(file);
			}
			return Optional.of(Pair.of(files.size(), total));
		}
		catch (IOException e)
		{
			CarpetTISAdditionMod.LOGGER.error("List lifetime recorder output files failed", e);
			return Optional.empty();
		}
	}
}
