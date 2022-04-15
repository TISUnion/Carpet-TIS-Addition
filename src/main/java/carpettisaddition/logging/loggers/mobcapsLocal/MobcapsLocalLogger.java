package carpettisaddition.logging.loggers.mobcapsLocal;

import carpet.utils.SpawnReporter;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import carpettisaddition.mixins.logger.mobcapsLocal.SpawnDensityCapperAccessor;
import carpettisaddition.mixins.logger.mobcapsLocal.SpawnDensityCapperDensityCapAccessor;
import carpettisaddition.utils.DimensionWrapper;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.world.SpawnDensityCapper;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class MobcapsLocalLogger extends AbstractHUDLogger
{
	public static final String NAME = "mobcapsLocal";
	private static final MobcapsLocalLogger INSTANCE = new MobcapsLocalLogger();

	private final Map<DimensionWrapper, SpawnDensityCapper> capperMap = Maps.newHashMap();
	@Nullable
	private Object2IntMap<SpawnGroup> mobcapsMap = null;

	private MobcapsLocalLogger()
	{
		super(NAME);
	}

	public static MobcapsLocalLogger getInstance()
	{
		return INSTANCE;
	}

	public void setCapper(DimensionWrapper dim, SpawnDensityCapper capper)
	{
		this.capperMap.put(dim, capper);
	}

	@Nullable
	public Object2IntMap<SpawnGroup> getMobcapsMap()
	{
		return mobcapsMap;
	}

	@Override
	public BaseText[] onHudUpdate(String option, PlayerEntity playerEntity)
	{
		SpawnDensityCapper capper = this.capperMap.get(DimensionWrapper.of(playerEntity));
		if (capper != null && playerEntity instanceof ServerPlayerEntity)
		{
			SpawnDensityCapper.DensityCap cap = ((SpawnDensityCapperAccessor)capper).getPlayersToDensityCap().getOrDefault(playerEntity, SpawnDensityCapperDensityCapAccessor.invokeConstructor());
			this.mobcapsMap = ((SpawnDensityCapperDensityCapAccessor) cap).getSpawnGroupsToDensity();
			BaseText result = SpawnReporter.printMobcapsForDimension(((ServerPlayerEntity)playerEntity).getWorld(), false).get(0);
			this.mobcapsMap = null;
			return new BaseText[]{Messenger.c("g [local] ", result)};
		}
		return new BaseText[]{Messenger.s("-- Not available --")};
	}
}
