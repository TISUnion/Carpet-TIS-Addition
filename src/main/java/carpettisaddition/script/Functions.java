package carpettisaddition.script;

import carpet.script.CarpetContext;
import carpet.script.Expression;
import carpet.script.argument.BlockArgument;
import carpet.script.value.ListValue;
import carpet.script.value.NumericValue;
import carpet.script.value.Value;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.stream.Collectors;

public class Functions {
    public static void apply(Expression expr){
        expr.addLazyFunction("register_block", -1, (c, t, lv)->{

            CarpetContext cc = (CarpetContext)c;
            BlockArgument blockLocator = BlockArgument.findIn(cc, lv, 0);
            BlockPos registeredBlock = blockLocator.block.getPos();

            boolean wasAlreadyThere = MicroTimingLoggerManager.trackedPositions.contains(registeredBlock);

            MicroTimingLoggerManager.trackedPositions.add(registeredBlock);

            return (_c, _t)->new NumericValue(wasAlreadyThere);
        });

        expr.addLazyFunction("deregister_block", -1, (c, t, lv)->{

            CarpetContext cc = (CarpetContext)c;
            BlockArgument blockLocator = BlockArgument.findIn(cc, lv, 0);
            BlockPos registeredBlock = blockLocator.block.getPos();

            return (_c, _t)-> new NumericValue(MicroTimingLoggerManager.trackedPositions.add(registeredBlock));
        });

        expr.addLazyFunction("register_block", -1, (c, t, lv)->{

            CarpetContext cc = (CarpetContext)c;
            BlockArgument blockLocator = BlockArgument.findIn(cc, lv, 0);
            BlockPos registeredBlock = blockLocator.block.getPos();

            return (_c, _t)-> new NumericValue(MicroTimingLoggerManager.trackedPositions.remove(registeredBlock));
        });

        expr.addLazyFunction("registered", -1, (c, t, lv)->{

            if(lv.size()==0){
                List<Value> blockList = MicroTimingLoggerManager.trackedPositions.stream().map(b-> ListValue.fromTriple(b.getX(),b.getY(),b.getZ())).collect(Collectors.toList());
                return (_c, _t) -> new ListValue(blockList);
            }

            CarpetContext cc = (CarpetContext)c;
            BlockArgument blockLocator = BlockArgument.findIn(cc, lv, 0);
            BlockPos registeredBlock = blockLocator.block.getPos();

            return (_c, _t)-> new NumericValue(MicroTimingLoggerManager.trackedPositions.contains(registeredBlock));
        });
    }
}
