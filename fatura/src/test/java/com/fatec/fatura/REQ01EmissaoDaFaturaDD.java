package com.fatec.fatura;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.fatec.fatura.model.Fatura;

class REQ01EmissaoDaFaturaDD {
	Fatura fatura = new Fatura();

	public String obtemDataAtual() {
		DateTime data = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/YYYY");
		// DateTime dataVencimento = dataAtual.plusDays(10);
		return data.toString(fmt);
	}

	String dataVenc = obtemDataAtual();

	@ParameterizedTest
	@CsvSource({
			//classes de equivalencia um valido e um invalido por atributo
			"1,71112917000126, 28/09/2023, moveis planejados, 999999999999991500, satisfatorio",
			"2,7111291700012, 28/09/2023, moveis planejados, 1500, CNPJ invalido"
			
			//analise do valor limite (selecione limites que sao relevantes para teste)
			
			//teste funcional sistematico (2 ct por particao, obrigatorio branco, null e zero, limite de strings e numericos
			
			//analise de cobertura - todos os comandos
			
			//analise de cobertura - todas as arestas
				
	})

	// é possivel criar um arquivo csv com a massa de dados no source folder de teste
	// @CsvFileSource(resources = "\\fatura.csv", numLinesToSkip = 1)
	void validaFatura(int numero, String cnpj, String dataVencimento, String desc, String valor, String re) {
		try {
			fatura = new Fatura(numero, cnpj, dataVencimento, desc, valor);
			assertNotNull(fatura);
			String dataDeHoje = obtemDataAtual();
			assertTrue(dataDeHoje.equals(fatura.getDataEmissao()));
			assertEquals ("satisfatorio", re);
		} catch (Exception e) {
			assertEquals(re, e.getMessage());
		}
	}

}
