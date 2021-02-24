package carpettisaddition.mixins.carpet;

import carpet.settings.SettingsManager;
import carpet.utils.Messenger;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static carpet.utils.Translations.tr;
import static carpettisaddition.CarpetTISAdditionServer.fancyName;
import static carpettisaddition.CarpetTISAdditionServer.version;


@Mixin(SettingsManager.class)
public class SettingsManagerMixin
{
    @SuppressWarnings("DefaultAnnotationParam")
    @Inject(
            method = "listAllSettings",
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=ui.version",  // after printed fabric-carpet version
                            ordinal = 0
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/command/ServerCommandSource;getPlayer()Lnet/minecraft/server/network/ServerPlayerEntity;",
                    ordinal = 0,
                    remap = true
            ),
            remap = false
    )
    private void printAdditionVersion(ServerCommandSource source, CallbackInfoReturnable<Integer> cir) {
        Messenger.m(source,
                String.format("g %s ", fancyName),
                String.format("g %s: ", tr("ui.version",  "version")),
                String.format("g %s", version)
        );
    }
}
