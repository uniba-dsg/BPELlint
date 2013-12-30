package isabel.model.bpel.mex;

import java.util.List;

import isabel.model.NavigationException;
import isabel.model.Referable;
import isabel.model.bpel.CorrelationElement;
import isabel.model.bpel.PartnerLinkElement;
import isabel.model.wsdl.OperationElement;
import isabel.model.wsdl.PortTypeElement;

public interface MessageActivity extends Referable{

    static enum Type {
        INVOKE, RECEIVE, ON_MESSAGE, ON_EVENT, REPLY
    }

    Type getType();

    PartnerLinkElement getPartnerLink() throws NavigationException;
    PortTypeElement getPortType() throws NavigationException;
    OperationElement getOperation() throws NavigationException;
    List<CorrelationElement> getCorrelations() throws NavigationException;

    String getPartnerLinkAttribute();
    String getOperationAttribute();
    String getPortTypeAttribute();
    String getMessageExchangeAttribute();

    boolean isReceiving();

}
