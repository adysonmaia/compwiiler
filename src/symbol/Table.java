package symbol;

import java.util.ArrayList;

public class Table {
	public ArrayList <Class> classes = new ArrayList <Class> ();
	
	public Class getClassByKey(String key){
		Symbol s = Symbol.symbol(key);
		for (int i=0;i<=classes.size()-1;i++){
			if (classes.get(i).name.equals(s))
				return classes.get(i);
		}
		return null;
	}
	
	public Object get(String currClass, String fieldKey){
		Class c = this.getClassByKey(currClass);
		if (c==null)
			return null;
		Binding b = c.getFieldByKey(Symbol.symbol(fieldKey));
		if (b==null)
			return null;
		return b.obj;
	}
	
	public Object get(String currClass, String currMethod, String mode, String key){
		Class c = this.getClassByKey(currClass);
		if (c==null)
			return null;
		Method m = c.getMethodByKey(Symbol.symbol(currMethod));
		if (m==null)
			return null;
		if (mode.equals("p") || mode.equals("l"))
			return m.getObjectByKey(mode,Symbol.symbol(key));
		return null;
	}
	
	public Object getMethod(String currClass, String currMethod){
		Class c = this.getClassByKey(currClass);
		if (c==null)
			return null;
		Method m = c.getMethodByKey(Symbol.symbol(currMethod));
		if (m==null)
			return null;
		return m.metbin.obj;
	}
	
	public Object getParamsObjectByIndex(String currClass, String currMethod, int i){
		Class c = this.getClassByKey(currClass);
		if (c==null)
			return null;
		Method m = c.getMethodByKey(Symbol.symbol(currMethod));
		if (m==null)
			return null;
		return m.params.paramsL.get(i).obj;
	}
	
	public Object getAll(String currClass, String currMethod, String key){
		Object o = getClassByKey(key);
		if (o==null){
			o = get(currClass, currMethod,"p", key);
			if (o==null){
				o = get(currClass, currMethod,"l", key);
				if (o==null){
					o = get(currClass, key);
				}
				return o;
			}
			return o;
		}
		return o;
	}

	public boolean put (String className){
		Class classe = this.getClassByKey(className);
		if (classe!=null)
			return false;
		classe = new Class (className);
		classes.add(classe);
		return true;
	}
	
	public boolean put (String currClass, String methodName, String type){
		Class classe  = this.getClassByKey(currClass);
		Symbol key    = Symbol.symbol(methodName);
		Method method = classe.getMethodByKey(key);
		if (method!=null)
			return false;
		method = new Method(new Binding(key,type) );
		classe.methods.add(method);
		return true;
	}
	
	public boolean put (String currClass, String currMethod, String mode, String varName, String type){
		Binding bind = new Binding(Symbol.symbol(varName),type);
		Class c = this.getClassByKey(currClass);
		if (currMethod.length()==0){
			if (c.fields.index(bind.key)==-1){
				c.fields.fieldsL.add(bind);
				return true;
			}else{
				return false;
			}
		}
		Method m = c.getMethodByKey(Symbol.symbol(currMethod));
		if (mode.equals("p")){
			if (m.params.index(bind.key)==-1&&(m.locals.index(bind.key)==-1)){
				m.params.paramsL.add(bind);
				return true;
			}else{
				return false;
			}
		}
		if (mode.equals("l")){
			if ((m.locals.index(bind.key)==-1)&&(m.params.index(bind.key)==-1)){
				m.locals.localsL.add(bind);
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
}
