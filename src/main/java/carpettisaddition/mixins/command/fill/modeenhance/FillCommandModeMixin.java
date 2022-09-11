package carpettisaddition.mixins.command.fill.modeenhance;

import carpettisaddition.commands.fill.modeenhance.FillModeEnhanceContext;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.BlockStateArgument;
import net.minecraft.server.command.FillCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Set;

@Mixin(FillCommand.Mode.class)
public class FillCommandModeMixin
{
	@Inject(
			method = "method_13358",  // lambda method in constructor parameter of FillCommand.Mode.REPLACE
			at = @At("HEAD"),
			remap = false,
			cancellable = true
	)
	private static <T extends Comparable<T>> void fillSoftReplaceLogic(BlockBox blockBox, BlockPos blockPos, BlockStateArgument blockStateArgument, ServerWorld serverWorld, CallbackInfoReturnable<BlockStateArgument> cir)
	{
		if (FillModeEnhanceContext.isSoftReplace.get())
		{
			BlockState existedBlockState = serverWorld.getBlockState(blockPos);
			BlockState targetBlockState = blockStateArgument.getBlockState();
			Collection<Property<?>> existedProperties = existedBlockState.getProperties();
			Collection<Property<?>> targetProperties = targetBlockState.getProperties();
			Set<Property<?>> specifiedProperties = ((BlockStateArgumentAccessor)blockStateArgument).getProperties();

			BlockState mergedBlockState = targetBlockState;
			Set<Property<?>> mergedProperties = Sets.newHashSet(specifiedProperties);
			for (Property<?> property1 : existedProperties)
			{
				@SuppressWarnings("unchecked")
				Property<T> property = (Property<T>)property1;

				if (targetProperties.contains(property) && !specifiedProperties.contains(property))
				{
					mergedBlockState = mergedBlockState.with(property, existedBlockState.get(property));
					mergedProperties.add(property);
				}
			}

			cir.setReturnValue(new BlockStateArgument(mergedBlockState, mergedProperties, ((BlockStateArgumentAccessor)blockStateArgument).getData()));
		}
	}
}
