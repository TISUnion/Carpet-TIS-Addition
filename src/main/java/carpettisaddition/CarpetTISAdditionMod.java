package carpettisaddition;

import carpettisaddition.utils.AutoMixinAuditExecutor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class CarpetTISAdditionMod implements ModInitializer
{
	private static final String MOD_ID = "carpet-tis-addition";
	private static String version;

	@Override
	public void onInitialize()
	{
		version = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();

		CarpetTISAdditionServer.init();
		AutoMixinAuditExecutor.run();
	}

	public static String getModId()
	{
		return MOD_ID;
	}

	public static String getVersion()
	{
		return version;
	}
}
