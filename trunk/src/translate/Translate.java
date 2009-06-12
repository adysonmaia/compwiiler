package translate;
import frame.Frame;
import syntaxtree.Program;
import symbol.Table;

public class Translate {
	private Translate()
    {
        super();       
    }
	
	public static void translate(Frame parentFrame, Table t, Program p)
    {
		// Criando os frames
        FrameBuilder.translate(parentFrame, t, p);
    }
}
