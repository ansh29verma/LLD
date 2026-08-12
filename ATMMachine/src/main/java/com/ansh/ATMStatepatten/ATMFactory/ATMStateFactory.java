package com.ansh.ATMStatepatten.ATMFactory;

import com.ansh.ATMStatepatten.ATMState;
import com.ansh.ATMStatepatten.ConcreteATMState.HasCardState;
import com.ansh.ATMStatepatten.ConcreteATMState.IdleState;
import com.ansh.ATMStatepatten.ConcreteATMState.SelectOperationState;
import com.ansh.ATMStatepatten.ConcreteATMState.TransactionState;

public class ATMStateFactory {
    private static ATMStateFactory instance = null;

    private ATMStateFactory() {}

    public static ATMStateFactory getInstance() {
        if (instance == null) {
            instance = new ATMStateFactory();
        }
        return instance;
    }

    public ATMState createIdleState() {
        return (ATMState) new IdleState();
    }

    public ATMState createHasCardState() {
        return (ATMState) new HasCardState();
    }

    public ATMState createSelectOperationState() {
        return (ATMState) new SelectOperationState();
    }

    public ATMState createTransactionState() {
        return (ATMState) new TransactionState();
    }
}
