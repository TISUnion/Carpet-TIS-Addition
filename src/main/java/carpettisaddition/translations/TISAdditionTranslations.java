package carpettisaddition.translations;

import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class TISAdditionTranslations
{
    private static final Map<String, Map<String, String>> translationCache = Maps.newLinkedHashMap();

    public static Map<String, String> getTranslationFromResourcePath(String lang)
    {
        return translationCache.computeIfAbsent(lang, l -> {
            String dataStr;
            try
            {
                dataStr = IOUtils.toString(
                        Objects.requireNonNull(TISAdditionTranslations.class.getClassLoader().getResourceAsStream(String.format("assets/carpettisaddition/lang/%s.yml", lang))),
                        StandardCharsets.UTF_8
                );
            }
            catch (NullPointerException | IOException e)
            {
                return null;
            }
            Map<String, Object> yamlMap = new Yaml().load(dataStr);
            Map<String, String> translation = Maps.newLinkedHashMap();
            build(translation, yamlMap, "");
            return translation;
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
}
