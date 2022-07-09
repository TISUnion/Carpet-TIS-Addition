package carpettisaddition.mixins.logger.mobcapsLocal;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11800
//$$ import net.minecraft.server.network.ServerPlayerEntity;
//$$ import net.minecraft.world.SpawnDensityCapper;
//$$ import org.spongepowered.asm.mixin.gen.Accessor;
//$$ import java.util.Map;
//#else
import carpettisaddition.utils.compat.DummyClass;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(
		//#if MC >= 11800
		//$$ SpawnDensityCapper.class
		//#else
		DummyClass.class
		//#endif
)
public interface SpawnDensityCapperAccessor
{
	//#if MC >= 11800
	//$$ @Accessor
	//$$ Map<ServerPlayerEntity, SpawnDensityCapper.DensityCap> getPlayersToDensityCap();
	//#endif
}