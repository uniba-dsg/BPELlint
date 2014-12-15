package bpellint.core.model.bpel;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.PrefixHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.XmlFile;
import bpellint.core.model.bpel.mex.MessageActivityImpl;
import bpellint.core.model.wsdl.PortTypeElement;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import static bpellint.core.model.Standards.CONTEXT;

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
        for (XmlFile wsdl : getProcessContainer().getWsdls()) {
        	Document correspondingWsdlDom = wsdl.getDocument();

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
		}

        throw new NavigationException("PortType not defined in any WSDL.");
    }

    public boolean hasNeitherMyRoleNorPartnerRole() {
        return !hasMyRole() && !hasPartnerRole();
    }
}
