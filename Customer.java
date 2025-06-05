package Wheels;

public class Customer {
    private String name;
    private String postcode;
    private int telephone;
    private int customerID;

    private static int customerCount = 1;

    // Construtor usado ao cadastrar novo cliente
    public Customer(String cName, String pcode, int tel){
        this.name = cName;
        this.postcode = pcode;
        this.telephone = tel;
        this.customerID = customerCount++;
    }

    // Construtor usado ao carregar cliente de arquivo (com ID já existente)
    public Customer(String cName, String pcode, int tel, int cID){
        this.name = cName;
        this.postcode = pcode;
        this.telephone = tel;
        this.customerID = cID;
        if (cID >= customerCount) {
            customerCount = cID + 1;
        }
    }

    public int getCustomerNumber(){
        return customerID;
    }

    public String getName(){
        return name;
    }

    public String getPostcode(){
        return postcode;
    }

    public int getTelephone() {
        return telephone;
    }

    // Exporta dados como linha CSV
    public String toCSV() {
        return name + "," + postcode + "," + telephone + "," + customerID;
    }

    // Para debug ou logs
    @Override
    public String toString() {
        return "Cliente #" + customerID + ": " + name + ", " + postcode + ", Tel: " + telephone;
    }
}