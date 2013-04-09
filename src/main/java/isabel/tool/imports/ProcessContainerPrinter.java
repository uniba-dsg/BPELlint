package isabel.tool.imports;

public class ProcessContainerPrinter {

	private final ProcessContainer model;

	public ProcessContainerPrinter(ProcessContainer model) {
		this.model = model;
	}

	public String toString() {
		return getStatus() + "\n" + getDetailedStatus();
	}

	public String getStatus() {
		String result = "";
		if (model.getBpel() != null) {
			result += "1";
		} else {
			result += "0";
		}
		result += " bpel";
		result += " " + model.getAllWsdls().size() + " wsdl(s)";
		result += " " + model.getAllXsds().size() + " xsd(s)";
		result += " " + model.getXsdSchema().size() + " schema(s)";

		return result;
	}

	public String getDetailedStatus() {
		String result = model.getBpel().toString();
		result += "\n";
		for (XmlFile entry : model.getAllWsdls()) {
			result += entry + "\n";
		}
		for (XmlFile entry : model.getAllXsds()) {
			result += entry + "\n";
		}

		return result;
	}
}
