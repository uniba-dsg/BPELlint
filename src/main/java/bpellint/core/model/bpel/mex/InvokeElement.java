package bpellint.core.model.bpel.mex;

import java.util.List;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.Referable;
import bpellint.core.model.bpel.CorrelationElement;
import bpellint.core.model.bpel.PartnerLinkElement;
import bpellint.core.model.bpel.fct.CompensateTargetable;
import bpellint.core.model.bpel.fct.CompensateTargetableImpl;
import bpellint.core.model.wsdl.OperationElement;
import bpellint.core.model.wsdl.PortTypeElement;

import nu.xom.Node;
import static bpellint.core.model.Standards.CONTEXT;

public class InvokeElement extends ContainerAwareReferable implements MessageActivity,
		CompensateTargetable {

	private final NodeHelper invoke;
	private final MessageActivity messageActivityDelegate;
	private final CompensateTargetableImpl compensateTargetDelegate;

	public InvokeElement(Node invoke, ProcessContainer processContainer) {
		super(invoke, processContainer);
		this.invoke = new NodeHelper(invoke, "invoke");
		this.messageActivityDelegate = new MessageActivityImpl(this, processContainer);
		this.compensateTargetDelegate = new CompensateTargetableImpl(invoke, processContainer);
	}

	@Override
	public Type getType() {
		return messageActivityDelegate.getType();
	}

	@Override
	public PartnerLinkElement getPartnerLink() throws NavigationException {
		return messageActivityDelegate.getPartnerLink();
	}

	@Override
	public PortTypeElement getPortType() throws NavigationException {
		return messageActivityDelegate.getPortType();
	}

	@Override
	public OperationElement getOperation() throws NavigationException {
		return messageActivityDelegate.getOperation();
	}

	@Override
	public List<CorrelationElement> getCorrelations()
			throws NavigationException {
		return messageActivityDelegate.getCorrelations();
	}

	@Override
	public String getPartnerLinkAttribute() {
		return messageActivityDelegate.getPartnerLinkAttribute();
	}

	@Override
	public String getOperationAttribute() {
		return messageActivityDelegate.getOperationAttribute();
	}

	@Override
	public String getPortTypeAttribute() {
		return messageActivityDelegate.getPortTypeAttribute();
	}

	@Override
	public boolean isReceiving() {
		return messageActivityDelegate.isReceiving();
	}

	@Override
	public String getMessageExchangeAttribute() {
		return messageActivityDelegate.getMessageExchangeAttribute();
	}

	public String getInputVariableAttribute() {
		return invoke.getAttribute("inputVariable");
	}

	public String getOutputVariableAttribute() {
		return invoke.getAttribute("outputVariable");
	}

	public boolean hasInputVariable() {
		return invoke.hasAttribute("inputVariable");
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
		return compensateTargetDelegate.hasCompensationHandler();
	}

	@Override
	public boolean hasFaultHandler() {
		return compensateTargetDelegate.hasFaultHandler();
	}

	@Override
	public Referable getEnclosingFctBarrier() {
		return compensateTargetDelegate.getEnclosingFctBarrier();
	}

}
