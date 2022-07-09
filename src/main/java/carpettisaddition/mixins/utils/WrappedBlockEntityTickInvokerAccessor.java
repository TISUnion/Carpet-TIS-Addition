package carpettisaddition.mixins.utils;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11700
//$$ import net.minecraft.world.chunk.BlockEntityTickInvoker;
//$$ import org.spongepowered.asm.mixin.gen.Accessor;
//#else
import carpettisaddition.utils.compat.DummyClass;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"))
@Mixin(
		//#if MC >= 11700
		//$$ targets = "net.minecraft.world.chunk.WorldChunk$WrappedBlockEntityTickInvoker"
		//#else
		DummyClass.class
		//#endif
)
public interface WrappedBlockEntityTickInvokerAccessor<T extends BlockEntity>
{
	//#if MC >= 11700
	//$$ @Accessor
	//$$ BlockEntityTickInvoker getWrapped();
	//#endif
}