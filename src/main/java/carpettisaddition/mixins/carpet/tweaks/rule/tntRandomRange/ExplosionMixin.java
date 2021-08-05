package carpettisaddition.mixins.carpet.tweaks.rule.tntRandomRange;

import carpet.CarpetSettings;
import carpettisaddition.helpers.carpet.tntRandomRange.WrappedRandom;
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

	private void tryUnWrapWorldRandom()
	{
		if (this.world.random instanceof WrappedRandom)
		{
			((WorldAccessor)this.world).setRandom(((WrappedRandom)this.world.random).unwrap());
		}
	}

	@Inject(method = "collectBlocksAndDamageEntities", at = @At("HEAD"))
	private void wrapWorldRandom(CallbackInfo ci)
	{
		// Do wrapping thing server side only
		if (!this.world.isClient())
		{
			this.tryUnWrapWorldRandom();
			if (CarpetSettings.tntRandomRange >= 0)
			{
				((WorldAccessor) this.world).setRandom(WrappedRandom.wrap(this.world.random));
			}
		}
	}

	@Inject(method = "collectBlocksAndDamageEntities", at = @At("RETURN"))
	private void unwrapWorldRandom(CallbackInfo ci)
	{
		this.tryUnWrapWorldRandom();
	}
}
