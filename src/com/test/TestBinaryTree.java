package com.test;

public class TestBinaryTree {

	public static void main(String[] args) {
		BinaryTree tree = new BinaryTree();
		try {
			BinaryTreeNode node = new BinaryTreeNode();
			node.value = 2;
			tree.insert(node);
			BinaryTreeNode node2 = new BinaryTreeNode();
			node2.value = 4;
			tree.insert(node2);
			BinaryTreeNode node3 = new BinaryTreeNode();
			node3.value = 1;
			tree.insert(node3);
			BinaryTreeNode node4 = new BinaryTreeNode();
			node4.value = 3;
			tree.insert(node4);
			BinaryTreeNode node5 = new BinaryTreeNode();
			node5.value = 8;
			tree.insert(node5);
			BinaryTreeNode node6 = new BinaryTreeNode();
			node6.value = 7;
			tree.insert(node6);
			tree.preOrderTraversal(tree.rootNode);
//			tree.printTreeNode(tree.rootNode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
