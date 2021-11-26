package carpettisaddition.helpers.rule.persistentLoggerSubscription;

import carpet.logging.LoggerRegistry;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.utils.FileUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LoggerSubscriptionStorage
{
	public static final String STORAGE_FILE_PATH = String.format("./config/%s/logger_subscriptions.json", CarpetTISAdditionServer.compactName);
	private static final LoggerSubscriptionStorage INSTANCE = new LoggerSubscriptionStorage();
	private static final ThreadPoolExecutor FILE_SAVE_POOL = new ThreadPoolExecutor(
			0, 1,
			10, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(1),
			new ThreadFactoryBuilder().setNameFormat(LoggerSubscriptionStorage.class.getSimpleName() + " IO").build(),
			new ThreadPoolExecutor.DiscardOldestPolicy()
	);
	// player name -> (logger name -> option)
	private DataStorage storage = null;

	public static LoggerSubscriptionStorage getInstance()
	{
		return INSTANCE;
	}

	public void load()
	{
		this.storage = null;
		File file = new File(STORAGE_FILE_PATH);
		if (FileUtil.isFile(file))
		{
			try (InputStreamReader isr = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))
			{
				this.storage = new Gson().fromJson(isr, DataStorage.class);
			}
			catch (Exception e)
			{
				CarpetTISAdditionServer.LOGGER.error("Failed to read logger subscription storage file", e);
			}
		}
		if (this.storage == null)
		{
			this.storage = new DataStorage();
			this.save();
		}
	}

	private void save()
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		String content = gson.toJson(this.storage);
		FILE_SAVE_POOL.submit(() -> this.__save(content));
	}

	private void __save(String content)
	{
		File file = new File(STORAGE_FILE_PATH);
		try
		{
			FileUtil.prepareFileDirectories(file);
		}
		catch (IOException e)
		{
			CarpetTISAdditionServer.LOGGER.error("Failed to create directory for logger subscription storage file", e);
			return;
		}
		try (BufferedWriter fw = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8))
		{
			fw.write(content);
		}
		catch (IOException e)
		{
			CarpetTISAdditionServer.LOGGER.error("Failed to write logger subscription storage file", e);
		}
		try
		{
			// A bit cool down to avoid way too frequent file writing
			Thread.sleep(3);
		}
		catch (InterruptedException ignored)
		{
		}
	}

	private void ensureStorageExists()
	{
		if (this.storage == null)
		{
			this.load();
		}
	}

	public boolean restoreSubscription(PlayerEntity player)
	{
		this.ensureStorageExists();
		Map<String, String> playerEntry = this.storage.get(player.getUuidAsString());
		if (playerEntry != null)
		{
			String playerName = player.getName().getString();
			playerEntry.forEach((loggerName, option) -> {
				if (LoggerRegistry.getLogger(loggerName) != null)
				{
					CarpetTISAdditionServer.LOGGER.debug("Restore {} {} for {}", loggerName, option, playerName);
					LoggerRegistry.subscribePlayer(playerName, loggerName, option);
				}
			});
			return true;
		}
		else
		{
			return false;
		}
	}

	private DataStorage.PlayerEntry getEntry(PlayerEntity player)
	{
		this.ensureStorageExists();
		return this.storage.computeIfAbsent(player.getUuidAsString(), uuid -> new DataStorage.PlayerEntry());
	}

	public void addSubscription(PlayerEntity player, String loggerName, @Nullable String option)
	{
		DataStorage.PlayerEntry entry = this.getEntry(player);
		if (!entry.containsKey(loggerName) || !Objects.equals(entry.get(loggerName), option))
		{
			entry.put(loggerName, option);
			this.save();
		}
	}

	public void removeSubscription(PlayerEntity player, String loggerName)
	{
		DataStorage.PlayerEntry entry = this.getEntry(player);
		if (entry.containsKey(loggerName))
		{
			entry.remove(loggerName);
			this.save();
		}
	}
}
