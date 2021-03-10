package carpettisaddition.mixins.command.lifetime.removal;

import net.minecraft.entity.ExperienceOrbEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ExperienceOrbEntity.class)
public interface ExperienceOrbEntityAccessor
{
	@Accessor
	int getAmount();

	@Accessor
	void setAmount(int amount);
}
