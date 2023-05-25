package gyh;

//Função main - Cria um objeto do tipo GYHLex e uma variável do tipo GYHToken
// O while tem a função de percorrer o arquivo, identifica-los e armazena-los
// na variavel t, assim imprimindo na tela o resultado

public class GYHMain{
	public static void main(String[] args){
		GYHLex lex = new GYHLex(args[0]);

		GYHToken t = null;
        while((t=lex.proximoToken()).nome != GYHTipoToken.Fim){ // chama as funcoes de leitura dos tokens ate o fim do aquivo
            System.out.print(t);
        }
	}
}
 