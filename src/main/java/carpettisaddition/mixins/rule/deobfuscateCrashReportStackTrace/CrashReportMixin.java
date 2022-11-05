package carpettisaddition.mixins.rule.deobfuscateCrashReportStackTrace;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.deobfuscator.StackTraceDeobfuscator;
import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrashReport.class)
public abstract class CrashReportMixin
{
	@Shadow private StackTraceElement[] stackTrace;

	@Inject(
			method = "addStackTrace",
			at = @At(
					value = "INVOKE",
					target = "Ljava/lang/StringBuilder;append(Ljava/lang/String;)Ljava/lang/StringBuilder;",
					ordinal = 0
			)
	)
	private void deobfuscateCrashReportStackTrace_applyCrashReport1(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.deobfuscateCrashReportStackTrace)
		{
			if (this.stackTrace != null)
			{
				this.stackTrace = StackTraceDeobfuscator.deobfuscateStackTrace(this.stackTrace);
			}
		}
	}

	@ModifyVariable(
			method = "getCauseAsString",
			at = @At(
					value = "INVOKE",
					target = "Ljava/io/StringWriter;<init>()V"
			),
			// this is needed for optifine compact
			// since optifine appends another Throwable local var in 1.17.1+ (at least e.g. 1.17.1 HD U H1)
			ordinal = 0
	)
	private Throwable deobfuscateCrashReportStackTrace_applyCrashReport2(Throwable throwable)
	{
		if (CarpetTISAdditionSettings.deobfuscateCrashReportStackTrace)
		{
			for (Throwable t = throwable; t != null; t = t.getCause())
			{
				t.setStackTrace(StackTraceDeobfuscator.deobfuscateStackTrace(t.getStackTrace()));
			}
		}
		return throwable;
	}
}
