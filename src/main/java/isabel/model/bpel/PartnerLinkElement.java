package isabel.model.bpel;

import isabel.model.ContainerAwareReferable;
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

public class PartnerLinkElement extends ContainerAwareReferable {

    private final NodeHelper partnerLink;

	public PartnerLinkElement(Node node, ProcessContainer processContainer) {
        super(node, processContainer);
        partnerLink = new NodeHelper(node, "partnerLink");
    }

    public boolean hasPartnerRole() {
        return partnerLink.hasAttribute("partnerRole");
    }

    public boolean hasMyRole() {
        return partnerLink.hasAttribute("myRole");
    }

    public boolean hasInitializePartnerRole() {
        return partnerLink.hasAttribute("initializePartnerRole");
    }

    public String getName() {
        return partnerLink.getAttribute("name");
    }

    String getPartnerLinkType() {
        return partnerLink.getAttribute("partnerLinkType");
    }

    String getPartnerRole() {
        return partnerLink.getAttribute("partnerRole");
    }

    String getMyRole() {
        return partnerLink.getAttribute("myRole");
    }

    public PortTypeElement partnerLinkToPortType(MessageActivityImpl messageActivity)
            throws NavigationException {
        String partnerLinkTypeAttribute = getPartnerLinkType();
        String wsdlImportNamespace = PrefixHelper.getPrefixNamespaceURI(toXOM(),
                PrefixHelper.getPrefix(partnerLinkTypeAttribute));
        Document correspondingWsdlDom = getProcessContainer().getWsdlByTargetNamespace(wsdlImportNamespace).getDocument();

        if (correspondingWsdlDom != null) {
            String partnerLinkTypeName = PrefixHelper
                    .removePrefix(partnerLinkTypeAttribute);

            Nodes partnerRolePortType = correspondingWsdlDom.query(
                    "//plink:partnerLinkType[@name='" + partnerLinkTypeName
                            + "']/" + "plink:role[@name='" + getPartnerRole()
                            + "']/@portType", CONTEXT);

            if (partnerRolePortType.hasAny()) {
                String portTypeQName = partnerRolePortType.get(0).getValue();
                String portTypeNamespaceURI = PrefixHelper.getPrefixNamespaceURI(partnerRolePortType.get(0), PrefixHelper.getPrefix(portTypeQName));
                return messageActivity.getPortType(portTypeQName, portTypeNamespaceURI);
            } else {
                Nodes myRolePortType = correspondingWsdlDom.query(
                        "//plink:partnerLinkType[@name='" + partnerLinkTypeName
                                + "']/" + "plink:role[@name='" + getMyRole()
                                + "']/@portType", CONTEXT);
                if (myRolePortType.hasAny()) {
                    String portTypeQName = myRolePortType.get(0).getValue();
                    String portTypeNamespaceURI = PrefixHelper.getPrefixNamespaceURI(myRolePortType.get(0), PrefixHelper.getPrefix(portTypeQName));
                    return messageActivity.getPortType(portTypeQName, portTypeNamespaceURI);
                }
            }
        }

        throw new NavigationException("PortType not defined in " + correspondingWsdlDom.getBaseURI());
    }

    public boolean hasNeitherMyRoleNorPartnerRole() {
        return !hasMyRole() && !hasPartnerRole();
    }
}
