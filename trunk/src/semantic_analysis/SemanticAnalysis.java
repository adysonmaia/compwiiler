package semantic_analysis;

import syntaxtree.Program;
import symbol.*;

public class SemanticAnalysis {
	Program p;
	public SemanticAnalysis (Program p){
		this.p=p;
	}
	public boolean start (){
		Table table;
		TableVisitor tv = new TableVisitor(this.p);
		table=tv.start();
		SemanticVisitor sv = new SemanticVisitor(table,this.p);
		sv.start();
		return ErrorMsg.anyErrors;
	}
}
