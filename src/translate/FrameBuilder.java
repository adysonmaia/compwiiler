package translate;

import java.util.ArrayList;

import visitor.Visitor;
import frame.*;
import syntaxtree.*;
import symbol.*;
import util.List;
import temp.Label;

public class FrameBuilder implements Visitor {
	private Table env;
	private Frame parent;
	private symbol.Class cinfo;
    private symbol.Method minfo;
	
	private FrameBuilder(Frame p, Table t)
    {
		super();
		env = t;
		parent = p;
    }
	
	static void translate(Frame p, Table t, Program pp)
    {
        FrameBuilder b = new FrameBuilder(p, t);
        
        pp.accept(b);
    }
	
	public void visit(Program node)
    {
	    for ( int i = 0; i < node.cl.size(); i++ ) {
	    	ClassDecl cd = node.cl.elementAt(i);
	    	if (cd instanceof ClassDeclSimple) {
	    		ClassDeclSimple cds = (ClassDeclSimple)cd;
				cinfo = env.getClassByKey( cds.i.s );
	    	} else if ( cd instanceof ClassDeclExtends  ) {
	    		ClassDeclExtends cde = (ClassDeclExtends)cd;
				cinfo = env.getClassByKey( cde.i.s );
	    	}
	    	node.cl.elementAt(i).accept(this);
	    }
    }
	
	// Identifier i1,i2;
	// Statement s;
	public void visit(MainClass node) {
		node.s.accept(this);
	}
	
	public void visit(ClassDeclSimple node)
    {       
		for ( int i = 0; i < node.ml.size(); i++ ) {
	        node.ml.elementAt(i).accept(this);
	    }
    }
	
	public void visit(ClassDeclExtends node)
    {
        
		for ( int i = 0; i < node.ml.size(); i++ ) {
	        node.ml.elementAt(i).accept(this);
	    }
    }
	
	public void visit(MethodDecl node)
    {
        List<Boolean> head = null, tail = null, nn;
        
        for ( int i = 0; i < node.fl.size(); i++ ) {
	        nn = new List<Boolean>(false, null);
            
            if ( head == null )
                head = tail = nn;
            else
                tail = tail.tail = nn;
	    }
        
        // colocando o parametro 'this'
        head = new List<Boolean>(false, head);
        minfo = cinfo.getMethodByKey( Symbol.symbol(node.i.s ) );
        
        Label methodName = new Label( "" + cinfo.name + "$" + minfo.name );
        
        Frame methodFrame = parent.newFrame(methodName, head);
                
        
        // facilitando a vida de muitas partes do compilador
        minfo.frame = methodFrame;
        minfo.thisPtr = methodFrame.formals.head;
        
        List<Access> f = methodFrame.formals.tail;
        
        minfo.params.accessL = new ArrayList <Access> ( minfo.params.paramsL.size() );
        minfo.locals.accessL = new ArrayList <Access> ( minfo.locals.localsL.size() );
        
        for (int i = 0; i < node.fl.size(); i++) {
			f = f.tail;
        	Formal aux = node.fl.elementAt(i);
        	int index = minfo.params.index( Symbol.symbol( aux.i.s ) );
        	if ( index >= 0) {
        		minfo.params.accessL.add(index, f.head); 
        	}
		}
        
        // criando espaco para as variaveis locais
        for (int i = 0; i < node.vl.size(); i++) {
			node.vl.elementAt(i).accept(this);
		}
    }
	
	public void visit(VarDecl node)
    {
        int index = minfo.locals.index( Symbol.symbol( node.i.s ));
        if (index >= 0) {
        	minfo.locals.accessL.add(index, minfo.frame.allocLocal(false));
        }
    }
	
//	 Type t;
	// Identifier i;
	public void visit(Formal n) {
		
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
