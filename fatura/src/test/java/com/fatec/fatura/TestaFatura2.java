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

class TestaFatura2 {
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

			"1,71112917000126, 28/09/2023, moveis planejados, 1500, satisfatorio",
			"1,7111291700012, 28/09/2023, moveis planejados, 1500, CNPJ invalido",
			"1,71112917000126, 02/10/2022, moveis planejados, 1500, Data de vencimento invalida", // dt venc domingo
			"1,71112917000126, 31/02/2022, moveis planejados, 1500, Data de vencimento invalida", // dt venc invalida
			"1,71112917000126, , moveis planejados, 1500, Data de vencimento invalida", // dt venc invalida
			"1,71112917000126, %, moveis planejados, 1500, Data de vencimento invalida", // dt venc caracter especial
			"1,71112917000126,, moveis planejados, 1500, Data de vencimento invalida", // dt venc null
			"1,71112917000126, ' ', moveis planejados, 1500, Data de vencimento invalida", // dt venc branco
			"1,71112917000123, 12/09/2023, moveis planejados, 1500, CNPJ invalido", // cnpj invalido
			"1,71112917000126, 12/08/2022, moveis planejados, 1500, Data de vencimento invalida", // cnpj invalido
			"1,71112917000126, 28/09/2023,'', 1500, Descriminacao do servico invalido", // servico invalido
			"1,71112917000126, 28/09/2023,, 1500, Descriminacao do servico invalido", // servico invalido
			"1,71112917000126, 28/09/2023,moveis planejados, , Valor invalido", // valor invalido
			"1,71112917000126, 28/09/2023,moveis planejados,'' , Valor invalido", // valor invalido
			"1,71112917000126, 28/09/2023,moveis planejados,0 , Valor invalido" // valor invalido
	})

	// é possivel criar um arquivo csv com a massa de dados no source folder de teste
	// @CsvFileSource(resources = "\\fatura.csv", numLinesToSkip = 1)
	void validaFatura(int numero, String cnpj, String dataVencimento, String desc, String valor, String re) {
		try {
			fatura = new Fatura(numero, cnpj, dataVencimento, desc, valor);
			assertNotNull(fatura);
			String dataDeHoje = obtemDataAtual();
			assertTrue(dataDeHoje.equals(fatura.getDataEmissao()));

		} catch (Exception e) {
			assertEquals(re, e.getMessage());
		}
	}

}
