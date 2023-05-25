package gyhparse;

import gyh.GYHLex;
import gyh.GYHTipoToken;
import gyh.GYHToken;

import java.util.ArrayList;
import java.util.List;


//LINK DO VIDEO ->>>>> https://drive.google.com/file/d/183-7wPAOag8X73zguWS8iZldmB0wF3bB/view?usp=sharing


//Analisador sintatico preditivo de descendencia recursiva. Foi escolhido por "simples" e facil de codar
// ja que, cada nao-terminal é uma funcao, as quais sao funcoes recursivas. (Cada funcao é um espelho das regras gramaticais de produçao)

public class GyhParse {
	
	private final static int TAMANHO_BUFFER = 10;
    List<GYHToken> bufferTokens;
    GYHLex lex;
    boolean chegouNoFim = false;

    public GyhParse(GYHLex lex) {
        this.lex = lex;
        bufferTokens = new ArrayList<GYHToken>();
        lerToken();
    }

    //Ler Token -> Utiliza o léxico para identificar o token e armazenar no buffer
    private void lerToken() {
        if (bufferTokens.size() > 0) { //Antes de fazer a leitura, garante que esta vazio o buffer
            bufferTokens.remove(0);
        }
        while (bufferTokens.size() < TAMANHO_BUFFER && !chegouNoFim) { //Enche o buffer
            GYHToken proximo = lex.proximoToken(); //Chamada o leitor Lexico
            bufferTokens.add(proximo); //Armazena no buffer
            if (proximo.nome == GYHTipoToken.Fim) { //Se chegou no final, acabou a leitura e o buffer esta preenchido.
                chegouNoFim = true;
            }
        }
        System.out.println("Lido:  " + lookahead(1)); //Avisa que fez a leitura e que o buffer esta preenchido
    }
    
    GYHToken lookahead(int k) {
        if (bufferTokens.isEmpty()) { //Se tiver vazio o buffer, retorna nulo
            return null;
        }
        if (k - 1 >= bufferTokens.size()) { //Se a posicao a ser lida for maior que o tamanho do buffer, pega o ultimo do buffer
            return bufferTokens.get(bufferTokens.size() - 1);
        }
        return bufferTokens.get(k - 1); //Retorna o K-esimo token que esta a frente no buffer
    }

    void match(GYHTipoToken tipo) {  //Compara o tipo token que estamos esperando com o proximo a ser lido, se for igual, show de bola
        if (lookahead(1).nome == tipo) {
            System.out.println("Match: " + lookahead(1));
            lerToken(); //Consome o token que deu match e avança a leitura
        } else {
            erroSintatico(tipo.toString());
        }
    }
  

    void erroSintatico(String... tokensEsperados) { //Recebe como parametro uma lista variavel de token (Um ou mais token)
        String mensagem = "Erro sintatico: Esperado um dos Tokens [";
        for(int i=0; i < tokensEsperados.length; i++) { //Printa os tokens esperado
            mensagem += tokensEsperados[i];
            if(i < tokensEsperados.length - 1)
                mensagem += ",";
        }
        mensagem += "]. Inesperado Token recebido: " + lookahead(1); //Printa o token que ta vindo
        throw new RuntimeException(mensagem);
    }
    
    //A partir desse ponto do codigo, cada regra gramatical do
    //programa virou uma funcao, que traduz a gramatica para o codigo.
    
    
    
    
    //programa : ':' 'DECLARACOES' listaDeclaracoes ':' 'ALGORITMO' listaComandos;
    
    //Metodo que chama todas as outras funcao de forma recursiva, até finalizar a analise
    public void programa() {
        match(GYHTipoToken.Delim);
        match(GYHTipoToken.PCDec);
        listaDeclaracoes();
        match(GYHTipoToken.Delim);
        match(GYHTipoToken.PCProg);
        listaComandos();
        match(GYHTipoToken.Fim);
    }

