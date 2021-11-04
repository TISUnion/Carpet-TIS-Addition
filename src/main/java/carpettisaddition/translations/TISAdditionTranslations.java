package carpettisaddition.translations;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.mixins.translations.ServerPlayerEntityAccessor;
import carpettisaddition.mixins.translations.StyleAccessor;
import carpettisaddition.mixins.translations.TranslatableTextAccessor;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Maps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TISAdditionTranslations
{
    public static final String DEFAULT_LANGUAGE = "en_us";
    public static final String TRANSLATION_NAMESPACE = CarpetTISAdditionServer.compactName;  // "carpettisaddition"
    public static final String TRANSLATION_KEY_PREFIX = TRANSLATION_NAMESPACE + ".";  // "carpettisaddition."
    private static final Map<String, Map<String, String>> translationCache = Maps.newLinkedHashMap();

    @NotNull
    public static Map<String, String> getTranslationFromResourcePath(String lang)
    {
        return translationCache.computeIfAbsent(lang, l -> {
            String dataStr;
            try
            {
                dataStr = IOUtils.toString(
                        Objects.requireNonNull(TISAdditionTranslations.class.getClassLoader().getResourceAsStream(String.format("assets/%s/lang/%s.yml", TRANSLATION_NAMESPACE, lang))),
                        StandardCharsets.UTF_8
                );
            }
            catch (NullPointerException | IOException e)
            {
                return Collections.emptyMap();
            }
            try
            {
                Map<String, Object> yamlMap = new Yaml().load(dataStr);
                Map<String, String> translation = Maps.newLinkedHashMap();
                build(translation, yamlMap, "");
                return translation;
            }
            catch (Exception e)
            {
                CarpetTISAdditionServer.LOGGER.error("Failed to load translation of language " + lang, e);
                return Collections.emptyMap();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static void build(Map<String, String> translation, Map<String, Object> yamlMap, String prefix)
    {
        yamlMap.forEach((key, value) -> {
            String fullKey = prefix.isEmpty() ? key : (!key.equals(".") ? prefix + "." + key : prefix);
            if (value instanceof String)
            {
                translation.put(fullKey, (String)value);
            }
            else if (value instanceof Map)
            {
                build(translation, (Map<String, Object>)value, fullKey);
            }
            else
            {
                throw new RuntimeException(String.format("Unknown type %s in with key %s", value.getClass(), fullKey));
            }
        });
    }

    /**
     * key -> translated formatting string
     */
    @Nullable
    public static String translateKey(String lang, String key)
    {
        return getTranslationFromResourcePath(lang.toLowerCase()).get(key);
    }

    public static BaseText translate(BaseText text)
    {
        return translate(Messenger.copy(text), DEFAULT_LANGUAGE);
    }

    public static BaseText translate(BaseText text, ServerPlayerEntity player)
    {
        return translate(Messenger.copy(text), ((ServerPlayerEntityAccessor)player).getClientLanguage());
    }

    private static BaseText translate(BaseText text, @NotNull String lang)
    {
        if (text instanceof TranslatableText)
        {
            TranslatableText translatableText = (TranslatableText)text;
            if (translatableText.getKey().startsWith(TRANSLATION_KEY_PREFIX))
            {
                String msgKeyString = translateKey(lang, translatableText.getKey());
                if (msgKeyString == null && !lang.equals(DEFAULT_LANGUAGE))
                {
                    msgKeyString = translateKey(DEFAULT_LANGUAGE, translatableText.getKey());
                }
                if (msgKeyString != null)
                {
                    BaseText origin = text;
                    TranslatableTextAccessor fixedTranslatableText = (TranslatableTextAccessor)(new TranslatableText(msgKeyString, translatableText.getArgs()));
                    try
                    {
                        fixedTranslatableText.getTranslations().clear();
                        fixedTranslatableText.invokeSetTranslation(msgKeyString);
                        text = Messenger.c(fixedTranslatableText.getTranslations().toArray(new Object[0]));
                    }
                    catch (TranslationException e)
                    {
                        text = Messenger.s(msgKeyString);
                    }

                    // migrating text data
                    text.getSiblings().addAll(origin.getSiblings());
                    text.setStyle(origin.getStyle());
                }
                else
                {
                    CarpetTISAdditionServer.LOGGER.warn("TISCM: Unknown translation key {}", translatableText.getKey());
                }
            }
        }

        // translate hover text
        HoverEvent hoverEvent = ((StyleAccessor)text.getStyle()).getHoverEventField();
        if (hoverEvent != null)
        {
            text.getStyle().setHoverEvent(new HoverEvent(hoverEvent.getAction(), translate((BaseText)hoverEvent.getValue(), lang)));
        }

        // translate sibling texts
        List<Text> siblings = text.getSiblings();
        for (int i = 0; i < siblings.size(); i++)
        {
            siblings.set(i, translate((BaseText)siblings.get(i), lang));
        }
        return text;
    }
}
