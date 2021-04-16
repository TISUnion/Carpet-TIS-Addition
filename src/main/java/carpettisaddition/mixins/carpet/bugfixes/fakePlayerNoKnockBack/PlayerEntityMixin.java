package carpettisaddition.mixins.carpet.bugfixes.fakePlayerNoKnockBack;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// lower priority so it will more likely be mixin later
@Mixin(value = PlayerEntity.class, priority = 500)
public abstract class PlayerEntityMixin
{
	@Redirect(
			method = "attack",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/entity/Entity;velocityModified:Z",
					ordinal = 0
			),
			require = 0  // in case it's fixed in fabric carpet using the same redirect
	)
	private boolean velocityModifiedAndNotCarpetFakePlayer(Entity target)
	{
		return target.velocityModified && !(target instanceof EntityPlayerMPFake);
	}
}
