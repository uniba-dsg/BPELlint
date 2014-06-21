package bpellint.model.bpel.mex;

import java.util.List;

import bpellint.model.NavigationException;
import bpellint.model.bpel.CorrelationElement;
import bpellint.model.bpel.PartnerLinked;
import bpellint.model.wsdl.OperationElement;
import bpellint.model.wsdl.PortTypeElement;


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
