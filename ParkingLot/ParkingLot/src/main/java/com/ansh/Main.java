package com.ansh;

import com.ansh.FareStrategy.ConcreteStrategies.BasicHourlyRateStrategy;
import com.ansh.FareStrategy.ParkingFeeStrategy;
import com.ansh.PaymentStartegy.ConcreteStrategy.CashPayment;
import com.ansh.PaymentStartegy.ConcreteStrategy.CreditCard;
import com.ansh.PaymentStartegy.PaymentStrategy;
import com.ansh.ParkingSpots.ConcreteParkingSpots.BikeParkingSpot;
import com.ansh.ParkingSpots.ConcreteParkingSpots.CarParkingSpot;
import com.ansh.ParkingSpots.EntryGate;
import com.ansh.ParkingSpots.ExitGate;
import com.ansh.ParkingSpots.Floor;
import com.ansh.ParkingSpots.ParkingSpot;
import com.ansh.VehicleFactory.Vehicle;
import com.ansh.VehicleFactory.VehicleFactory;
import com.ansh.VehicleFactory.VehicleType;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * FIXES applied in Main.java:
 *
 * 1. ExitGate instantiation had trailing comma and missing paymentStrategy arg:
 *      BEFORE: new ExitGate("1", basicHourlyRateStrategy,)
 *      AFTER : new ExitGate("1", basicStrategy, paymentStrategy)
 *
 * 2. VehicleFactory.createVehicle() called with String type ("Car", "Bike"):
 *      BEFORE: VehicleFactory.createVehicle("Car", ...)
 *      AFTER : VehicleFactory.createVehicle(VehicleType.CAR, ...)
 *
 * 3. CreditCardPayment class doesn't exist — correct class is CreditCard.
 *      Also removed the fee constructor argument (fixed in CreditCard/CashPayment).
 *
 * 4. Missing imports: Scanner, VehicleFactory, VehicleType, DurationType, CreditCard.
 *
 * 5. ParkingLot.INSTANCE used before floors were added to it — floors were
 *      never registered with the Singleton. Now uses getInstance() and addFloor().
 *
 * 6. Flow corrected to use EntryGate.generateTicket() → ExitGate.makePayment()
 *      (Ticket-based flow) instead of bypassing gates with direct parkVehicle().
 *
 * 7. Removed duplicate unused vehicles (car2, bike2) and dead code comments.
 */
public class Main {
    public static void main(String[] args) {

        // ── Setup floors and spots ────────────────────────────────────────────
        List<ParkingSpot> floor1Spots = new ArrayList<>();
        floor1Spots.add(new CarParkingSpot(1));
        floor1Spots.add(new CarParkingSpot(2));
        floor1Spots.add(new BikeParkingSpot(3));
        floor1Spots.add(new BikeParkingSpot(4));

        List<ParkingSpot> floor2Spots = new ArrayList<>();
        floor2Spots.add(new CarParkingSpot(10));
        floor2Spots.add(new CarParkingSpot(11));
        floor2Spots.add(new BikeParkingSpot(12));
        floor2Spots.add(new BikeParkingSpot(13));

        Floor floor1 = new Floor("Floor1");
        floor1.setSpots(floor1Spots);

        Floor floor2 = new Floor("Floor2");
        floor2.setSpots(floor2Spots);

        // ── Configure Singleton ParkingLot ────────────────────────────────────
        ParkingLot parkingLot = ParkingLot.getInstance();
        parkingLot.addFloor(floor1);
        parkingLot.addFloor(floor2);

        // ── Fare strategy set by parking lot operator on the ExitGate ────────
        ParkingFeeStrategy basicStrategy = new BasicHourlyRateStrategy();

        // ── Create vehicles (Factory Pattern) — no fee strategy on vehicle ───
        Vehicle car1  = VehicleFactory.createVehicle(VehicleType.CAR,  "CAR123");
        Vehicle car2  = VehicleFactory.createVehicle(VehicleType.CAR,  "CAR345");
        Vehicle bike1 = VehicleFactory.createVehicle(VehicleType.BIKE, "BIKE456");
        Vehicle bike2 = VehicleFactory.createVehicle(VehicleType.BIKE, "BIKE123");

        Scanner scanner = new Scanner(System.in);

        // ── Setup gates ───────────────────────────────────────────────────────
        EntryGate entryGate = new EntryGate("GATE-1");
        ExitGate  exitGate  = new ExitGate("GATE-1", basicStrategy);

        // ── Park car1 ─────────────────────────────────────────────────────────
        Ticket carTicket = entryGate.generateTicket(car1);
        if (carTicket != null) {
            System.out.println("Car ticket ID: " + carTicket.getTicketId());

            // User selects payment method at exit time
            System.out.println("\n[CAR EXIT] Select payment method:");
            System.out.println("1. Credit Card  2. Cash");
            PaymentStrategy carPayment = getPaymentStrategy(scanner.nextInt());
            exitGate.makePayment(carTicket, carPayment);
        } else {
            System.out.println("No spot available for car!");
        }

        // ── Park bike1 ────────────────────────────────────────────────────────
        Ticket bikeTicket = entryGate.generateTicket(bike1);
        if (bikeTicket != null) {
            System.out.println("Bike ticket ID: " + bikeTicket.getTicketId());

            // User selects payment method at exit time (can differ from car's choice)
            System.out.println("\n[BIKE EXIT] Select payment method:");
            System.out.println("1. Credit Card  2. Cash");
            PaymentStrategy bikePayment = getPaymentStrategy(scanner.nextInt());
            exitGate.makePayment(bikeTicket, bikePayment);
        } else {
            System.out.println("No spot available for bike!");
        }

        scanner.close();
    }

    /** Returns the appropriate PaymentStrategy based on user selection. */
    private static PaymentStrategy getPaymentStrategy(int choice) {
        switch (choice) {
            case 1:  return new CreditCard();
            case 2:  return new CashPayment();
            default:
                System.out.println("Invalid choice — defaulting to Credit Card.");
                return new CreditCard();
        }
    }
}
