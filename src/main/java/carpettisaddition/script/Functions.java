package carpettisaddition.script;

import carpet.script.annotation.Locator;
import carpet.script.annotation.ScarpetFunction;
import carpet.script.value.BlockValue;
import carpet.script.value.ListValue;
import carpet.script.value.Value;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;

import java.util.List;
import java.util.stream.Collectors;

public class Functions {
    @ScarpetFunction(maxParams = 3)
    public boolean register_block(@Locator.Block BlockValue block) {
        return MicroTimingLoggerManager.trackedPositions.add(block.getPos());
    }
    
    @ScarpetFunction(maxParams = 3)
    public boolean unregister_block(@Locator.Block BlockValue block) {
        return MicroTimingLoggerManager.trackedPositions.remove(block.getPos());
    }
    
    @ScarpetFunction
    public ListValue registered_blocks() {
        List<Value> blockList = MicroTimingLoggerManager.trackedPositions.stream().map(b-> ListValue.fromTriple(b.getX(),b.getY(),b.getZ())).collect(Collectors.toList());
        return new ListValue(blockList);
    }
    
    @ScarpetFunction(maxParams = 3)
    public boolean is_registered(@Locator.Block BlockValue block) {
        return MicroTimingLoggerManager.trackedPositions.contains(block.getPos());
    }
}
