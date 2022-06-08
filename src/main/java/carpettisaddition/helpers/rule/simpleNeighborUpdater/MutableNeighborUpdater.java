package carpettisaddition.helpers.rule.simpleNeighborUpdater;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.block.ChainRestrictedNeighborUpdater;
import net.minecraft.world.block.NeighborUpdater;
import net.minecraft.world.block.SimpleNeighborUpdater;
import org.jetbrains.annotations.Nullable;

public class MutableNeighborUpdater implements NeighborUpdater {
    private final SimpleNeighborUpdater oldNeighborUpdater;
    private final ChainRestrictedNeighborUpdater newNeighborUpdater;

    public MutableNeighborUpdater(World world, ChainRestrictedNeighborUpdater updater) {
        this.oldNeighborUpdater = new SimpleNeighborUpdater(world);
        this.newNeighborUpdater = updater;
    }

    @Override
    public void replaceWithStateForNeighborUpdate(Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, int flags, int maxUpdateDepth) {
        if (CarpetTISAdditionSettings.simpleNeighborUpdaterEnabled) {
            oldNeighborUpdater.replaceWithStateForNeighborUpdate(direction, neighborState, pos, neighborPos, flags, maxUpdateDepth);
        } else {
            newNeighborUpdater.replaceWithStateForNeighborUpdate(direction, neighborState, pos, neighborPos, flags, maxUpdateDepth);
        }
    }

    @Override
    public void updateNeighbor(BlockPos pos, Block sourceBlock, BlockPos sourcePos) {
        if (CarpetTISAdditionSettings.simpleNeighborUpdaterEnabled) {
            oldNeighborUpdater.updateNeighbor(pos, sourceBlock, sourcePos);
        } else {
            newNeighborUpdater.updateNeighbor(pos, sourceBlock, sourcePos);
        }
    }

    @Override
    public void updateNeighbor(BlockState state, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (CarpetTISAdditionSettings.simpleNeighborUpdaterEnabled) {
            oldNeighborUpdater.updateNeighbor(state, pos, sourceBlock, sourcePos, notify);
        } else {
            newNeighborUpdater.updateNeighbor(state, pos, sourceBlock, sourcePos, notify);
        }
    }

    @Override
    public void updateNeighbors(BlockPos pos, Block sourceBlock, @Nullable Direction except) {
        if (CarpetTISAdditionSettings.simpleNeighborUpdaterEnabled) {
            oldNeighborUpdater.updateNeighbors(pos, sourceBlock, except);
        } else {
            NeighborUpdater.super.updateNeighbors(pos, sourceBlock, except);
        }
    }
}
