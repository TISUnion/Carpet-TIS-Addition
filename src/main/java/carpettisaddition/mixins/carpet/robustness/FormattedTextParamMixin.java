package carpettisaddition.mixins.carpet.robustness;

import carpet.script.utils.ShapeDispatcher;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11600
//$$ import carpet.script.value.FormattedTextValue;
//$$ import carpet.script.value.Value;
//$$ import carpet.utils.Messenger;
//$$ import carpettisaddition.CarpetTISAdditionServer;
//$$ import com.google.gson.JsonParseException;
//$$ import net.minecraft.nbt.Tag;
//$$ import org.spongepowered.asm.mixin.Overwrite;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.16"))
@Mixin(ShapeDispatcher.FormattedTextParam.class)
public abstract class FormattedTextParamMixin
{
	//#if MC >= 11600
	//$$
	//$$ /**
	//$$  * @reason Added try-catch in case it deserialize failed
	//$$  * Might happen e.g. when client receive text tag from 1.15.2 server with old fabric carpet, since the content would
	//$$  * be a raw string instead of a serialized text string
	//$$  * @author Fallen_Breath
	//$$  */
	//$$ @Overwrite(remap = false)
	//$$ public Value decode(Tag tag)
	//$$ {
	//$$ 	String str = tag.asString();
	//$$ 	try
	//$$ 	{
	//$$ 		// vanilla carpet
	//$$ 		return FormattedTextValue.deserialize(str);
	//$$ 	}
	//$$ 	catch (JsonParseException e)
	//$$ 	{
	//$$ 		CarpetTISAdditionServer.LOGGER.warn("Fail to decode incoming tag in FormattedTextParam, text \"{}\" is not deserialize-able", str);
	//$$ 		return new FormattedTextValue(Messenger.s(str));
	//$$ 	}
	//$$ }
	//#endif
}