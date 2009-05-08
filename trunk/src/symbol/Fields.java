package symbol;

import java.util.ArrayList;

public class Fields {
	ArrayList <Binding> fieldsL = new ArrayList <Binding> ();
	public int index(Symbol key){
		for (int i=0;i<=fieldsL.size()-1;i++){
			if (fieldsL.get(i).key.equals(key))
				return i; 
		}
		return -1;
	}
}
