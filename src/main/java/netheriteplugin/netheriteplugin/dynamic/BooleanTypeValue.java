package netheriteplugin.netheriteplugin.dynamic;

public class BooleanTypeValue implements DynamicTypeValue {
    private Boolean value;

    public BooleanTypeValue(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return this.value;
    }
}
