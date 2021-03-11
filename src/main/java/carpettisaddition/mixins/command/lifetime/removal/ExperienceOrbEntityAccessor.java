package carpettisaddition.mixins.command.lifetime.removal;

import net.minecraft.entity.ExperienceOrbEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ExperienceOrbEntity.class)
public interface ExperienceOrbEntityAccessor
{
	@Accessor(value = "amount")
	int getAmountCTA();

	@Accessor(value = "amount")
	void setAmountCTA(int amount);
}
