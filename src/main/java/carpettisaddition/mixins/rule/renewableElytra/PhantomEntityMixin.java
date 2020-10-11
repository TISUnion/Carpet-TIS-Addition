package carpettisaddition.mixins.rule.renewableElytra;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(PhantomEntity.class)
public abstract class PhantomEntityMixin extends FlyingEntity
{
	public PhantomEntityMixin(EntityType<? extends PhantomEntity> entityType, World world)
	{
		super(entityType, world);
	}

	protected void dropLoot(DamageSource source, boolean causedByPlayer)
	{
		super.dropLoot(source, causedByPlayer);
		if (CarpetTISAdditionSettings.renewableElytra > 0.0D)
		{
			if (source.getAttacker() instanceof ShulkerEntity && this.random.nextDouble() < CarpetTISAdditionSettings.renewableElytra)
			{
				this.dropStack(new ItemStack(Items.ELYTRA));
			}
		}
	}
}
