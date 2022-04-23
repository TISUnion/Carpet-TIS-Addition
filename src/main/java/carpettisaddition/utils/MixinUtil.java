package carpettisaddition.utils;

import carpettisaddition.CarpetTISAdditionServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class MixinUtil
{
	public static void audit(@Nullable ServerCommandSource source)
	{
		MutableText response;
		try
		{
			MixinEnvironment.getCurrentEnvironment().audit();
			response = Messenger.s("Mixin environment audited successfully");
		}
		catch (Exception e)
		{
			CarpetTISAdditionServer.LOGGER.error("Error when auditing mixin", e);
			response = Messenger.s(String.format("Mixin environment auditing failed, check console for more information (%s)", e));
		}
		if (source != null)
		{
			Messenger.tell(source, response);
		}
	}
}
