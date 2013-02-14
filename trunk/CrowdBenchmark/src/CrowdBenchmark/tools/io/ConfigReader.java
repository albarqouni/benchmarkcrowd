package CrowdBenchmark.tools.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigReader extends Reader {

	private String currentpath = "";
	private Map<String, String> listConfig = new HashMap<String, String>();

	/*
	 * public void displayData() { for (String key : listConfig.keySet()) {
	 * System.out.println(key + "=" + listConfig.get(key)); }
	 * 
	 * }
	 */

	public Map<String, String> getConfig() {
		return listConfig;
	}

	public void setCurrentPth(String path) {
		currentpath = path;
	}

	@Override
	public void readfile(String filename) {
		String path = currentpath + filename;

		try {
			// InputStream is = this.getClass().getClassLoader()
			// .getResourceAsStream(filename);
			// BufferedReader dataInput = new BufferedReader(
			// new InputStreamReader(is));
			BufferedReader dataInput = new BufferedReader(new FileReader(
					new File(path)));
			String line;

			while ((line = dataInput.readLine()) != null) {
				// buffer.append(cleanLine(line.toLowerCase()));
				line = line.replaceAll(" ", "");
				line = line.replaceAll("\"", "");
				String[] pair = line.split("=");
				if (pair.length == 2) {
					listConfig.put(pair[0], pair[1]);
				}
			}

			dataInput.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		/*
		 * File fXmlFile = new File(path); DocumentBuilderFactory dbFactory =
		 * DocumentBuilderFactory.newInstance(); DocumentBuilder dBuilder =
		 * null; Document doc = null; try { dBuilder =
		 * dbFactory.newDocumentBuilder(); doc = dBuilder.parse(fXmlFile); }
		 * catch (ParserConfigurationException | SAXException | IOException e) {
		 * e.printStackTrace(); }
		 * 
		 * doc.getDocumentElement().normalize(); Element root =
		 * doc.getDocumentElement(); parseNode(root);
		 * 
		 * for (String key : listConfig.keySet()) { System.out.println(key +
		 * ": " + listConfig.get(key)); }
		 */

		/*
		 * System.out.println("Stop here"); System.exit(0);
		 */

	}

	private void parseNode(Element root) {
		NodeList list = root.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Node node = list.item(i);

				if (node.getChildNodes().getLength() == 1
						&& node.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE) {
					String key = node.getNodeName();
					String value = node.getTextContent();
					value = value.replaceAll(" ", "");
					value = value.replaceAll("\"", "");
					listConfig.put(key, value);
					// System.out.println(key + ":" + value);
				} else {
					parseNode((Element) list.item(i));
				}

			}
		}
	}

}
