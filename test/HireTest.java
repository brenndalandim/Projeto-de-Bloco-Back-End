package Wheels.test;

import Wheels.Bike;
import Wheels.Customer;
import Wheels.Hire;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class HireTest {

    @Test
    void testHire() {
        Bike bike = new Bike(10, 20, 25);
        Customer customer = new Customer("João", "12345-678", "99999-0000", "joao@email.com");
        Hire hire = new Hire(new Date(), 3, bike, customer);

        assertEquals(3, hire.getNumberOfDays());
        assertEquals(bike, hire.getBike());
        assertEquals(customer, hire.getCustomer());
    }
}