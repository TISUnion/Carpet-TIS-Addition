package carpettisaddition.script;

//#if MC >= 11600
//$$ import carpet.script.annotation.Locator;
//$$ import carpet.script.annotation.ScarpetFunction;
//$$ import carpet.script.value.ListValue;
//$$ import carpet.script.value.Value;
//$$ import carpet.script.value.ValueConversions;
//$$ import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
//$$ import net.minecraft.util.math.BlockPos;
//$$ import java.util.List;
//$$ import java.util.stream.Collectors;
//#endif

public class Functions
{
	//#if MC >= 11600
	//$$ @ScarpetFunction(maxParams = 3)
	//$$ public boolean register_block(@Locator.Block BlockPos pos) {
	//$$ 	return MicroTimingLoggerManager.trackedPositions.add(pos);
	//$$ }
 //$$
	//$$ @ScarpetFunction(maxParams = 3)
	//$$ public boolean unregister_block(@Locator.Block BlockPos pos) {
	//$$ 	return MicroTimingLoggerManager.trackedPositions.remove(pos);
	//$$ }
 //$$
	//$$ @ScarpetFunction
	//$$ public ListValue registered_blocks() {
	//$$ 	List<Value> blockList = MicroTimingLoggerManager.trackedPositions.stream().map(ValueConversions::of).collect(Collectors.toList());
	//$$ 	return new ListValue(blockList);
	//$$ }
 //$$
	//$$ @ScarpetFunction(maxParams = 3)
	//$$ public boolean is_registered(@Locator.Block BlockPos pos) {
	//$$ 	return MicroTimingLoggerManager.trackedPositions.contains(pos);
	//$$ }
	//#endif
}