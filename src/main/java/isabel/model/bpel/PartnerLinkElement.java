package isabel.model.bpel;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.mex.MessageActivityImpl;
import isabel.model.wsdl.PortTypeElement;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class PartnerLinkElement extends NodeHelper {

    public PartnerLinkElement(Node node) {
        super(node);
    }

    public boolean hasPartnerRole() {
        return hasAttribute("partnerRole");
    }

    public boolean hasMyRole() {
        return hasAttribute("myRole");
    }

    public boolean hasInitializePartnerRole() {
        return hasAttribute("initializePartnerRole");
    }

    public String getName() {
        return getAttribute("name");
    }

    String getPartnerLinkType() {
        return getAttribute("partnerLinkType");
    }

    String getPartnerRole() {
        return getAttribute("partnerRole");
    }

    String getMyRole() {
        return getAttribute("myRole");
    }

    public PortTypeElement partnerLinkToPortType(ProcessContainer fileHandler, MessageActivityImpl messageActivity)
            throws NavigationException {
        String partnerLinkTypeAttribute = getPartnerLinkType();
        String wsdlImportNamespace = PrefixHelper.getPrefixNamespaceURI(toXOM().getDocument(),
                PrefixHelper.getPrefix(partnerLinkTypeAttribute));
        Document correspondingWsdlDom = fileHandler.getWsdlByTargetNamespace(wsdlImportNamespace).getDocument();

        if (correspondingWsdlDom != null) {
            String partnerLinkTypeName = PrefixHelper
                    .removePrefix(partnerLinkTypeAttribute);

            Nodes partnerRolePortType = correspondingWsdlDom.query(
                    "//plink:partnerLinkType[@name='" + partnerLinkTypeName
                            + "']/" + "plink:role[@name='" + getPartnerRole()
                            + "']/@portType", CONTEXT);

            if (partnerRolePortType.hasAny()) {
                String portTypeQName = partnerRolePortType.get(0).getValue();
                String portTypeNamespaceURI = PrefixHelper.getPrefixNamespaceURI(correspondingWsdlDom, PrefixHelper.getPrefix(portTypeQName));
                return messageActivity.getPortType(portTypeQName, portTypeNamespaceURI);
            } else {
                Nodes myRolePortType = correspondingWsdlDom.query(
                        "//plink:partnerLinkType[@name='" + partnerLinkTypeName
                                + "']/" + "plink:role[@name='" + getMyRole()
                                + "']/@portType", CONTEXT);
                if (myRolePortType.hasAny()) {
                    String portTypeQName = myRolePortType.get(0).getValue();
                    String portTypeNamespaceURI = PrefixHelper.getPrefixNamespaceURI(correspondingWsdlDom, PrefixHelper.getPrefix(portTypeQName));
                    return messageActivity.getPortType(portTypeQName, portTypeNamespaceURI);
                }
            }
        }

        throw new NavigationException("PortType not defined");
    }

    public boolean hasNeitherMyRoleNorPartnerRole() {
        return !hasMyRole() && !hasPartnerRole();
    }
}
