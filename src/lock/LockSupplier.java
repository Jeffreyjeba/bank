package lock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LockSupplier {

	private  Map<Integer ,Userlock> lockCache = new ConcurrentHashMap<Integer, Userlock>();
	
	public Userlock getLock(int id) {
		Userlock lock=lockCache.get(id);
		if(lock== null) {
			Userlock newLock=createNewLock();
			lockCache.put(id, newLock);
			return newLock;
		}
		else {
			return lock;
		}
	}
	
	private Userlock createNewLock() {
		return new Userlock();
	}	
}