    //listaDeclaracoes : declaracao listaDeclaracoes | declaracao;
    void listaDeclaracoes() {
    	
        // Como a declaracao é composta por 3 simbolos (VARIAVEL ':' tipoVar;), nos vamos olhar o proximo elemento
    	
        if (lookahead(4).nome == GYHTipoToken.Delim) { //Se o 4º elemento for um delimitador, quer dizer que terminou as declaracoes e o proximo token é o programa
            declaracao();
        } else if (lookahead(4).nome == GYHTipoToken.Var) { //Se o 4º elemento for uma variavel, que dizer que as declaraçoes ainda nao acabaram
            declaracao();
            listaDeclaracoes();
        } else {
            erroSintatico(GYHTipoToken.Delim.toString(), GYHTipoToken.Var.toString());
        }
    }

    //declaracao : VARIAVEL ':' tipoVar;
    void declaracao() {
        match(GYHTipoToken.Var);
        match(GYHTipoToken.Delim);
        tipoVar();
        
    }

    //tipoVar : 'INTEIRO' | 'REAL';
    void tipoVar() {
        if (lookahead(1).nome == GYHTipoToken.PCInt) {
            match(GYHTipoToken.PCInt);
        } else if (lookahead(1).nome == GYHTipoToken.PCReal) {
            match(GYHTipoToken.PCReal);
        } else {
            erroSintatico("INTEIRO","REAL");
        }
    }
    
    //Metodo abaixo necesssita fatorar e remover a recursao a direita, já que mesmo fatorado
    //ja que as duas primeiras condicoes começam com a expressaoAritmetica, causando recursao a direita.
    //A solucao e dividir em tres passo essa funçao. Caso as duas primeiras condiçoes foram uma expressaoAritmetica, ele avança,
    //e ai sim ele ver qual expressao é, resolvendo a recursao a direita (expressaoAritmetica : expressaoAritmetica2)

    //expressaoAritmetica : expressaoAritmetica '+' termoAritmetico | expressaoAritmetica '-' termoAritmetico | termoAritmetico;
    // fatorar à esquerda:
    // expressaoAritmetica : expressaoAritmetica ('+' termoAritmetico | '-' termoAritmetico) | termoAritmetico;
    // fatorar não é suficiente, pois causa loop infinito
    // remover a recursão à esquerda
    // expressaoAritmetica : termoAritmetico expressaoAritmetica2
    // expressaoAritmetica2 : ('+' termoAritmetico | '-' termoAritmetico) expressaoAritmetica2 | <<vazio>>
    
    void expressaoAritmetica() {
        termoAritmetico();
        expressaoAritmetica2();
        
    }

    void expressaoAritmetica2() {
        if (lookahead(1).nome == GYHTipoToken.OpAritSoma || lookahead(1).nome == GYHTipoToken.OpAritSub) {  //Verifica se é uma expressaoAritmetica
            expressaoAritmetica2SubRegra1();
            expressaoAritmetica2();
        } else { // vazio
        }
    }

    void expressaoAritmetica2SubRegra1() { //Identifica qual expressaoAritmetica é
        if (lookahead(1).nome == GYHTipoToken.OpAritSoma) {
            match(GYHTipoToken.OpAritSoma);
            termoAritmetico();
        } else if (lookahead(1).nome == GYHTipoToken.OpAritSub) {
            match(GYHTipoToken.OpAritSub);
            termoAritmetico();
        } else {
            erroSintatico("+","-");
        }
    }
    
    //Exatamente a mesma logica de cima, só que agora é divisao e multiplicacao

    //termoAritmetico : termoAritmetico '*' fatorAritmetico | termoAritmetico '/' fatorAritmetico | fatorAritmetico;
    // também precisa fatorar à esquerda e eliminar recursão à esquerda
    // termoAritmetico : fatorAritmetico termoAritmetico2
    // termoAritmetico2 : ('*' fatorAritmetico | '/' fatorAritmetico) termoAritmetico2 | <<vazio>>
    void termoAritmetico() {
        fatorAritmetico();
        termoAritmetico2();
    }

    void termoAritmetico2() {
        if (lookahead(1).nome == GYHTipoToken.OpAritMult || lookahead(1).nome == GYHTipoToken.OpAritDiv) {
            termoAritmetico2SubRegra1();
            termoAritmetico2();
        } else { // vazio
        }
    }

    void termoAritmetico2SubRegra1() {
        if (lookahead(1).nome == GYHTipoToken.OpAritMult) {
            match(GYHTipoToken.OpAritMult);
            fatorAritmetico();
        } else if (lookahead(1).nome == GYHTipoToken.OpAritDiv) {
            match(GYHTipoToken.OpAritDiv);
            fatorAritmetico();
        } else {
            erroSintatico("*","/");
        }
    }

