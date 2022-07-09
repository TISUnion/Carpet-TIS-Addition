package carpettisaddition.mixins.command.info.entity;

import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemEntity.class)
public interface ItemEntityAccessor
{
	@Accessor(
			//#if MC >= 11700
			//$$ "itemAge"
			//#endif
	)
	int getAge();
}
