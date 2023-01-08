/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

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
