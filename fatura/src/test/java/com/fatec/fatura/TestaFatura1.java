package com.fatec.fatura;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

import com.fatec.fatura.model.Fatura;



class TestaFatura1 {
	Fatura fatura = new Fatura();
	Logger logger = LogManager.getLogger(this.getClass());
	
	public String obtemDataAtual() {
		DateTime data = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/YYYY");
		//DateTime dataVencimento = dataAtual.plusDays(10);
		return data.toString(fmt);
	}
	/**
	 * verificar o comportamento do sistema ao instanciar a fatura com dados validos.
	 */
	@Test
	void ct01_quando_dados_validos_fatura_nao_eh_nulo() {
		try {

			// dado que fatura foi emitida com dados validos
			// quando instancio o objeto
		
			fatura = new Fatura(1, "71112917000126", "02/10/2023", "moveis planejados", "1500");
			// entao fatura nao e nula e a data de emisssao é igual a data de hoje
			assertNotNull(fatura);
			String dataDeHoje = obtemDataAtual();
			assertTrue(dataDeHoje.equals(fatura.getDataEmissao()));
		} catch (Exception e) {
			logger.info(">>>>>> nao deveria falhar => " + e.getMessage());
			fail("nao deveria falhar fatura valida");

		}
		
	}
	/**
	 * verificar o comportamento do sistema na emissao da fatura com cnpj invalido
	 */
	
	@Test
	void ct02_quando_cnpj_invalido_entao_retorna_msg_cnpj_invalido() {
		try {
			// dado que que o cnpj eh invalido
			// quando instancio o objeto
			fatura = new Fatura(1, "7111291700012", "02/10/2023", "moveis planejados", "1500");
			fail("deveria falhar emissao da fatura com cnpj invalido");
		} catch (Exception e) {
			//entao retorna mensagem de cnpj invalido
			assertEquals ("CNPJ invalido", e.getMessage());
		}
		
	}
	
	@Test
	void ct04_quando_data_valida_retorna_true() {
		assertTrue(fatura.isValida("30/09/2022"));
	}
	@Test
	void ct05_quando_data_invalida_retorna_false() {
		assertFalse(fatura.isValida("32/09/2022"));
	}

	@Test
	void ct06_dado_que_a_data_eh_segunda_quando_consulta_eh_domingo_retorna_false() {
		assertFalse(fatura.ehDomingo("12/09/2022"));
	}

	@Test
	void ct07_dado_que_a_data_eh_domingo_quando_consulta_eh_domingo_retorna_true() {
		assertTrue(fatura.ehDomingo("11/09/2022"));
	}

	@Test
	void ct08_dado_que_a_data_eh_valida_quando_consulta_retorna_true() {
		assertTrue(fatura.isValida("11/09/2022"));
	}
	@Test
	void ct08_dado_que_a_data_eh_null_quando_consulta_retorna_true() {
		assertFalse(fatura.isValida(null));
	}

	@Test
	void ct09_dado_que_a_dataVencimento_eh_domingo_retorna_dataVencimento_invalida() {
		try {
			// dado que 11/09/2022 é domino
			// quando confirma o cadastro
			fatura = new Fatura(1, "71112917000126", "11/09/2022", "moveis planejados", "1500");
			fail("deveria disparar a exception para data invalida domingo");
		} catch (Exception e) {
			// então retorna data invalida
			assertEquals("Data de vencimento invalida", e.getMessage());
		}
	}

	@Test
	void ct10_dado_que_a_dataVencimento_invalida_retorna_mensagem_erro() {
		// dado que a data de vencimento eh 31/02/2022
		Fatura fatura = new Fatura();
		// quando - confirma o cadastro
		boolean ehvalido = fatura.isValida("31/02/2022");
		// entao - retorna 
		assertFalse(ehvalido);

	}

}
