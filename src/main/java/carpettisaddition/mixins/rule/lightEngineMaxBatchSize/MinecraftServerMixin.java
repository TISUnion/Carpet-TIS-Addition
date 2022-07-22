package carpettisaddition.mixins.rule.lightEngineMaxBatchSize;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
	@ModifyArg(
			method = "prepareStartRegion",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/world/dimension/DimensionType;getAll()Ljava/lang/Iterable;"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerLightingProvider;setTaskBatchSize(I)V",
					ordinal = 0
			),
			index = 0
	)
	private int adjustBatchSize(int value)
	{
		return CarpetTISAdditionSettings.lightEngineMaxBatchSize;
	}
}
