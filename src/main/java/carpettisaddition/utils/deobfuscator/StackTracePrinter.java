package carpettisaddition.utils.deobfuscator;

import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import com.google.common.base.Joiner;
import net.minecraft.text.BaseText;

import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.min;

public class StackTracePrinter
{
	private static final int DEFAULT_MAX_STACK_TRACE_SIZE = 64;
	private static final Translator translator = StackTraceDeobfuscator.translator;

	private StackTraceElement[] stackTrace;
	private int maxStackTraceSize;
	private String ignorePackagePath;

	private StackTracePrinter()
	{
		this.stackTrace = Thread.currentThread().getStackTrace();
		this.maxStackTraceSize = DEFAULT_MAX_STACK_TRACE_SIZE;
	}

	public static StackTracePrinter create()
	{
		return new StackTracePrinter();
	}

	public static BaseText makeSymbol(Class<?> ignoreClass)
	{
		return create().ignore(ignoreClass).deobfuscate().toSymbolText();
	}

	// limits the maximum display line
	public StackTracePrinter limit(int maxStackTraceSize)
	{
		this.maxStackTraceSize = maxStackTraceSize;
		return this;
	}

	// all StackTraceElements before any StackTraceElement with package starts with given path will be ignored
	public StackTracePrinter ignore(String ignorePackagePath)
	{
		this.ignorePackagePath = ignorePackagePath;
		return this;
	}

	public StackTracePrinter ignore(Class<?> ignoreClass)
	{
		return this.ignore(ignoreClass.getPackage().getName());
	}

	public StackTracePrinter deobfuscate()
	{
		this.stackTrace = StackTraceDeobfuscator.deobfuscateStackTrace(this.stackTrace, this.ignorePackagePath);
		return this;
	}

	public StackTraceElement[] toStackTraceElements()
	{
		return this.stackTrace;
	}

	@SuppressWarnings("DeprecatedIsStillUsed")
	@Deprecated
	public BaseText toBaseText()
	{
		List<StackTraceElement> list = Arrays.asList(this.stackTrace).subList(0, min(this.stackTrace.length, this.maxStackTraceSize));
		int restLineCount = this.stackTrace.length - this.maxStackTraceSize;
		BaseText text = Messenger.c(translator.tr("deobfuscated_stack_trace"), Messenger.s(String.format(" (%s)\n", StackTraceDeobfuscator.MAPPING_VERSION)));
		text.append(Messenger.s(Joiner.on("\n").join(list)));
		if (restLineCount > 0)
		{
			text.append(Messenger.c("w \n", translator.tr("n_more_lines", restLineCount)));
		}
		return text;
	}

	// a $ symbol with hover text showing the stack trace
	public BaseText toSymbolText()
	{
		BaseText baseText = this.toBaseText();
		return Messenger.fancy("f", Messenger.s("$"), baseText, null);  // no COPY_TO_CLIPBOARD in 1.14
	}
}
