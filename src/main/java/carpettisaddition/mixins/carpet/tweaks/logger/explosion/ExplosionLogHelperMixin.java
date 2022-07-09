package carpettisaddition.mixins.carpet.tweaks.logger.explosion;

import carpet.logging.logHelpers.ExplosionLogHelper;
import carpettisaddition.helpers.carpet.tweaks.logger.explosion.ITntEntity;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ExplosionLogHelper.class)
public abstract class ExplosionLogHelperMixin
{
	@Shadow(remap = false) @Final public Entity entity;

	/**
	 * velocity -> angle in [0, 2pi)
	 * for carpet rule hardcodeTNTangle
	 */
	private double calculateAngle(Vec3d velocity)
	{
		double vx = velocity.getX(), vz = velocity.getZ();
		double angle;
		if (vz != 0)
		{
			angle = Math.atan(vx / vz);
			if (vz > 0)
			{
				angle += Math.PI;
			}
			else if (vx > 0)
			{
				angle += 2 * Math.PI;
			}
		}
		else
		{
			angle = vx > 0 ? 1.5 * Math.PI : 0.5 * Math.PI;
		}
		return angle;
	}

	@Inject(
			method = "lambda$onExplosionDone$1",
			at = @At("RETURN"),
			remap = false,
			cancellable = true
	)
	private void attachBlockDestroyedWarning(
			//#if MC >= 11700
			//$$ long gametime, String option,
			//#else
			List<BaseText> messages_, String option,
			//#endif
			CallbackInfoReturnable<BaseText[]> cir
	)
	{
		if (this.entity instanceof TntEntity)
		{
			ITntEntity iTntEntity = (ITntEntity)this.entity;
			if (iTntEntity.dataRecorded())
			{
				List<BaseText> messages = Lists.newArrayList(cir.getReturnValue());

				String angleString = String.valueOf(this.calculateAngle(iTntEntity.getInitializedVelocity()));
				String posString = TextUtil.coord(iTntEntity.getInitializedPosition());
				BaseText tntText = Messenger.fancy(
						"r",
						Messenger.s("[TNT]"),
						Messenger.c(
								"w Initialized Data\n",
								String.format("w - Position: %s\n", posString),
								String.format("w - Angle: %s", angleString)
						),
						new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, posString + " " + angleString)
				);
				switch (option)
				{
					case "brief":
						BaseText lastMessage = messages.get(messages.size() - 1);
						messages.set(messages.size() - 1, Messenger.c(lastMessage, "w  ", tntText));
						break;
					case "full":
						messages.add(Messenger.c(Messenger.s("  "), tntText));
						break;
				}

				cir.setReturnValue(messages.toArray(new BaseText[0]));
			}
		}
	}
}
