package carpettisaddition.mixins.logger.microtiming.hooks;

import carpettisaddition.logging.loggers.microtiming.interfaces.ITileTickListWithServerWorld;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11800
//$$ import net.minecraft.world.tick.WorldTickScheduler;
//#else
import carpettisaddition.utils.compat.DummyClass;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(
		//#if MC >= 11800
		//$$ WorldTickScheduler.class
		//#else
		DummyClass.class
		//#endif
)
public abstract class ServerTickSchedulerMixin implements ITileTickListWithServerWorld
{
	private ServerWorld serverWorld$TISCM;

	@Override
	@Nullable
	public ServerWorld getServerWorld()
	{
		return this.serverWorld$TISCM;
	}

	@Override
	public void setServerWorld(ServerWorld serverWorld$TISCM)
	{
		this.serverWorld$TISCM = serverWorld$TISCM;
	}
}