package gyh;

import java.util.ArrayList;

// Classe principal, chama o GYHLeitor para ler o arquivo e armazena-lo na vari�vel ldat
// O m�todo proximoToken percorre o arquivo at� o final, identificando os tokens.

// A maioria dos m�todos abaixo operam em tr�s diferentes Estados.
// O estado 1 ocorre quando identifica o come�o de um poss�vel token.
// O estado 2 ocorre quando ele chega no final da leitura desse token e o retorna com sucesso.
// O estado 3 ocorre quando ele chega ao final do arquivo ou a outro token sem conseguir identificar,
// retornando assim um nulo, o que resultar� em um erro lexico.

public class GYHLex {
	GYHLeitor ldat; //leitor de arquivos
	ArrayList<String> linhalist;                  // Linhas do Arquivo

    
    public GYHLex(){}
    
    
    public GYHToken proximoToken(){//chama todas as funcoes ate o fim do arquivo para efetuar a leitura dos tokens
        GYHToken proximo = null;
        espacosEComentarios();
        ldat.confirmar();
        proximo = fim();
        
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        
        proximo = palavrasChave();
        
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        
        proximo = variavel();
        
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        
        proximo = numReal();
        
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        
        proximo = numInt();
        
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        
        proximo = operadorAritmetico();
        
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        
        proximo = operadorRelacional();
        
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        
        proximo = delimAtrib();
        
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        
        proximo = parenteses();
        
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        
        proximo = aspas();
        
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        
        proximo = cadeia();
        
        if(proximo == null){
            ldat.zerar();
        }else{
            ldat.confirmar();
            return proximo;
        }
        System.err.println("Erro lexico!");
        System.err.println(ldat.erroLexico());
        return null;
    }
    
    private GYHToken operadorAritmetico(){ //define os operadores aritimeticos
        int caractereLido = ldat.LerProxCaractere();
        char c = (char) caractereLido;
        if(c == '*'){
            return new GYHToken(GYHTipoToken.OpAritMult, ldat.getLex());
        }else if(c == '/'){
            return new GYHToken(GYHTipoToken.OpAritDiv, ldat.getLex());
        }else if(c == '+'){
            return new GYHToken(GYHTipoToken.OpAritSoma, ldat.getLex());
        }else if(c == '-'){
            return new GYHToken(GYHTipoToken.OpAritSub, ldat.getLex());
        }else{
            return null;
        }
    }
    
    private GYHToken aspas(){ // define a utiliza��o das aspas
        int caractereLido = ldat.LerProxCaractere();
        char c = (char) caractereLido;
        if(c == '"'){
        	while(true) {
        		c = (char) ldat.LerProxCaractere();
        		if(c == '"') {
        			return new GYHToken(GYHTipoToken.Cadeia, ldat.getLex());
        		} else if(c == '\n') {
        			System.err.println("Erro lexico!");
        	        System.err.println(ldat.erroLexico());
        	        return null;
        		}
        	}
        }else{
            return null;
        }
    }
   
    
    private GYHToken parenteses(){ // define a utiliza��o dos parenteses
        int caractereLido = ldat.LerProxCaractere();
        char c = (char) caractereLido;
        if(c == '('){
            return new GYHToken(GYHTipoToken.AbrePar, ldat.getLex());
        }else if(c == ')'){
            return new GYHToken(GYHTipoToken.FechaPar, ldat.getLex());
        }else{
            return null;
        }
    }
    
    private GYHToken operadorRelacional(){ //define os aperadores relacionais
		int caractereLido = ldat.LerProxCaractere();
		char c = (char) caractereLido;
		if(c == '<'){
			c =  (char)ldat.LerProxCaractere();
			if(c == '='){
				return new GYHToken(GYHTipoToken.OpRelMenorIgual, "<=");
			}else{
				ldat.retroceder();
				return new GYHToken(GYHTipoToken.OpRelMenor, "<");
			}
		}else if(c == '>'){
			c =  (char) ldat.LerProxCaractere();
			if(c == '='){
				return new GYHToken(GYHTipoToken.OpRelMaiorIgual, ">=");
			}else{
				ldat.retroceder();
				return new GYHToken(GYHTipoToken.OpRelMaior, ">");
			}
		}else if(c == '='){
			return new GYHToken(GYHTipoToken.OpRelIgual, "=");
		}else if(c == '!'){
			c =  (char) ldat.LerProxCaractere();
			if(c == '='){
				return new GYHToken(GYHTipoToken.OpRelDif, "!=");
			}
		}
		return null;
	}
    
    private GYHToken numInt(){ //define os numeros inteiros
		int estado = 1;
		while(true){
			char c = (char) ldat.LerProxCaractere();
			if(estado == 1){
				if(Character.isDigit(c)){
					estado = 2;
				}else{
					return null;
				}
			}else if(!Character.isDigit(c)){
				ldat.retroceder();
				return new GYHToken(GYHTipoToken.NumInt, ldat.getLex());
			}
		}
	}
    
