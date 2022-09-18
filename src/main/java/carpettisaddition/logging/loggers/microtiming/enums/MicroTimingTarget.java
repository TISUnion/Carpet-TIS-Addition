package carpettisaddition.logging.loggers.microtiming.enums;

import carpettisaddition.utils.Messenger;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

public enum MicroTimingTarget
{
	LABELLED,
	IN_RANGE,
	ALL,
	MARKER_ONLY;

	public static final double IN_RANGE_RADIUS = 32.0D;

	public static void deprecatedWarning(@Nullable ServerCommandSource source)
	{
		if (source != null)
		{
			BaseText text = Messenger.tr("carpettisaddition.rule.microTimingTarget.deprecate_not_marker_warning");
			Messenger.tell(source, Messenger.formatting(text, Formatting.DARK_RED, Formatting.ITALIC));
		}
	}
}
