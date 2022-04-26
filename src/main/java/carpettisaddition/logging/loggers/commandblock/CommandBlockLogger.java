package carpettisaddition.logging.loggers.commandblock;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.ChatUtil;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.CommandBlockExecutor;
import net.minecraft.world.World;

public class CommandBlockLogger extends AbstractLogger
{
	public static final String NAME = "commandBlock";
	public static final int MINIMUM_LOG_INTERVAL = 3 * 20;  // 3s
	public static final int MAXIMUM_PREVIEW_LENGTH = 16;
	private static final CommandBlockLogger INSTANCE = new CommandBlockLogger();

	public CommandBlockLogger()
	{
		super(NAME);
	}

	public static CommandBlockLogger getInstance()
	{
		return INSTANCE;
	}

	private void logCommandBlockExecution(World world, BaseText nameText, BaseText posText, CommandBlockExecutor executor, String removeCommand)
	{
		if (!TISAdditionLoggerRegistry.__commandBlock)
		{
			return;
		}

		ICommandBlockExecutor iExecutor = (ICommandBlockExecutor)executor;
		long time = world.getTime();
		String commandPreview = executor.getCommand();
		if (commandPreview.length() > MAXIMUM_PREVIEW_LENGTH)
		{
			commandPreview = commandPreview.substring(0, MAXIMUM_PREVIEW_LENGTH - 3) + "...";
		}
		String finalCommandPreview = commandPreview;

		this.log((option) -> {
			boolean isThrottledLogging = !option.equals("all");
			if (time - iExecutor.getLastLoggedTime() < MINIMUM_LOG_INTERVAL && isThrottledLogging)
			{
				return null;
			}
			if (isThrottledLogging)
			{
				iExecutor.setLastLoggedTime(time);
			}
			return new BaseText[]{Messenger.c(
					tr(
							"executed",
							Messenger.formatting(Messenger.copy(nameText), Formatting.GOLD),
							Messenger.fancy(
									"c",
									Messenger.s(finalCommandPreview),
									Messenger.s(executor.getCommand()),
									new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, executor.getCommand())
							)
					),
					"g  @ ",
					posText,
					"w  ",
					Messenger.fancy(
							"r",
							Messenger.s("[×]"),
							tr("remove_executor", nameText),
							new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, removeCommand)
					)
			)};
		});
	}

	public void onCommandBlockActivated(World world, BlockPos pos, BlockState state, CommandBlockExecutor executor)
	{
		this.logCommandBlockExecution(
				world,
				Messenger.block(state.getBlock()),
				Messenger.coord("w", pos, DimensionWrapper.of(world)),
				executor,
				String.format("/execute in %s run setblock %d %d %d %s", DimensionWrapper.of(world).getIdentifier(), pos.getX(), pos.getY(), pos.getZ(), Registry.BLOCK.getId(Blocks.AIR))
		);
	}

	public void onCommandBlockMinecartActivated(CommandBlockMinecartEntity entity)
	{
		if (ChatUtil.isEmpty(entity.getCommandExecutor().getCommand()))
		{
			return;
		}
		this.logCommandBlockExecution(
				entity.getEntityWorld(),
				Messenger.entity(null, entity),
				Messenger.coord("w", entity.getPos(), DimensionWrapper.of(entity)),
				entity.getCommandExecutor(),
				String.format("/kill %s", entity.getUuidAsString())
		);
	}
}
