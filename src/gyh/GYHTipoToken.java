package gyh;

// Enum com todos os tipos de tokens suportado pela linguagem.

public enum GYHTipoToken{
	PCDec, PCProg, PCA, PCInt, PCReal, PCLer, 
	PCImprimir, PCSe, PCSenao, PCEntao, PCEnqto, PCIni, PCFim,
	OpAritMult, OpAritDiv, OpAritSoma, OpAritSub, OpRelMenor, OpRelMenorIgual, 
	OpRelMaior, OpRelMaiorIgual, OpRelIgual, OpRelDif,
	OpBoolE, OpBoolOu, Delim, Atrib, AbrePar, FechaPar,
	Var, NumInt, NumReal, Cadeia, Fim
}
