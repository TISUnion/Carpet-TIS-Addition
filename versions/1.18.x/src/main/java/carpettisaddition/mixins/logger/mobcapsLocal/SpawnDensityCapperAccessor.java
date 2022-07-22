package carpettisaddition.mixins.logger.mobcapsLocal;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.SpawnDensityCapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(SpawnDensityCapper.class)
public interface SpawnDensityCapperAccessor
{
	@Accessor
	Map<ServerPlayerEntity, SpawnDensityCapper.DensityCap> getPlayersToDensityCap();
}