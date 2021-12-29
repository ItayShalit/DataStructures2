/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
	private HeapNode min = null;
	private HeapNode first = null;
	private static int totalLinksCounter = 0;
	private static int totalCutsCounter = 0;
			
   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty()
    {
    	if (this.first == null) 
    		return true;
    	return false;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key)
    {   
    	HeapNode node = new HeapNode(key); 
    	insertNode(node);
    	return node; // should be replaced by student code
    }
    
    
    /**
     * Inserts a node into the heap, as the first item in it. 
     * @pre The added key does not belong to the heap.
     */
    public void insertNode(HeapNode node) 
    {
    	/* Add code here */
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
    	HeapNode child = this.min.getChild();
    	if (child != null) { //Adding the children of this.min to the series of roots, instead of this.min.
	     	HeapNode lastChild = child;
	     	while(lastChild.getNext() != child) {
	     		lastChild = lastChild.getNext();
	     	}
	     	if (this.min.getPrev() != null) 
	     		this.min.getPrev().setNext(child);
	     	if (this.min.getNext() != null) 
	     		this.min.getNext().setPrev(lastChild);
	     	if (this.min == this.first) 
	     		this.first = child;
    	}
	    else {
	    	if (this.min.getPrev() != null) 
	      		this.min.getPrev().setNext(this.min.getNext());
	        if (this.min.getNext() != null) 
	      		this.min.getNext().setPrev(this.min.getPrev());
	    	if (this.min == this.first) 
	      		this.first = this.min.getNext();
	    }
    	
    	/*In which order to implement the melding of trees?*/    	
     	
    	
     	HeapNode newMin = this.first;
     	HeapNode root = this.first;
     	if (this.first != null) { //Find node with minimal key.
	     	while (root.getNext() != first) {
	     		if (root.getNext().getKey() <  newMin.getKey())
	     			newMin = root.getNext();
	     		root = root.getNext();
	     	}
     	}
     	this.min = newMin;
    }
    
    /**
     * Joins two trees.
     *@pre root1, root2 are the roots of the trees to be joined.
     *@pre The trees do not share common keys.
     *@pre root1.getRank() == root2.getRank()
     * Melds heap2 with the current heap.
     *
     */
    
    public void joinTrees (HeapNode root1, HeapNode root2)
    {
    	if (root1.getKey() < root2.getKey()) 
    		addRootAsChild(root1, root2);
    	else
    		addRootAsChild(root2, root1);
    }
    
    /**
     * @pre SmallerKeyRoot.getKey() != LargerKeyRoot.getKey()
     * @post  SmallerKeyRoot.getChild() == LargerKeyRoot
     * @post totalLinksCounter == 1 + @prev totalLinksCounter
     */
    private void addRootAsChild(HeapNode SmallerKeyRoot, HeapNode LargerKeyRoot) {
    	LargerKeyRoot.setParent(SmallerKeyRoot);
    	LargerKeyRoot.setNext(SmallerKeyRoot.getChild());
    	LargerKeyRoot.setPrev(SmallerKeyRoot.getChild().getPrev());
    	SmallerKeyRoot.getChild().setPrev(LargerKeyRoot);
    	SmallerKeyRoot.setChild(LargerKeyRoot);
    	SmallerKeyRoot.setRank(SmallerKeyRoot.getRank() + 1);
    	LargerKeyRoot.setMark(false); //Now that it's no longer a root, it's required to make sure that its mark is correct.
    	totalLinksCounter++;
    }

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin()
    {
    	return new HeapNode(678);// should be replaced by student code
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	if (this.isEmpty()) {
    		this.first = heap2.first;
    		this.min = heap2.min;
    	}
    	else if(!(heap2.isEmpty())){
    		HeapNode node = this.first;
    		while(node.getNext() != this.first) //Reaching the last node of this heap.
    			node = node.getNext();
    		node.setNext(heap2.getFirst());
    		heap2.getFirst().setPrev(node);
    		node = heap2.getFirst();
    		while(node.getNext() != heap2.getFirst()) //Reaching the last node of heap2.
    			node = node.getNext();
    		node.setNext(this.first);
    		this.first.setPrev(node);
    		
    	}
    }
    

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size()
    {
    	return -123; // should be replaced by student code
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
    * 
    */
    public int[] countersRep()
    {
    	if (this.isEmpty())
    		return new int[0];
    	HeapNode root = this.first;
    	int maxRank = this.first.getRank();
    	while(root.getNext() != this.first) {
    		if (root.getNext().getRank() > maxRank)
    			maxRank = root.getNext().getRank();
     		root = root.getNext();
    	}
    	int[] arr = new int[maxRank + 1];
    	root = this.first;
    	arr[root.getRank()]++;
    	while(root.getNext() != this.first) {
    		arr[root.getNext().getRank()]++;
    		root = root.getNext();
    	}
        return arr; 
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) 
    {    
    	return; // should be replaced by student code
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	x.setKey(x.getKey() - delta);
    	boolean cutX = false;
    	while(x.getParent() != null) {
    		if ((x.getKey() < x.getParent().getKey())||cutX) {
				if (x.getNext() != x) //If x is not the only child of its parent.
					x.getParent().setChild(x.getNext()); //Update the child of x's parent to be x's next node.
				x.getNext().setPrev(x.getPrev());
    			if (x.getPrev() != null) {
    				x.getPrev().setNext(x.getNext());
    			}
    			if (x.getParent().getChild() == x) /*If we haven't updated the child of x's parent, which means x is its only child,
    				                                 then we now set its son to be null*/
    				x.getParent().setChild(null);
    			HeapNode parent = x.getParent();
    			parent.setRank(parent.getRank() - 1);
    			x.setParent(null);
    			this.insertNode(x);
    			if ((parent.getMark())&&(parent.getParent() != null)) { //x's parent is already marked, and isn't the root.
    				cutX = true; //Ensures that the parent will be cutted in the next iteration
    				parent.setMark(false);
    			}
    			else {
    				parent.setMark(true);
    			}
    			x = parent;
    			totalCutsCounter++;
    		}
    		else {
    			break;
    		}
    	}
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
    	return -234; // should be replaced by student code
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks()
    {    
    	return totalLinksCounter; 
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return totalCutsCounter;  //Is updated in decreaseKey, and should also be updated in delete.
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    * 
    * k <= n = theta(deg(H)) in a binomial tree.
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {   
    	FibonacciHeap waitingItems = new FibonacciHeap();
        int[] arr = new int[k];
        HeapNode node = H.findMin();
        while() {
        	HeapNode[] nodesToWaitingList = new HeapNode[H.findMin().getRank()];
        	int index = 0;
        	HeapNode minKeyNode = node.getKey();
        	while() {
        		
        	}
        	
    }
        
        
        
        /*continue according to theoretical implementation.
        
        return arr; // should be replaced by student code
    }
    
    public HeapNode getFirst() {
    	return this.first;
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

    	public int key;
    	private HeapNode prev = null;
    	private HeapNode next = null;
    	private HeapNode child = null;
    	private HeapNode parent = null;
    	private boolean mark = false;
    	private int rank;

    	public HeapNode(int key) {
    		this.key = key;
    	}

    	public int getKey() {
    		return this.key;
    	}
    	
    	public HeapNode getNext() {
    		return next;
    	}
    	
    	public HeapNode getPrev() {
    		return prev;
    	}
    	
    	public HeapNode getChild() {
    		return child;
    	}
    	
    	public HeapNode getParent() {
    		return parent;
    	}
    	
    	public boolean getMark() {
    		return this.mark;
    	}
    	
    	public int getRank() {
    		return this.rank;
    	}
    	
    	public int setKey(int k) {
    		return this.key = k;
    	}
    	
    	public void setNext(HeapNode node) {
    		this.next = node;
    	}
    	
    	public void setPrev(HeapNode node) {
    		this.prev = node;
    	}
    	
    	public void setChild(HeapNode node) {
    		this.child = node;
    	}
    	
    	public void setParent(HeapNode node) {
    		this.parent = node;
    	}
    	
    	public void setMark(boolean b) {
    		this.mark = b;
    	}
    	
    	public void setRank(int n) {
    		this.rank = n;
    	}
    	
    }
  }
}
