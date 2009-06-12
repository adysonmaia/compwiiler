package translate;

import syntaxtree.*;
import visitor.Visitor;
import frame.Frame;
import symbol.*;
import tree.*;
import util.List;

public class StatementHandler implements Visitor {

	private Exp result;
    
    private symbol.Class cinfo;
    
    private symbol.Method minfo;
    
    private Table env;
    
    private Frame frame;
    
    private tree.Exp getVariable(Symbol name)
    {
        int index = minfo.locals.index(name);
        if ( index >= 0 ) {
        	return minfo.locals.accessL.get(index).exp( new TEMP(frame.FP()) );
        }
        
    	index = -1;
    	index = minfo.params.index(name);
    	if ( index >= 0 ) {
        	return minfo.params.accessL.get(index).exp( new TEMP(frame.FP()) );
        }
        
        // obtendo atributo de classe
        int idx = cinfo.getFieldOffset(name);
        
        MEM node = new MEM( 
                new BINOP(BINOP.PLUS,
                        minfo.thisPtr.exp(new TEMP(frame.FP())),
                        new BINOP(BINOP.LSHIFT, new CONST(idx), new CONST(2))));
        
        return node;
    }
	
    private StatementHandler(Frame f, Table t, symbol.Class c, Method m)
    {
        super();
        
        frame = f;
        env = t;
        cinfo = c;
        minfo = m;
    }
    
    static Exp translate(Frame f, Table t, symbol.Class c, Method m, Statement stm)
    {
        StatementHandler h = new StatementHandler(f, t, c, m);
        
        stm.accept(h);
        
        return h.result;
    }

    
	public void visit(Program n) {
		// TODO Auto-generated method stub

	}

	public void visit(MainClass n) {
		// TODO Auto-generated method stub

	}

	public void visit(ClassDeclSimple n) {
		// TODO Auto-generated method stub

	}

	public void visit(ClassDeclExtends n) {
		// TODO Auto-generated method stub

	}

	public void visit(VarDecl n) {
		// TODO Auto-generated method stub

	}

	public void visit(MethodDecl n) {
		// TODO Auto-generated method stub

	}

	public void visit(Formal n) {
		// TODO Auto-generated method stub

	}

	public void visit(IntArrayType n) {
		// TODO Auto-generated method stub

	}

	public void visit(BooleanType n) {
		// TODO Auto-generated method stub

	}

	public void visit(IntegerType n) {
		// TODO Auto-generated method stub

	}

	public void visit(IdentifierType n) {
		// TODO Auto-generated method stub

	}

	public void visit(Block n) {
		// TODO Auto-generated method stub

	}

	public void visit(If n) {
		// TODO Auto-generated method stub

	}

	public void visit(While n) {
		// TODO Auto-generated method stub

	}

	public void visit(Print n) {
		// TODO Auto-generated method stub

	}

	public void visit(Assign n) {
		// TODO Auto-generated method stub

	}

	public void visit(ArrayAssign n) {
		// TODO Auto-generated method stub

	}

	public void visit(And n) {
		// TODO Auto-generated method stub

	}

	public void visit(LessThan n) {
		// TODO Auto-generated method stub

	}

	public void visit(Plus n) {
		// TODO Auto-generated method stub

	}

	public void visit(Minus n) {
		// TODO Auto-generated method stub

	}

	public void visit(Times n) {
		// TODO Auto-generated method stub

	}

	public void visit(ArrayLookup n) {
		// TODO Auto-generated method stub

	}

	public void visit(ArrayLength n) {
		// TODO Auto-generated method stub

	}

	public void visit(Call n) {
		// TODO Auto-generated method stub

	}

	public void visit(IntegerLiteral n) {
		// TODO Auto-generated method stub

	}

	public void visit(True n) {
		// TODO Auto-generated method stub

	}

	public void visit(False n) {
		// TODO Auto-generated method stub

	}

	public void visit(IdentifierExp n) {
		// TODO Auto-generated method stub

	}

	public void visit(This n) {
		// TODO Auto-generated method stub

	}

	public void visit(NewArray n) {
		// TODO Auto-generated method stub

	}

	public void visit(NewObject n) {
		// TODO Auto-generated method stub

	}

	public void visit(Not n) {
		// TODO Auto-generated method stub

	}

	public void visit(Identifier n) {
		// TODO Auto-generated method stub

	}

}
