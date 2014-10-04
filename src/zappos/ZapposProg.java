package zappos;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class ZapposProg {
	
	private String apiKey = "52ddafbe3ee659bad97fcce7c53592916a6bfd73";
	
	public void changeAPIKey(String key){
		this.apiKey = key;
	}
	
	class ProcessItems{
		
		private int numProds,totalSum; 		// Input by the user
		
		/* Default Values
		*  May change in Parameter Tuning done within program.
		*/
		private int limitValue = 30;  		// limit for subset size considered
		private int numResults = 3;			// Number of results to be shown - max heap size - 1
		private int retrievedItems = 8000;	// Number of Items to get from API call - top N popular
		private int maxPages = 200;			// Max Pages to search items in specific range
		private int numIterations = 5;		// Number of times to run the process to increase likelihood of approximate value.
		
		//Test Variables
		private int countSubsets = 0;
		
		private ArrayList<JsonObject> items;			//Items retrieved using API are stored here.
		private Comparator<ItemsObj> comparator;		//comparator for heaps
		private PriorityQueue<ItemsObj> maxHeapItems;	//MaxHeap to maintains numResults minimum items.
		private PriorityQueue<ItemsObj> minHeapItem;	//To Display result

		/**
		 * Constructor
		 * @param N = Number of items
		 * @param totalSum = Total Price
		 */
		ProcessItems(int N,int totalSum){
			this.numProds = N;
			this.totalSum = totalSum;
			retreiveItemsHelper();
		}
		
		/**
		 * Public method to change parameter and run the process
		 * @param N
		 * @param totalSum
		 */
		public void changeParametersRunProcess(int N,int totalSum){
			this.numProds = N;
			this.totalSum = totalSum;
			
			if(numProds<=0 || totalSum<=0){
				System.out.println("Invalid Search: Try Again");
				return;
			}

			runProcess();
		}
		
		/**
		 * Sets Memory and calls retreiveItems()
		 */
		private void retreiveItemsHelper(){
			
			if(numProds<=0 || totalSum<=0){
				System.out.println("Invalid Search: Try Again");
				return;
			}
			items = new ArrayList<JsonObject>();
			retreiveItems();
		}
		
		/**
		 * Execute Process for finding Items
		 */
		public void runProcess(){
			comparator = new maxObjectComparator();
			maxHeapItems = new PriorityQueue<ItemsObj>(numResults,comparator); //MaxHeap	
			comparator = new minObjectComparator();
			minHeapItem = new PriorityQueue<ItemsObj>(numResults,comparator);

			if(!adjustParameters()){
				return;
			}
			
			if(items.size()==0 || items.size()<numProds){
				System.out.println("No Popular Items Found: Try Again");
				return;
			}
			
			for (int i = 0; i < numIterations; i++) {
				evaluateItems();
			}
			
			printHeap();
			System.out.println("Total Subsets Counted = "+countSubsets);
			
		}
		
		
		/**
		 * Tunes parameter depending on input size
		 */
		
		private boolean adjustParameters(){
			
			try{
				int para = 160;
				int val = para/numProds;
				int count = 0;
				
				while(val<numProds+2){
					para+=50;
					val = para/numProds;
					count++;
					if(count>500)
					{
						System.out.println("These many gifts not supported : Try again ");
						return false;
	
					}
				}
				limitValue = val;
				System.out.println("Parameter value set to : "+limitValue);
				System.out.println();
				System.out.println("Top 3 Results -- ");
				System.out.println();
			}
			catch(Exception e){
				return false;
			}
			return true;
		}
		
		/**
		 * Class which represents an objects stored in Heap.
		 * Heap sorts on |GivenSum - SumofNProducts|  
		 * This value should be as minimum as possible.
		 * @author rohitkondekar
		 */		
		class ItemsObj{
			private String strItems;
			private Float difference;
			public String getStrItems() {
				return strItems;
			}
			public void setStrItem(String strItems) {
				this.strItems = strItems;
			}
			public Float getDifference() {
				return difference;
			}
			public void setDifference(Float difference) {
				this.difference = difference;
			}
			
			@Override
			public boolean equals(Object obj) {
				ItemsObj ob = (ItemsObj)obj;
				return this.getStrItems().equals(ob.getStrItems());
			}
		}
		
		/**
		 * Comparator for Heap Maintainance.
		 * Creates a MaxHeap using Java's Priority Queue implementation.
		 * @author rohitkondekar
		 */
		private class maxObjectComparator implements Comparator<ItemsObj>
		{
			@Override
			public int compare(ItemsObj arg0, ItemsObj arg1) {
				return -arg0.getDifference().compareTo(arg1.getDifference());				
			}			
		}	
		
		private class minObjectComparator implements Comparator<ItemsObj>
		{
			@Override
			public int compare(ItemsObj arg0, ItemsObj arg1) {
				return arg0.getDifference().compareTo(arg1.getDifference());				
			}			
		}	
		
		/**
		 * Retrieves Items.
		 * Items retrieved ~ retrievedItems
		 * Max Pages ~ maxPages
		 * Stores Items in items array. 
		 */
		private void retreiveItems(){
			String surl = "http://api.zappos.com/Search?key="+apiKey+"&sort={\"productPopularity\":\"desc\"}"
					+ "&excludes=[\"styleId\",\"originalPrice\",\"productUrl\",\"colorId\","
					+ "\"brandName\",\"thumbnailImageUrl\",\"percentOff\"]&limit=100&page=##1";
			int page = 1;
			
			try{
				System.out.println("Requesting Items... Please Wait");
				System.out.println();
				while(items.size()<retrievedItems && page<maxPages){
					
					/*
					 * Setup Connection
					 */
					surl = surl.replace("##1", Integer.toString(page));
					URL url = new URL(surl);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Accept", "application/json");
					
					/*
					 * Read from Connection
					 */					
					if(conn.getResponseCode()==200){
						JsonReader jsonreader = Json.createReader(conn.getInputStream());
						JsonArray jarray = jsonreader.readObject().getJsonArray("results");
						
						for(int i=0;i<jarray.size();i++){
							JsonObject obj= (JsonObject) jarray.get(i);							
							float price = getPrice(obj.get("price"));					
							if(price<=totalSum)										
								items.add(obj);
						}
					}
					else{
						System.err.println("Check URL: Some Error in opening connection to url - "+surl);
						System.err.println(conn.getResponseCode()+" "+conn.getResponseMessage());
					}
					page++;
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}		
			System.out.println("REST Request Completed. Number of items requested -- " + items.size());
		}
		
		private void evaluateItems(){
			
			Integer[] list = new Integer[items.size()];
			for(int i=0;i<list.length;i++){
				list[i] = i;
			}
			Collections.shuffle(Arrays.asList(list));
			
			int[] permutationArray = new int[limitValue];
			for (int i = permutationArray.length-numProds; i < permutationArray.length; i++) {
				permutationArray[i] = 1;
			}
			
			while(my_next_permutation(permutationArray)){
				countSubsets++;
				StringBuilder itemNames = new StringBuilder("");
				float sum = 0;
				for (int i = 0; i < permutationArray.length; i++) {
					if(permutationArray[i]==1){
						itemNames.append(items.get(list[i]).get("productId")+": "+items.get(list[i]).get("productName")+" price ="+items.get(list[i]).get("price")+" ; ");
						sum = sum+getPrice(items.get(list[i]).get("price"));
					}
				}
				float dif = Math.abs(sum-totalSum);
				
				ItemsObj obj = new ItemsObj();
				obj.setStrItem(itemNames.toString());
				obj.setDifference(dif);
				
				if(maxHeapItems.size()>=numResults && maxHeapItems.peek().getDifference()>dif && !maxHeapItems.contains(obj))
					maxHeapItems.remove();
				else if(maxHeapItems.size()>=numResults)
					continue;
								
				maxHeapItems.add(obj);	
			}
			
		}	
		
		/**
		 * Gets Float Price from JSON value
		 * @param price
		 * @return
		 */
		private Float getPrice(JsonValue price){
			return Float.parseFloat(price.toString().replace("$", "").replaceAll("\"", "").replaceAll(",", ""));
		}
		
		/**
		 * Prints Output
		 */
		private void printHeap(){
			
			while(!maxHeapItems.isEmpty()){
				minHeapItem.add(maxHeapItems.remove());
			}
			
			while(!minHeapItem.isEmpty()){
				ItemsObj obj = minHeapItem.remove();		
				System.out.print(obj.getStrItems());
				System.out.println("  with difference value = "+obj.getDifference());
			}
		}	
	}
	
	/**
	 * Generates Permutations of given array
	 * @param a
	 * @return
	 */
	public boolean my_next_permutation(int[] a) {
		int n=a.length;
		int i,j,k,temp;
		i=n-2;
		while (i>=0 && a[i]>=a[i+1]) --i;
		if (i<0) {
			for (j=0,k=n-1; j<k; j++,k--) {
				temp=a[j];
				a[j]=a[k];
				a[k]=temp;
			}
			return false;
		}
		j=n-1;
		while (a[i]>=a[j]) --j;
		temp=a[i];
		a[i]=a[j];
		a[j]=temp;
		for (j=i+1,k=n-1; j<k; j++,k--) {
			temp=a[j];
			a[j]=a[k];
			a[k]=temp;
		}
		return true;
	}
	
	public static void main(String[] args) {
//		runTests();
		try{
			int num = Integer.parseInt(args[0]);
			int totPrice = Integer.parseInt(args[1]);
			ZapposProg zp = new ZapposProg();
			ProcessItems pt = zp.new ProcessItems(num,totPrice);
			pt.runProcess();			
		}
		catch(Exception e){
			System.err.println("Invalid Input! or Something went wrong!");
		}
	}
	
	/**
	 * Tests
	 */
	public static void runTests(){
		ZapposProg zp = new ZapposProg();
		ProcessItems pt = zp.new ProcessItems(3,700);
		pt.runProcess();
		System.out.println("------- 1");
		pt.changeParametersRunProcess(1, 200);
		System.out.println("------- 2");
		pt.changeParametersRunProcess(2, 400);
		System.out.println("------- 2");
		pt.changeParametersRunProcess(2, 800);
		System.out.println("------- 2");
		pt.changeParametersRunProcess(2, 1000);
		System.out.println("------- 5");
		pt.changeParametersRunProcess(5, 600);
		System.out.println("------- 8 ");
		pt.changeParametersRunProcess(8, 800);
		System.out.println("------- 10");
		pt.changeParametersRunProcess(10, 1000);
		System.out.println("------- 13");
		pt.changeParametersRunProcess(13, 1000);
		System.out.println("------- 20");
		pt.changeParametersRunProcess(20, 1000);
		System.out.println("------- 100");
		pt.changeParametersRunProcess(100, 8000);
		System.out.println("------- 10");
		pt.changeParametersRunProcess(10, 20000000);
		System.out.println("------- 10000");
		pt.changeParametersRunProcess(10000, 50000);
		System.out.println("------- 100000000");
		pt.changeParametersRunProcess(10000000, 2000);
		System.out.println("------- 0");
		pt.changeParametersRunProcess(0, 200);
		System.out.println("------- -10");
		pt.changeParametersRunProcess(-10, 200);
		System.out.println("------- 10 -200");
		pt.changeParametersRunProcess(10, -200);
	}

}
