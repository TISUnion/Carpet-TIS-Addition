package carpettisaddition.script;

import carpet.script.CarpetEventServer.Event;
import carpet.script.value.ListValue;
import carpet.script.value.NumericValue;
import carpet.script.value.StringValue;
import carpettisaddition.logging.loggers.microtiming.events.BaseEvent;
import carpettisaddition.logging.loggers.microtiming.events.BlockStateChangeEvent;
import carpettisaddition.logging.loggers.microtiming.events.DetectBlockUpdateEvent;
import carpettisaddition.logging.loggers.microtiming.events.EmitBlockUpdateEvent;
import carpettisaddition.logging.loggers.microtiming.events.EmitBlockUpdateRedstoneDustEvent;
import carpettisaddition.logging.loggers.microtiming.events.ExecuteBlockEventEvent;
import carpettisaddition.logging.loggers.microtiming.events.ExecuteTileTickEvent;
import carpettisaddition.logging.loggers.microtiming.events.ScheduleBlockEventEvent;
import carpettisaddition.logging.loggers.microtiming.events.ScheduleTileTickEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;

public class BlockEvents extends Event {
    public BlockEvents(String name, int reqArgs, boolean isGlobalOnly) {
        super(name, reqArgs, isGlobalOnly);
    }

    public static void determineBlockEvent(BaseEvent event, World world, BlockPos pos){
        if(event instanceof DetectBlockUpdateEvent)
            BLOCK_EVENT.onBlockEvent("detected_block_update", world, pos);
        if(event instanceof BlockStateChangeEvent)
            BLOCK_EVENT.onBlockEvent("block_state_changed", world, pos);
        if(event instanceof ExecuteBlockEventEvent)
            BLOCK_EVENT.onBlockEvent("executed_block_event", world, pos);
        if(event instanceof ExecuteTileTickEvent)
            BLOCK_EVENT.onBlockEvent("executed_tile_tick", world, pos);
        if(event instanceof EmitBlockUpdateEvent)
            BLOCK_EVENT.onBlockEvent("emitted_block_update", world, pos);
        if(event instanceof EmitBlockUpdateRedstoneDustEvent)
            BLOCK_EVENT.onBlockEvent("emitted_block_update_redstone_dust", world, pos);
        if(event instanceof ScheduleBlockEventEvent)
            BLOCK_EVENT.onBlockEvent("scheduled_block_event", world, pos);
        if(event instanceof ScheduleTileTickEvent)
            BLOCK_EVENT.onBlockEvent("scheduled_tile_tick", world, pos);
    }


    public void onBlockEvent(String type,World world, BlockPos pos){}

    public static BlockEvents BLOCK_EVENT = new BlockEvents("block_event", 1, true) {
        @Override
        public void onBlockEvent(String type, World world, BlockPos pos) {
            this.handler.call(
                    () -> Arrays.asList(new StringValue(type),new ListValue(Arrays.asList(new NumericValue(pos.getX()),new NumericValue(pos.getY()),new NumericValue(pos.getZ())))),
                    () -> world.getServer().getCommandSource()
            );
        }
    };
}
