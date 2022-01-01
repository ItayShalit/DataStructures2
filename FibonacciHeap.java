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
    private int TotalMarks = 0;
    private int TotalTrees = 0;
    private int size = 0;
    
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
        private int rank = 0;

        public HeapNode(int key) {
            this.key = key;
            this.next = this;
            this.prev = this;
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

        public boolean getMarked() {
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
        this.size ++;
        this.TotalTrees ++;
        return node;
    }


    /**
     * Inserts a node into the heap, as the first item in it.
     * @pre The added key does not belong to the heap.
     */
    public void insertNode(HeapNode node)
    {
        if (isEmpty())
        {
            this.first = node;
            this.min = node;
            node.setNext(node);
            node.setPrev(node);
        }
        else
        {
            HeapNode lastnode = this.first.getPrev(); // last node in the first row of roots
            //changes the pointers to make node the first one
            lastnode.setNext(node);
            node.setNext(this.first);
            node.setPrev(lastnode);
            first.setPrev(node);
            this.first = node;
            if (node.getKey() < this.min.getKey())// updates the min if necessary
                this.min = node;
        }
    }


    /**
     * public void deleteMin()
     *
     * Deletes the node containing the minimum key.
     *
     */
    public void deleteMin()
    {
    	if (!(this.isEmpty())){
	        HeapNode child = this.min.getChild();
	        if (child != null) { //Adding the children of this.min to the series of roots, instead of this.min.
	        	HeapNode node = child;	        	
	        	do{
		        	node.setParent(null);
		        	node = node.getNext();
	        	}
	        	while(node != child);
	        	if (this.min.getNext() != this.min) { //If min is not the only tree root in the heap
		            HeapNode lastChild = child.getPrev();
		            this.min.getPrev().setNext(child);
		            child.setPrev(this.min.getPrev());
		            this.min.getNext().setPrev(lastChild);
		            lastChild.setNext(this.min.getNext());
		            if (this.min == this.first)
		                this.first = child;
	        	}
	        	else {
	        		this.first = this.min.getChild();
	        	}
	        }
	        else {
	        	if (this.min.getNext() != this.min) { //If min is not the only tree root in the heap
		            this.min.getPrev().setNext(this.min.getNext());
	                this.min.getNext().setPrev(this.min.getPrev());
		            if (this.min == this.first)
		                this.first = this.min.getNext();
	        	}
		        else {
		        	this.min = null;
		            this.first = null;
		        }
	        }
	        this.size --;
	        successiveLinking(); //Also updates minimum	        
    	}
    }

    /**
     * Update the heap to be the result of a successive linking process, commited over the trees in the heap, 
     * and sets the new heap form as 
     */
    
    private void successiveLinking() {
    	if (!(this.isEmpty())) {
	    	HeapNode node = this.first;
	    	int maxRank = node.getRank();
	    	int numOfTrees = 1;
	    	while(node.getNext() != this.first) { //Finds the maximal rank of a tree in the heap.
	    		node = node.getNext();
	    		numOfTrees++;
	    		if (node.getRank() > maxRank)
	    			maxRank = node.getRank();
	    	}
	    	HeapNode[] buckets = new HeapNode[maxRank + numOfTrees];
	    	node = this.first.getPrev();
	    	HeapNode nextNode = node.getNext();
	    	do { //Starting from the first root node, and iterating until the last, while performing successive linking.
	    		node = nextNode;
	    		nextNode = node.getNext();
	    		while(buckets[node.getRank()] != null) {
	    			int prevRank = node.getRank();
	    			node = joinTrees(node, buckets[node.getRank()]);
	    			buckets[prevRank] = null;
	    		}
	    		buckets[node.getRank()] = node;   
	    	}
	    	while(nextNode != first);
	    	int minBucketWithNode = buckets.length - 1;
	    	for(int i = 0; i < buckets.length; i++) { //Finding the index of minimal bucket that has a node in it.
	    											  //We assume buckets isn't empty, because of the pre - condition.
	    		if (buckets[i] != null) {
	    			minBucketWithNode = i;
	    			break;
	    		}
	    	}
	    	this.first = buckets[minBucketWithNode];
	    	this.first.setNext(this.first);
	    	this.first.setPrev(this.first);
	    	this.min = buckets[minBucketWithNode];
	    	this.TotalTrees = 1;
	    	for (int i = minBucketWithNode + 1; i < buckets.length; i++) { //Links all trees in the bucket to form a heap,
	    		                                                           //and updates the heap fields.
	    		if (buckets[i] != null) {
	    			first.getPrev().setNext(buckets[i]);
	    			buckets[i].setPrev(first.getPrev());
	    			first.setPrev(buckets[i]);
	    			buckets[i].setNext(first);
	    			first = buckets[i];
	    			if (buckets[i].getKey() < this.min.getKey())
	    				this.min = buckets[i];
	    			this.TotalTrees++;
	    		}
	    	}
    	}
   }
    
    /**
     * Joins two trees.
     *@pre root1, root2 are the roots of the trees to be joined.
     *@pre The trees do not share common keys.
     *@pre root1.getRank() == root2.getRank()
     * Melds heap2 with the current heap, and returns the root of the new tree formed.
     *
     */

    public HeapNode joinTrees (HeapNode root1, HeapNode root2)
    {
        if (root1.getKey() < root2.getKey()) {
            addRootAsChild(root1, root2);
        	return root1;
        }
        else {
            addRootAsChild(root2, root1);
        	return root2;
        }
    }

    /**
     * @pre SmallerKeyRoot.getKey() != LargerKeyRoot.getKey()
     * @pre both smallerKeyRoot and LargerKkeyRoot are already in the tree.
     * @post  SmallerKeyRoot.getChild() == LargerKeyRoot
     * @post totalLinksCounter == 1 + @prev totalLinksCounter
     */
    private void addRootAsChild(HeapNode SmallerKeyRoot, HeapNode LargerKeyRoot) {
        LargerKeyRoot.setParent(SmallerKeyRoot);
        if (SmallerKeyRoot.getChild() != null) {
	        LargerKeyRoot.setNext(SmallerKeyRoot.getChild());
	        LargerKeyRoot.setPrev(SmallerKeyRoot.getChild().getPrev());
	        SmallerKeyRoot.getChild().getPrev().setNext(LargerKeyRoot);
	        SmallerKeyRoot.getChild().setPrev(LargerKeyRoot);
        }
        else {
        	LargerKeyRoot.setNext(LargerKeyRoot);
	        LargerKeyRoot.setPrev(LargerKeyRoot);
        }
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
        return this.min;
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
        	if (this.min.getKey() > heap2.getMin().getKey()) //We assume no duplicates
        		this.min = heap2.getMin();
            HeapNode node = this.first.getPrev(); //The last root node of this heap.
            node.setNext(heap2.getFirst());
            heap2.getFirst().setPrev(node);
            node = heap2.getFirst().getPrev(); //The last root node of this heap.
            node.setNext(this.first);
            this.first.setPrev(node);
        }
        this.TotalTrees = this.TotalTrees + heap2.TotalTrees;
        this.TotalMarks = this.TotalMarks + heap2.TotalMarks;
    }
    


    /**
     * public int size()
     *
     * Returns the number of elements in the heap.
     *
     */
    public int size()
    {
        return this.size;
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
        if(x.getKey() != this.min.getKey())
        {
            int a = x.getKey() - this.min.getKey() + 1; // the difference between x's value and the minimum value + 1
            decreaseKey(x,a); //guarantees x will be the new minimum
        }
        deleteMin();
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
        if (x.getKey() < this.min.getKey())
        	this.min = x;
        cascadingCuts(x, false);
    }
    
    /**
     * Receives a node in a tree, and commits cascading cuts up the tree.
     * If cutX parameter is passed as false, x is only cut in case its key is smaller than the key of its parent.
     * If cutX parameter is passed as true, x is neccesserily cut, and then the process goes to his parent and potentially
     * up to the root.
     */
    private void cascadingCuts(HeapNode x, boolean cutX) {
        while(x.getParent() != null) {
            if ((x.getKey() < x.getParent().getKey())||cutX) { //If this condition is true, x will be cut.
                if (x.getNext() != null) {
                    if (x.getNext() != x) //If x is not the only child of its parent.
                        x.getParent().setChild(x.getNext()); //Update the child of x's parent to be x's next node.
                    x.getNext().setPrev(x.getPrev());
                }
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
                if ((parent.getMarked())&&(parent.getParent() != null)) { //x's parent is already marked, and isn't the root.
                    cutX = true; //Ensures that the parent will be cut in the next iteration.
                    parent.setMark(false);
                    this.TotalMarks--;
                }
                else
                {
                    parent.setMark(true);
                    this.TotalMarks ++;
                }
   
                x = parent;
                totalCutsCounter++;
                this.TotalTrees ++;
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
        return (TotalTrees - 2*TotalMarks); // should be replaced by student code
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
     */
    public static int[] kMin(FibonacciHeap H, int k)
    {
        int[] arr = new int[100];
        return arr; // should be replaced by student code
    }

    public HeapNode getFirst() {
        return this.first;
    }
    
    public HeapNode getMin() {
        return this.min;
    }
    
}