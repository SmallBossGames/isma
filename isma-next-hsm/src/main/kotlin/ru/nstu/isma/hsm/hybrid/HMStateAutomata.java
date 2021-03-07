package ru.nstu.isma.hsm.hybrid;

import ru.nstu.isma.hsm.HSM;
import ru.nstu.isma.hsm.exp.HMExpression;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:32
 */
public class HMStateAutomata implements Serializable {
    protected Map<String, HMState> states = new LinkedHashMap<>();

    protected HashSet<HMTransaction> transactions = new LinkedHashSet<>();

    protected HashSet<HMPseudoState> pseudoStates = new LinkedHashSet<>();

    protected HMState init;

    protected HSM parent;

    public HMStateAutomata(HSM parent) {
        this.parent = parent;
    }

    public Map<String, HMState> getStates() {
        return states;
    }

    public HashSet<HMTransaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(HMState from, HMState to, HMExpression condition) {
        HMTransaction tr = new HMTransaction();
        tr.setSource(from);
        tr.setTarget(to);
        tr.setCondition(condition);
        transactions.add(tr);
    }

    public boolean addState(HMState state) {
        if (states.containsKey(state.getCode())) {
            return false;
        }
        states.put(state.getCode(), state);
        state.getVariables().setParent(parent.getVariableTable());
        return true;
    }

    public HMState getState(String key) {
        return states.get(key);
    }

    public List<HMTransaction> getAllFrom(HMState st) {
        List<HMTransaction> tr = new LinkedList<HMTransaction>();
        for (HMTransaction transaction : transactions) {
            if (transaction.getSource().getCode().equals(st.getCode())) {
                tr.add(transaction);
            }
        }
        return tr;
    }

    public List<HMTransaction> getAllTo(HMState st) {
        List<HMTransaction> tr = new LinkedList<HMTransaction>();
        for (HMTransaction transaction : transactions) {
            if (transaction.getTarget().getCode().equals(st.getCode())) {
                tr.add(transaction);
            }
        }
        return tr;
    }

    public void addPseudoState(HMPseudoState ps) {
        pseudoStates.add(ps);
        ps.getVariables().setParent(parent.getVariableTable());
    }

    public List<HMPseudoState> getAllPseudoStates() {
        return pseudoStates.stream().collect(Collectors.toList());
    }


    public HMState getInit() {
        return init;
    }

    public void setInit(HMState init) {
        this.init = init;
    }
}
