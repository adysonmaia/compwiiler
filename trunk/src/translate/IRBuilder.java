package translate;

import syntaxtree.*;
import visitor.Visitor;
import symbol.*;
import frame.*;
import temp.*;
import tree.*;

public class IRBuilder implements Visitor {
	
	private Table env;
    private Frame frame;
    
    private symbol.Class cinfo;
    private symbol.Method minfo;

    private IRBuilder(Table e, Frame f)
    {
        super();
        
        env = e;
        frame = f;
    }
    
    static void build(Table e, Program p, Frame f)
    {
        IRBuilder b = new IRBuilder(e, f);
        
        Label l = new Label("_minijava_main_1");
        b.frame = f.newFrame(l, null);
        
        p.accept(b);
    }
    
	public void visit(Program node) {
		node.m.accept(this);
	    for(int i=0;i<=node.cl.size()-1;i++)
			node.cl.elementAt(i).accept(this);
	}

	public void visit(MainClass node) {
		Stm s = StatementHandler.translate(frame, env, null, null, node.s).unNx();

	}

	public void visit(ClassDeclSimple node) {
		cinfo = env.getClassByKey(node.i.s);
		
		for(int i=0;i<node.ml.size();i++)
			node.ml.elementAt(i).accept(this);

	}

	public void visit(ClassDeclExtends node) {
		cinfo = env.getClassByKey(node.i.s);
		
		for(int i=0;i<node.ml.size();i++)
			node.ml.elementAt(i).accept(this);

	}

	public void visit(VarDecl n) {
		// TODO Auto-generated method stub

	}

	public void visit(MethodDecl node) {
        minfo = cinfo.getMethodByKey( Symbol.symbol(node.i.s ) );
        
        Stm result = null;
        
        if ( node.sl == null || node.sl.size() == 0) {
            result = new EXPSTM(new CONST(0));
		} else {
			for (int i=0; i < node.sl.size(); ++i ) {
				Stm s = StatementHandler.translate(frame, env, cinfo, minfo, node.sl.elementAt(i) ).unNx();
				if ( result == null )
	                result = s;
	            else
	                result = new SEQ(result, s);
			}
		}		
        
        Exp r = new Nx(result);
        Exp v = ExpHandler.translate(frame, env, cinfo, minfo, node.e);
        
        tree.Exp body = new ESEQ(r.unNx(), v.unEx());
        Stm b = minfo.frame.procEntryExit1(body);
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
