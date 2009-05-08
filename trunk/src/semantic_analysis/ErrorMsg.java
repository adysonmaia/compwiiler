package semantic_analysis;

public class ErrorMsg {
	static boolean anyErrors=false;
		
	static void complain(String msg){
		anyErrors=true;
		System.out.println(msg);
	}
	
}
