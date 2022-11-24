package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionException;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Restriction(
		require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.17"),
		conflict = @Condition(value = ModIds.carpet_extra, versionPredicates = ">=1.4.14 <=1.4.43")
)
@Mixin(World.class)
public abstract class WorldMixin
{
	@SuppressWarnings("ConstantConditions")
	@ModifyVariable(
			method = "updateNeighbor",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;"
			)
	)
	private Throwable yeetUpdateSuppressionCrash_wrapStackOverflow(Throwable throwable, BlockPos sourcePos, Block sourceBlock, BlockPos neighborPos)
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash)
		{
			if (throwable instanceof StackOverflowError || throwable instanceof UpdateSuppressionException)
			{
				throw new UpdateSuppressionException((World)(Object)this, neighborPos);
			}
		}
		return throwable;
	}
}
