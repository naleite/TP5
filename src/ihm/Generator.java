package ihm;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class Generator {
	 File bpmn, fxml;
	 Document doc_bpmn;
	 Document doc_fxml;
	 StreamResult result;
	 Transformer transformer;
	 DOMSource source;
	 
	 public Generator(File bpmn, File fxml){
		 this.bpmn=bpmn;
		 this.fxml=fxml;
		 
		 DocumentBuilderFactory Factory1 = DocumentBuilderFactory.newInstance();
		 DocumentBuilderFactory Factory2 = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder dBuilder = Factory1.newDocumentBuilder();
			DocumentBuilder rBuilder = Factory2.newDocumentBuilder();
			this.doc_bpmn=dBuilder.parse(this.bpmn);
			this.doc_fxml=rBuilder.parse(this.fxml);
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			 transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {

			e.printStackTrace();
		}
		 source = new DOMSource(doc_fxml);
		 result = new StreamResult(this.fxml);
	 }

	public void parseBPMN(){
		doc_bpmn.normalizeDocument();
		NodeList usertasks=doc_bpmn.getElementsByTagName("bpmn2:userTask");
		init_fxml();
		for(int i=0;i<usertasks.getLength();i++){
			Node task=usertasks.item(i) ;
			String nodename=task.getAttributes().getNamedItem("name").getNodeValue();
			System.out.println(nodename);
			createButtons(nodename);
		}

	}

	private void init_fxml() {
		NodeList paneList = doc_fxml.getElementsByTagName("HBox");
		Node btnPane=paneList.item(0).getChildNodes().item(1);

		//int longth=btnPane.getChildNodes().getLength();
		//System.out.println(btnPane.getNodeName()+" "+longth);
		//remove tous les nodes existants

		NodeList list=btnPane.getChildNodes();
		//System.out.println(list.item(4));

		while(list.getLength()!=0) {
			btnPane.removeChild(list.item(0));
		}


		System.out.println(btnPane.getChildNodes().getLength());
	}

	public void  createButtons(String btnName){
		 //<Button fx:id="btnCreate" layoutX="14.0" layoutY="8.0" mnemonicParsing="false" text="Create" />
		 NodeList paneList = doc_fxml.getElementsByTagName("HBox");
		 Node btnPane=paneList.item(0).getChildNodes().item(1);

		 Element newBtn= doc_fxml.createElement("Button");
		 Attr attr_fxid=doc_fxml.createAttribute("fx:id");
		 attr_fxid.setValue(btnName+"btn");

		 Attr attr_text=doc_fxml.createAttribute("text");
		 attr_text.setValue(btnName);


		 newBtn.setAttributeNode(attr_fxid);
		 newBtn.setAttributeNode(attr_text);

		 btnPane.appendChild(newBtn);
		 //btn.getAttributes().getNamedItem("text").setNodeValue(btnName);
		 //btn.getAttributes().getNamedItem("disable").setNodeValue(disable+"");
		//String s = btn.getAttributes().getNamedItem("text").getNodeValue();
		//System.out.println(s);
		write();
	 }
	 
		private void write(){
			try {
				transformer.transform(source, result);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	 
	 
	 
}
