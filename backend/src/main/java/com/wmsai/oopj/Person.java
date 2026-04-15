package com.wmsai.oopj;

/**
 * Abstract Person class — base for Employee hierarchy.
 * Covers T035, OOPJ-18 (Polymorphism, Abstraction, Inheritance, Encapsulation).
 * 
 * All fields are private (encapsulation), accessed through getters/setters.
 */
public abstract class Person {
    private String name;
    private String email;
    private String phone;

    public Person() {}

    public Person(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters (Encapsulation)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    /** Abstract method — subclasses must define their display format. */
    public abstract String getDisplayInfo();

    @Override
    public String toString() {
        return getDisplayInfo();
    }
}
