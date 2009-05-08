package symbol;

import java.util.ArrayList;

public class Class {
	public Symbol name;
	public ArrayList <Method> methods = new ArrayList <Method>();
	public Fields fields = new Fields();
	
	public Class (String name){
		this.name=Symbol.symbol(name);
	}
	
	public Binding getFieldByKey(Symbol key){
		for (int i=0;i<=fields.fieldsL.size()-1;i++){
			if (fields.fieldsL.get(i).key.equals(key))
				return fields.fieldsL.get(i);
		}
		return null;
	}
	
	public Method getMethodByKey(Symbol key){
		for (int i=0;i<=methods.size()-1;i++){
			if (methods.get(i).metbin.key.equals(key))
				return methods.get(i);
		}
		return null;
	}
}
