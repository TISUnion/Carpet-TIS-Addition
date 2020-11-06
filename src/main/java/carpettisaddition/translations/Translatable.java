package carpettisaddition.translations;

public interface Translatable
{
	String tr(String key, String text, boolean autoFormat);

	String tr(String key, String text);

	String tr(String key);
}
