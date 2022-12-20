package netheriteplugin.netheriteplugin.dynamic;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemStackListTypeValue implements DynamicTypeValue {
    private List<ItemStack> value;

    public ItemStackListTypeValue(List<ItemStack> value) {
        this.value = value;
    }

    public ItemStackListTypeValue() {
        this.value = new ArrayList();

        for(int i = 0; i < 9; ++i) {
            this.value.add(i, new ItemStack(Material.AIR));
        }

        this.value.set(0, new ItemStack(Material.BOOK));
    }

    public List<ItemStack> getValue() {
        return this.value;
    }
}
