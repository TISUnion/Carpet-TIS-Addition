package carpettisaddition.mixins.logger.mobcapsLocal;

import carpet.utils.SpawnReporter;
import carpettisaddition.logging.loggers.mobcapsLocal.MobcapsLocalLogger;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.entity.SpawnGroup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SpawnReporter.class)
public abstract class SpawnReporterMixin
{
	@Shadow @Final public static int MAGIC_NUMBER;

	@Unique
	private static SpawnGroup currentSpawnGroup$CTA = null;

	@ModifyVariable(
			method = "printMobcapsForDimension",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/ArrayList;<init>()V",
					remap = false
			),
			ordinal = 0,
			remap = false
	)
	private static int applyMobcapsLocalLoggerValueOverrideForMaxMobLimit(int chunkcount)
	{
		Object2IntMap<SpawnGroup> mobcapsMap = MobcapsLocalLogger.getInstance().getMobcapsMap();
		if (mobcapsMap != null && currentSpawnGroup$CTA != null)
		{
			chunkcount = MAGIC_NUMBER;
		}
		return chunkcount;
	}

	@ModifyArg(
			method = "printMobcapsForDimension",
			at = @At(
					value = "INVOKE",
					target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap;getOrDefault(Ljava/lang/Object;I)I",
					remap = false
			),
			index = 0,
			remap = false
	)
	private static Object storeCurrentSpawnGroup(Object spawnGroup)
	{
		if (spawnGroup instanceof SpawnGroup)
		{
			currentSpawnGroup$CTA = (SpawnGroup)spawnGroup;
		}
		return spawnGroup;
	}

	@ModifyVariable(
			method = "printMobcapsForDimension",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap;getOrDefault(Ljava/lang/Object;I)I",
					ordinal = 0
			),
			ordinal = 3,  // chunkcount, var10 (loop index), var11 (loop limit), cur
			remap = false
	)
	private static int applyMobcapsLocalLoggerValueOverrideForCurrentMobCount(int cur)
	{
		Object2IntMap<SpawnGroup> mobcapsMap = MobcapsLocalLogger.getInstance().getMobcapsMap();
		if (mobcapsMap != null && currentSpawnGroup$CTA != null)
		{
			cur = mobcapsMap.getOrDefault(currentSpawnGroup$CTA, -1);
		}
		return cur;
	}
}
