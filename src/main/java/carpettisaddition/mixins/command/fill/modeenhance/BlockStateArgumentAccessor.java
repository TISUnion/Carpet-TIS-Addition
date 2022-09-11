package carpettisaddition.mixins.command.fill.modeenhance;

import net.minecraft.command.arguments.BlockStateArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(BlockStateArgument.class)
public interface BlockStateArgumentAccessor
{
	@Accessor
	Set<Property<?>> getProperties();

	@Accessor
	CompoundTag getData();
}
