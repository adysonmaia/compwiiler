package translate;

import syntaxtree.*;
import visitor.Visitor;
import frame.Frame;
import symbol.*;
import tree.*;
import temp.*;
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
        
//        MEM node = new MEM( 
//                new BINOP(BINOP.PLUS,
//                        minfo.thisPtr.exp(new TEMP(frame.FP())),
//                        new BINOP(BINOP.LSHIFT, new CONST(idx), new CONST(2))));
        MEM node = new MEM( 
                new BINOP(BINOP.PLUS,
                        minfo.thisPtr.exp(new TEMP(frame.FP())),
                        new BINOP(BINOP.TIMES, new CONST(idx), new CONST( frame.wordsize() ))));
        
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
		if ( n.sl == null || n.sl.size() == 0) {
            result = new Nx(new EXPSTM(new CONST(0)));
            return;
		}
		
		Stm r = null;
		
		for (int i=0; i < n.sl.size(); ++i ) {
			Stm s = StatementHandler.translate(frame, env, cinfo, minfo, n.sl.elementAt(i) ).unNx();
			if ( r == null )
                r = s;
            else
                r = new SEQ(r, s);
		}
		result = new Nx(r);

	}

	public void visit(If node) {
		Label t = new Label();
        Label f = new Label();
        Label j = new Label();
        
        Exp cond = ExpHandler.translate(frame, env, cinfo, minfo, node.e);
        Exp th = StatementHandler.translate(frame, env, cinfo, minfo, node.s1);
        Exp el = node.s2 == null ? null : 
            StatementHandler.translate(frame, env, cinfo, minfo, node.s2);
        
        if ( el == null )
        {
            Stm r = new SEQ(cond.unCx(t,j),
                    new SEQ(new LABEL(t),
                            new SEQ(th.unNx(),
                                    new LABEL(j))));
            
            result = new Nx(r);
        }
        else
        {
            Stm r = new SEQ(cond.unCx(t,f),
                    new SEQ(new LABEL(t),
                            new SEQ(new SEQ(th.unNx(),new JUMP(j)),
                    new SEQ(new LABEL(f), 
                            new SEQ(el.unNx(),
                    new LABEL(j))))));
            
            result = new Nx(r);
        }

	}

	public void visit(While node) {
		Label test = new Label();
        Label done = new Label();
        Label body = new Label();
        
        Exp cond = ExpHandler.translate(frame, env, cinfo, minfo, node.e );
        Exp b = StatementHandler.translate(frame, env, cinfo, minfo, node.s);
        
        Stm r = new SEQ(new LABEL(test),
                  new SEQ(cond.unCx(body, done),
                          new SEQ(new LABEL(body),
                                  new SEQ(b.unNx(),
                                          new SEQ(new JUMP(test),
                                                  new LABEL(done))))));
        
        result = new Nx(r);

	}

	public void visit(Print node) {
		tree.Exp arg = ExpHandler.translate(frame, env, cinfo, minfo, node.e).unEx();
        
        List<tree.Exp> param = new List<tree.Exp>(arg, null);
        
        tree.Exp call = frame.externalCall("printInt", param);
        
        result = new Nx(new EXPSTM(call));

	}

	public void visit(Assign node) {
		Symbol name = Symbol.symbol(node.i.s);
        
        tree.Exp var = getVariable(name);
        
        Exp e = ExpHandler.translate(frame, env, cinfo, minfo, node.e);
        
        Stm s = new MOVE(var, e.unEx());
        
        result = new Nx(s);

	}

	public void visit(ArrayAssign node) {
        Symbol name = Symbol.symbol(node.i.s);
        
        tree.Exp var = getVariable(name);
        
       
        tree.Exp idx = ExpHandler.translate(frame, env, cinfo, minfo, node.e1).unEx();
        tree.Exp val = ExpHandler.translate(frame, env, cinfo, minfo, node.e2).unEx();
        
        
        tree.Exp arrayIndex = new BINOP(BINOP.TIMES, new BINOP(BINOP.PLUS, idx, new CONST(1)), new CONST( frame.wordsize()));
        
        tree.Exp access = new MEM(new BINOP(BINOP.PLUS, var, arrayIndex));
        
        Stm s =  new MOVE(access, val);
        
        result = new Nx(s);

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
