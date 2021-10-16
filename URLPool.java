import java.util.*;

public class URLPool
{
    //2 списка для просмотренных и непросмотренных ссылок
	public LinkedList<URLDepthPair> reviewedURLs;
    public LinkedList<URLDepthPair> untrackedURLs;
    public LinkedList<String> trackedUrls;
    public int waitingThread;

    public URLPool(URLDepthPair depthPair)
    {
		reviewedURLs = new LinkedList<URLDepthPair>();
		untrackedURLs = new LinkedList<URLDepthPair>();
		trackedUrls = new LinkedList<String>();
		waitingThread = 0;
    	untrackedURLs.add(depthPair);
    }


    public synchronized int getWaitThreads()
    {
    	return waitingThread;
    }
    //если список неотсмотренных пустой, поток ставится на ожидание и кол-во ожидающих потоков становится на 1 больше
    //иначе мы вынимаем из списка неотсмотренных первую пару, добавляем в просмотренные и возвращаем
    public synchronized URLDepthPair get()
    {
    	URLDepthPair depthPair = null;
    	if(untrackedURLs.size() == 0){
    		try{
    			waitingThread++;
    			this.wait();
    		}
    		catch(InterruptedException e)
    		{
    			System.err.println("InterruptedException: " + e.getMessage());
    			return null;
    		}
    	}
    	depthPair = untrackedURLs.removeFirst();
    	reviewedURLs.add(depthPair);
    	trackedUrls.add(depthPair.getURL());
    	return depthPair;
    }
    //перебираем список новых пар и добавляем в непросмотренные
    //если кол-во ожидающих потоков > 0, то ожидающих потоков становится меньше на 1
    public synchronized void put(LinkedList<URLDepthPair> newdepthPairs)
    {
    	for(int i = 0; i < newdepthPairs.size(); i++){
    		untrackedURLs.add(newdepthPairs.get(i));
    	}
    	if(waitingThread > 0){
    		waitingThread--;
            //поток возобновляет работу
    		this.notify();
    	}
    }

    public synchronized LinkedList<URLDepthPair> getReviewedList()
    {
    	return reviewedURLs;
    }
}