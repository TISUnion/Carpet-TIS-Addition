package carpettisaddition.mixins.translations;

import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(TranslatableTextContent.class)
public interface TranslatableTextAccessor
{
	@Invoker
	void invokeForEachPart(String translation, Consumer<StringVisitable> partsConsumer);
}
