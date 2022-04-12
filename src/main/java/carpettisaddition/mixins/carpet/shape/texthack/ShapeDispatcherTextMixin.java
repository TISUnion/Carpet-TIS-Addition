package carpettisaddition.mixins.carpet.shape.texthack;

import carpet.script.utils.ShapeDispatcher;
import carpet.script.value.Value;
import carpettisaddition.helpers.carpet.shape.IShapeDispatcherText;
import carpettisaddition.helpers.carpet.shape.ScarpetDisplayedTextHack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ShapeDispatcher.DisplayedText.class)
public abstract class ShapeDispatcherTextMixin implements IShapeDispatcherText
{
	private boolean isMicroTimingMarkerText = false;

	@Override
	public boolean isMicroTimingMarkerText()
	{
		return this.isMicroTimingMarkerText;
	}

	@Inject(
			method = "init",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=align",
					ordinal = 0
			),
			remap = false
	)
	private void checkScarpetDisplayedTextHack(Map<String, Value> options, CallbackInfo ci)
	{
		if (options.containsKey("align"))
		{
			String alignStr = options.get("align").getString();
			if (ScarpetDisplayedTextHack.MICRO_TIMING_TEXT_MAGIC_STRING.equals(alignStr))
			{
				this.isMicroTimingMarkerText = true;
			}
		}
	}
}
