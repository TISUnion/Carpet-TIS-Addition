package carpettisaddition.mixins.carpet.commands.randomlyTriggerAction;

import carpet.helpers.EntityPlayerActionPack;
import carpettisaddition.helpers.carpet.randomlyTriggerAction.IEntityPlayerActionPackAction;
import carpettisaddition.helpers.carpet.randomlyTriggerAction.PlayerActionPackHelper;
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
