package semantic_analysis;

import syntaxtree.*;
import visitor.Visitor;
import symbol.Table;;


public class TableVisitor implements Visitor {
	String currClass="";
	String currMethod="";
	Program p;
	Table table;
	
	public TableVisitor(Program p){
		this.p = p;
		table  = new Table();
	}
	
	public Table start(){
		p.accept(this);
		return table;
	}
	
	// MainClass m;
	// ClassDeclList cl;
	public void visit(Program n) {
		n.m.accept(this);
	    for ( int i = 0; i < n.cl.size(); i++ ) {
	        n.cl.elementAt(i).accept(this);
	    }
	}

	// Identifier i1,i2;
	// Statement s;
	public void visit(MainClass n) {
	    currClass = n.i1.s;
		table.put(currClass);
		currMethod = "main";
		table.put(currClass, currMethod, "");
		table.put(currClass, currMethod, "p", n.i2.s, "String []");
		n.s.accept(this);
		currMethod = "";
		currClass  = "";
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public void visit(ClassDeclSimple n) {
		currClass = n.i.s;
		if(!table.put(currClass))
			ErrorMsg.complain("Error : Duplicate class "+n.i.s+".");
		for ( int i = 0; i < n.vl.size(); i++ ) {
	        n.vl.elementAt(i).accept(this);
	    }
	    for ( int i = 0; i < n.ml.size(); i++ ) {
	        n.ml.elementAt(i).accept(this);
	    }
		currClass="";
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public void visit(ClassDeclExtends n) {
	    currClass = n.i.s;
		if(!table.put(currClass))
			ErrorMsg.complain("Error : Duplicate class "+n.i.s+".");
		for ( int i = 0; i < n.vl.size(); i++ ) {
	        n.vl.elementAt(i).accept(this);
	    }
	    for ( int i = 0; i < n.ml.size(); i++ ) {
	        n.ml.elementAt(i).accept(this);
	    }
		currClass="";
	}

	// Type t;
	// Identifier i;
	public void visit(VarDecl n) {
		if(!table.put(currClass,currMethod,"l",n.i.s, n.t.s))
			ErrorMsg.complain("Error : Duplicate variable "+n.i.s+".");
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public void visit(MethodDecl n) {
	    currMethod=n.i.s;
		if(!table.put(currClass,n.i.s,n.t.s)){
			ErrorMsg.complain("Error : Duplicate method "+n.i.s+".");
			return;
		}
		for ( int i = 0; i < n.fl.size(); i++ ) {
	        n.fl.elementAt(i).accept(this);
	    }
	    for ( int i = 0; i < n.vl.size(); i++ ) {
	        n.vl.elementAt(i).accept(this);
	    }
		currMethod="";
	}

	// Type t;
	// Identifier i;
	public void visit(Formal n) {
		if (!table.put(currClass, currMethod, "p", n.i.s, n.t.s))
			ErrorMsg.complain("Error : Duplicate variable "+n.i.s+".");
	}

	public void visit(IntArrayType n) {

	}

	public void visit(BooleanType n) {
	
	}

	public void visit(IntegerType n) {
	
	}

	public void visit(IdentifierType n) {
		
	}
      // StatementList sl;
	public void visit(Block n) {
		
	}

	// Exp e;
	// Statement s1,s2;
	public void visit(If n) {
		
	}

	// Exp e;
	// Statement s;
	public void visit(While n) {
		
	}

	// Exp e;
	public void visit(Print n) {
		
	}

	// Identifier i;
	// Exp e;
	public void visit(Assign n) {
		
	}

	// Identifier i;
	// Exp e1,e2;
	public void visit(ArrayAssign n) {
		
	}

	// Exp e1,e2;
	public void visit(And n) {
		
	}

	// Exp e1,e2;
	public void visit(LessThan n) {
		
	}

	// Exp e1,e2;
	public void visit(Plus n) {
		
	}

	// Exp e1,e2;
	public void visit(Minus n) {
		
	}

	// Exp e1,e2;
	public void visit(Times n) {
		
	}

	// Exp e1,e2;
	public void visit(ArrayLookup n) {
		
	}

	// Exp e;
	public void visit(ArrayLength n) {
		
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	public void visit(Call n) {
		
	}

	// int i;
	public void visit(IntegerLiteral n) {
		
	}

	public void visit(True n) {
		
	}

	public void visit(False n) {
		
	}

	// String s;
	public void visit(IdentifierExp n) {
		
	}

	public void visit(This n) {
		
	}

	// Exp e;
	public void visit(NewArray n) {
		
	}

	// Identifier i;
	public void visit(NewObject n) {
		
	}

	// Exp e;
	public void visit(Not n) {
		
	}

	// String s;
	public void visit(Identifier n) {
		
	}
}
