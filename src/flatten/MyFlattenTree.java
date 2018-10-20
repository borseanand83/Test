package flatten;

import java.util.ArrayList;
import java.util.List;

public class MyFlattenTree<T> implements FlattenTree<T> {
	
	private final Function<T, T> fLeft;
	private final Function<Triple<Tree<T>>, Triple<Tree<T>>> fRight;
	
	public MyFlattenTree(){
		fLeft = null;
		fRight= null;
	}
	public MyFlattenTree(Function<T, T> fLeft,Function<Triple<Tree<T>>, Triple<Tree<T>>> fRight) {
		this.fLeft = fLeft;
		this.fRight = fRight;
	}
	
	public List<T> flattenInOrder(Tree<T> tree) {
		if(tree==null)
			throw new IllegalArgumentException();
		List<T> outputList = new ArrayList<T>();
		Either<T, Triple<Tree<T>>> either = tree.get();
		if(either.isLeft()){
			//this T below is leaf
			T t= either.ifLeft(new Function<T, T>() { 

                public T apply(T p) {
                    return p;
                }

            });
			outputList.add(t);
		}
		else 
		{
			return either.ifRight(new Function<Triple<Tree<T>>, List<T>>() {

				@Override
				public List<T> apply(Triple<Tree<T>> p) 
				{
					 List<T> nodes = new ArrayList<T>();
                     nodes.addAll(flattenInOrder(p.left()));
                     nodes.addAll(flattenInOrder(p.middle()));
                     nodes.addAll(flattenInOrder(p.right()));

                     return nodes;
				}
			});
			
			//flattenInOrder(t.);
		}
		return outputList;
	}

	public List<T> flattenInOrder1(Tree<T> tree) {
		if(tree==null)
			throw new IllegalArgumentException();
		List<T> outputList = new ArrayList<T>();
		Either<T, Triple<Tree<T>>> either = tree.get();
		if(either.isLeft()){
			//this T below is leaf
			T t= either.ifLeft(fLeft);
			outputList.add(t);
		}
		else 
		{
			Triple<Tree<T>> rightNode  = either.ifRight(fRight);
			flattenInOrder(rightNode.left());
			flattenInOrder(rightNode.middle());
			flattenInOrder(rightNode.right());
		}
		return outputList;
	}
	
	
	public static void main(String[] args) 
	{
		Tree<Integer> nodes = Tree.Node.tree(5, 4, 9);
		Tree<Integer> root = new Tree.Node<Integer>(Tree.Leaf.leaf(1), nodes, Tree.Leaf.leaf(6));
		MyFlattenTree<Integer> myFlattenTree = new MyFlattenTree<Integer>();
		System.out.println("Flattened tree: " + myFlattenTree.flattenInOrder(root));
		
		Function<Integer,Integer> fleft = new Function<Integer,Integer>() { 

            public Integer apply(Integer p) {
                return p;
            }

        };

		Function<Triple<Tree<Integer>>,Triple<Tree<Integer>>> fRight = new Function<Triple<Tree<Integer>>,Triple<Tree<Integer>>>() { 
            public Triple<Tree<Integer>> apply(Triple<Tree<Integer>> p) {
                return p;
            }
        };

        MyFlattenTree<Integer> myFlattenTree1 = new MyFlattenTree<Integer>(fleft,fRight);
        System.out.println("Flattened tree 11: " + myFlattenTree1.flattenInOrder(root));
	}
}