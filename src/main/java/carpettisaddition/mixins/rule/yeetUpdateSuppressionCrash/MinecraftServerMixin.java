package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionException;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.crash.CrashException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.BooleanSupplier;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.17"))
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
	@Redirect(
			method = "tickWorlds",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V"
			)
	)
	private void yeetUpdateSuppressionCrash_implOnTickWorlds(ServerWorld serverWorld, BooleanSupplier shouldKeepTicking)
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash)
		{
			try
			{
				serverWorld.tick(shouldKeepTicking);
			}
			catch (CrashException e)
			{
				if (!(e.getCause() instanceof UpdateSuppressionException))
				{
					throw e;
				}
				UpdateSuppressionException.report((UpdateSuppressionException)e.getCause());
			}
			catch (UpdateSuppressionException e)
			{
				UpdateSuppressionException.report(e);
			}
		}
		else
		{
			// vanilla
			serverWorld.tick(shouldKeepTicking);
		}
	}
}
