package carpettisaddition.mixins.rule.deobfuscateCrashReportStackTrace;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.deobfuscator.StackTraceDeobfuscator;
import net.minecraft.util.crash.CrashReportSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReportSection.class)
public abstract class CrashReportSectionMixin
{
	@Shadow private StackTraceElement[] stackTrace;

	@Inject(method = "addStackTrace", at = @At("HEAD"))
	private void deobfuscateCrashReportStackTrace_applyCrashReportSection(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.deobfuscateCrashReportStackTrace)
		{
			if (this.stackTrace != null)
			{
				this.stackTrace = StackTraceDeobfuscator.deobfuscateStackTrace(this.stackTrace);
			}
		}
	}
}
