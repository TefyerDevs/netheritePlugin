package netheriteplugin.netheriteplugin.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String id;
    private List<ItemStack> items;

    public Recipe(String id, List<ItemStack> items) {
        this.id = id;
        this.items = items;
    }

    public Recipe(String id) {
        this.id = id;
        this.items = new ArrayList();

        for(int i = 0; i < 9; ++i) {
            this.items.add(new ItemStack(Material.AIR));
        }

    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}