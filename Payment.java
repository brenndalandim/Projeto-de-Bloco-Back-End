package Wheels;

public class Payment {
    private Customer customer = null;
    private int paymentId = 0;
    private double totalPayment = 0.0;

    private static int paymentCount = 1;

    public Payment(Customer cust) {
        customer = cust;
        paymentId = paymentCount++;
    }

    public void calculateTotalPayment(Hire hire) {
        // calcula e armazena o total com base na bike
        totalPayment = hire.getBike().calculateCost(hire.getNumberOfDays());

        // ainda exibe o recibo
        issueReceipt(hire);
    }

    private void issueReceipt(Hire hire) {
        String cust = hire.getCustomer().getName();
        String pcode = hire.getCustomer().getPostcode();
        System.out.println("Printing out receipt for '" + cust + "' ......");
        System.out.println("In postcode: " + pcode + "\n");

        System.out.println("Hiring bike number '" + hire.getBike().getBikeNumber() + "' for "
                + hire.getNumberOfDays() + " days" + "\n");

        // Exibe o custo total
        System.out.println("Total cost: R$ " + String.format("%.2f", totalPayment));
    }

    public double getTotalPayment() {
        return totalPayment;
    }
}