package isabel.model.bpel;

import isabel.model.wsdl.OperationElement;

public interface MessageActivity {

    PartnerLinkElement getPartnerLink();
    OperationElement getOperation();

    String getPartnerLinkAttribute();
    String getOperationAttribute();

}
