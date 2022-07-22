package carpettisaddition.utils;

import carpettisaddition.CarpetTISAdditionServer;
import net.fabricmc.loader.api.FabricLoader;

public class AutoMixinAuditExecutor
{
	private static final String KEYWORD_PROPERTY = "carpettisaddition.mixin_audit";

	public static void run()
	{
		if (FabricLoader.getInstance().isDevelopmentEnvironment() && "true".equals(System.getProperty(KEYWORD_PROPERTY)))
		{
			CarpetTISAdditionServer.LOGGER.info("Triggered auto mixin audit");
			boolean ok = MixinUtil.audit(null);
			CarpetTISAdditionServer.LOGGER.info("Mixin audit result: " + (ok ? "successful" : "failed"));
			System.exit(ok ? 0 : 1);
		}
	}
}
