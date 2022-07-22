package carpettisaddition.logging.loggers.mobcapsLocal;

import carpet.logging.HUDLogger;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.BaseText;

// a placeholder class, its implementation is in 1.18+
public class MobcapsLocalLogger extends AbstractHUDLogger
{
	public static final String NAME = "mobcapsLocal";
	private static final MobcapsLocalLogger INSTANCE = new MobcapsLocalLogger();

	private MobcapsLocalLogger()
	{
		super(NAME, true);
	}

	public static MobcapsLocalLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public BaseText[] onHudUpdate(String option, PlayerEntity playerEntity)
	{
		return null;
	}

	@Override
	public HUDLogger createCarpetLogger()
	{
		throw new RuntimeException("MobcapsLocal logger can only be used in mc1.18+");
	}
}