    //fatorAritmetico : NUMINT | NUMREAL | VARIAVEL | '(' expressaoAritmetica ')'
    void fatorAritmetico() {
        if (lookahead(1).nome == GYHTipoToken.NumInt) {
            match(GYHTipoToken.NumInt);
        } else if (lookahead(1).nome == GYHTipoToken.NumReal) {
            match(GYHTipoToken.NumReal);
        } else if (lookahead(1).nome == GYHTipoToken.Var) {
            match(GYHTipoToken.Var);
        } else if (lookahead(1).nome == GYHTipoToken.AbrePar) {
            match(GYHTipoToken.AbrePar);
            expressaoAritmetica();
            match(GYHTipoToken.FechaPar);
        } else {
            erroSintatico(GYHTipoToken.NumInt.toString(),GYHTipoToken.NumReal.toString(),GYHTipoToken.Var.toString(),"(");
        }
    }

    //expressaoRelacional : expressaoRelacional operadorBooleano termoRelacional | termoRelacional;
    // Precisa eliminar a recursão à esquerda
    // expressaoRelacional : termoRelacional expressaoRelacional2;
    // expressaoRelacional2 : operadorBooleano termoRelacional expressaoRelacional2 | <<vazio>>
    void expressaoRelacional() {
        termoRelacional();
        expressaoRelacional2();
    }

    void expressaoRelacional2() {
        if (lookahead(1).nome == GYHTipoToken.OpBoolE || lookahead(1).nome == GYHTipoToken.OpBoolOu) {
            operadorBooleano();
            termoRelacional();
            expressaoRelacional2();
        } else { // vazio
        }
    }

    //termoRelacional : expressaoAritmetica OP_REL expressaoAritmetica
    //Nao foi resolvido a expressao relacional entre parenteses.
    void termoRelacional() {
        if (
        		lookahead(1).nome == GYHTipoToken.NumInt
	            || lookahead(1).nome == GYHTipoToken.NumReal
	            || lookahead(1).nome == GYHTipoToken.Var
	            || lookahead(1).nome == GYHTipoToken.AbrePar
            ) {
            // Há um não-determinismo aqui.
            // AbrePar pode ocorrer tanto em expressaoAritmetica como em (expressaoRelacional)
            // Tem uma forma de resolver este problema, mas não usaremos aqui
            // Vamos modificar a linguagem, eliminando a possibilidade
            // de agrupar expressões relacionais com parêntesis
            expressaoAritmetica();
            opRel();
            expressaoAritmetica();
        } else {
            erroSintatico(GYHTipoToken.NumInt.toString(),GYHTipoToken.NumReal.toString(),GYHTipoToken.Var.toString(),"(");
        }
    }

    
    //testa se os operadores relaciones sao os suportados pelo lexico
    void opRel() {
        if (lookahead(1).nome == GYHTipoToken.OpRelDif) {
            match(GYHTipoToken.OpRelDif);
        } else if (lookahead(1).nome == GYHTipoToken.OpRelIgual) {
            match(GYHTipoToken.OpRelIgual);
        } else if (lookahead(1).nome == GYHTipoToken.OpRelMaior) {
            match(GYHTipoToken.OpRelMaior);
        } else if (lookahead(1).nome == GYHTipoToken.OpRelMaiorIgual) {
            match(GYHTipoToken.OpRelMaiorIgual);
        } else if (lookahead(1).nome == GYHTipoToken.OpRelMenor) {
            match(GYHTipoToken.OpRelMenor);
        } else if (lookahead(1).nome == GYHTipoToken.OpRelMenorIgual) {
            match(GYHTipoToken.OpRelMenorIgual);
        } else {
            erroSintatico("<>","=",">",">=","<","<=");
        }
    }

    //operadorBooleano : 'E' | 'OU';
    void operadorBooleano() {
        if (lookahead(1).nome == GYHTipoToken.OpBoolE) {
            match(GYHTipoToken.OpBoolE);
        } else if (lookahead(1).nome == GYHTipoToken.OpBoolOu) {
            match(GYHTipoToken.OpBoolOu);
        } else {
            erroSintatico("E","OU");
        }
    }

