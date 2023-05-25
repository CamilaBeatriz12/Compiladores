package gyhparse;
import gyh.GYHLex;
import gyhparse.GyhParse;

public class MainParse {

	public static void main(String args[]) {
		GYHLex lex = new GYHLex(args[0]);
		GyhParse parser = new GyhParse(lex);
        parser.programa(); //Top - Down
    }
}
	
 