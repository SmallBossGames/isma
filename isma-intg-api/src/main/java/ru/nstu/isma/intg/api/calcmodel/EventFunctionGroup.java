package ru.nstu.isma.intg.api.calcmodel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Maria
 * @since 15.05.2016
 */
public class EventFunctionGroup implements Serializable {

    public enum StepChoiceRule implements Serializable {
        NONE,
        MIN,
        MAX
    }

    private final StepChoiceRule stepChoiceRule;
    private final ArrayList<EventFunction> eventFunctions;

    public EventFunctionGroup(StepChoiceRule stepChoiceRule, ArrayList<EventFunction> eventFunctions) {
        this.stepChoiceRule = stepChoiceRule;
        this.eventFunctions = eventFunctions;
    }

    public StepChoiceRule getStepChoiceRule() {
        return stepChoiceRule;
    }

    public ArrayList<EventFunction> getEventFunctions() {
        return eventFunctions;
    }

}
