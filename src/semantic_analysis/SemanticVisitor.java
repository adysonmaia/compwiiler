package semantic_analysis;

import syntaxtree.*;
import visitor.TypeVisitor;
import symbol.Table;

public class SemanticVisitor implements TypeVisitor {
	Table table;
	String CurrClass="";
	String CurrMethod="";
	Program p;
	public SemanticVisitor(Table table, Program p){
		this.table = table;
		this.p     = p;
	}
	
	public void start(){
		p.accept(this);
	}
	
    // MainClass m;
	// ClassDeclList cl;
	public Type visit(Program n) {
		n.m.accept(this);
		for ( int i = 0; i < n.cl.size(); i++ ) {
	        n.cl.elementAt(i).accept(this);
	    }
		return null;
	}

	// Identifier i1,i2;
	// Statement s;
	public Type visit(MainClass n) {
		this.CurrClass=n.i1.s;
		this.CurrMethod="main";
		n.s.accept(this);
		this.CurrMethod="";
		this.CurrClass="";		
		return null;
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclSimple n) {
		this.CurrClass = n.i.s;
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		this.CurrClass="";
		return null;
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclExtends n) {
		this.CurrClass = n.i.s;
		if (table.getClassByKey(n.j.s)==null)
			ErrorMsg.complain("Error : Class "+n.j.s+" not declared.");
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		this.CurrClass="";
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(VarDecl n) {
		if (n.t instanceof IdentifierType)
			((IdentifierType)n.t).accept(this);	
		return null;
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public Type visit(MethodDecl n) {	
		this.CurrMethod = n.i.s;
		for (int i = 0; i < n.fl.size(); i++) {
			n.fl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		Type t = n.e.accept(this);
		String s;
		if (t != null)
			s = t.s;
		else
			s = n.t.s;
		if (!(s.equals(n.t.s)))
			ErrorMsg.complain("Error : Type mismatch: The return expression must be of the same type as declared in the declaration of the method.");
		this.CurrMethod="";
		
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(Formal n) {
		if (n.t instanceof IdentifierType)
			((IdentifierType)n.t).accept(this);	
		return null;
	}

	public Type visit(IntArrayType n) {
		return null;
	}

	public Type visit(BooleanType n) {
		return null;
	}

	public Type visit(IntegerType n) {
		return null;
	}

	// String s;
	public Type visit(IdentifierType n) {
		if (table.getAll(CurrClass,CurrMethod,n.s)==null)
			ErrorMsg.complain("Error : Class "+n.s+" not declared.");
		return null;
	}

	// StatementList sl;
	public Type visit(Block n) {
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		return null;
	}

	// Exp e;
	// Statement s1,s2;
	public Type visit(If n) {
		Type t = n.e.accept(this);
		if (t!=null){			
			if (!(t instanceof BooleanType))
				ErrorMsg.complain("Error : Type mismatch: The guard of a if statement must be of type boolean.");
		}
		n.s1.accept(this);
		n.s2.accept(this);
		return null;
	}

	// Exp e;
	// Statement s;
	public Type visit(While n) {
		Type t = n.e.accept(this);
		if (t!=null) {
			if (!(t instanceof BooleanType))			
				ErrorMsg.complain("Error : Type mismatch: The guard of a while statement must be of type boolean.");
		}
		n.s.accept(this);
		return null;
	}

	// Exp e;
	public Type visit(Print n) {
		Type t = n.e.accept(this);
		if (t!=null) {
			if (!(t instanceof IntegerType))
				ErrorMsg.complain("Error  Type mismatch: The argument of a System.out.println statement must be of type int.");
		}
		return null;
	}

	// Identifier i;
	// Exp e;
	public Type visit(Assign n) {
		Object o = table.getAll(CurrClass,CurrMethod,n.i.s);
		if(o==null)
			ErrorMsg.complain("Error : Variable "+n.i.s+" not declared.");			
		Type t = n.e.accept(this);
		if ((t==null) || (o==null))
			return null;
		if (!(t.s.equals(((String)o))))
			ErrorMsg.complain("Error : Type mismatch: Type mismatch: The right hand side of an assign statement must be of the same type of the left hand side.");
		return null;
	}

	// Identifier i;
	// Exp e1,e2;
	public Type visit(ArrayAssign n) {
		Object o = table.getAll(CurrClass,CurrMethod,n.i.s);
		if(o==null)
			ErrorMsg.complain("Error : Variable "+n.i.s+" not declared.");
		else{
			if (!( ((String)o).equals("int[]") ))
				ErrorMsg.complain("Error : Type mismatch: The variable "+n.i.s+" must be of type int[].");
		}
		Type t1=n.e1.accept(this);
		Type t2=n.e2.accept(this);
		if ((t1==null) || (t2==null) || (o==null))
			return null;
		if ( !(t1 instanceof IntegerType) )
			ErrorMsg.complain("Error : Type mismatch: The index of an array must be of type int.");
		if (!(t2 instanceof IntegerType))
			ErrorMsg.complain("Error : Type mismatch: The right hand side of an array assign statment must be of type int.");
		return null;
	}

	// Exp e1,e2;
	public Type visit(And n) {
		Type t1=n.e1.accept(this);
		Type t2=n.e2.accept(this);
		if ((t1==null) || (t2==null))
			return new BooleanType();
		if (! (t1 instanceof BooleanType))
			ErrorMsg.complain("Error : Type mismatch: The left hand side of an and expression must be of type boolean.");
		if (! (t2 instanceof BooleanType))
			ErrorMsg.complain("Error : Type mismatch: The right hand side of an and expression must be of type boolean.");
		return new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(LessThan n) {
		Type t1=n.e1.accept(this);
		Type t2=n.e2.accept(this);
		if ((t1==null) || (t2==null))
			//return new IntegerType();
			return new BooleanType();
		if (! (t1 instanceof IntegerType))
			ErrorMsg.complain("Error : Type mismatch: The left hand side of a less than expression must be of type integer.");
		if (! (t2 instanceof IntegerType))
			ErrorMsg.complain("Error : Type mismatch: The right hand side of a less than expression must be of type integer.");
		return new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(Plus n) {
		Type t1=n.e1.accept(this);
		Type t2=n.e2.accept(this);
		if ((t1==null) || (t2==null))
			return new IntegerType();
		if (! (t1 instanceof IntegerType))
			ErrorMsg.complain("Error : Type mismatch: The left hand side of a plus expression must be of type int.");
		if (! (t2 instanceof IntegerType))
			ErrorMsg.complain("Error : Type mismatch: The right hand side of a plus expression must be of type int.");
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(Minus n) {
		Type t1=n.e1.accept(this);
		Type t2=n.e2.accept(this);
		if ((t1==null) || (t2==null))
			return new IntegerType();
		if (! (t1 instanceof IntegerType))
			ErrorMsg.complain("Error : Type mismatch: The left hand side of a minus expression must be of type int.");
		if (! (t2 instanceof IntegerType))
			ErrorMsg.complain("Error : Type mismatch: The right hand side of a minus expression must be of type int.");
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(Times n) {
		Type t1=n.e1.accept(this);
		Type t2=n.e2.accept(this);
		if ((t1==null) || (t2==null))
			return new IntegerType();
		if (! (t1 instanceof IntegerType))
			ErrorMsg.complain("Error : Type mismatch: The left hand side of a times expression must be of type int.");
		if (! (t2 instanceof IntegerType))
			ErrorMsg.complain("Error : Type mismatch: The right hand side of a times expression must be of type int.");
		return new IntegerType();		
	}

	// Exp e1,e2;
	public Type visit(ArrayLookup n) {
		Type t1=n.e1.accept(this);
		Type t2=n.e2.accept(this);
		if ((t1==null) || (t2==null))
			return new IntegerType();
		if (! (t1 instanceof IntArrayType))
			ErrorMsg.complain("Error : Type mismatch: The expression must be of type int[].");
		if (! (t2 instanceof IntegerType))
			ErrorMsg.complain("Error : Type mismatch: The index expression must be of type int.");
		return new IntegerType();
	}

	// Exp e;
	public Type visit(ArrayLength n) {
		Type t = n.e.accept(this);
		if (t==null){
			return new IntegerType();
		}
		if (! (t instanceof IntArrayType))
			ErrorMsg.complain("Error : Type mismatch: The expression before dot must be of type int[].");
		return new IntegerType();
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	public Type visit(Call n) {
		Type t = n.e.accept(this);
		if (t==null)
			return null;
		if (!( t instanceof IdentifierType)){
			ErrorMsg.complain("Error : Type mismatch: The expression before dot must be a object.");
			return null;
		}
		IdentifierType it = (IdentifierType)t;
		if (table.getAll("", "", it.s)==null){
			ErrorMsg.complain("Error : Object "+((IdentifierExp)n.e).s+" is not of a valid class.");
			return null;
		}		
		Object o = table.getMethod(it.s,n.i.s);
		if (o==null){
			ErrorMsg.complain("Error : Method "+n.i.s+" not declared in class "+it.s+".");
			return null;
		}		
		
		for (int i=0; i < n.el.size(); i++){
			Object ob = table.getParamsObjectByIndex(it.s,n.i.s,i);
			Exp e=n.el.elementAt(i);
			if (!(e.accept(this).s.equals((String)ob) ))
				ErrorMsg.complain("Error : Type mismatch: The argument "+(i+1)+" of the method "+n.i.s+" must be of type "+((String)ob)+".");
		}
		
		if (((String)o).equals("int"))
			return new IntegerType();
		else{
			if (((String)o).equals("boolean"))
				return new BooleanType();
			else{
				if (((String)o).equals("int[]"))
					return new IntArrayType();
				else
					return new IdentifierType((String)o);
			}
		}
	}

	// int i;
	public Type visit(IntegerLiteral n) {
		return new IntegerType();
	}

	public Type visit(True n) {
		return new BooleanType();
	}

	public Type visit(False n) {
		return new BooleanType();
	}

	// String s;
	public Type visit(IdentifierExp n) {
		Object o = table.getAll(CurrClass,CurrMethod,n.s);
		if(o==null){
			ErrorMsg.complain("Error : Variable "+n.s+" not declared.");
			return null;
		}
		
		if (((String)o).equals("int"))
			return new IntegerType();
		else{
			if (((String)o).equals("boolean"))
				return new BooleanType();
			else{
				if (((String)o).equals("int[]"))
					return new IntArrayType();
				else
					return new IdentifierType((String)o);
			}
		}
	}

	public Type visit(This n) {
		return new IdentifierType(CurrClass);
	}

	// Exp e;
	public Type visit(NewArray n) {
		Type t = n.e.accept(this);
		if (t!=null){
			if (t instanceof IntegerType)
				return new IntArrayType();
			else
				ErrorMsg.complain("Error : Type mismatch: The especification of the array's size must be type int.");
		}
		return new IntArrayType();
	}

	// Identifier i;
	public Type visit(NewObject n) {
		if(table.getAll(CurrClass,CurrMethod,n.i.s)==null)
			ErrorMsg.complain("Error : Variable "+n.i.s+" not declared.");
		return new IdentifierType(n.i.s);
	}

	// Exp e;
	public Type visit(Not n) {
		return new BooleanType();
	}

	// String s;
	public Type visit(Identifier n) {
		if(table.getAll(CurrClass,CurrMethod,n.s)==null)
			ErrorMsg.complain("Error : Variable "+n.s+" not declared.");
		return null;
	}

}
