import java.util.*;
import net.htmlparser.jericho.*;

class HTMLReadWriter{
	String encode;
	byte[] fileBinary;
	String stringFileBinary;
	Source html;
	OutputDocument output;
	
	
	public List<String> read(){
		String value;
		List<String> valueList = new ArrayList<String>();
		List<Element> elementList = html.getAllElements();
	
		if(elementList != null){
			for(Element element: elementList){
				value = element.getAttributeValue("href");
				if(value != null)valueList.add(value);
				value = element.getAttributeValue("src");
				if(value != null)valueList.add(value);
			}
		}
		return valueList;
	}
	
	
	
	public void replacePath(String argValue, String cToFilePath) throws Exception{ //HTML内のパスをローカルに書き換える
		List<Element> elementList = html.getAllElements();
		if(elementList != null){
			for(Element link: elementList){
				String value = link.getAttributeValue("href");
				if(value != null && value.equals(argValue) == true){
					Attributes attributes = link.getAttributes();
					Map<String,String> attributesMap = output.replace(attributes,true);
					attributesMap.put("href", cToFilePath);
				}
				value = link.getAttributeValue("src");
				if(value != null && value.equals(argValue) == true){
					Attributes attributes = link.getAttributes();
					Map<String,String> attributesMap = output.replace(attributes,true);
					attributesMap.put("src", cToFilePath);
				}
			}
		}
	}
	
	
	
	public void setFileBinary(byte[] binary) throws Exception{
		try{
			fileBinary = binary;
			encode = new Source(new String(fileBinary)).getEncoding();
			stringFileBinary = new String(fileBinary, encode);
			html = new Source(stringFileBinary);
			output = new OutputDocument(html);
		}
		catch(NullPointerException e){
			encode = "UTF-8";
		}
		catch(Exception e){
			throw e;
		}
	}
	
	
	
	public byte[] getFileBinary() throws Exception{
		return output.toString().getBytes(encode);
	}
}