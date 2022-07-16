package carpettisaddition.mixins.command.manipulate.entity;

import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobEntity.class)
public interface MobEntityAccessor
{
	@Accessor("persistent")
	void setPersistent$TISCM(boolean value);
}
