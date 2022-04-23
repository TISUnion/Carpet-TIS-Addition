package carpettisaddition.logging.loggers.memory;

import carpettisaddition.logging.loggers.AbstractHUDLogger;
import carpettisaddition.utils.Messenger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;

public class MemoryHUDLogger extends AbstractHUDLogger
{
	public static final String NAME = "memory";

	private static final MemoryHUDLogger INSTANCE = new MemoryHUDLogger();

	private MemoryHUDLogger()
	{
		super(NAME);
	}

	public static MemoryHUDLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public MutableText[] onHudUpdate(String option, PlayerEntity playerEntity)
	{
		final long bytesPerMB = 1024 * 1024;
		long occupiedMemoryMB = (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / bytesPerMB;
		long totalMemoryMB = Runtime.getRuntime().maxMemory() / bytesPerMB;
		return new MutableText[]{
				Messenger.c(String.format("g %dM / %dM", occupiedMemoryMB, totalMemoryMB))
		};
	}
}
