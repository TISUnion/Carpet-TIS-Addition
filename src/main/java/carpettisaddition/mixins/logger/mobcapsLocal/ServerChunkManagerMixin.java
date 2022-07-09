package carpettisaddition.mixins.logger.mobcapsLocal;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.world.ServerChunkManager;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11800
//$$ import carpettisaddition.logging.loggers.mobcapsLocal.MobcapsLocalLogger;
//$$ import carpettisaddition.utils.compat.DimensionWrapper;
//$$ import net.minecraft.server.world.ServerWorld;
//$$ import net.minecraft.world.SpawnDensityCapper;
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(ServerChunkManager.class)
public abstract class ServerChunkManagerMixin
{
	//#if MC >= 11800
	//$$ @Shadow @Final ServerWorld world;
 //$$
	//$$ @ModifyArg(
	//$$ 		method = "tickChunks",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/world/SpawnHelper;setupSpawn(ILjava/lang/Iterable;Lnet/minecraft/world/SpawnHelper$ChunkSource;Lnet/minecraft/world/SpawnDensityCapper;)Lnet/minecraft/world/SpawnHelper$Info;"
	//$$ 		)
	//$$ )
	//$$ private SpawnDensityCapper mobcapsLocalLoggerRecordsCapper(SpawnDensityCapper capper)
	//$$ {
	//$$ 	MobcapsLocalLogger.getInstance().setCapper(DimensionWrapper.of(this.world), capper);
	//$$ 	return capper;
	//$$ }
	//#endif
}