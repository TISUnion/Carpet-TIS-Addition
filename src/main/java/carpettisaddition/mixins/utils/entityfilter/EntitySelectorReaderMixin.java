package carpettisaddition.mixins.utils.entityfilter;

import carpettisaddition.utils.entityfilter.IEntitySelector;
import com.mojang.brigadier.StringReader;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntitySelectorReader.class)
public abstract class EntitySelectorReaderMixin
{
	@Shadow private int startCursor;

	@Shadow @Final private StringReader reader;

	@Inject(method = "read", at = @At("TAIL"))
	private void storeReadString(CallbackInfoReturnable<EntitySelector> cir)
	{
		int currentCursor = this.reader.getCursor();
		((IEntitySelector)cir.getReturnValue()).setInputText(this.reader.getString().substring(this.startCursor, currentCursor));
	}
}
