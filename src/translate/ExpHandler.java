package translate;

import syntaxtree.*;
import visitor.Visitor;
import frame.*;
import symbol.*;
import tree.*;
import util.List;
import temp.*;

public class ExpHandler implements Visitor {

	private Exp result;
    
    private Table env;
    private symbol.Class cinfo;
    private symbol.Method minfo;
    private Frame frame;
    
    private tree.Exp getVariable(Symbol name)
    {
    	
    	if ( minfo != null )
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
        }
        
        // se esta aqui, a variavel eh um atributo de classe
        tree.Exp t = minfo.thisPtr.exp(new TEMP(frame.FP()));
        
        int offset = cinfo.getFieldOffset(name);
        
        //tree.Exp addr = new BINOP(BINOP.PLUS, t, new BINOP(BINOP.LSHIFT, new CONST(offset), new CONST(2)));
        tree.Exp addr = new BINOP(BINOP.PLUS, t, new BINOP(BINOP.TIMES, new CONST(offset), new CONST( frame.wordsize() )));
        
        tree.Exp fetch = new MEM(addr);
        
        return fetch;
    }
    
    
    private ExpHandler(Frame f, Table e, symbol.Class c, symbol.Method m)
    {
        super();
        
        frame = f;
        env = e;
        cinfo = c;
        minfo = m;
        
        result = null;
    }
    
    static Exp translate(Frame f, Table e, symbol.Class c, symbol.Method m, syntaxtree.Exp node)
    {
        ExpHandler h = new ExpHandler(f, e, c, m);
        
        node.accept(h);
        
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

	public void visit(And node)
    {
        Temp res = new Temp();
        
        Exp lhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e1);
        Exp rhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e2);
        
        Label f = new Label();        
        Label secondPart = new Label();
        Label join = new Label();
        Label t = new Label();
        
        tree.Exp total = new ESEQ(
                new SEQ(new CJUMP(CJUMP.NE, lhs.unEx(), new CONST(0), secondPart, f),
                        new SEQ(new LABEL(secondPart),
                                new SEQ(rhs.unCx(t, f),
                                        new SEQ(new LABEL(f),
                                                new SEQ(new MOVE(new TEMP(res), new CONST(0)),
                                                        new SEQ(new JUMP(join),
                                                                new SEQ(new LABEL(t),
                                                                        new SEQ(new MOVE(new TEMP(res), new CONST(1)),
                                                                                new LABEL(join))))))))), new TEMP(res));
        
        result = new Ex(total);
    }

	public void visit(LessThan node) {
		Exp lhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e1);
        Exp rhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e2);
        
        int op = CJUMP.LT;
        
        result = new RelCx(op, lhs, rhs); 

	}

	public void visit(Plus node) {
		Exp lhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e1);
        Exp rhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e2);
        
        tree.Exp cmp = new BINOP(BINOP.PLUS, lhs.unEx(), rhs.unEx());
        
        result = new Ex(cmp);

	}

	public void visit(Minus node) {
		Exp lhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e1);
        Exp rhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e2);
        
        tree.Exp cmp = new BINOP(BINOP.MINUS, lhs.unEx(), rhs.unEx());
        
        result = new Ex(cmp);

	}

	public void visit(Times node) {
		Exp lhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e1);
        Exp rhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e2);
        
        tree.Exp cmp = new BINOP(BINOP.TIMES, lhs.unEx(), rhs.unEx());
        
        result = new Ex(cmp);

	}

	public void visit(ArrayLookup n) {
		// TODO Auto-generated method stub

	}

	public void visit(ArrayLength n) {
		// TODO Auto-generated method stub

	}

	public void visit(Call node) {

	}

	public void visit(IntegerLiteral node) {
		result = new Ex(new CONST(node.i));

	}

	public void visit(True n) {
		result = new Ex(new CONST(1));

	}

	public void visit(False n) {
		 result = new Ex(new CONST(0));

	}

	public void visit(IdentifierExp n) {
		// TODO Auto-generated method stub

	}

	public void visit(This n) {
		result = new Ex(minfo.thisPtr.exp(new TEMP(frame.FP())));

	}

	public void visit(NewArray node) {
		tree.Exp size = ExpHandler.translate(frame, env, cinfo, minfo, node.e).unEx();
        
        List<tree.Exp> params = new List<tree.Exp>(size, null);
        
        Temp t = new Temp();
        
        tree.Exp e = new ESEQ(new MOVE(new TEMP(t), frame.externalCall("newArray", params)),
                new TEMP(t));
        
        result = new Ex( e );

	}

	public void visit(NewObject node) {
        symbol.Class c = env.getClassByKey(node.i.s);
        
        // tamanho do objeto: numero de atributos + 1 palavras
        int tamanho = (c.fields.fieldsL.size() + 1) * frame.wordsize();
        
        Label vtableName = new Label("vtable_" + c.name);
        
        List<tree.Exp> params = new List<tree.Exp>(new CONST(tamanho), 
                new List<tree.Exp>(new NAME(vtableName),null));
        
        tree.Exp e = frame.externalCall("newObject", params);
        
        result = new Ex( e );

	}

	public void visit(Not node) {
		Temp r = new Temp();
        Label t = new Label();
        Label f = new Label();
        Label join = new Label();
        
        SEQ s = new SEQ( ExpHandler.translate(frame, env, cinfo, minfo, node.e ).unCx(t,f),
                new SEQ(new LABEL(t),
                        new SEQ(new MOVE(new TEMP(r), new CONST(0)), 
                                new SEQ(new JUMP(join),
                                        new SEQ(new LABEL(f),
                                                new SEQ(new MOVE(new TEMP(r), new CONST(1)),
                                                        new LABEL(join)))))));
        
        ESEQ res = new ESEQ(s, new TEMP(r));
        
        result = new Ex(res);

	}

	public void visit(Identifier node) {
		Symbol name = Symbol.symbol(node.s);
        
        tree.Exp fetch = getVariable(name);
        
        result = new Ex(fetch);

	}

}
