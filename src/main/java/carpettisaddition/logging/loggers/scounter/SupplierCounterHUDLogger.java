package carpettisaddition.logging.loggers.scounter;

import carpettisaddition.commands.scounter.SupplierCounterCommand;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.BaseText;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SupplierCounterHUDLogger extends AbstractHUDLogger
{
	private static final SupplierCounterHUDLogger INSTANCE = new SupplierCounterHUDLogger();
	public static final String NAME = "scounter";

	public SupplierCounterHUDLogger()
	{
		super(NAME);
	}

	public static SupplierCounterHUDLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public @Nullable String getDefaultLoggingOption()
	{
		return "";
	}

	@Override
	public @Nullable String[] getSuggestedLoggingOption()
	{
		return SupplierCounterCommand.COLORS.toArray(new String[0]);
	}

	@Override
	public BaseText[] onHudUpdate(String option, PlayerEntity playerEntity)
	{
		List<BaseText> lines = new ArrayList<>();
		Arrays.asList(option.split(",")).forEach(color -> {
			SupplierCounterCommand.getInstance().getCounter(color).ifPresent(counter -> {
				lines.add(counter.reportBrief(false));
			});
		});
		return lines.toArray(new BaseText[0]);
	}
}
