package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.utils.compat.DummyClass;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DummyClass.class)
public abstract class WitherEntityMixin
{
	// in 1.14, wither entity despawn due to difficulty is not handled in WitherEntity class
	// but in HostileEntity class
}
