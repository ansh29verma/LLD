# Parking Lot System — Complete Flow & Design

---

## Design Patterns Used

| Pattern   | Where                          | Class / Interface                                    |
|-----------|-------------------------------|------------------------------------------------------|
| Singleton | One ParkingLot app-wide        | `ParkingLot.getInstance()`                           |
| Factory   | Create correct Vehicle subtype | `VehicleFactory.createVehicle()`                     |
| Strategy  | Spot selection algorithm       | `ParkingStrategy` → `NaturalOrderParking`            |
| Strategy  | Fee calculation                | `ParkingFeeStrategy` → `BasicHourly` / `Premium`     |
| Strategy  | Payment method                 | `PaymentStrategy` → `CashPayment` / `CreditCard`     |

---

## Responsibility Model

```
Vehicle          → licensePlate, vehicleType ONLY
                   (does not know about pricing)

ParkingLot       → manages floors, gates, spot lookup
                   uses ParkingStrategy to pick a spot

ExitGate         → owns ParkingFeeStrategy (set by operator)
                   accepts PaymentStrategy per transaction (chosen by user)

User at exit     → selects PaymentStrategy (Credit Card / Cash)
                   per vehicle, at exit time
```

---

## System Setup Flow

```
main()
  │
  ├─[1] Create ParkingSpots
  │       new CarParkingSpot(1), CarParkingSpot(2)
  │       new BikeParkingSpot(3), BikeParkingSpot(4)
  │
  ├─[2] Create Floors & assign spots
  │       Floor("Floor1").setSpots([...])
  │       Floor("Floor2").setSpots([...])
  │
  ├─[3] Configure Singleton ParkingLot       ← Singleton Pattern
  │       ParkingLot.getInstance()
  │         .addFloor(floor1)
  │         .addFloor(floor2)
  │       Default ParkingStrategy: NaturalOrderParking
  │
  ├─[4] Create Fee Strategy                  ← Strategy Pattern
  │       new BasicHourlyRateStrategy()
  │         CAR  = $10/hr | BIKE = $5/hr | OTHER = $8/hr
  │
  ├─[5] Create Vehicles                      ← Factory Pattern
  │       VehicleFactory.createVehicle(CAR,  "CAR123")  → CarVehicle
  │       VehicleFactory.createVehicle(BIKE, "BIKE456") → BikeVehicle
  │
  └─[6] Create Gates
          new EntryGate("GATE-1")
          new ExitGate("GATE-1", feeStrategy)
```

---

## Vehicle Entry Flow

```
entryGate.generateTicket(vehicle)
  │
  ├─ ParkingLot.getInstance().findAvailableSpot(vehicleType)
  │     │
  │     └─ loop floors
  │           floor.getAvailableSpots(vehicleType)  → List<ParkingSpot>
  │           parkingStrategy.park(availableSpots)  → ParkingSpot  ← Strategy
  │
  ├─ spot.parkVehicle(vehicle)
  │     ├─ canParkVehicle(vehicle)  — validates type match
  │     ├─ spot.vehicle = vehicle
  │     └─ spot.isOccupied = true
  │
  └─ return new Ticket(vehicle, spot)
          ticketId   = UUID
          entryTime  = now()
          vehicle    = vehicle ref
          spot       = assigned spot ref
          isActive   = true
```

---

## Vehicle Exit & Payment Flow

```
User selects payment method (per vehicle, at exit time)
  └─ getPaymentStrategy(choice)
        1 → new CreditCard()    ← Strategy Pattern
        2 → new CashPayment()

exitGate.makePayment(ticket, paymentStrategy)
  │
  ├─[1] ticket.setExitTime(now())
  │
  ├─[2] feeStrategy.calculateFee(ticket)       ← Strategy Pattern (on ExitGate)
  │       duration = exitTime - entryTime  (ChronoUnit.HOURS)
  │       switch(vehicleType):
  │         BASIC:   CAR=$10/hr  BIKE=$5/hr   OTHER=$8/hr
  │         PREMIUM: CAR=$15/hr  BIKE=$7.5/hr OTHER=$12/hr
  │
  ├─[3] ticket.setCharges(charges)
  │
  ├─[4] paymentStrategy.processPayment(charges)  ← Strategy (user's choice)
  │       CreditCard  → "Processing credit card payment of $X"
  │       CashPayment → "Processing cash payment of $X"
  │
  ├─[5] ticket.setActive(false)
  │
  ├─[6] spot.vacate()
  │       spot.vehicle = null
  │       spot.isOccupied = false
  │
  └─[7] return new Payment(charges, ticket, paymentStrategy)
              paymentId = UUID
              pTime     = now()
              amount    = charges
              ticket    = ticket ref
```

