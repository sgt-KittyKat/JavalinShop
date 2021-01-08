package database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable
public class Product {
    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String description;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Employee creator;
    public Product() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Employee getCreator() {
        return creator;
    }

    public void setCreator(Employee creator) {
        this.creator = creator;
    }
    public void saveChanges(Product updated) {
        if (updated.name != null && !updated.name.equals(this.name)) {
            this.name = updated.name;
        }
        if (updated.description != null && !updated.description.equals(this.description)) {
            this.description = updated.description;
        }
        if (updated.creator != null && !updated.creator.equals(this.creator)) {
            this.creator = updated.creator;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description) &&
                Objects.equals(creator, product.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, creator);
    }
}
