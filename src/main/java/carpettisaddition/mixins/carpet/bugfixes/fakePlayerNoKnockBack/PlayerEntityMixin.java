package carpettisaddition.mixins.carpet.bugfixes.fakePlayerNoKnockBack;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin
{
	@Redirect(
			method = "attack",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/entity/Entity;velocityModified:Z",
					ordinal = 0
			)
	)
	private boolean velocityModifiedAndNotCarpetFakePlayer(Entity target)
	{
		return target.velocityModified && !(target instanceof EntityPlayerMPFake);
	}
}
