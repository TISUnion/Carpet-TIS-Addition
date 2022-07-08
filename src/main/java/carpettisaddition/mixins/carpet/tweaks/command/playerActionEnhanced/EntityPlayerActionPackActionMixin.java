package carpettisaddition.mixins.carpet.tweaks.command.playerActionEnhanced;

import carpet.helpers.EntityPlayerActionPack;
import carpettisaddition.helpers.carpet.playerActionEnhanced.IEntityPlayerActionPackAction;
import carpettisaddition.helpers.carpet.playerActionEnhanced.PlayerActionPackHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerActionPack.Action.class)
public abstract class EntityPlayerActionPackActionMixin implements IEntityPlayerActionPackAction
{
	@Mutable
	@Shadow(remap = false) @Final public int interval;

	private boolean randomly = false;
	private int randomlyLowerBound;
	private int randomlyUpperBound;

	@Override
	public void setRandomlyRange(int lower, int upper)
	{
		this.randomly = true;
		this.randomlyLowerBound = lower;
		this.randomlyUpperBound = upper;
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "FIELD",
					target = "Lcarpet/helpers/EntityPlayerActionPack$Action;interval:I"
					//#if MC >= 11600
					//$$ , ordinal = 1
					//#endif
			),
			remap = false
	)
	private void changeIntervalRandomly(CallbackInfoReturnable<Boolean> cir)
	{
		if (this.randomly)
		{
			this.interval = PlayerActionPackHelper.getRandomInt(this.randomlyLowerBound, this.randomlyUpperBound);
		}
	}
}
