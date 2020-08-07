package carpettisaddition.mixins.carpet;

import carpet.settings.SettingsManager;
import carpet.utils.Messenger;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static carpet.utils.Translations.tr;
import static carpettisaddition.CarpetTISAdditionServer.fancyName;
import static carpettisaddition.CarpetTISAdditionServer.version;

@Mixin(SettingsManager.class)
public class SettingsManagerMixin {
    @Inject(method = "listAllSettings", at = @At(
            value = "INVOKE",
            target = "Lcarpet/settings/SettingsManager;listSettings(Lnet/minecraft/server/command/ServerCommandSource;Ljava/lang/String;Ljava/util/Collection;)I",
            shift = At.Shift.AFTER
    ))
    private void printAddonVersion(ServerCommandSource source, CallbackInfoReturnable<Integer> cir) {
        Messenger.m(source,
                String.format("g %s ", fancyName),
                String.format("g %s: ", tr("ui.version",  "version")),
                String.format("g %s", version)
        );
    }
}
