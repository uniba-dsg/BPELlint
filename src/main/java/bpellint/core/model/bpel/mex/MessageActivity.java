package bpellint.core.model.bpel.mex;

import java.util.List;

import bpellint.core.model.NavigationException;
import bpellint.core.model.bpel.CorrelationElement;
import bpellint.core.model.bpel.PartnerLinked;
import bpellint.core.model.wsdl.OperationElement;
import bpellint.core.model.wsdl.PortTypeElement;


public interface MessageActivity extends PartnerLinked {

    static enum Type {
        INVOKE, RECEIVE, ON_MESSAGE, ON_EVENT, REPLY
    }

    Type getType();

    PortTypeElement getPortType() throws NavigationException;
    OperationElement getOperation() throws NavigationException;
    List<CorrelationElement> getCorrelations() throws NavigationException;

    String getPartnerLinkAttribute();
    String getOperationAttribute();
    String getPortTypeAttribute();
    String getMessageExchangeAttribute();

    boolean isReceiving();

}
