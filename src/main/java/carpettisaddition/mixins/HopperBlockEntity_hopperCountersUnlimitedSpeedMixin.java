package carpettisaddition.mixins;

import carpet.CarpetSettings;
import carpet.utils.WoolTool;
import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;


@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntity_hopperCountersUnlimitedSpeedMixin extends LootableContainerBlockEntity
{
	@Shadow protected abstract boolean insert();
	@Shadow protected abstract boolean isFull();
	@Shadow public abstract double getHopperX();
	@Shadow public abstract double getHopperY();
	@Shadow public abstract double getHopperZ();

	public HopperBlockEntity_hopperCountersUnlimitedSpeedMixin()
	{
		super(BlockEntityType.HOPPER);
	}

	@Inject(
			method = "insertAndExtract",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/entity/HopperBlockEntity;setCooldown(I)V"
			)
	)
	private void onActionSuccess(Supplier<Boolean> extractMethod, CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetSettings.hopperCounters && CarpetTISAdditionSettings.hopperCountersUnlimitedSpeed)
		{
			World world = getWorld();
			if (world == null)
			{
				return;
			}
			DyeColor wool_color = WoolTool.getWoolColorAtPosition(world, new BlockPos(getHopperX(), getHopperY(), getHopperZ()).offset(this.getCachedState().get(HopperBlock.FACING)));
			if (wool_color == null)
			{
				return;
			}
			for (int i = 0; i < Short.MAX_VALUE; i++)
			{
				boolean flag = false;
				if (!this.isInvEmpty())
				{
					flag = this.insert();
				}
				if (!this.isFull())
				{
					flag |= (Boolean)extractMethod.get();
				}
				if (!flag)
				{
					break;
				}
			}
		}
	}
}
