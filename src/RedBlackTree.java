
import java.util.Arrays;
import java.util.Queue;
//The key of the RBT is jobID 
//Red Black Tree 
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Red-black tree implementation as discussed here:
 * http://code.naishe.in/2012/10/red-black-tree-insertion.html
 * 
 * @author Nishant Neeraj (http://naishe.in)
 *
 */
public class RedBlackTree {
	public static enum COLOR{RED, BLACK};
	private RBNode root = null;
 
	public static void main(String[] args) {
		int[] a = {50, 19, 30};
		//int[] a = {8, 11, 14, 18, 22, 23, 31, 37, 41, 47, 60, 74, 84, 87, 88, 97, 99};
		Arrays.sort(a);
		RedBlackTree tree = new RedBlackTree();
		//insertion of a sorted array in a BST will generate a 
		//tree of height (n-1) will all elements in a straight line
		for(int v: a)
			tree.insert(v);
		
		tree.print();
	}
	
	public void print() {
		RBNode MARKER_NODE = new RBNode(0, COLOR.BLACK);
		RBNode EMPTY_NODE = new RBNode(0, COLOR.BLACK);
		
		Queue<RBNode> q = new ArrayBlockingQueue<RedBlackTree.RBNode>(100);
		q.offer(root);
		q.offer(MARKER_NODE);
		while(!q.isEmpty()){
			RBNode curr = q.poll();
			if(curr == MARKER_NODE && !q.isEmpty()){
				q.offer(MARKER_NODE);
				System.out.println("");
			}else{
				if(curr == EMPTY_NODE)
					System.out.print(" x ");
				else{
					if(curr != MARKER_NODE){
						String print = "  "+curr.val+"  ";
						if(curr.color == COLOR.RED)
							print = " ["+curr.val+"] ";
						System.out.print(print);
					}
					if(curr.left == null)
						q.offer(EMPTY_NODE);
					else
						q.offer(curr.left);
					
					if(curr.right == null)
						q.offer(EMPTY_NODE);
					else
						q.offer(curr.right);
				}
			}
		}
	}

	public void insert(int v) {
		if (root == null){
			root = new RBNode(v, COLOR.BLACK);
			return;
		}
		
		RBNode parent = root;
		RBNode current = root;
		while (current != null){
			parent = current;
			if(v < current.val)
				current = current.left;
			else
				current = current.right;
		}
		
		RBNode node = new RBNode(v);
		node.parent = parent;
		if(v < parent.val)
			parent.left = node;
		else
			parent.right = node;

		fixTree(node);
	}

	public Integer next(int nGrater) {
		
		if(root==null) {
			return null;
		}
		
		Integer next = null;
		RBNode MARKER_NODE = new RBNode(0, COLOR.BLACK);
		RBNode EMPTY_NODE = new RBNode(0, COLOR.BLACK);
		
		Queue<RBNode> q = new ArrayBlockingQueue<RedBlackTree.RBNode>(100);
		q.offer(root);
		q.offer(MARKER_NODE);
		while(!q.isEmpty()){
			RBNode curr = q.poll();
			if(curr == MARKER_NODE && !q.isEmpty()){
				q.offer(MARKER_NODE);
			}else{
				if(curr != EMPTY_NODE) {
					if(curr.val==nGrater) {
						if(curr.left!=null) {
							next = curr.right.val;
							break;
						}
						else {
							break;
						}
					}
//					if(curr != MARKER_NODE){
//						next = curr.left.val;
//					}
					if(curr.left == null)
						q.offer(EMPTY_NODE);
					else
						q.offer(curr.left);
					
					if(curr.right == null)
						q.offer(EMPTY_NODE);
					else
						q.offer(curr.right);
				}
			}
		}
		return next;
	}
	
	public Integer prev(int nGrater) {
		
		Integer prev = null;
		
		RBNode MARKER_NODE = new RBNode(0, COLOR.BLACK);
		RBNode EMPTY_NODE = new RBNode(0, COLOR.BLACK);
		
		if(root==null) {
			return null;
		}
		Queue<RBNode> q = new ArrayBlockingQueue<RedBlackTree.RBNode>(100);
		q.offer(root);
		q.offer(MARKER_NODE);
		while(!q.isEmpty()){
			RBNode curr = q.poll();
			if(curr == MARKER_NODE && !q.isEmpty()){
				q.offer(MARKER_NODE);
			}else{
				if(curr != EMPTY_NODE) {
					if(curr.val==nGrater) {
						if(curr.parent!=null) {
							prev = curr.parent.val;
							break;
						}
					}
					if(curr != MARKER_NODE){
					}
					if(curr.left == null)
						q.offer(EMPTY_NODE);
					else
						q.offer(curr.left);
					
					if(curr.right == null)
						q.offer(EMPTY_NODE);
					else
						q.offer(curr.right);
				}
			}
		}
		return prev;
	}
	
	private void fixTree(RBNode node) {
		if(node.parent == null || node.parent.color == COLOR.BLACK)
			return;
		
		RBNode uncle = getUncle(node);
		//case 1: Uncle Red
		if(isRedUncle(uncle)){
			//if uncle is red, implies uncle is not-null && grandparent exists
			node.parent.color = COLOR.BLACK;
			node.parent.parent.color = COLOR.RED;
			uncle.color = COLOR.BLACK;
			fixTree(node.parent.parent);
		}else if(isUncleTheRightChild(node)){
			//case 3: if node also the right child
			if(node.parent.right == node)
				rotateLeft(node);
			//case 2: fix colors
			node.parent.color = COLOR.BLACK;
			node.parent.parent.color = COLOR.RED;
			rotateRight(node.parent);
		}else{
			//case 3: if node also the left child
			if(node.parent.left == node)
				rotateRight(node);
			//case 2: fix colors
			node.parent.color = COLOR.BLACK;
			node.parent.parent.color = COLOR.RED;
			rotateLeft(node.parent);
		}
		
	root.color = COLOR.BLACK;
		
	}

	/**
	 * <pre>
	 *          g                                   g 
	 *         /                                   /
	 *        y        Right Rotation(x)          x
	 *       / \      ---------------->          / \
	 *      x   gamma                        alpha  y
	 *     / \                                     / \
	 * alpha  beta                             beta  gamma
	 * </pre>
	 */
	@SuppressWarnings("unused")
	private void rotateRight(RBNode node) {
		RBNode x = node;
		RBNode y = x.parent;
		RBNode g = y.parent;
		RBNode alpha = x.left;
		RBNode beta = x.right;
		RBNode gamma = y.right;
		
		//Fix x
		x.parent = g;
		if(g != null){
			if(isLeftChild(y))
				g.left = x;
			else
				g.right = x;
		}else{
			this.root = x;
		}
		
		x.right = y;
		
		//fix y
		y.parent = x;
		y.left = beta;
		
		//fix beta
		if(beta != null)
			beta.parent = y;
	}

	/**
	 * <pre>
	 *          g                              g
	 *         /                              /
	 *       x        Left Rotation(y)       y
	 *      /  \     ---------------->      / \
	 *  alpha   y                          x   gamma
	 *         / \                        / \
	 *      beta gamma                alpha  beta
	 * </pre>     
	 * @param node
	 */
	@SuppressWarnings("unused")
	private void rotateLeft(RBNode node) {
		RBNode y 		= node;
		RBNode x 		= node.parent;
		RBNode g 		= x.parent;
		RBNode alpha 	= x.left; //it does not move
		RBNode beta 	= y.left;
		RBNode gamma 	= y.right; //it does not move
		
		//Fix y
		y.parent = g;
		
		if (g != null){
			if(isLeftChild(x))
				g.left = y;
			else
				g.right = y;
		}else{
			this.root = y;
		}
		
		//Fix x
		y.left = x;
		x.parent = y;
		
		//Fix beta
		x.right = beta;
		if(beta != null)
			beta.parent = x;
	}

	private static boolean isLeftChild(RBNode node) {
		return node.parent.left == node;
	}

	private static boolean isUncleTheRightChild(RBNode node) {
		return node.parent == node.parent.parent.left;
	}

	private static boolean isRedUncle(RBNode uncle) {
		return uncle != null && uncle.color == COLOR.RED;
	}

	private static RBNode getUncle(RBNode node) {
		if (node.parent == null || node.parent.parent == null)
			return null;
		
		RBNode parent = node.parent;
		RBNode grandParent = parent.parent;
		
		if (parent == grandParent.left)
			return grandParent.right;
		else
			return grandParent.left;
	}

	public static class RBNode{
		public RBNode(int val, COLOR color) {
			this.val = val;
			this.color = color;
		}
		
		public RBNode(int val){
			this(val, COLOR.RED);
		}
		
		public COLOR color;
		public int val;
		public RBNode left;
		public RBNode right;
		public RBNode parent;
	}

}