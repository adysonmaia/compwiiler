package symbol;

import java.util.ArrayList;
import frame.Frame;
import frame.Access;

public class Method {
	public Binding metbin;
	public Symbol name;
	public Parameters params = new Parameters();
	public Locals locals = new Locals();
	
	public Access thisPtr;
    public Frame frame;
	
	public Method(Binding metbin){
		this.metbin=metbin;
		this.name = this.metbin.key;
	}
	
	public Object getObjectByKey(String mode, Symbol key){
		ArrayList <Binding> l=null;
		if(mode.equals("p"))
			l=params.paramsL;
		if(mode.equals("l"))
			l=locals.localsL;
		for (int i=0;i<=l.size()-1;i++){
			if (l.get(i).key == key)
				return (l.get(i).obj);
		}
		return null;
	}
}
