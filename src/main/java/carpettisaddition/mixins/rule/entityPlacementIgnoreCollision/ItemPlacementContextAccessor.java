package carpettisaddition.mixins.rule.entityPlacementIgnoreCollision;

import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemPlacementContext.class)
public interface ItemPlacementContextAccessor
{
	@Accessor
	void setCanReplaceExisting(boolean value);
}
