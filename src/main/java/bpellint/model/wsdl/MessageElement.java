package bpellint.model.wsdl;

import java.util.LinkedList;
import java.util.List;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.PrefixHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.Standards;

import nu.xom.Node;
import nu.xom.Nodes;

public class MessageElement extends ContainerAwareReferable {

	private final NodeHelper message;

	public MessageElement(Node message, ProcessContainer processContainer) {
		super(message, processContainer);
		this.message = new NodeHelper(message, "message");
	}

	public boolean hasAnyPart() {
		try {
			return !getParts().isEmpty();
		} catch (NavigationException e) {
			return false;
		}
	}

	public boolean hasOnlyOnePart() {
		try {
			return getParts().size() == 1;
		} catch (NavigationException e) {
			return false;
		}
	}

	public PartElement getSinglePart() {
		try {
			return getParts().get(0);
		} catch (NavigationException e) {
			return null;
		}
	}

	public List<PartElement> getParts() throws NavigationException {
		Nodes parts = toXOM().query("./wsdl:part", Standards.CONTEXT);
		if(!parts.hasAny()){
			throw new NavigationException("<message> has no parts");
		}
		List<PartElement> partElements = new LinkedList<>();
		for (Node part : parts) {
			partElements.add(new PartElement(part, getProcessContainer()));
		}
		return partElements;
	}

	public String getName() {
		return message.getAttribute("name");
	}

	public List<PropertyAliasElement> getPropertyAliases() throws NavigationException {
		List<PropertyAliasElement> propertyAliases = new LinkedList<>();
		Nodes allPropertyAliases = toXOM().query("//vprop:propertyAlias", Standards.CONTEXT);
		for (Node propertyAliasNode : allPropertyAliases) {
			PropertyAliasElement propertyAlias = new PropertyAliasElement(propertyAliasNode, getProcessContainer());
			if (getName().equals(PrefixHelper.removePrefix(propertyAlias.getMessageTypeAttribute()))) {
				propertyAliases.add(propertyAlias);
			}
		}
		if (propertyAliases.isEmpty()) {
			throw new NavigationException("No propertyAlias for this message");
		}
		return propertyAliases;
	}

}
