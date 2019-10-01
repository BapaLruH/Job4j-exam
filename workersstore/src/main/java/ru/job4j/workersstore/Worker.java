package ru.job4j.workersstore;

import java.io.Serializable;

public class Worker implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private long dateOfBirth;
    private String linkToPhoto;
    private Profession profession;

    public Worker(int id, String firstName, String lastName, long dateOfBirth, String linkToPhoto, Profession profession) {
        this(firstName, lastName, dateOfBirth, linkToPhoto, profession);
        this.id = id;
    }

    public Worker(String firstName, String lastName, long dateOfBirth, String linkToPhoto, Profession profession) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.linkToPhoto = linkToPhoto;
        this.profession = profession;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLinkToPhoto() {
        return linkToPhoto;
    }

    public void setLinkToPhoto(String linkToPhoto) {
        this.linkToPhoto = linkToPhoto;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Worker worker = (Worker) o;

        if (id != worker.id) return false;
        if (dateOfBirth != worker.dateOfBirth) return false;
        if (firstName != null ? !firstName.equals(worker.firstName) : worker.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(worker.lastName) : worker.lastName != null)
            return false;
        if (linkToPhoto != null ? !linkToPhoto.equals(worker.linkToPhoto) : worker.linkToPhoto != null)
            return false;
        return profession != null ? profession.equals(worker.profession) : worker.profession == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (int) (dateOfBirth ^ (dateOfBirth >>> 32));
        result = 31 * result + (linkToPhoto != null ? linkToPhoto.hashCode() : 0);
        result = 31 * result + (profession != null ? profession.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", linkToPhoto='" + linkToPhoto + '\'' +
                ", profession=" + profession +
                '}';
    }
}
