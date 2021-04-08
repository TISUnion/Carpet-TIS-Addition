package carpettisaddition.mixins.carpet.rule.tntRandomRange;

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

	@Inject(method = "collectBlocksAndDamageEntities", at = @At("HEAD"))
	private void wrapWorldRandom(CallbackInfo ci)
	{
		// -1.0D is the default value of rule tntRandomRange
		if (CarpetSettings.tntRandomRange != -1.0D)
		{
			((WorldAccessor)this.world).setRandom(WrappedRandom.wrap(this.world.random));
		}
	}

	@Inject(method = "collectBlocksAndDamageEntities", at = @At("RETURN"))
	private void unwrapWorldRandom(CallbackInfo ci)
	{
		if (this.world.random instanceof WrappedRandom)
		{
			((WorldAccessor)this.world).setRandom(((WrappedRandom)this.world.random).unwrap());
		}
	}
}
