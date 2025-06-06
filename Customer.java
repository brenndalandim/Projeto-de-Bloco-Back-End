package Wheels;

import java.util.List;

public class Customer {
    private String name;
    private String postcode;
    private String telephone;
    private int customerID;
    private String email;  // Novo campo

    private static int customerCount = 1;

    // Construtor usado ao cadastrar novo cliente
    public Customer(String cName, String pcode, String tel, String email){
        this.name = cName;
        this.postcode = pcode;
        this.telephone = tel;
        this.email = email;
        this.customerID = customerCount++;
    }

    // Construtor usado ao carregar cliente de arquivo
    public Customer(String cName, String pcode, String tel, String email, int cID){
        this.name = cName;
        this.postcode = pcode;
        this.telephone = tel;
        this.email = email;
        this.customerID = cID;

        if (cID >= customerCount) {
            customerCount = cID + 1;
        }
    }

    public int getCustomerNumber() {
        return customerID;
    }

    public static Customer getCustomerByNumber(List<Customer> customerList, int number) {
        for (Customer c : customerList) {
            if (c.getCustomerNumber() == number) {
                return c;
            }
        }
        return null;
    }


    public String getName() {
        return name;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    // Atualizado para incluir email
    public String toCSV() {
        return name + "," + postcode + "," + telephone + "," + email + "," + customerID;
    }

    @Override
    public String toString() {
        return "Cliente #" + customerID + ": " + name + ", " + postcode + ", Tel: " + telephone + ", Email: " + email;
    }
}