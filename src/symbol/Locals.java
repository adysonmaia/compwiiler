package symbol;

import java.util.ArrayList;

public class Locals {
ArrayList <Binding> localsL = new ArrayList <Binding>();
	
	public int index (Symbol key){
		for (int i=0;i<=localsL.size()-1;i++){
			if (localsL.get(i).key==key)
				return i; 
		}
		return -1;
	}
}
