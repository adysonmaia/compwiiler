package symbol;

import java.util.ArrayList;
import frame.Access;

public class Parameters {
	public ArrayList <Binding> paramsL = new ArrayList <Binding>(); 
	public ArrayList <Access> accessL;
	
	public int index (Symbol key){
		for (int i=0;i<=paramsL.size()-1;i++){
			if (paramsL.get(i).key.equals(key)){
				return i;
			}
		}
		return -1;
	}
}
