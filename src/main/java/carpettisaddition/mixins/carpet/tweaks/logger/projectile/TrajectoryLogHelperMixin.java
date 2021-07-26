package carpettisaddition.mixins.carpet.tweaks.logger.projectile;

import carpet.logging.logHelpers.TrajectoryLogHelper;
import carpet.utils.Messenger;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.carpet.tweaks.logger.projectile.ProjectileLoggerTarget;
import carpettisaddition.helpers.carpet.tweaks.logger.projectile.TrajectoryLoggerUtil;
import carpettisaddition.helpers.carpet.tweaks.logger.projectile.VisualizeTrajectoryHelper;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(TrajectoryLogHelper.class)
public abstract class TrajectoryLogHelperMixin
{
	@Shadow(remap = false) private ArrayList<Vec3d> positions;
	@Shadow(remap = false) private boolean doLog;
	@Shadow(remap = false) @Final private static int MAX_TICKS_PER_LINE;

	// visualize logging
	@Unique
	private World world;
	@Unique
	private Entity entity;
	private boolean doVisualizeLogging;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void initTISCMStuffs(String logName, CallbackInfo ci)
	{
		this.doVisualizeLogging = false;
		if (TrajectoryLoggerUtil.isVisualizer.get())
		{
			TrajectoryLoggerUtil.isVisualizer.set(false);
			this.doLog = false;
			return;
		}
		if ("projectiles".equals(logName))
		{
			this.entity = TrajectoryLoggerUtil.currentEntity.get();
			TrajectoryLoggerUtil.currentEntity.set(null);
			if (this.entity != null)
			{
				this.world = this.entity.getEntityWorld();
				this.doVisualizeLogging = this.world != null;
			}
		}
	}

	@Inject(method = "onFinish", at = @At("HEAD"), remap = false, cancellable = true)
	private void checkIfIsVisualizeLogger(CallbackInfo ci)
	{
		if (this.doVisualizeLogging)
		{
			if (VisualizeTrajectoryHelper.isVisualizeProjectile(this.entity))
			{
				this.doLog = false;
			}
			else
			{
				VisualizeTrajectoryHelper.clearVisualizers();
			}
		}
	}

	private Optional<HitResult> getHitResult()
	{
		if (this.entity instanceof ProjectileLoggerTarget)
		{
			return Optional.ofNullable(((ProjectileLoggerTarget)this.entity).getHitResult());
		}
		return Optional.empty();
	}

	/**
	 * lambda method in {@link TrajectoryLogHelper#onFinish}
	 */
	@SuppressWarnings("UnresolvedMixinReference")
	@Inject(method = "lambda$onFinish$0", at = @At("TAIL"), remap = false, cancellable = true)
	private void projectileLoggerEnhance(String option, CallbackInfoReturnable<BaseText[]> cir)
	{
		List<BaseText> comp = Lists.newArrayList(cir.getReturnValue());
		Optional<HitResult> hitResultOptional = getHitResult();
		Vec3d hitPos = null;
		BaseText hitType = null;
		if (hitResultOptional.isPresent())
		{
			hitPos = hitResultOptional.get().getPos();
			if (hitResultOptional.get() instanceof BlockHitResult)
			{
				hitType = Messenger.c("w block ", TextUtil.getCoordinateText(null, ((BlockHitResult)hitResultOptional.get()).getBlockPos(), this.world.getDimension().getType()));
			}
			else if (hitResultOptional.get() instanceof EntityHitResult)
			{
				hitType = Messenger.c("w entity (", TextUtil.getEntityText(null, ((EntityHitResult)hitResultOptional.get()).getEntity()), "w )");
			}
			else
			{
				hitType = Messenger.s("?");
			}
		}
		switch (option)
		{
			case "brief":
				if (hitResultOptional.isPresent())
				{
					BaseText lastLine = comp.get(comp.size() - 1);
					BaseText marker = TextUtil.getFancyText(
							"g",
							Messenger.s(" x"),
							Messenger.c(
									"w Hit: ",
									hitType,
									String.format("w \nx: %f", hitPos.getX()),
									String.format("w \ny: %f", hitPos.getY()),
									String.format("w \nz: %f", hitPos.getZ())
							),
							null
					);
					if (lastLine.getString().length() >= MAX_TICKS_PER_LINE * 2)  // " x".length() == 2
					{
						comp.add(marker);
					}
					else
					{
						comp.set(comp.size() - 1, (BaseText)lastLine.append(marker));
					}
				}
				break;
			case "full":
				if (hitResultOptional.isPresent())
				{
					comp.add(Messenger.c("w Hit: ", hitType));
				}
				break;
			case "visualize":
				if (this.doVisualizeLogging)
				{
					if (CarpetTISAdditionSettings.visualizeProjectileLoggerEnabled)
					{
						comp.add(Messenger.c(String.format("w Visualize projectile logger: visualized %d tick(s)", this.positions.size())));
						for (int i = 0; i < this.positions.size(); i++)
						{
							VisualizeTrajectoryHelper.createVisualizer(this.world, this.positions.get(i), String.valueOf(i));
						}
						hitResultOptional.ifPresent(hitResult -> VisualizeTrajectoryHelper.createVisualizer(this.world, hitResult.getPos(), "Hit"));
					}
					else
					{
						comp.add(Messenger.c("w visualize logger: visualize is not enabled"));  // TODO: click event for rule
					}
				}
				break;
		}
		cir.setReturnValue(comp.toArray(new BaseText[0]));
	}
}
