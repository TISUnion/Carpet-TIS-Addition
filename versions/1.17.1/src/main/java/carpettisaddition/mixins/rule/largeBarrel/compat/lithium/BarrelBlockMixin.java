package carpettisaddition.mixins.rule.largeBarrel.compat.lithium;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.jellysquid.mods.lithium.common.hopper.RemovableBlockEntity;
import me.jellysquid.mods.lithium.common.world.blockentity.BlockEntityGetter;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"),
		@Condition(ModIds.lithium)
})
@Mixin(value = BarrelBlock.class, priority = 2000)
public abstract class BarrelBlockMixin extends BlockWithEntity
{
	protected BarrelBlockMixin(Settings settings)
	{
		super(settings);
	}

	/**
	 * aka "block.hopper" optimization in lithium
	 * reference:
	 * - lithium < 0.9.0: {@link me.jellysquid.mods.lithium.mixin.block.hopper.BlockEntityMixin}
	 * - lithium >= 0.9.0 (>=1.19): {@link me.jellysquid.mods.lithium.mixin.util.inventory_change_listening.BlockEntityMixin}
	 */
	private static final boolean LITHIUM_HOPPER_OPTIMIZATION_LOADED = RemovableBlockEntity.class.isAssignableFrom(BarrelBlockEntity.class);

	@SuppressWarnings("deprecation")
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved)
	{
		super.onBlockAdded(state, world, pos, oldState, moved);
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			resetLithiumHopperCache(world, pos, state);
		}
	}

	@Inject(method = "onStateReplaced", at = @At("RETURN"))
	public void resetLithiumHopperCacheForLargeBarrel(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			resetLithiumHopperCache(world, pos, state);
		}
	}

	private static void resetLithiumHopperCache(WorldAccess world, BlockPos changedBarrelPos, BlockState changedBarrelState)
	{
		if (LITHIUM_HOPPER_OPTIMIZATION_LOADED && !world.isClient())
		{
			Direction changedBarrelDirection = changedBarrelState.get(BarrelBlock.FACING);
			BlockPos affectedBarrelPos = changedBarrelPos.offset(changedBarrelDirection.getOpposite());
			BlockState affectedBarrelState = world.getBlockState(affectedBarrelPos);
			if (affectedBarrelState.getBlock() instanceof BarrelBlock && affectedBarrelState.get(BarrelBlock.FACING) == changedBarrelDirection.getOpposite())
			{
				BlockEntity barrelBlockEntity = ((BlockEntityGetter)world).getLoadedExistingBlockEntity(affectedBarrelPos);
				if (barrelBlockEntity instanceof BarrelBlockEntity)
				{
					// let lithium re-calculate the target inventory via HopperHelper#vanillaGetBlockInventory
					// we have a nice mixin injection there (HopperHelperMixin) to handle largeBarrel like vanilla
					((RemovableBlockEntity)barrelBlockEntity).increaseRemoveCounter();
				}
			}
		}
	}
}