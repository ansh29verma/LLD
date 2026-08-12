package com.ansh.ATMStatepatten;

import com.ansh.ATMStatepatten.ATMContext.ATMMachineContext;

public interface ATMState {
    String getStateName();

    // Method to handle state transitions
    ATMState next(ATMMachineContext context);
}
