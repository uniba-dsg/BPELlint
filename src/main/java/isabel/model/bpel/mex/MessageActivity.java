package isabel.model.bpel.mex;

import java.util.List;

import isabel.model.NavigationException;
import isabel.model.bpel.CorrelationElement;
import isabel.model.bpel.PartnerLinked;
import isabel.model.wsdl.OperationElement;
import isabel.model.wsdl.PortTypeElement;

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
