package findcommonancestor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class MyFindCommonAncestor implements FindCommonAncestor {

	@Override
	public String findCommmonAncestor(String[] commitHashes,
			String[][] parentHashes, String commitHash1, String commitHash2) {
		
		if(commitHashes == null || parentHashes == null){  
            throw new IllegalArgumentException();  
        }  

		//create a map with key as the node and value as its parent first
		Map<String,String[]> parentChildMap = new HashMap<String,String[]>();  
		for (int i = 0; i < commitHashes.length; i++) {
			parentChildMap.put(commitHashes[i], parentHashes[i]);
		}
		return searchCommonParent(parentChildMap,commitHash1,commitHash2);
	}

//	public static List<String> visitUpwards(Map<String, String[]> parentChildMap, String current) {
//	    List<String> list = new ArrayList<String>();
//	    list.add(current);
//	    if(parentChildMap.get(current) == null) return list;
//	    else {           
//	       for(String s: parentChildMap.get(current)) {              
//	          list.addAll(visitUpwards(parentChildMap, s));                            
//	       }
//	       return list;
//	    }
//	} 
	
//	public static Set<String> tracePath(Map<String, String[]> parentChildMap, String node) {
//	    Queue<String> queue = new PriorityQueue<String>();
//	    Set<String> result = new LinkedHashSet<String>();
//	    queue.add(node);
//	    while(!queue.isEmpty()) {
//	        String current = queue.remove();
//	        result.add(current);
//	        if(parentChildMap.get(current) != null)
//	            for(String s: parentChildMap.get(current)) {
//	                queue.add(s);
//	            }
//	    }
//	    return result;
//	}
	
	private String searchCommonParent(Map<String, String[]> parentChildMap, String node1,String node2) {
	    Queue<String> queue1 = new PriorityQueue<String>();
	    Queue<String> queue2 = new PriorityQueue<String>();
	    queue1.add(node1);
	    queue2.add(node2);
	    while(!queue1.isEmpty() || !queue2.isEmpty()) {
	        String current1 = queue1.remove();
	        String current2 = queue2.remove();
	        if(parentChildMap.get(current1) != null && parentChildMap.get(current2)!=null)
	            for(String parentNode1: parentChildMap.get(current1)) {
	            	if(!Arrays.asList(parentChildMap.get(current2)).contains(parentNode1)){
	            		queue1.add(parentNode1);
	            		for(String parentNode2: parentChildMap.get(current2))
	            			queue2.add(parentNode2);
	            	}
	            	else{
	            		return parentNode1;
	            	}	
	            	
	            }
	    }
	    return null;
	}
	
	
//	public String getCA(String[] nodes, String[][] parentNodes, String targetNode1, String targetNode2) {  
//        if(nodes == null || parentNodes == null){  
//            throw new IllegalArgumentException();  
//        }  
//
//        Map<String, String[]> map = new HashMap<String, String[]>();  
//        for(int i = 0; i < nodes.length; i++){  
//            map.put(nodes[i], parentNodes[i]);  
//        }  
//        //These are the parents visited as we go up  
//        Set<String> parentsSeen1 = new HashSet<String>();
//        Set<String> parentsSeen2 = new HashSet<String>();
//        parentsSeen1.add(targetNode1);  
//
//        Queue<String> path = new LinkedList<String>();  
//        String[] parents1 = map.get(targetNode1);  
//        //The root is the common parent  
//        if(parents1 == null){  
//            return targetNode1;  
//        }   
//
//        //Build the path up  
//        for(String parent:parents1){  
//            path.add(parent);  
//        }  
//        while(!path.isEmpty()){  
//            String currentParent = path.remove();  
//            parentsSeen1.add(currentParent);  
//            parents1 = map.get(currentParent);  
//            if(parents1 == null){  
//                continue;   
//            }  
//            for(String parent:parents1){  
//                path.add(parent);  
//            }  
//        }  
//
//        parents1 = map.get(targetNode2);  
//        //It is the root, so it is the common parent  
//        if(parents1 == null){  
//            return targetNode2;  
//        }  
//        //Start going up for the targetNode2. The first parent that we have already visited is the common parent  
//        for(String parent:parents1){  
//            if(parentsSeen1.contains(parent)){  
//                return parent;  
//            }  
//            path.add(parent);  
//        }  
//
//        while(!path.isEmpty()){  
//            String currentParent = path.remove();  
//            if(parentsSeen1.contains(currentParent)){  
//                return currentParent;  
//            }             
//            parents1 = map.get(currentParent);  
//            if(parents1 == null){  
//                continue;  
//            }  
//            for(String parent:parents1){  
//                path.add(parent);  
//            }  
//        }  
//        return null;            
//    }
//	
//	public static void main(String[] args) {
//		String[] commits = {"G", "F", "E", "D", "C", "B", "A"};
//		String[][] parents ={{"F","D"},{"E"}, {"B"}, {"C"}, {"B"}, {"A"}, null}; 
//		String commit1 = "D";
//		String commit2 = "F";
//		System.out.println(new MyFindCommonAncestor().findCommmonAncestor(commits,parents,commit1,commit2));
//	}
}
