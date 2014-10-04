Zappos
======

Steps to execute Code:
============================================

1. clone the repo.
2. go to project -> src -> zappos (where the java file is)
3. compile code -> javac ZapposProg.java -cp ../../lib/javax.json-1.0.4.jar
4. execute code -> java -cp ../../lib/javax.json-1.0.4.jar:. ZapposProg #numberofgifts #totalprice

where #numberofgifts = I/P Number of Gifts
      #totalprice = I/P Total price
      

Output Format
============================================
3 best matched products are shown if found.

Each line contains product combinations with there names and prices. At end I have also shown the difference of total sum from the actual required Price (Difference Value).

E.g. for 4 items

"Flawless Basecoat Nail Foundation" price ="$19.00" ; "Porkchop MX" price ="$60.00" ; "Spitsbergen Hat" price ="$36.00" ; "Marc Jacobs Fragrance EDP 1.7 OZ Spray" price ="$85.00" ; 
Difference value = 0.0


Process Explanation:
============================================

1. Get Enough data though REST API call using Zappos Popularity Index. i.e. More the item is popular better the chance of one buying it.
2. In this code - I have around 8000 items retireved or at max 200 Pages of API calls.
3. Randomly pick any small (around 150 ~ depends on the value of input) items from this set.
4. The size of this set depends on the input size (number of gift), the function optimizeParameters determines this size. Using the principle that more the number of items, smaller this set should be.
5. Once we get this set, we go through all of its subsets of size n (#numberofgifts). The sets are generated using integer array permutation, which is quick and faster as compared to recursive method.
6. while going though all these subsets (product combinations) of size n, find the totalsum price and substract it from given price and store this in a max heap along with product names.
7. Now as the set size was small repeat steps 3-6 multiple times to better approximate the process.
8. At end you have top 3 items stored in heap, which is shown to the user.


HeapSize: 3 <3 products are recommended in best first order> - can be changed using variable - numResults



Variable Parameters:
============================================
1. limitValue      #The set size, on which subsets are generated.
2. numResults      #Heap Size.
3. retrievedItems  #Number of items to get using API calls.
4. maxPages        #Max number of pages to search.
5. numIterations   #number of times to repeat the subset generation process.



Parameter Optimization Function
============================================
adjustParameters() -- Optimizes set size depending on how large the input size is.
Controls the accuracy and running time tradeoff. Therefore optimized to run fast.


How to instantiate object
============================================
ZapposProg zp = new ZapposProg();
ProcessItems pt = zp.new ProcessItems(num,totPrice);
pt.runProcess();		


How to reuse object
============================================
pt.changeParametersRunProcess(1, 200);


Sample Results:
============================================

Example 1:
Number of Gifts: 4
Total Price: 200 dollars

Top 3 Results -- 

"Flawless Basecoat Nail Foundation" price ="$19.00" ; "Porkchop MX" price ="$60.00" ; "Spitsbergen Hat" price ="$36.00" ; "Marc Jacobs Fragrance EDP 1.7 OZ Spray" price ="$85.00" ; 
Difference value = 0.0

"Flawless Basecoat Nail Foundation" price ="$19.00" ; "Spitsbergen Hat" price ="$36.00" ; "Marc Jacobs Fragrance EDP 1.7 OZ Spray" price ="$85.00" ; "Porkchop MX" price ="$60.00" ; 
Difference value = 0.0

"Les Soins D'exception Bancoulier Oil Conditioner" price ="$60.00" ; "Flawless Basecoat Nail Foundation" price ="$19.00" ; "Spitsbergen Hat" price ="$36.00" ; "Marc Jacobs Fragrance EDP 1.7 OZ Spray" price ="$85.00" ; 
Difference value = 0.0



Example 2:
Number of Gifts: 8
Total Price: 1000 dollars

Top 3 Results -- 

"Revolution&#8482; Large Spatula" price ="$16.00" ; "Porkchop MX" price ="$60.00" ; "Revolution&#8482; Large Spatula" price ="$16.00" ; "MC2 2 Qt. Sauce Pan with Lid" price ="$135.00" ; "Old Leather Collection - Messenger Bag" price ="$525.00" ; "Spitsbergen Hat" price ="$36.00" ; "Krista Super Skinny in London Calling" price ="$176.00" ; "Spitsbergen Hat" price ="$36.00" ; 
Difference value = 0.0

"Deep Dish Pie Dish" price ="$21.00" ; "Revolution&#8482; Large Spatula" price ="$16.00" ; "MC2 2 Qt. Sauce Pan with Lid" price ="$135.00" ; "Viva La Juicy Body Cr&#232;me 6.7 oz." price ="$55.00" ; "Old Leather Collection - Messenger Bag" price ="$525.00" ; "Spitsbergen Hat" price ="$36.00" ; "Krista Super Skinny in London Calling" price ="$176.00" ; "Spitsbergen Hat" price ="$36.00" ; 
Difference value = 0.0

"Revolution&#8482; Large Spatula" price ="$16.00" ; "Deep Dish Pie Dish" price ="$21.00" ; "MC2 2 Qt. Sauce Pan with Lid" price ="$135.00" ; "Viva La Juicy Body Cr&#232;me 6.7 oz." price ="$55.00" ; "Old Leather Collection - Messenger Bag" price ="$525.00" ; "Spitsbergen Hat" price ="$36.00" ; "Krista Super Skinny in London Calling" price ="$176.00" ; "Spitsbergen Hat" price ="$36.00" ; 
Difference value = 0.0



Example 3:
Number of Gifts: 12
Total Price: 800 dollars

Top 3 Results -- 

"Monroe Starter Hat" price ="$30.00" ; "Health Factor Shampoo 8.45 oz." price ="$27.50" ; "Spitsbergen Hat" price ="$37.00" ; "Health Factor Shampoo 8.45 oz." price ="$27.50" ; "Old Leather Card Case" price ="$65.00" ; "Carbine" price ="$100.00" ; "MC2 2 Qt. Sauce Pan with Lid" price ="$135.00" ; "Carbine" price ="$140.00" ; "Age Defying Copper Serum" price ="$155.00" ; "Tantalizer Lips with Benefits" price ="$18.00" ; "Flawless Basecoat Nail Foundation" price ="$19.00" ; "Pedometer" price ="$46.00" ; 
Difference value = 0.0

"Monroe Starter Hat" price ="$30.00" ; "Health Factor Shampoo 8.45 oz." price ="$27.50" ; "Spitsbergen Hat" price ="$37.00" ; "Health Factor Shampoo 8.45 oz." price ="$27.50" ; "Old Leather Card Case" price ="$65.00" ; "Carbine" price ="$100.00" ; "MC2 2 Qt. Sauce Pan with Lid" price ="$135.00" ; "Carbine" price ="$140.00" ; "Athalon 29\" Hybrid Travelers" price ="$154.99" ; "Tantalizer Lips with Benefits" price ="$18.00" ; "Flawless Basecoat Nail Foundation" price ="$19.00" ; "Pedometer" price ="$46.00" ; 
Difference value = 0.010009766

"Monroe Starter Hat" price ="$30.00" ; "Health Factor Shampoo 8.45 oz." price ="$27.50" ; "Spitsbergen Hat" price ="$37.00" ; "Health Factor Shampoo 8.45 oz." price ="$27.50" ; "Old Leather Card Case" price ="$65.00" ; "Carbine" price ="$100.00" ; "Athalon 29\" Hybrid Travelers" price ="$154.99" ; "MC2 2 Qt. Sauce Pan with Lid" price ="$135.00" ; "Carbine" price ="$140.00" ; "Tantalizer Lips with Benefits" price ="$18.00" ; "Flawless Basecoat Nail Foundation" price ="$19.00" ; "Pedometer" price ="$46.00" ; 
Difference value = 0.010009766


Example 4:
Number of Gifts: 20
Total Price: 1500 dollars

Top 3 Results -- 

"Tantalizer Lips with Benefits" price ="$18.00" ; "Athalon 29\" Hybrid Travelers" price ="$154.99" ; "Haute Pooch Handbag Charm" price ="$24.00" ; "Serrated Blade for M610 Food Slicer" price ="$29.99" ; "Viva La Juicy Body Cr&#232;me 6.7 oz." price ="$55.00" ; "Jewel Pouch" price ="$65.00" ; "Athalon 29\" Hybrid Travelers" price ="$154.99" ; "Tantalizer Lips with Benefits" price ="$18.00" ; "Parcel" price ="$239.99" ; "Parcel" price ="$239.99" ; "Carbine" price ="$100.00" ; "Tantalizer Lips with Benefits" price ="$18.00" ; "Reflex W" price ="$109.99" ; "The Pressure" price ="$250.00" ; "Good Grips&#174; Press-Sure Corner Caddy" price ="$29.99" ; 
Difference value = 7.9299316

"Tantalizer Lips with Benefits" price ="$18.00" ; "22B055     " price ="$220.00" ; "Athalon 29\" Hybrid Travelers" price ="$154.99" ; "Haute Pooch Handbag Charm" price ="$24.00" ; "Serrated Blade for M610 Food Slicer" price ="$29.99" ; "Viva La Juicy Body Cr&#232;me 6.7 oz." price ="$55.00" ; "Jewel Pouch" price ="$65.00" ; "Athalon 29\" Hybrid Travelers" price ="$154.99" ; "Tantalizer Lips with Benefits" price ="$18.00" ; "Parcel" price ="$239.99" ; "Carbine" price ="$100.00" ; "Tantalizer Lips with Benefits" price ="$18.00" ; "Reflex W" price ="$109.99" ; "The Pressure" price ="$250.00" ; "Good Grips&#174; Press-Sure Corner Caddy" price ="$29.99" ; 
Difference value = 12.060059

"Tantalizer Lips with Benefits" price ="$18.00" ; "22B055     " price ="$220.00" ; "Athalon 29\" Hybrid Travelers" price ="$154.99" ; "Haute Pooch Handbag Charm" price ="$24.00" ; "Serrated Blade for M610 Food Slicer" price ="$29.99" ; "Viva La Juicy Body Cr&#232;me 6.7 oz." price ="$55.00" ; "Jewel Pouch" price ="$65.00" ; "Athalon 29\" Hybrid Travelers" price ="$154.99" ; "Tantalizer Lips with Benefits" price ="$18.00" ; "Parcel" price ="$239.99" ; "Parcel" price ="$239.99" ; "Carbine" price ="$100.00" ; "Tantalizer Lips with Benefits" price ="$18.00" ; "Reflex W" price ="$109.99" ; "Good Grips&#174; Press-Sure Corner Caddy" price ="$29.99" ; 
Difference value = 22.070068

