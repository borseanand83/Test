package iteration;

import java.util.Queue;

public class MyFunction2 implements Function2<Integer,Queue, Queue>{ 

	@Override
	public Queue apply(Integer t, Queue u) {
		if(t!=null)
		{
			u.add(t);
			return u;
		}
			
		return null;
	}

}
