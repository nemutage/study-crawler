import java.net.*;
import java.io.*;
import java.util.*;

class Downloader{
	private byte[] fileBinary;
	private int status;
	
	public boolean download(String str, String filePath) throws Exception{
		try{
			return download(str, filePath, "GET");
		}
		catch(IOException e){
			return download(str, filePath, "POST");
		}
		catch(Exception e){
			throw e;
		}
	}
	
	
	
	private boolean download(String str, String filePath, String httpMethod) throws Exception{ 
		try{
			URL url = new URL(str);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(httpMethod);
			connection.connect();
			InputStream input = new BufferedInputStream(connection.getInputStream());
			
			int len;
			byte[] buffer = new byte[1024];
			
			if(filePath != null){
				if(new File(filePath).exists()){ 
					status = -1;
					return false; 
				}
				int index = filePath.lastIndexOf("\\");
				new File(filePath.substring(0, index + 1)).mkdirs(); 
				OutputStream output = new BufferedOutputStream(new FileOutputStream(filePath));
				
				while((len = input.read(buffer)) != -1){
					output.write(buffer, 0, len);
				}
				input.close();
				output.close();
			}
			else{
				List<Byte> byteList = new ArrayList<Byte>();
				
				while((len = input.read(buffer)) != -1){
					for(int i = 0; i < len; i++){
						byteList.add(new Byte(buffer[i]));
					}
				}
				int size = byteList.size();
				fileBinary = new byte[size];
				for(int i = 0; i < size; i++){
					fileBinary[i] = byteList.get(i).byteValue();
				}
				input.close();
			}
			return true;
		}
		catch(FileNotFoundException e){
			System.out.println("< DownloadFile : FNFE> ");
			System.out.println(str); 
			status = -2;
			return false;
		}
		catch(IOException e){
			if(httpMethod.equals("POST")){
				System.out.println("< DownloadFile : IOE> ");
				System.out.println(str);
				status = -3;
				return false;
			}
			throw e;
		}
		catch(Exception e){
			throw e;
		}
	}
	
	
	
	public int getStatus(){
		return status;
	}
	
	
	
	public byte[] getFileBinary(){
		return fileBinary;
	}
	
	
	
	public String httpLink(String str) throws Exception{
		URL url = new URL(str);
		String result = url.getProtocol() + ":";
		return result;
		
	}
	
	
	
	public String httpToRLink(String str) throws Exception{
		URL url = new URL(str);
		String result = url.getProtocol() + "://" + url.getAuthority() + "/";
		return result;
		
	}
	
	
	
	public String httpToDLink(String str) throws Exception{ 
		URL url = new URL(str);
		String result;
		if(url.getPath().equals("")){
			result = url.getProtocol() + "://" + url.getAuthority() + "/";
			return result;
		}
		else{
			if(str.endsWith("/"))return str;
			else{
				int index = str.lastIndexOf("/");
				result = str.substring(0, index+1);
				return result;
			}
		}
	}
	
	
	
	public String makeFilePath(String str) throws Exception{
		URL url = new URL(str);
		String result = url.getHost()+ "/" + url.getPath();
		if(url.getPath().endsWith("/") || url.getPath().equals(""))result = result + "index.html";
		result = System.getProperty("user.dir") + "\\hosts\\" + result.replace("/", "\\");
		return result;
	}
	
	
	
	public boolean save(byte[] binary, String filePath) throws Exception{
		if(new File(filePath).exists()){  
			status = -1;
			return false; 
		}
		String str;
		int index = filePath.lastIndexOf("\\");
		str = filePath.substring(0, index+1);
		new File(str).mkdirs(); 
		OutputStream output = new BufferedOutputStream(new FileOutputStream(filePath));
		output.write(binary, 0, binary.length);
		output.close();
		return true;
	}
}