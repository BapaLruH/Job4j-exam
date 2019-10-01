package ru.job4j.workersstore;

import java.io.Serializable;

class Profession implements Serializable {
    private int id;
    private String name;
    private int specialtyCode;

    public Profession(String name, int specialtyCode) {
        this.name = name;
        this.specialtyCode = specialtyCode;
    }

    public Profession(int id, String name, int specialtyCode) {
        this(name, specialtyCode);
        this.id = id;
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

    public int getSpecialtyCode() {
        return specialtyCode;
    }

    public void setSpecialtyCode(int specialtyCode) {
        this.specialtyCode = specialtyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profession that = (Profession) o;

        if (id != that.id) return false;
        if (specialtyCode != that.specialtyCode) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + specialtyCode;
        return result;
    }

    @Override
    public String toString() {
        return "Profession{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialtyCode=" + specialtyCode +
                '}';
    }
}
