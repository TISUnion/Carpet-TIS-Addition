package carpettisaddition.mixins.carpet.tntRandomRange;

import carpet.CarpetSettings;
import carpettisaddition.helpers.tntRandomRange.IWorld;
import carpettisaddition.helpers.tntRandomRange.WrappedRandom;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// increase priority to make the wrapWorldRandom injection be in front of other cancellable injections
@Mixin(value = Explosion.class, priority = 800)
public class ExplosionMixin
{
	@Shadow @Final
	private World world;

	@Inject(method = "collectBlocksAndDamageEntities", at = @At("HEAD"))
	private void wrapWorldRandom(CallbackInfo ci)
	{
		if (CarpetSettings.tntRandomRange != -1.0D)
		{
			((IWorld)this.world).setRandomCTA(WrappedRandom.wrap(this.world.random));
		}
	}

	@Inject(method = "collectBlocksAndDamageEntities", at = @At("RETURN"))
	private void unwrapWorldRandom(CallbackInfo ci)
	{
		if (this.world.random instanceof WrappedRandom)
		{
			((IWorld)this.world).setRandomCTA(((WrappedRandom)this.world.random).unwrap());
		}
	}
}
