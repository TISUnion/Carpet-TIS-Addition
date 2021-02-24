package carpettisaddition.script;

import carpet.script.CarpetEventServer.Event;
import carpet.script.value.ListValue;
import carpet.script.value.StringValue;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.logging.loggers.microtiming.events.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.Arrays;

public class MicroTimingEvent extends Event {

    public static void noop() {} //to load events before scripts do

    public MicroTimingEvent(String name, int reqArgs, boolean isGlobalOnly) {
        super(name, reqArgs, isGlobalOnly);
    }

    public static void determineBlockEvent(BaseEvent event, World world, BlockPos pos){
        if(event instanceof DetectBlockUpdateEvent)
            MICRO_TIMING_EVENT.onMicroTimingEvent("detected_block_update", pos, world.getDimension());
        if(event instanceof BlockStateChangeEvent)
            MICRO_TIMING_EVENT.onMicroTimingEvent("block_state_changed", pos, world.getDimension());
        if(event instanceof ExecuteBlockEventEvent)
            MICRO_TIMING_EVENT.onMicroTimingEvent("executed_block_event", pos, world.getDimension());
        if(event instanceof ExecuteTileTickEvent)
            MICRO_TIMING_EVENT.onMicroTimingEvent("executed_tile_tick", pos, world.getDimension());
        if(event instanceof EmitBlockUpdateEvent)
            MICRO_TIMING_EVENT.onMicroTimingEvent("emitted_block_update", pos, world.getDimension());
        if(event instanceof EmitBlockUpdateRedstoneDustEvent)
            MICRO_TIMING_EVENT.onMicroTimingEvent("emitted_block_update_redstone_dust", pos, world.getDimension());
        if(event instanceof ScheduleBlockEventEvent)
            MICRO_TIMING_EVENT.onMicroTimingEvent("scheduled_block_event", pos, world.getDimension());
        if(event instanceof ScheduleTileTickEvent)
            MICRO_TIMING_EVENT.onMicroTimingEvent("scheduled_tile_tick", pos, world.getDimension());
    }

    public void onMicroTimingEvent(String type, BlockPos pos, DimensionType dimension){}

    public static MicroTimingEvent MICRO_TIMING_EVENT = new MicroTimingEvent("microtiming_event", 2, true) {
        @Override
        public void onMicroTimingEvent(String type, BlockPos pos, DimensionType dimension) {
            this.handler.call(
                    () -> Arrays.asList(StringValue.of(type), ListValue.fromTriple(pos.getX(), pos.getY(), pos.getZ()), StringValue.of(dimension.toString())),
                    () -> CarpetTISAdditionServer.minecraft_server.getCommandSource()
            );
        }
    };
}
