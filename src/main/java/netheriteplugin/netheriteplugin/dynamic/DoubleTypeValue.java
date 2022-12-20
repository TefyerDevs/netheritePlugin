package netheriteplugin.netheriteplugin.dynamic;

public class DoubleTypeValue implements DynamicTypeValue {
    private Double value;

    public DoubleTypeValue(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return this.value;
    }
}
