package carpettisaddition.logging.loggers.turtleegg;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.utils.TextUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TurtleEggLogger extends AbstractLogger
{
	public static final String NAME = "turtleEgg";

	private static final TurtleEggLogger INSTANCE = new TurtleEggLogger();

	private TurtleEggLogger()
	{
		super(NAME);
	}

	public static TurtleEggLogger getInstance()
	{
		return INSTANCE;
	}

	public void onBreakingEgg(World world, BlockPos pos, BlockState state, Entity entity)
	{
		if (!world.isClient && ExtensionLoggerRegistry.__turtleEgg)
		{
			return;
		}
		LoggerRegistry.getLogger(NAME).log(() -> {
			//  xxx breaks egg @ {}
			return new BaseText[]{Messenger.c(
					"g [\uD83E\uDD5A] ",
					entity.getName(),
					"w  x ",
					TextUtil.getBlockName(state.getBlock()),
					"g  @ ",
					TextUtil.getCoordinateText(null, pos, world.getDimension().getType())
			)};
		});
	}
}
