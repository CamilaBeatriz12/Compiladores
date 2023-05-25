package gyh;

//import java.io.File;
//import java.io.InputStream;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

//Esta classe tem como fun��o fazer a leitura do arquivo e definir as vari�veis necess�ria para armazenar e ler os tokens.
// Todos os tokens s�o armazenados no buffer, onde cada fun��o usar� ele para identificar (ou n�o) os lexemas

public class GYHLeitor{
	int ponteiro;
	String lexema = "";
	int inicioLexema = 0;
	
	public GYHLeitor() {}
	ArrayList<String> lendoLinhas(String nome){
		ArrayList<String> linhas = new ArrayList<String>(); // Cria��o do uma lista que ir� conter todas as linhas do nosso arquivo
	    
	    try{
	        Scanner scan = new Scanner(new File(nome)); // Ir� abrir o arquivo que foi passado por par�metro 

	        while(scan.hasNextLine()){ // Verifica��o para saber se a continua��o no arquivo
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
    	System.out.print(lexema); // comentar para ficar melhor a sa�da
    	inicioLexema = ponteiro;
    	lexema = "";
    }
	
}