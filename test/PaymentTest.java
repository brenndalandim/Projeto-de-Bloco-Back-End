package Wheels.test;

import Wheels.Bike;
import Wheels.Customer;
import Wheels.Hire;
import Wheels.Payment;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void testPayment() {
        Bike bike = new Bike(20, 15, 20);
        Customer customer = new Customer("Maria", "11111-000", "98888-0000", "maria@email.com");
        Hire hire = new Hire(new Date(), 5, bike, customer);

        Payment payment = new Payment(customer);
        payment.calculateTotalPayment(hire);

        double expected = 20 + (15 * 5);
        assertEquals(expected, payment.getTotalPayment(), 0.001);
    }
}
