package carpettisaddition.utils.mixin;

import carpettisaddition.utils.ModIds;
import me.jellysquid.mods.lithium.common.config.LithiumConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class LithiumConfigAccess
{
	@Nullable
	private static final Object config;

	private static boolean isLithiumLoaded()
	{
		return FabricLoader.getInstance().isModLoaded(ModIds.lithium);
	}

	/**
	 * For 1.15.2 lithium, check {@link me.jellysquid.mods.lithium.mixin.LithiumMixinPlugin#setupMixins}
	 * and fill interested rules in the switch statement below
	 *
	 * For 1.16+ lithium, {@link me.jellysquid.mods.lithium.common.config.LithiumConfig#getEffectiveOptionForMixin} exists
	 * so no more manual maintaining
	 */
	public static boolean isLithiumMixinRuleEnabled(String mixinRule)
	{
		if (config == null)
		{
			return false;
		}

		switch (mixinRule)
		{
			default:
				return false;
		}
	}

	static
	{
		if (isLithiumLoaded())
		{
			config = LithiumConfig.load(new File("./config/lithium.properties"));
		}
		else
		{
			config = null;
		}
	}
}
