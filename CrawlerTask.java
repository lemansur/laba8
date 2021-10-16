import java.util.*;

public class CrawlerTask implements Runnable
{
	URLPool myPool;
	public CrawlerTask(URLPool pool)
	{
		myPool = pool;
	}
	//метод, который выполняет каждый поток
	public void run(){
		//получает пару из пула
		URLDepthPair depthPair = myPool.get();
		int depth = depthPair.getDepth();
		//если глубина меньше указанной
		if(depth < Crawler.depth){
			LinkedList<String> urls = new LinkedList<String>();
			LinkedList<URLDepthPair> newUrls = new LinkedList<URLDepthPair>();
			//вызывает метод, возвращающий список всех ссылок на html документе
			urls = Crawler.getAllUrl(depthPair);
			//цикл перебирает все ссылки
			for(int i = 0; i < urls.size(); i++){
				String newURL = urls.get(i);
				//если ссылка еще не попадалась, то создает новую пару
				if(!myPool.trackedUrls.contains(newURL)){
					URLDepthPair newDepthPair = new URLDepthPair(newURL, depth + 1);
					//добавляет в список новых ссылок эту пару
					newUrls.add(newDepthPair);
					myPool.trackedUrls.add(newURL);
				}
			}
			//кладет в пул все найденные ссылки
			myPool.put(newUrls);
		}
	}
}