package symbol;

import java.util.ArrayList;

public class Method {
	public Binding metbin;
	public Parameters params = new Parameters();
	public Locals locals = new Locals();
	
	public Method(Binding metbin){
		this.metbin=metbin;
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
