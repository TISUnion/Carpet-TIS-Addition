package carpettisaddition.mixins.carpet.hooks;

import carpet.api.settings.SettingsManager;
import carpet.utils.Translations;
import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.utils.Messenger;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static carpettisaddition.CarpetTISAdditionServer.fancyName;


@Mixin(SettingsManager.class)
public class SettingsManagerMixin
{
    @Inject(
            method = "listAllSettings",
            at = @At(
                    value = "INVOKE",
                    target = "Lcarpet/api/settings/SettingsManager;getCategories()Ljava/lang/Iterable;"
            ),
            remap = false
    )
    private void printAdditionVersion(ServerCommandSource source, CallbackInfoReturnable<Integer> cir) {
        Messenger.tell(
                source,
                Messenger.c(
                        String.format("g %s ", fancyName),
                        String.format("g %s: ", Translations.tr("ui.version",  "version")),
                        String.format("g %s", CarpetTISAdditionMod.getVersion())
                )
        );
    }
}
