package netheriteplugin.netheriteplugin.dynamic;

public class StringTypeValue implements DynamicTypeValue {
    private String value;

    public StringTypeValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
