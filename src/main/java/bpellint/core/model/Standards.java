package bpellint.core.model;

import nu.xom.XPathContext;

public class Standards {

	public static final String WSDL_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/";
	public static final String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
	public static final String BPEL_NAMESPACE = "http://docs.oasis-open.org/wsbpel/2.0/process/executable";
	public static final String PLINK_NAMESPACE = "http://docs.oasis-open.org/wsbpel/2.0/plnktype";
	public static final String VPROP_NAMESPACE = "http://docs.oasis-open.org/wsbpel/2.0/varprop";

	public static final XPathContext CONTEXT = new XPathContext();

	static {
		CONTEXT.addNamespace("bpel", BPEL_NAMESPACE);
		CONTEXT.addNamespace("wsdl", WSDL_NAMESPACE);
		CONTEXT.addNamespace("xsd", XSD_NAMESPACE);
		CONTEXT.addNamespace("plink", PLINK_NAMESPACE);
		CONTEXT.addNamespace("vprop", VPROP_NAMESPACE);
	}

}
