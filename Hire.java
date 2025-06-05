package Wheels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Hire {
    private Date startDate = null;
    private Customer customer = null;
    private Bike bike = null;
    private int numberOfDays = 0;
    private int hireId = 0;

    private static int hireCount = 1;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    // Construtor normal
    public Hire(Date sDate, int numDays, Bike bikeToHire, Customer cust) {
        startDate = sDate;
        numberOfDays = numDays;
        customer = cust;
        bike = bikeToHire;
        hireId = hireCount++;
    }

    // Construtor usado ao carregar de arquivo (com ID)
    public Hire(int id, Date sDate, int numDays, Bike bikeToHire, Customer cust) {
        this.hireId = id;
        this.startDate = sDate;
        this.numberOfDays = numDays;
        this.bike = bikeToHire;
        this.customer = cust;

        if (id >= hireCount) {
            hireCount = id + 1;
        }
    }

    // Getters
    public Customer getCustomer() {
        return customer;
    }

    public Bike getBike() {
        return bike;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public Date getStartDate() {
        return startDate;
    }

    public int getHireId() {
        return hireId;
    }

    // Exporta dados em formato CSV
    public String toCSV() {
        return hireId + "," + sdf.format(startDate) + "," + numberOfDays + "," +
                customer.getCustomerNumber() + "," + bike.getBikeNumber();
    }

    // Constrói um Hire a partir de uma linha CSV
    public static Hire fromCSV(String linha, Customer cliente, Bike bike) throws ParseException {
        String[] dados = linha.split(",");
        int id = Integer.parseInt(dados[0]);
        Date data = sdf.parse(dados[1]);
        int dias = Integer.parseInt(dados[2]);

        return new Hire(id, data, dias, bike, cliente);
    }

    // Metodo para verificar se o hire está atrasado
    public boolean isOverdue() {
        Date now = new Date();
        long diff = now.getTime() - startDate.getTime();
        long daysPassed = TimeUnit.MILLISECONDS.toDays(diff);
        return daysPassed > numberOfDays;
    }

    // Metodo para gerar mensagem de notificação de atraso
    public String getOverdueMessage() {
        if (!isOverdue()) return null;
        long daysOverdue = TimeUnit.MILLISECONDS.toDays(new Date().getTime() -
                (startDate.getTime() + TimeUnit.DAYS.toMillis(numberOfDays)));
        return "Cliente " + customer.getName() + " está com atraso de " + daysOverdue + " dias na devolução da bike " + bike.getBikeNumber() + ".";
    }
}