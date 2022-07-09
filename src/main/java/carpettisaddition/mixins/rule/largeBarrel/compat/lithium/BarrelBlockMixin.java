package carpettisaddition.mixins.rule.largeBarrel.compat.lithium;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockWithEntity;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11700
//$$ import carpettisaddition.CarpetTISAdditionSettings;
//$$ import me.jellysquid.mods.lithium.common.hopper.RemovableBlockEntity;
//$$ import me.jellysquid.mods.lithium.common.world.blockentity.BlockEntityGetter;
//$$ import net.minecraft.block.BlockState;
//$$ import net.minecraft.block.entity.BarrelBlockEntity;
//$$ import net.minecraft.block.entity.BlockEntity;
//$$ import net.minecraft.util.math.BlockPos;
//$$ import net.minecraft.util.math.Direction;
//$$ import net.minecraft.world.World;
//$$ import net.minecraft.world.WorldAccess;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

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

	//#if MC >= 11700
	//$$ /**
	//$$  * aka "block.hopper" optimization in lithium
	//$$  * based on {@link me.jellysquid.mods.lithium.mixin.block.hopper.BlockEntityMixin}
	//$$  */
	//$$ private static final boolean LITHIUM_HOPPER_OPTIMIZATION_LOADED = RemovableBlockEntity.class.isAssignableFrom(BlockEntity.class);
 //$$
 //$$
	//$$ @SuppressWarnings("deprecation")
	//$$ @Override
	//$$ public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved)
	//$$ {
	//$$ 	super.onBlockAdded(state, world, pos, oldState, moved);
	//$$ 	if (CarpetTISAdditionSettings.largeBarrel)
	//$$ 	{
	//$$ 		resetLithiumHopperCache(world, pos, state);
	//$$ 	}
	//$$ }
 //$$
	//$$ @Inject(method = "onStateReplaced", at = @At("RETURN"))
	//$$ public void resetLithiumHopperCacheForLargeBarrel(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci)
	//$$ {
	//$$ 	if (CarpetTISAdditionSettings.largeBarrel)
	//$$ 	{
	//$$ 		resetLithiumHopperCache(world, pos, state);
	//$$ 	}
	//$$ }
 //$$
	//$$ private static void resetLithiumHopperCache(WorldAccess world, BlockPos changedBarrelPos, BlockState changedBarrelState)
	//$$ {
	//$$ 	if (LITHIUM_HOPPER_OPTIMIZATION_LOADED && !world.isClient())
	//$$ 	{
	//$$ 		Direction changedBarrelDirection = changedBarrelState.get(BarrelBlock.FACING);
	//$$ 		BlockPos affectedBarrelPos = changedBarrelPos.offset(changedBarrelDirection.getOpposite());
	//$$ 		BlockState affectedBarrelState = world.getBlockState(affectedBarrelPos);
	//$$ 		if (affectedBarrelState.getBlock() instanceof BarrelBlock && affectedBarrelState.get(BarrelBlock.FACING) == changedBarrelDirection.getOpposite())
	//$$ 		{
	//$$ 			BlockEntity barrelBlockEntity = ((BlockEntityGetter)world).getLoadedExistingBlockEntity(affectedBarrelPos);
	//$$ 			if (barrelBlockEntity instanceof BarrelBlockEntity && barrelBlockEntity instanceof RemovableBlockEntity)
	//$$ 			{
	//$$ 				// let lithium re-calculate the target inventory via HopperHelper#vanillaGetBlockInventory
	//$$ 				// we have a nice mixin injection there to handle largeBarrel like vanilla
	//$$ 				((RemovableBlockEntity)barrelBlockEntity).increaseRemoveCounter();
	//$$ 			}
	//$$ 		}
	//$$ 	}
	//$$ }
	//#endif
}