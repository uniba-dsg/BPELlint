package isabel.model.bpel;

import isabel.model.NodeHelper;
import nu.xom.Node;

public class EventHandlersElement extends NodeHelper {
    public EventHandlersElement(Node node) {
        super(node);
    }

    public boolean hasOnAlarms() {
        return !hasEmptyQueryResult("bpel:onAlarm");
    }

    public boolean hasOnEvents() {
        return !hasEmptyQueryResult("bpel:onEvent");
    }
}
