package carpettisaddition.mixins.utils.entityfilter;

import carpettisaddition.utils.entityfilter.IEntitySelector;
import net.minecraft.command.EntitySelector;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntitySelector.class)
public abstract class EntitySelectorMixin implements IEntitySelector
{
	private String inputText$CTA;

	@Override
	public void setInputText(String inputText)
	{
		this.inputText$CTA = inputText;
	}

	@Override
	public String getInputText()
	{
		return this.inputText$CTA;
	}
}
