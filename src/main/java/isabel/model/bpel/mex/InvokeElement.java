package isabel.model.bpel.mex;

import java.util.List;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Referable;
import isabel.model.Standards;
import isabel.model.bpel.CorrelationElement;
import isabel.model.bpel.PartnerLinkElement;
import isabel.model.bpel.ProcessElement;
import isabel.model.bpel.ScopeElement;
import isabel.model.bpel.fct.CatchAllElement;
import isabel.model.bpel.fct.CatchElement;
import isabel.model.bpel.fct.CompensateTarget;
import isabel.model.bpel.fct.CompensationHandlerElement;
import isabel.model.bpel.fct.TerminationHandlerElement;
import isabel.model.wsdl.OperationElement;
import isabel.model.wsdl.PortTypeElement;
import nu.xom.Node;
import static isabel.model.Standards.CONTEXT;

public class InvokeElement extends NodeHelper implements MessageActivity, CompensateTarget {

    private final MessageActivity delegate;

    @Override
    public Type getType() {
        return delegate.getType();
    }

    @Override
    public PartnerLinkElement getPartnerLink() throws NavigationException {
        return delegate.getPartnerLink();
    }

    @Override
    public PortTypeElement getPortType() throws NavigationException {
        return delegate.getPortType();
    }

    @Override
    public OperationElement getOperation() throws NavigationException {
        return delegate.getOperation();
    }
    
	@Override
	public List<CorrelationElement> getCorrelations() throws NavigationException {
		return delegate.getCorrelations();
	}

    @Override
    public String getPartnerLinkAttribute() {
        return delegate.getPartnerLinkAttribute();
    }

    @Override
    public String getOperationAttribute() {
        return delegate.getOperationAttribute();
    }

    @Override
    public String getPortTypeAttribute() {
        return delegate.getPortTypeAttribute();
    }

    @Override
    public boolean isReceiving() {
        return delegate.isReceiving();
    }

	@Override
	public String getMessageExchangeAttribute() {
		return delegate.getMessageExchangeAttribute();
	}

    @Override
    public Node toXOM() {
        return delegate.toXOM();
    }

    public InvokeElement(Node node, ProcessContainer processContainer) {
        super(node);

        delegate = new MessageActivityImpl(this, processContainer);
    }

    public String getInputVariableAttribute() {
        return getAttribute("inputVariable");
    }

    public String getOutputVariableAttribute() {
        return getAttribute("outputVariable");
    }

    public boolean hasInputVariable() {
        return toXOM().query("@inputVariable", CONTEXT).hasAny();
    }

    public boolean hasToParts() {
        return toXOM().query("bpel:toParts", CONTEXT).hasAny();
    }

    public boolean hasFromParts() {
        return toXOM().query("bpel:fromParts", CONTEXT).hasAny();
    }

    public boolean hasOutputVariable() {
        return !getOutputVariableAttribute().isEmpty();
    }

	@Override
	public boolean hasCompensationHandler() {
		return toXOM().query("./bpel:compensationHandler", Standards.CONTEXT).hasAny();
	}

	@Override
	public boolean hasFaultHandler() {
		return toXOM().query("./bpel:faultHandlers", Standards.CONTEXT).hasAny();
	}

	@Override
	public Referable getEnclosingFctBarrier() {
		NodeHelper parent = this;
		while(!"process".equals(parent.getLocalName())) {
			parent = parent.getParent();
			String localName = parent.getLocalName();
			if ("scope".equals(localName)) {
				return new ScopeElement(parent);
			}
			if ("catch".equals(parent.getLocalName())) {
				return new CatchElement(parent.toXOM());
			}
			if ("catchAll".equals(parent.getLocalName())) {
				return new CatchAllElement(parent.toXOM());
			}
			if ("compensationHandler".equals(parent.getLocalName())) {
				return new CompensationHandlerElement(parent.toXOM());
			}
			if ("terminationHandler".equals(parent.getLocalName())) {
				return new TerminationHandlerElement(parent.toXOM());
			}
		}
		return new ProcessElement(parent);
	}

}
