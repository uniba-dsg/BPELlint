package bpellint.model.bpel;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import nu.xom.Node;

public class EventHandlersElement extends ContainerAwareReferable {

    private final NodeHelper eventHandlers;

	public EventHandlersElement(Node eventHandlers, ProcessContainer processContainer) {
        super(eventHandlers, processContainer);
        this.eventHandlers = new NodeHelper(eventHandlers, "eventHandlers");
    }

    public boolean hasOnAlarms() {
        return !eventHandlers.hasEmptyQueryResult("bpel:onAlarm");
    }

    public boolean hasOnEvents() {
        return !eventHandlers.hasEmptyQueryResult("bpel:onEvent");
    }
}
