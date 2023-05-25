package gyh;

//Classe responsável por retorna uma string com o token formatado.

public class GYHToken{
	public GYHTipoToken nome;
	public String lexema;
	
	public GYHToken(GYHTipoToken nome, String lexema){
		this.nome = nome;
		this.lexema = lexema;
	}
	
	
	@Override
	public String toString(){
		return "<"+lexema+","+nome+">";
	}
}
