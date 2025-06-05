package Wheels;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Notification {
    private int hireId;
    private String customerName;
    private int bikeNumber;
    private String message;
    private String dateSent;

    public Notification(int hireId, String customerName, int bikeNumber, String message) {
        this.hireId = hireId;
        this.customerName = customerName;
        this.bikeNumber = bikeNumber;
        this.message = message;
        this.dateSent = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
    }

    public String toCSV() {
        return hireId + "," + customerName + "," + bikeNumber + "," + message + "," + dateSent;
    }

    public static Notification fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        int hireId = Integer.parseInt(parts[0]);
        String customerName = parts[1];
        int bikeNumber = Integer.parseInt(parts[2]);
        String message = parts[3];
        String dateSent = parts[4];
        Notification n = new Notification(hireId, customerName, bikeNumber, message);
        n.dateSent = dateSent;
        return n;
    }

    public int getHireId() {
        return hireId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getBikeNumber() {
        return bikeNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getDateSent() {
        return dateSent;
    }
}