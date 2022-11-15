package carpettisaddition.settings;

import carpet.settings.ParsedRule;
import carpet.settings.SettingsManager;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.network.TISCMProtocolRuleListener;
import carpettisaddition.settings.validator.AbstractValidator;
import carpettisaddition.translations.TranslationConstants;
import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

//#if MC >= 11901
//$$ import java.lang.reflect.Constructor;
//#else
import carpet.settings.Validator;
import carpettisaddition.mixins.settings.ParsedRuleAccessor;
import carpettisaddition.mixins.settings.SettingsManagerAccessor;
import carpettisaddition.translations.TISAdditionTranslations;
import org.jetbrains.annotations.Nullable;
import java.lang.annotation.Annotation;
//#endif

public class CarpetRuleRegistrar
{
	private final SettingsManager settingsManager;
	private final List<ParsedRule<?>> rules = Lists.newArrayList();

	private CarpetRuleRegistrar(SettingsManager settingsManager)
	{
		this.settingsManager = settingsManager;
	}

	public static void register(SettingsManager settingsManager, Class<?> settingsClass)
	{
		CarpetRuleRegistrar registrar = new CarpetRuleRegistrar(settingsManager);
		registrar.parseSettingsClass(settingsClass);
		registrar.registerToCarpet();
	}

	public void parseSettingsClass(Class<?> settingsClass)
	{
		for (Field field : settingsClass.getDeclaredFields())
		{
			Rule rule = field.getAnnotation(Rule.class);
			if (rule != null)
			{
				this.parseRule(field, rule);
			}
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static Class<? extends AbstractValidator>[] extractValidators(Rule rule)
	{
		List<Class<? extends AbstractValidator>> validators = Lists.newArrayList(rule.validators());
		if (Arrays.asList(rule.categories()).contains(CarpetTISAdditionSettings.TISCM_PROTOCOL))
		{
			validators.add(TISCMProtocolRuleListener.class);
		}
		return validators.toArray(new Class[0]);
	}

	@SuppressWarnings("rawtypes")
	private void parseRule(Field field, Rule rule)
	{
		Class<? extends AbstractValidator>[] validators = extractValidators(rule);

		//#if MC >= 11901
		//$$ try
		//$$ {
		//$$ 	Class<?> ruleAnnotationClass = Class.forName("carpet.settings.ParsedRule$RuleAnnotation");
		//$$ 	Constructor<?> ctr1 = ruleAnnotationClass.getDeclaredConstructors()[0];
		//$$ 	ctr1.setAccessible(true);
		//$$ 	Object ruleAnnotation = ctr1.newInstance(false, null, null, null, rule.categories(), rule.options(), rule.strict(), "", validators);
  //$$
		//$$ 	Class<?> parsedRuleClass = Class.forName("carpet.settings.ParsedRule");
		//$$ 	Constructor<?> ctr2 = parsedRuleClass.getDeclaredConstructors()[0];
		//$$ 	ctr2.setAccessible(true);
		//$$ 	Object carpetRule = ctr2.newInstance(field, ruleAnnotation, this.settingsManager);
  //$$
		//$$ 	this.rules.add((CarpetRule<?>)carpetRule);
		//$$ }
		//$$ catch (Exception e)
		//$$ {
		//$$ 	throw new RuntimeException(e);
		//$$ }
		//#else
		carpet.settings.Rule cmRule = new carpet.settings.Rule()
		{
			private final String basedKey = TranslationConstants.CARPET_TRANSLATIONS_KEY_PREFIX + "rule." + this.name() + ".";

			@Nullable
			private String tr(String key)
			{
				return TISAdditionTranslations.getTranslationString(TranslationConstants.DEFAULT_LANGUAGE, this.basedKey + key);
			}

			@Override
			public String desc()
			{
				String desc = this.tr("desc");
				if (desc == null)
				{
					throw new NullPointerException(String.format("Rule %s has no translated desc", this.name()));
				}
				return desc;
			}

			@Override
			public String[] extra()
			{
				List<String> extraMessages = Lists.newArrayList();
				for (int i = 0; ; i++)
				{
					String message = this.tr("extra." + i);
					if (message == null)
					{
						break;
					}
					extraMessages.add(message);
				}
				return extraMessages.toArray(new String[0]);
			}

			@Override public String name() {return field.getName();}
			@Override public String[] category() {return rule.categories();}
			@Override public String[] options() {return rule.options();}
			@Override public boolean strict() {return rule.strict();}
			@Override public Class<? extends Validator>[] validate() {return validators;}
			@Override public Class<? extends Annotation> annotationType() {return rule.annotationType();}

			//#if MC >= 11600
			//$$ @Override public String appSource() {return "";}
			//$$ @SuppressWarnings("unchecked") @Override public Class<? extends carpet.settings.Condition>[] condition() {return new Class[0];}
			//#endif
		};

		ParsedRule<?> parsedRule = ParsedRuleAccessor.invokeConstructor(
				field, cmRule
				//#if MC >= 11600
				//$$ , this.settingsManager
				//#else
				//#endif
		);
		this.rules.add(parsedRule);
		//#endif
	}

	public void registerToCarpet()
	{
		for (ParsedRule<?> rule : this.rules)
		{
			//#if MC >= 11901
			//$$ try
			//$$ {
			//$$ 	this.settingsManager.addCarpetRule(rule);
			//$$ }
			//$$ catch (UnsupportedOperationException e)
			//$$ {
			//$$ 	CarpetTISAdditionServer.LOGGER.warn("[TISCM] Failed to register rule {} to fabric carpet: {}", rule.name(), e);
			//$$ }
			//#else
			Object existingRule = ((SettingsManagerAccessor)this.settingsManager).getRules$TISCM().put(rule.name, rule);
			if (existingRule != null)
			{
				CarpetTISAdditionServer.LOGGER.warn("[TISCM] Overwriting existing rule {}", existingRule);
			}
			//#endif
		}
	}
}
