package symbol;

import java.util.ArrayList;

import frame.Access;

public class Locals {
	public ArrayList <Binding> localsL = new ArrayList <Binding>();
	public ArrayList <Access> accessL;
	
	public int index (Symbol key){
		for (int i=0;i<=localsL.size()-1;i++){
			if (localsL.get(i).key==key)
				return i; 
		}
		return -1;
	}
}
