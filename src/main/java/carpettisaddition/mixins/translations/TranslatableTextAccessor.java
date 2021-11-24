package carpettisaddition.mixins.translations;

import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(TranslatableText.class)
public interface TranslatableTextAccessor
{
	@Invoker
	void invokeSetTranslation(String translation, Consumer<StringVisitable> consumer);
}
