import java.net.*;
import java.util.*;

class CrawlThread extends Thread{
	private List linkList;
	private String link;
	
	CrawlThread(List linkList, String link){
		this.linkList = linkList;
		this.link = new String(link);
	}
	
	public void run(){
		try{
			Downloader downloader = new Downloader();
			HTMLReadWriter htmlReadWriter = new HTMLReadWriter();
			String HtmlPath = downloader.makeFilePath(link);
			List<String> valueList;
			
			// http://host/directory/file
			String http = downloader.httpLink(link);     // http:
			String httpToR = downloader.httpToRLink(link); // http://host/
			String httpToD = downloader.httpToDLink(link);// http://host/directory/
			
			System.out.println(link); 
			if(downloader.download(link, null) == false)return;
			htmlReadWriter.setFileBinary(downloader.getFileBinary());
			valueList = htmlReadWriter.read();
			
			for(String value: valueList){
				if(value.startsWith("mailto:"))continue; 
				else if(value.startsWith("#"))continue;
				else if(value.startsWith("http://"))link = "";
				else if(value.startsWith("https://"))link = "";
				else if(value.startsWith("//"))link = http;
				else if(value.startsWith("/"))link = httpToR;
				else link = httpToD;
				link = link + value;
				
				URL url = new URL(link);
				if(url.getPath().equals("") || url.getPath().endsWith(".html") || url.getPath().endsWith("/")){
					linkList.add(link);
					htmlReadWriter.replacePath(value, downloader.makeFilePath(link));
					continue;
				}
				if(downloader.download(link, downloader.makeFilePath(link)) == false && downloader.getStatus() != -1)continue;
				System.out.println(link);
				htmlReadWriter.replacePath(value, downloader.makeFilePath(link));
			}
			downloader.save(htmlReadWriter.getFileBinary(), HtmlPath);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
}