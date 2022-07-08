package carpettisaddition.utils.settings;

import carpet.settings.ParsedRule;
import carpet.settings.SettingsManager;
import carpet.settings.Validator;
import carpettisaddition.mixins.utils.settings.ParsedRuleAccessor;
import carpettisaddition.mixins.utils.settings.SettingsManagerAccessor;
import carpettisaddition.translations.TISAdditionTranslations;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

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

	private void parseRule(Field field, Rule rule)
	{
		carpet.settings.Rule cmRule = new carpet.settings.Rule()
		{
			private final String basedKey = TISAdditionTranslations.CARPET_TRANSLATIONS_KEY_PREFIX + "rule." + this.name() + ".";

			@Nullable
			private String tr(String key)
			{
				return TISAdditionTranslations.translateKeyToFormattingString(TISAdditionTranslations.DEFAULT_LANGUAGE, this.basedKey + key);
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
			@SuppressWarnings("rawtypes")
			@Override public Class<? extends Validator>[] validate() {return rule.validators();}

			@Override public Class<? extends Annotation> annotationType() {return rule.annotationType();}
		};
		this.rules.add(ParsedRuleAccessor.invokeConstructor(field, cmRule));
	}

	public void registerToCarpet()
	{
		for (ParsedRule<?> rule : this.rules)
		{
			((SettingsManagerAccessor)this.settingsManager).getRules$TISCM().put(rule.name, rule);
		}
	}
}