    private GYHToken numReal(){ //define os numeros reais
        int estado = 1;
        while(true){
            char c = (char) ldat.LerProxCaractere();

            if(estado == 1){
                if(Character.isDigit(c) || c == '.'){

                    if(c == '.') {
                        c = (char) ldat.LerProxCaractere();
                        if(Character.isDigit(c)) {
                            estado = 2;
                        }
                    }
                    
                }else{
                    return null;
                }
            }
             else if(!Character.isDigit(c) && c != '.' ){
                ldat.retroceder();
                return new GYHToken(GYHTipoToken.NumReal, ldat.getLex());
            }
        }
    }
    
    private GYHToken variavel() { //define as variaiveis 
        int estado = 1;
        while (true){
            char c = (char) ldat.LerProxCaractere();
            if(estado == 1){
                if(Character.isLetter(c)){
                    estado = 2;
                }else{
                    return null;
                }
            }else if(estado == 2){
                if(!Character.isLetterOrDigit(c)){
                    ldat.retroceder();
                    return new GYHToken(GYHTipoToken.Var, ldat.getLex());
                }
            }
        }
    }
    
    private GYHToken cadeia(){ // define as cadeia de caracteres
        int estado = 1;
        while (true){
            char c = (char) ldat.LerProxCaractere();
            
            if(estado == 1){
            	
                if(c == '\'' || c == '"'){
                    estado = 2;
                }else{
                    return null;
                }
            }else if(estado == 2){
            	
                if(c == '\n'){
                    return null;
                }
                if(c == '\'' || c == '"'){
                    return new GYHToken(GYHTipoToken.Cadeia, ldat.getLex());
                }else if(c == '\\'){
                    estado = 3;
                }
            }else if(estado == 3){
                if(c == '\n'){
                    return null;
                }else{
                    estado = 2;
                }
            }
        }
    }
    
    private void espacosEComentarios() {
        int estado = 1;
        while (true) {
            char c = (char) ldat.LerProxCaractere();
            if (estado == 1) {
                if (Character.isWhitespace(c) || c == ' ') {
                    estado = 2;
                } else if (c == '#') {
                    estado = 3;
                } else {
                    ldat.retroceder();
                    return;
                }
            } else if (estado == 2) {
                if (c == '#') {
                    estado = 3;
                } else if (!(Character.isWhitespace(c) || c == ' ')) {
                    ldat.retroceder();
                    return;
                }
            } else if (estado == 3) {
                if (c == '\n') {
                    return;
                }
            }
        }
    }
    
    private GYHToken delimAtrib(){ // define os delimitadores e tambem as atribuicoes devido a semelhan�a entre as funcoes
		int caractereLido = ldat.LerProxCaractere();
		char c = (char) caractereLido;
		if(c == ':'){
			c = (char) ldat.LerProxCaractere();
			if(c == '='){
				return new GYHToken(GYHTipoToken.Atrib, ":=");
			}else{
				ldat.retroceder();
				return new GYHToken(GYHTipoToken.Delim, ":");
			}
		}else{
			return null;
		}
	}
    
    private GYHToken palavrasChave(){ // define as palavras chaves como tokens 
        while(true){
            char c = (char) ldat.LerProxCaractere();
            if(!Character.isLetter(c)){
                ldat.retroceder();
                String lexema = ldat.getLex();
                if(lexema.equals("DEC")){
                    return new GYHToken(GYHTipoToken.PCDec, lexema);
                    
                }else if(lexema.equals("INT")){
                    return new GYHToken(GYHTipoToken.PCInt, lexema);
                    
                }else if(lexema.equals("ATRIBUIR")){
                    return new GYHToken(GYHTipoToken.Atrib, lexema);
                    
                }else if (lexema.equals("A")) {
                    return new GYHToken(GYHTipoToken.PCA, lexema);
                    
                }else if(lexema.equals("LER")){
                    return new GYHToken(GYHTipoToken.PCLer, lexema);
                    
                }else if(lexema.equals("IMPRIMIR")){
                    return new GYHToken(GYHTipoToken.PCImprimir, lexema);
                    
                }else if(lexema.equals("SE")){
                    return new GYHToken(GYHTipoToken.PCSe, lexema);
                    
                }else if(lexema.equals("SENAO")){
                    return new GYHToken(GYHTipoToken.PCSenao, lexema);
                    
                }else if(lexema.equals("ENTAO")){
                    return new GYHToken(GYHTipoToken.PCEntao, lexema);
                    
                }else if(lexema.equals("ENQTO")){
                    return new GYHToken(GYHTipoToken.PCEnqto, lexema);
                    
                }else if(lexema.equals("PROG")){
					return new GYHToken(GYHTipoToken.PCProg, lexema);
					
				}else if(lexema.equals("INI")){
                    return new GYHToken(GYHTipoToken.PCIni, lexema);
                    
                }else if(lexema.equals("FIM")){
                    return new GYHToken(GYHTipoToken.PCFim, lexema);
                    
                }else if(lexema.equals("E")){
                    return new GYHToken(GYHTipoToken.OpBoolE, lexema);
                    
                }else if(lexema.equals("OU")){
                    return new GYHToken(GYHTipoToken.OpBoolOu, lexema);
                    
                }else{
                    return null;
                }
            }
        }
    }
    
    private GYHToken fim(){ // define o fim do loop
		int caracterelido = ldat.LerProxCaractere();
		if(caracterelido == -1){
			return new GYHToken(GYHTipoToken.Fim, "Fim");
		}else{
			return null;
		}
	}
}
