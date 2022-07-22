package carpettisaddition.mixins.utils;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"))
@Mixin(targets = "net.minecraft.world.chunk.WorldChunk$WrappedBlockEntityTickInvoker")
public interface WrappedBlockEntityTickInvokerAccessor<T extends BlockEntity>
{
	@Accessor
	BlockEntityTickInvoker getWrapped();
}