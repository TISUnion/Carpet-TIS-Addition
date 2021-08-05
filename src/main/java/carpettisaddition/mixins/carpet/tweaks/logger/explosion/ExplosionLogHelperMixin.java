package carpettisaddition.mixins.carpet.tweaks.logger.explosion;

import carpet.utils.Messenger;
import carpettisaddition.helpers.carpet.tweaks.logger.explosion.ITntEntity;
import carpettisaddition.utils.TextUtil;
import carpettisaddition.utils.compact.ExplosionLogHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Dynamic;
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

	@Dynamic
	@Inject(
			method = "lambda$onExplosionDone$1",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;toArray([Ljava/lang/Object;)[Ljava/lang/Object;",
					remap = false
			),
			remap = false
	)
	private void attachBlockDestroyedWarning(List<BaseText> messages, String option, CallbackInfoReturnable<BaseText[]> cir)
	{
		if (this.entity instanceof TntEntity)
		{
			ITntEntity iTntEntity = (ITntEntity)this.entity;
			if (iTntEntity.dataRecorded())
			{
				String angleString = String.valueOf(this.calculateAngle(iTntEntity.getInitializedVelocity()));
				String posString = TextUtil.getCoordinateString(iTntEntity.getInitializedPosition());
				BaseText tntText = TextUtil.getFancyText(
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
			}
		}
	}
}
