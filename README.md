zappos
======

Steps to execute Code:
============================================

1. clone the repo.
2. go to project -> src -> zappos (where the java file is)
3. compile code -> javac ZapposProg.java -cp ../../lib/javax.json-1.0.4.jar
4. execute code -> java -cp ../../lib/javax.json-1.0.4.jar:. ZapposProg #numberofgifts #totalprice

where #numberofgifts = I/P Number of Gifts
      #totalprice = I/P Total price
      
============================================


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



