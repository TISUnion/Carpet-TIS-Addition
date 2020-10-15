package carpettisaddition;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class CarpetTISAdditionMixinPlugin implements IMixinConfigPlugin
{
	private final Logger LOGGER = LogManager.getLogger();
	private static final String LITHIUM_MOD_ID = "lithium";
	public static final String ON_SCHEDULE_TILE_TICK_EVENT_MAIN_MIXIN = "ServerTickSchedulerMixin";
	public static final String ON_SCHEDULE_TILE_TICK_EVENT_BACKUP_MIXIN = "ScheduleTileTickEventMixins";

	@Override
	public void onLoad(String mixinPackage)
	{

	}

	@Override
	public String getRefMapperConfig()
	{
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		boolean isLithiumLoaded = FabricLoader.getInstance().isModLoaded(LITHIUM_MOD_ID);
		if (mixinClassName.contains(ON_SCHEDULE_TILE_TICK_EVENT_BACKUP_MIXIN))
		{
			return isLithiumLoaded;
		}
		if (mixinClassName.contains(ON_SCHEDULE_TILE_TICK_EVENT_MAIN_MIXIN))
		{
			if (isLithiumLoaded)
			{
				LOGGER.info("Detected Lithium mod, loading backup mixin for onScheduleTileTick event");
				LOGGER.info("ScheduleTileTick events might not be fully listened");
				return false;
			}
			else
			{
				return true;
			}
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
	{

	}

	@Override
	public List<String> getMixins()
	{
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
	{

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
	{

	}
}
