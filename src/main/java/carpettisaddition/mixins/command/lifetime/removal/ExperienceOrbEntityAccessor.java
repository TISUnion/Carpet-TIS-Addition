package carpettisaddition.mixins.command.lifetime.removal;

import net.minecraft.entity.ExperienceOrbEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// used in 1.17+
@Mixin(ExperienceOrbEntity.class)
public interface ExperienceOrbEntityAccessor
{
	@Accessor(value = "amount")
	int getAmount$TISCM();

	@Accessor(value = "amount")
	void setAmount$TISCM(int amount);
}