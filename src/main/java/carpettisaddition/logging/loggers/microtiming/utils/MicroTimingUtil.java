package carpettisaddition.logging.loggers.microtiming.utils;

import carpet.utils.Messenger;
import carpet.utils.WoolTool;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.state.property.Properties;
import net.minecraft.text.BaseText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;

public class MicroTimingUtil
{
	public static final Direction[] DIRECTION_VALUES = Direction.values();
	private static final Map<DyeColor, String> COLOR_STYLE = Maps.newHashMap();
	static
	{
		COLOR_STYLE.put(DyeColor.WHITE, "w");
		COLOR_STYLE.put(DyeColor.ORANGE, "d");
		COLOR_STYLE.put(DyeColor.MAGENTA, "m");
		COLOR_STYLE.put(DyeColor.LIGHT_BLUE, "c");
		COLOR_STYLE.put(DyeColor.YELLOW, "y");
		COLOR_STYLE.put(DyeColor.LIME, "l");
		COLOR_STYLE.put(DyeColor.PINK, "r");
		COLOR_STYLE.put(DyeColor.GRAY, "f");
		COLOR_STYLE.put(DyeColor.LIGHT_GRAY, "g");
		COLOR_STYLE.put(DyeColor.CYAN, "q");
		COLOR_STYLE.put(DyeColor.PURPLE, "p");
		COLOR_STYLE.put(DyeColor.BLUE, "v");
		COLOR_STYLE.put(DyeColor.BROWN, "n");
		COLOR_STYLE.put(DyeColor.GREEN, "e");
		COLOR_STYLE.put(DyeColor.RED, "r");
		COLOR_STYLE.put(DyeColor.BLACK, "k");
	}

	public static String getColorStyle(DyeColor color)
	{
		return COLOR_STYLE.getOrDefault(color, "w");
	}

	public static BaseText getColoredValue(Object value)
	{
		BaseText text = Messenger.s(value.toString());
		Formatting color = null;
		if (Boolean.TRUE.equals(value))
		{
			color = Formatting.GREEN;
		}
		else if (Boolean.FALSE.equals(value))
		{
			color = Formatting.RED;
		}
		if (value instanceof Number)
		{
			color = Formatting.GOLD;
		}
		if (color != null)
		{
			TextUtil.attachColor(text, color);
		}
		return text;
	}

	public static BaseText getSuccessText(boolean value, boolean showReturnValue, BaseText hoverExtra)
	{
		BaseText hintText = value ?
				Messenger.c("e " + MicroTimingLoggerManager.tr("Successful")) :
				Messenger.c("r " + MicroTimingLoggerManager.tr("Failed"));
		if (hoverExtra != null)
		{
			hintText.append(Messenger.c("w \n", hoverExtra));
		}
		if (showReturnValue)
		{
			hintText.append(Messenger.c(
					String.format("w \n%s: ", MicroTimingLoggerManager.tr("Return value")),
					getColoredValue(value)
			));
		}
		return value ?
				TextUtil.getFancyText("e", Messenger.s("√"), hintText, null) :
				TextUtil.getFancyText("r", Messenger.s("×"), hintText, null);
	}
	public static BaseText getSuccessText(boolean bool, boolean showReturnValue)
	{
		return getSuccessText(bool, showReturnValue, null);
	}

	private static boolean isPositionAvailable(World world, BlockPos pos)
	{
		return world.getChunkManager().shouldTickBlock(pos);
	}

	public static Optional<DyeColor> getWoolColor(World world, BlockPos pos)
	{
		if (!MicroTimingLoggerManager.isLoggerActivated() || !isPositionAvailable(world, pos))
		{
			return Optional.empty();
		}
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		BlockPos woolPos = pos;

		if (block instanceof ObserverBlock || block instanceof EndRodBlock ||
				block instanceof PistonBlock || block instanceof PistonExtensionBlock)
		{
			woolPos = pos.offset(state.get(Properties.FACING).getOpposite());
		}
		else if (block instanceof AbstractButtonBlock || block instanceof LeverBlock)
		{
			Direction facing;
			if (state.get(Properties.WALL_MOUNT_LOCATION) == WallMountLocation.FLOOR)
			{
				facing = Direction.UP;
			}
			else if (state.get(Properties.WALL_MOUNT_LOCATION) == WallMountLocation.CEILING)
			{
				facing = Direction.DOWN;
			}
			else
			{
				facing = state.get(Properties.HORIZONTAL_FACING);
			}
			woolPos = pos.offset(facing.getOpposite());
		}
		else if (block instanceof WallRedstoneTorchBlock || block instanceof TripwireHookBlock)
		{
			woolPos = pos.offset(state.get(Properties.HORIZONTAL_FACING).getOpposite());
		}
		else if (
				block instanceof AbstractRailBlock ||
				block instanceof AbstractRedstoneGateBlock ||
				block instanceof RedstoneTorchBlock ||
				block instanceof RedstoneWireBlock ||
				block instanceof AbstractPressurePlateBlock
		)  // on block
		{
			woolPos = pos.down();
		}
		else
		{
			return Optional.empty();
		}

		return Optional.ofNullable(WoolTool.getWoolColorAtPosition(world, woolPos));
	}

	public static Optional<DyeColor> getEndRodWoolColor(World world, BlockPos pos)
	{
		if (!MicroTimingLoggerManager.isLoggerActivated() || !isPositionAvailable(world, pos))
		{
			return Optional.empty();
		}
		for (Direction facing: DIRECTION_VALUES)
		{
			BlockPos blockEndRodPos = pos.offset(facing);
			BlockState iBlockState = world.getBlockState(blockEndRodPos);
			if (iBlockState.getBlock() == Blocks.END_ROD && iBlockState.get(FacingBlock.FACING).getOpposite() == facing)
			{
				BlockPos woolPos = blockEndRodPos.offset(facing);
				DyeColor color = WoolTool.getWoolColorAtPosition(world, woolPos);
				if (color != null)
				{
					return Optional.of(color);
				}
			}
		}
		return Optional.empty();
	}

	public static Optional<DyeColor> getWoolOrEndRodWoolColor(World world, BlockPos pos)
	{
		Optional<DyeColor> optionalDyeColor = getWoolColor(world, pos);
		if (!optionalDyeColor.isPresent())
		{
			optionalDyeColor = getEndRodWoolColor(world, pos);
		}
		return optionalDyeColor;
	}

	public static BaseText getTranslatedText(Block block)
	{
		return TextUtil.attachColor(new TranslatableText(block.getTranslationKey()), Formatting.WHITE);
	}

	public static String getFormattedDirectionString(Direction direction)
	{
		String name = direction.toString();
		String translatedName = MicroTimingLoggerManager.tr("direction." + name, name);
		char sign = direction.getDirection().offset() > 0 ? '+' : '-';
		return String.format("%s (%c%s)", translatedName, sign, direction.getAxis());
	}
}
