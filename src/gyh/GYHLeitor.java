package gyh;

//import java.io.File;
//import java.io.InputStream;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

//Esta classe tem como funï¿½ï¿½o fazer a leitura do arquivo e definir as variï¿½veis necessï¿½ria para armazenar e ler os tokens.
// Todos os tokens sï¿½o armazenados no buffer, onde cada funï¿½ï¿½o usarï¿½ ele para identificar (ou nï¿½o) os lexemas

public class GYHLeitor{
	int ponteiro;
	String lexema = "";
	int inicioLexema = 0;
	
	public GYHLeitor() {}
	ArrayList<String> lendoLinhas(String nome){
		ArrayList<String> linhas = new ArrayList<String>(); // Criação do uma lista que irá conter todas as linhas do nosso arquivo
	    
	    try{
	        Scanner scan = new Scanner(new File(nome)); // Irá abrir o arquivo que foi passado por parâmetro 

	        while(scan.hasNextLine()){ // Verificação para saber se a continuação no arquivo
	             linhas.add(scan.nextLine()); // Le a linha inteira como string e adiciona esta na lista
	        }
	      }catch(Exception e){
	        System.out.println(e);
	      }
	        return linhas; // Retorna a lista de linhas
	}
	
	public void zerar() {
    	ponteiro = inicioLexema;
    	lexema = "";
    }

    public void confirmar() {
    	System.out.print(lexema); // comentar para ficar melhor a saída
    	inicioLexema = ponteiro;
    	lexema = "";
    }
	
}