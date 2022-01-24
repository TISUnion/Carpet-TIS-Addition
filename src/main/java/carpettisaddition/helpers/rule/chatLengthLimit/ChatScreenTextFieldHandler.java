package carpettisaddition.helpers.rule.chatLengthLimit;

import net.minecraft.client.gui.widget.TextFieldWidget;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class ChatScreenTextFieldHandler
{
	public static final Set<TextFieldWidget> CHAT_SCREEN_TEXT_FIELD_WIDGETS = Collections.newSetFromMap(new WeakHashMap<>());
}
