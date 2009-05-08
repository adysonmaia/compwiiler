package symbol;

public class Binding {
	Object obj;
	Symbol key;
	
	public Binding (Symbol key, Object obj){
		this.obj=obj;
		this.key=key;
	}
}
