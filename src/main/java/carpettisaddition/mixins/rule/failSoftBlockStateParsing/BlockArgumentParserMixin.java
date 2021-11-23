package carpettisaddition.mixins.rule.failSoftBlockStateParsing;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.failSoftBlockStateParsing.DummyPropertyEnum;
import net.minecraft.command.arguments.BlockArgumentParser;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockArgumentParser.class)
public abstract class BlockArgumentParserMixin
{
	@Inject(
			method = "parsePropertyValue",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/StringReader;setCursor(I)V",
					remap = false
			),
			cancellable = true
	)
	private void failSoftBlockStateParsing(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.failSoftBlockStateParsing)
		{
			ci.cancel();
		}
	}

	/**
	 * Replace the property field with our DUMMY_PROPERTY so the following parsing is able to continue
	 * DUMMY_PROPERTY has no possible values (since it's an enum property with 0 enum value) so it doesn't provide
	 * any suggestion, and it will fail in BlockArgumentParser#parsePropertyValue which will be suppressed
	 * in our @Inject above
	 */
	@ModifyVariable(
			method = "parseBlockProperties",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/state/StateManager;getProperty(Ljava/lang/String;)Lnet/minecraft/state/property/Property;"
			),
			index = 3
	)
	private Property<?> failSoftBlockStateParsing(Property<?> value)
	{
		if (CarpetTISAdditionSettings.failSoftBlockStateParsing)
		{
			if (value == null)
			{
				value = DummyPropertyEnum.DUMMY_PROPERTY;
			}
		}
		return value;
	}
}
