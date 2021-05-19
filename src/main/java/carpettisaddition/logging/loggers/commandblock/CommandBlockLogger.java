package carpettisaddition.logging.loggers.commandblock;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.utils.TextUtil;
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

		LoggerRegistry.getLogger(NAME).log((option) -> {
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
					TextUtil.attachFormatting(TextUtil.copyText(nameText), Formatting.GOLD),
					TextUtil.getSpaceText(),
					"w " + this.tr("executed"),
					TextUtil.getSpaceText(),
					TextUtil.getFancyText(
							"c",
							Messenger.s(finalCommandPreview),
							Messenger.s(executor.getCommand()),
							new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, executor.getCommand())
					),
					"g  @ ",
					posText,
					"w  ",
					TextUtil.getFancyText(
							"r",
							Messenger.s("[×]"),
							this.advTr("remove_executor", "Click to remove %1$s", nameText),
							new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, removeCommand)
					)
			)};
		});
	}

	public void onCommandBlockActivated(World world, BlockPos pos, BlockState state, CommandBlockExecutor executor)
	{
		this.logCommandBlockExecution(
				world,
				TextUtil.getBlockName(state.getBlock()),
				TextUtil.getCoordinateText("w", pos, world.getDimension().getType()),
				executor,
				String.format("/execute in %s run setblock %d %d %d %s", world.getDimension().getType(), pos.getX(), pos.getY(), pos.getZ(), Registry.BLOCK.getId(Blocks.AIR))
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
				TextUtil.getEntityText(null, entity),
				TextUtil.getCoordinateText("w", entity.getPos(), entity.getEntityWorld().getDimension().getType()),
				entity.getCommandExecutor(),
				String.format("/kill %s", entity.getUuidAsString())
		);
	}
}
