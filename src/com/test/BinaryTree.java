package com.test;

public class BinaryTree {
	
	public BinaryTreeNode rootNode;
	
	public void insert(BinaryTreeNode node) throws Exception{
		if(rootNode == null){
			rootNode = new BinaryTreeNode();
			rootNode = node;
		}
		else{
			findPosition(node,this.rootNode);
		}
	}
	
	public void findPosition(BinaryTreeNode node,BinaryTreeNode rootNode){
		if(node.value < rootNode.value){
			if(rootNode.leftNode == null){
				rootNode.leftNode = new BinaryTreeNode();
				rootNode.leftNode = node;
				node.parent = rootNode;
			}
			else{
				findPosition(node, rootNode.leftNode);
			}
		}
		else if(node.value > rootNode.value){
			if(rootNode.rightNode == null){
				rootNode.rightNode = new BinaryTreeNode();
				rootNode.rightNode = node;
				node.parent = rootNode;
			}
			else{
				findPosition(node, rootNode.rightNode);
			}
		}
	}
	
	public void leftNodeTraversal(BinaryTreeNode node){
		if(node.leftNode!=null)
		{
			leftNodeTraversal(node.leftNode);
		}
		System.out.println(node.value+"->");
		
	}
	
	public void rightNodeTraversal(BinaryTreeNode node){
		if(node.rightNode!=null)
		{
			rightNodeTraversal(node.rightNode);
		}
		System.out.println("<-"+node.value);
		
	}
	
	public void preOrderTraversal(BinaryTreeNode rootNode){
		
		if(rootNode.leftNode!=null)
			preOrderTraversal(rootNode.leftNode);
		if(rootNode.rightNode!=null)
			preOrderTraversal(rootNode.rightNode);
		System.out.println(rootNode.value);
	}

	public void postOrderTraversal(){
		rightNodeTraversal(rootNode);
		System.out.println("Root Node:"+rootNode.value);
		leftNodeTraversal(rootNode);
		
	}
	
	public void inOrderTraversal(){
		System.out.println("Root Node:"+rootNode.value);
		leftNodeTraversal(rootNode);
		rightNodeTraversal(rootNode);
	}


	
//	public void printTree(){
//		System.out.println(rootNode.value);
//		System.out.println("/");
//		System.out.println(printTreeNode(rootNode.leftNode));
//		System.out.println("\\");
//		System.out.println(printTreeNode(rootNode.rightNode));
//	}

	public void printTreeNode(BinaryTreeNode node){
		if(node.leftNode !=null){
			System.out.println("/");
			printTreeNode(node.leftNode);
		}
		else if(node.rightNode != null){
			System.out.println("\\");
			printTreeNode(node.rightNode);
		}
		else{
			System.out.println(node.value);
		}

	}
	
	
	
}