    //listaComandos : comando listaComandos | comando;
    //fatorar à esquerda
    // listaComandos : comando (listaComandos | <<vazio>>)  Ver se o proxima ainda pertence a lista de comando
    void listaComandos() {
        comando();
        listaComandosSubRegra1();
    }

    void listaComandosSubRegra1() { //Ver se é algum dos comandos
        if (
    		lookahead(1).nome == GYHTipoToken.Atrib ||
	        lookahead(1).nome == GYHTipoToken.PCLer ||
	        lookahead(1).nome == GYHTipoToken.PCImprimir ||
	        lookahead(1).nome == GYHTipoToken.PCSe ||
	        lookahead(1).nome == GYHTipoToken.PCEnqto || 
	        lookahead(1).nome == GYHTipoToken.PCIni
        ) {
            listaComandos(); //Se o prox ainda é um comando, chama a lista comando de novo
        } else {
            // vazio //acabou a lista de comandos
        }
    }
 
    //comando : comandoAtribuicao | comandoEntrada | comandoSaida | comandoCondicao | comandoRepeticao | subAlgoritmo;
    void comando() {
        if (lookahead(1).nome == GYHTipoToken.Atrib) { //Ver qual comando que é
            comandoAtribuicao();
        } else if (lookahead(1).nome == GYHTipoToken.PCLer) {
            comandoEntrada();
        } else if (lookahead(1).nome == GYHTipoToken.PCImprimir) {
            comandoSaida();
        } else if (lookahead(1).nome == GYHTipoToken.PCSe) {
            comandoCondicao();
        } else if (lookahead(1).nome == GYHTipoToken.PCEnqto) {
            comandoRepeticao();
        } else if (lookahead(1).nome == GYHTipoToken.PCIni) {
            subAlgoritmo();
        } else {
            erroSintatico("ATRIBUICAO","LER","IMPRIMIR","SE","ENQUANTO","INICIO");
        }
    }

    //comandoAtribuicao : VARIAVEL ':=' ExpressaoAritmetica;
    void comandoAtribuicao() {
    	match(GYHTipoToken.Var);
        match(GYHTipoToken.Atrib);
        expressaoAritmetica();
    }

    //comandoEntrada : 'LER' VARIAVEL;
    void comandoEntrada() {
        match(GYHTipoToken.PCLer);
        match(GYHTipoToken.Var);
    }

    //comandoSaida : 'IMPRIMIR'  (VARIAVEL | CADEIA);
    void comandoSaida() {
        match(GYHTipoToken.PCImprimir);
        comandoSaidaSubRegra1();
    }

    void comandoSaidaSubRegra1() {
        if (lookahead(1).nome == GYHTipoToken.Var) {
            match(GYHTipoToken.Var);
        } else if (lookahead(1).nome == GYHTipoToken.Cadeia) {
            match(GYHTipoToken.Cadeia);
        } else {
            erroSintatico(GYHTipoToken.Var.toString(),GYHTipoToken.Cadeia.toString());
        }
    }

    //comandoCondicao : 'SE' expressaoRelacional 'ENTAO' comando | 'SE' expressaoRelacional 'ENTAO' comando 'SENAO' comando;
    // fatorar à esquerda
    // comandoCondicao : 'SE' expressaoRelacional 'ENTAO' comando ('SENAO' comando | <<vazio>>)
    void comandoCondicao() {
        match(GYHTipoToken.PCSe);
        expressaoRelacional();
        match(GYHTipoToken.PCEntao);
        comando();
        comandoCondicaoSubRegra1();
    }

    void comandoCondicaoSubRegra1() {
        if (lookahead(1).nome == GYHTipoToken.PCSenao) {
            match(GYHTipoToken.PCSenao);
            comando();
        } else {
            // vazio
        }
    }

    //comandoRepeticao : 'ENQUANTO' expressaoRelacional comando;
    void comandoRepeticao() {
        match(GYHTipoToken.PCEnqto);
        expressaoRelacional();
        comando();
    }

    //subAlgoritmo : 'INICIO' listaComandos 'FIM';
    void subAlgoritmo() {
        match(GYHTipoToken.PCIni);
        listaComandos();
        match(GYHTipoToken.PCFim);
    }

}
