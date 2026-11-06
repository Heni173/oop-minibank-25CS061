package model;

import java.io.Serializable;

import model.annotation.Id;

// Represents a bank customer with personal and address details.
public class Customer implements Cloneable, Serializable
{
    // Defines the unique version identifier for serialization.
    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private String mobile;
    // Marks the customer ID as the unique identifier.
    @Id
    private final String customerId;
    private Address address;

    // Maintains the count of generated customer IDs.
    private static long customerCounter = 101;

    // Generates a unique customer ID.
    private static String generateCustomerId()
    {
        return "CUST" + customerCounter++;
    }

    // Nested class representing the customer's address.
    public static class Address implements Serializable
    {
        private static final long serialVersionUID = 1L;

        private String line;
        private String city;
        private String pincode;

        // Initializes the address with line, city and pincode.
        public Address(String line, String city, String pincode)
        {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        // Returns the address line.
        public String getLine()
        {
            return line;
        }

        // Returns the city name.
        public String getCity()
        {
            return city;
        }

        // Returns the pincode.
        public String getPincode()
        {
            return pincode;
        }
    }

    // Initializes a customer with personal and address details.
    public Customer(String name, String email, String mobile, Address address)
    {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        customerId = generateCustomerId();
    }

    // Returns the customer's name.
    public String getName()
    {
        return name;
    }

    // Returns the customer's email address.
    public String getEmail()
    {
        return email;
    }

    // Returns the customer's mobile number.
    public String getMobile()
    {
        return mobile;
    }

    // Returns the unique customer ID.
    public String getCustomerId()
    {
        return customerId;
    }

    // Returns the customer's address.
    public Address getAddress()
    {
        return address;
    }

    // Creates and returns a copy of the customer object.
    @Override
    public Customer clone()
    {
        try
        {
            return (Customer) super.clone();
        }
        catch(CloneNotSupportedException e)
        {
            return null;
        }
    }
}