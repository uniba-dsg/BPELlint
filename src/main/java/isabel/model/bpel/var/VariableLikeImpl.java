package isabel.model.bpel.var;

import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;
import isabel.model.wsdl.PropertyAliasElement;
import isabel.model.wsdl.PropertyElement;
import nu.xom.Node;
import nu.xom.Nodes;

public class VariableLikeImpl extends ContainerAwareReferable implements VariableLike {

	private final NodeHelper variableLike;

	public VariableLikeImpl(Node variableLike, ProcessContainer processContainer) {
		super(variableLike, processContainer);
		this.variableLike = new NodeHelper(variableLike);
	}

	@Override
    public boolean hasVariableMessageType() {
        return variableLike.hasAttribute("messageType");
    }

    @Override
    public boolean hasVariableElement() {
        return variableLike.hasAttribute("element");
    }

	@Override
	public String getVariableElement() {
		return variableLike.getAttribute("element");
	}

	@Override
    public String getVariableName() {
        return variableLike.getAttribute("name");
    }

    public String getType() {
        return variableLike.getAttribute("type");
    }

    @Override
    public String getVariableMessageType() {
        return variableLike.getAttribute("messageType");
    }

	@Override
	public PropertyAliasElement resolvePropertyAlias(PropertyElement property)
			throws NavigationException {
		Nodes aliases = property.toXOM().query("./../vprop:propertyAlias",Standards.CONTEXT);

		for (Node aliasNode : aliases) {
			NodeHelper alias = new NodeHelper(aliasNode);

			if (isEqual(getVariableMessageType(), alias.getAttribute("messageType"))) {
				return new PropertyAliasElement(aliasNode, getProcessContainer());
			} else if (isEqual(getVariableElement(), alias.getAttribute("element"))) {
				return new PropertyAliasElement(aliasNode, getProcessContainer());
			} else if (isEqual(getType(), alias.getAttribute("type"))) {
				return new PropertyAliasElement(aliasNode, getProcessContainer());
			} 
		}

		throw new NavigationException("This variable has no propertyAlias.");
	}

	private boolean isEqual(String qName1, String qName2) {
		return PrefixHelper.removePrefix(qName1).equals(PrefixHelper.removePrefix(qName2)) && !qName1.isEmpty();
	}

}
