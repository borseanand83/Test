package iteration;

import java.util.PriorityQueue;
import java.util.Queue;

public class MyFolder<T, U> implements Folder<T, U>
{
    public U fold(U u, Queue<T> ts, Function2<T, U, U> function)
    {
    	System.out.println("U:"+u);
        if(u == null || ts == null || function == null)
            throw new IllegalArgumentException();

        if (ts.isEmpty()) {
            return u;
        }

        // The recursive implementation will overflow the stack for
        // any data set of real size, your job is to implement a
        // non-recursive solution
        
         return fold(function.apply(ts.poll(), u), ts, function);
//        return null;
    }
    
    
    public U fold1(U u, Queue<T> ts, Function2<T, U, U> function)
    {
        if(u == null || ts == null || function == null)
            throw new IllegalArgumentException();

        if (ts.isEmpty()) {
            return u;
        }

        for (int i = 0; i < ts.size(); i++) {
			u = function.apply(ts.poll(), u);
		}
        return u;
    }
    
    public static void main(String[] args) {
    	Folder<Integer, Queue> folder = new MyFolder<Integer, Queue>();
		Queue<Integer> ts = new PriorityQueue<Integer>();
		Queue<Integer> u = new PriorityQueue<Integer>();
		Queue<Integer> r = new PriorityQueue<Integer>();
		ts.add(5);
		ts.add(4);
		ts.add(3);
		ts.add(2);
		ts.add(1);
		Function2<Integer, Queue, Queue> func = new MyFunction2();
    	folder.fold(ts,ts,func);
	}
}