package carpettisaddition.script;

import carpet.script.CarpetEventServer.Event;
import carpet.script.value.ListValue;
import carpet.script.value.StringValue;
import carpettisaddition.CarpetTISAdditionServer;
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

import java.util.Arrays;

public class BlockEvents extends Event {

    public static void noop() {} //to load events before scripts do

    public BlockEvents(String name, int reqArgs, boolean isGlobalOnly) {
        super(name, reqArgs, isGlobalOnly);
    }

    public static void determineBlockEvent(BaseEvent event, BlockPos pos){
        if(event instanceof DetectBlockUpdateEvent)
            BLOCK_EVENT.onBlockEvent("detected_block_update", pos);
        if(event instanceof BlockStateChangeEvent)
            BLOCK_EVENT.onBlockEvent("block_state_changed", pos);
        if(event instanceof ExecuteBlockEventEvent)
            BLOCK_EVENT.onBlockEvent("executed_block_event", pos);
        if(event instanceof ExecuteTileTickEvent)
            BLOCK_EVENT.onBlockEvent("executed_tile_tick", pos);
        if(event instanceof EmitBlockUpdateEvent)
            BLOCK_EVENT.onBlockEvent("emitted_block_update", pos);
        if(event instanceof EmitBlockUpdateRedstoneDustEvent)
            BLOCK_EVENT.onBlockEvent("emitted_block_update_redstone_dust", pos);
        if(event instanceof ScheduleBlockEventEvent)
            BLOCK_EVENT.onBlockEvent("scheduled_block_event", pos);
        if(event instanceof ScheduleTileTickEvent)
            BLOCK_EVENT.onBlockEvent("scheduled_tile_tick", pos);
    }


    public void onBlockEvent(String type, BlockPos pos){}

    public static BlockEvents BLOCK_EVENT = new BlockEvents("block_event", 2, true) {
        @Override
        public void onBlockEvent(String type, BlockPos pos) {
            this.handler.call(
                    () -> Arrays.asList(StringValue.of(type), ListValue.fromTriple(pos.getX(), pos.getY(), pos.getZ())),
                    () -> CarpetTISAdditionServer.minecraft_server.getCommandSource()
            );
        }
    };
}