---

## Complete Object Interaction

```
main()
 ├── ParkingLot (Singleton)
 │     ├── Floor[]
 │     │     └── ParkingSpot[]
 │     │           ├── CarParkingSpot
 │     │           └── BikeParkingSpot
 │     └── ParkingStrategy ──────────────────── Strategy Pattern
 │           └── NaturalOrderParking (default)
 │
 ├── VehicleFactory ──────────────────────────── Factory Pattern
 │     └── Vehicle
 │           ├── CarVehicle
 │           └── BikeVehicle
 │
 ├── EntryGate
 │     └── generateTicket(vehicle) → Ticket
 │
 ├── ExitGate
 │     ├── ParkingFeeStrategy ─────────────────── Strategy Pattern (operator)
 │     │     ├── BasicHourlyRateStrategy
 │     │     └── PremiumRateStrategy
 │     └── makePayment(ticket, paymentStrategy)
 │           └── PaymentStrategy ──────────────── Strategy Pattern (user)
 │                 ├── CreditCard
 │                 └── CashPayment
 │
 ├── Ticket
 │     ├── ticketId, entryTime, exitTime, charges
 │     ├── Vehicle ref
 │     └── ParkingSpot ref
 │
 └── Payment (receipt)
       ├── paymentId, pTime, amount
       ├── Ticket ref
       └── PaymentStrategy ref
```

---

## All Fixes Applied (Complete List)

| # | File | Issue | Fix |
|---|------|-------|-----|
| 1 | `ParkingLot.java` | `@Data`/`@AllArgsConstructor` breaks Singleton | `@Getter` only, private constructor, double-checked locking |
| 2 | `ParkingLot.java` | `parkingStrategy` stored but never used | `findAvailableSpot()` now delegates to strategy |
| 3 | `ParkingLot.java` | No default ParkingStrategy | `NaturalOrderParking` set in private constructor |
| 4 | `ParkingStrategy.java` | Abstract class for single-method contract | Changed to `interface` |
| 5 | `NaturalOrderParking.java` | `extends` abstract class | Changed to `implements` interface |
| 6 | `CarVehicle.java` | Missing `extends Vehicle` | Added |
| 7 | `Vehicle.java` | Held `feeStrategy` — vehicle shouldn't price itself | Removed field + `calculateFee()` method |
| 8 | `VehicleFactory.java` | Accepted `feeStrategy` param | Removed — factory only sets type + plate |
| 9 | `Floor.java` | No `getAvailableSpots()` — needed by ParkingStrategy | Added method |
| 10 | `Floor.java` | Unused imports (`HashMap`, `Map`, `Vehicle`) | Removed |
| 11 | `ExitGate.java` | `paymentStrategy` fixed at construction — all vehicles same method | Moved to `makePayment(ticket, paymentStrategy)` parameter |
| 12 | `Main.java` | `ExitGate("1", strategy,)` — trailing comma, missing arg | Fixed |
| 13 | `Main.java` | `VehicleFactory.createVehicle("Car", ...)` — String not enum | Fixed to `VehicleType.CAR` |
| 14 | `Main.java` | `CreditCardPayment` class doesn't exist | Fixed to `CreditCard` |
| 15 | `Main.java` | Floors never registered with Singleton | `getInstance()` + `addFloor()` |
| 16 | `Main.java` | Payment chosen once for all vehicles | Moved inside each vehicle's exit block |
| 17 | `CashPayment.java` | Constructor took `fee`; wrong print message | Removed arg; fixed to "cash payment" |
| 18 | `CreditCard.java` | Constructor took `fee` | Removed arg |
| 19 | `BasicHourlyRateStrategy.java` | `between(exitTime, entryTime)` — reversed | Fixed to `between(entryTime, exitTime)` |
| 20 | `PremiumRateStrategy.java` | Same reversal + identical rates to Basic | Fixed order + actual premium rates |
| 21 | `Payment.java` | `pType` never set (always null) | Removed unused field |
| 22 | `Payment.java` | `processPayment()` redundant — already done in ExitGate | Removed method |
| 23 | `EntryGate.java` | `findAvailableSpot()` called twice — race condition | Single call, null-check, then park |

---

## How to View UML Diagrams

- **IntelliJ IDEA**: Install *PlantUML Integration* plugin → open `.puml` files
- **VS Code**: Install *PlantUML* extension → `Alt+D` to preview
- **Online**: Paste into [https://www.plantuml.com/plantuml/uml/](https://www.plantuml.com/plantuml/uml/)
