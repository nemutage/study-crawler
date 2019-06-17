import java.util.*;

class Main{
	public static void main(String args[]){
		try{
			if(args.length != 2)return;
			List<String> linkListA = new ArrayList<String>();
			List<String> linkListB = new ArrayList<String>();
			
			linkListA.add(args[0]);
			for(int i = 1; i <= Integer.parseInt(args[1]); i++){
				CrawlThread[] crawlThread = new CrawlThread[linkListA.size()];
				int threadNum = 0;
				for(String link: linkListA){
					crawlThread[threadNum] = new CrawlThread(linkListB, link);
					crawlThread[threadNum].start();
					threadNum++;
				}
				for(int j = 0; j < threadNum; j++){
					crawlThread[j].join();
				}
				linkListA = linkListB;
				linkListB = new ArrayList<String>();
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
}