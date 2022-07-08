package carpettisaddition.mixins.carpet.access;

import carpet.utils.Messenger;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.text.BaseText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.15"))
@Mixin(Messenger.class)
public interface MessengerInvoker
{
	//#if MC < 11500
	//$$ @Invoker(value = "_applyStyleToTextComponent", remap = false)
	//$$ static BaseText call_applyStyleToTextComponent(BaseText comp, String style)
	//$$ {
	//$$ 	return null;
	//$$ }
	//#endif
}