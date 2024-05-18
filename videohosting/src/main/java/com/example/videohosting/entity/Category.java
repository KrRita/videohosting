package com.example.videohosting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_category")
    private Long idCategory;
    @Column(name="name_category")
    private String name;

    public Category(Long idCategory, String name) {
        this.idCategory = idCategory;
        this.name = name;
    }

    public Category() {
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (!idCategory.equals(category.idCategory)) return false;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        int result = idCategory.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Category{" +
               "idCategory=" + idCategory +
               ", name='" + name + '\'' +
               '}';
    }
}
