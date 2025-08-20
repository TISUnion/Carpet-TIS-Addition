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
import carpettisaddition.utils.FileUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

class RecordWriterThread implements AutoCloseable
{
	private static final AtomicLong counter = new AtomicLong();
	private static final byte[] SYSTEM_LINE_SEP = System.lineSeparator().getBytes(StandardCharsets.UTF_8);

	private final Path outputFile;
	private final long maxOutputRecordCount;
	private final long maxOutputFileBytes;
	private final StopCallback stopCallback;
	private final Thread thread;
	private final BlockingQueue<Record> writeQueue = new LinkedBlockingQueue<>(10000);
	private final AtomicLong recordWritten = new AtomicLong(0);
	private final AtomicLong bytesWritten = new AtomicLong(0);
	private final AtomicBoolean working = new AtomicBoolean(true);

	@FunctionalInterface
	interface StopCallback
	{
		void onStop(RecordWriterThread wrt);
	}

	public RecordWriterThread(Path outputFile, long maxOutputRecordCount, long maxOutputFileBytes, StopCallback stopCallback)
	{
		this.outputFile = outputFile;
		this.maxOutputRecordCount = maxOutputRecordCount;
		this.maxOutputFileBytes = maxOutputFileBytes;
		this.stopCallback = stopCallback;
		this.thread = new Thread(this::writerThreadImpl);
		this.thread.setName("tiscm-lifetime-record-writer-" + counter.getAndIncrement());
		this.thread.start();
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void write(Record record)
	{
		if (this.working.get())
		{
			this.writeQueue.offer(record);
		}
	}

	public boolean isFileLimitExceeded()
	{
		return this.maxOutputRecordCount > 0 && this.getRecordWritten() >= this.maxOutputRecordCount ||
				this.maxOutputFileBytes > 0 && this.getBytesWritten() >= this.maxOutputFileBytes;
	}

	private void writerThreadImpl()
	{
		BufferedOutputStream writer = null;
		try
		{
			FileUtils.touchFileDirectory(this.outputFile);

			try
			{
				while (!Thread.currentThread().isInterrupted())
				{
					Record record = this.writeQueue.take();
					byte[] line = record.toJson().getBytes(StandardCharsets.UTF_8);

					if (writer == null)
					{
						writer = new BufferedOutputStream(Files.newOutputStream(this.outputFile));
					}

					writer.write(line);
					writer.write(SYSTEM_LINE_SEP);
					writer.flush();
					this.recordWritten.addAndGet(1);
					this.bytesWritten.addAndGet(line.length + SYSTEM_LINE_SEP.length);

					if (this.isFileLimitExceeded())
					{
						CarpetTISAdditionMod.LOGGER.info(
								"File limit exceeded (cnt {} / {}, size {} / {}), recording stopped",
								this.maxOutputRecordCount, this.recordWritten.get(),
								this.maxOutputFileBytes, this.bytesWritten.get()
						);
						break;
					}
				}
			}
			finally
			{
				if (writer != null)
				{
					writer.close();
				}
			}
		}
		catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
		catch (IOException e)
		{
			CarpetTISAdditionMod.LOGGER.error("Error while writing lifetime record output to file {}", this.outputFile, e);
		}
		finally
		{
			this.working.set(false);
			this.writeQueue.clear();
			this.stopCallback.onStop(this);
		}
	}

	@Override
	public void close()
	{
		this.thread.interrupt();
		try
		{
			this.thread.join();
		}
		catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
	}

	public Path getOutputFile()
	{
		return this.outputFile;
	}

	public long getRecordWritten()
	{
		return this.recordWritten.get();
	}

	public long getBytesWritten()
	{
		return this.bytesWritten.get();
	}
}
