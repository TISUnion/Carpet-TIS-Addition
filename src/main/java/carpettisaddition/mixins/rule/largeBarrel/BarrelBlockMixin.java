package carpettisaddition.mixins.rule.largeBarrel;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.NameableContainerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BarrelBlock.class)
public abstract class BarrelBlockMixin extends BlockWithEntity
{
	protected BarrelBlockMixin(Settings settings)
	{
		super(settings);
	}

	@ModifyArg(
			method = "activate",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/player/PlayerEntity;openContainer(Lnet/minecraft/container/NameableContainerFactory;)Ljava/util/OptionalInt;"
			)
	)
	private NameableContainerFactory largeBarrel(NameableContainerFactory nameableContainerFactory)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			if (nameableContainerFactory instanceof BarrelBlockEntity)
			{
				BarrelBlockEntity barrelBlockEntity = (BarrelBlockEntity) nameableContainerFactory;
				return this.createContainerFactory(barrelBlockEntity.getCachedState(), barrelBlockEntity.getWorld(), barrelBlockEntity.getPos());
			}
		}
		// vanilla
		return nameableContainerFactory;
	}

	/**
	 * Just like {@link net.minecraft.block.ChestBlock#createContainerFactory}
	 */
	@Nullable
	@Override
	public NameableContainerFactory createContainerFactory(BlockState state, World world, BlockPos pos)
	{
		NameableContainerFactory vanillaResult = super.createContainerFactory(state, world, pos);
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			return LargeBarrelHelper.getBlockEntitySource(state, world, pos).apply(LargeBarrelHelper.NAME_RETRIEVER).orElse(vanillaResult);
		}
		return vanillaResult;
	}

	@Inject(method = "getComparatorOutput", at = @At("HEAD"), cancellable = true)
	private void getLargeBarrelComparatorOutputMaybe(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<Integer> cir)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			cir.setReturnValue(Container.calculateComparatorOutput(LargeBarrelHelper.getInventory(state, world, pos)));
		}
	}
}
