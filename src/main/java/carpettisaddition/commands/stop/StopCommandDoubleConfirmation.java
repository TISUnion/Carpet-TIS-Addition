package carpettisaddition.commands.stop;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class StopCommandDoubleConfirmation
{
	private static final Object LOCK = new Object();
	private static final long CONFIRM_WAIT_DURATION_MS = 60 * 1000;  // 60s
	private static long previousExecuteMs = -1;

	public static void handleDoubleConfirmation(CommandContext<ServerCommandSource> commandContext, CallbackInfoReturnable<Integer> cir)
	{
		if (!CarpetTISAdditionSettings.stopCommandDoubleConfirmation)
		{
			return;
		}

		long currentTimeMs = System.currentTimeMillis();
		synchronized (LOCK)
		{
			if (previousExecuteMs > 0 && currentTimeMs - previousExecuteMs <= CONFIRM_WAIT_DURATION_MS)
			{
				// double confirmed, do the /stop
				return;
			}

			// 1st time or confirmation timeout
			previousExecuteMs = currentTimeMs;
		}

		Messenger.tell(commandContext.getSource(), Messenger.fancy(
				Messenger.tr("carpettisaddition.command.stop.double_confirmation.message"),
				Messenger.tr("carpettisaddition.command.stop.double_confirmation.hover_hint"),
				new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/carpet stopCommandDoubleConfirmation")
		));
		cir.setReturnValue(0);
	}
}
