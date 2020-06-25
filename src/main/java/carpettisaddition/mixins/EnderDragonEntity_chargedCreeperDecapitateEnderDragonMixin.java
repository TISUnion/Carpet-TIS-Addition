package carpettisaddition.mixins;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntity_chargedCreeperDecapitateEnderDragonMixin extends MobEntity implements Monster
{
	private boolean flagDropHead;

	public EnderDragonEntity_chargedCreeperDecapitateEnderDragonMixin(EntityType<? extends EnderDragonEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Inject(
			method = "<init>",
			at = @At(value = "RETURN")
	)
	private void onConstructed(EntityType<? extends EnderDragonEntity> entityType, World world, CallbackInfo ci)
	{
		this.flagDropHead = false;
	}

	@Inject(
			method = "updatePostDeath",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;remove()V"
			)
	)
	private void dropHead(CallbackInfo ci)
	{
		if (this.flagDropHead)
		{
			this.dropStack(new ItemStack(Items.DRAGON_HEAD));
		}
	}

	@Inject(
			method = "damagePart",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;setHealth(F)V"
			)
	)
	private void onTakingDeathDamage(EnderDragonPart enderDragonPart, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetTISAdditionSettings.renewableDragonHead)
		{
			Entity entity = damageSource.getAttacker();
			if (entity instanceof CreeperEntity)
			{
				CreeperEntity creeperEntity = (CreeperEntity) entity;
				if (creeperEntity.shouldDropHead())
				{
					creeperEntity.onHeadDropped();
					this.flagDropHead = true;
				}
			}
		}
	}
}
