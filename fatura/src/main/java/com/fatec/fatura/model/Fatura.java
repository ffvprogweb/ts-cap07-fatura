package com.fatec.fatura.model;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Fatura {

	int numero;
	String CNPJdaContratada;
	String dataEmissao;
	String dataVencimento;
	String servicoContratado;
	BigDecimal valor;
	Logger logger = LogManager.getLogger(this.getClass());

	public Fatura(int numero, String cnpj, String dataVencimento, String desc, String valor) {
		this.numero = numero;
		this.CNPJdaContratada = setCnpj(cnpj);
		this.dataEmissao = setDataEmissao();
		this.dataVencimento = setDataVencimento(dataVencimento);
		this.servicoContratado = setServicoContratado(desc);
		this.valor = setValorFatura(valor);
	}

	public Fatura() {
	}

	public int getNumero() {
		return numero;
	}

	public String getDataEmissao() {
		return dataEmissao;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	private String setDataEmissao() {
		DateTime dataAtual = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/YYYY");
		logger.info(">>>>>> setDataEmissao para data de hoje => " + dataAtual.toString(fmt));
		return dataAtual.toString(fmt);
	}

	public String setDataVencimento(String data) {
		if ((data != null) && (dataIsValida(data) == true) && (dtVencMaiorDtAtual(getDataEmissao(), data) == true)
				&& (ehDomingo(data)) == false) {
			logger.info(">>>>>> setDataVencimento  => " + data);
			return data;
		} else {
			throw new IllegalArgumentException("Data de vencimento invalida");
		}
	}

	public boolean ehDomingo(String data) {
		if (dataIsValida(data) && data != null) {
			DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
			DateTime umaData = fmt.parseDateTime(data);
			if (umaData.dayOfWeek().getAsText().equals("domingo")) {
				logger.info(">>>>>> ehdomingo => true ");
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean dataIsValida(String data) {
		if (data != null) {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			df.setLenient(false);
			try {
				df.parse(data);
				return true;
			} catch (ParseException ex) {
				logger.info(">>>>>> isValida false=> " + ex.getMessage());
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean dtVencMaiorDtAtual(String dataAtual, String dataVencimento) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
			DateTime dtAtual = formatter.parseDateTime(dataAtual);
			DateTime dtVenc = formatter.parseDateTime(dataVencimento);

			Days d = Days.daysBetween(dtAtual, dtVenc);
			if (d.getDays() >= 0) {
				logger.info(">>>>>> dataVencMaiorDataAtual => true ");
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.info(">>>>>> data de venc maior que data atual erro nao esperado =>" + e.getMessage());
			throw new IllegalArgumentException("Data invalida");
		}
	}

	public BigDecimal setValorFatura(String entrada) {
		try {
			BigDecimal valorTemp = new BigDecimal(entrada);
			BigDecimal zero = BigDecimal.ZERO;
			if (valorTemp.compareTo(zero) > 0) {
				DecimalFormat formato = new DecimalFormat("#,##0.00");
				String valorFormatado = formato.format(valorTemp);
				logger.info(">>>>>> valor formatado  =>" + valorFormatado);
				return valorTemp;
			}else {
				throw new IllegalArgumentException("Valor invalido");
			}
		} catch (NumberFormatException e) {
			logger.info(">>>>>> valor invalido  =>" + e.getMessage());
			throw new IllegalArgumentException("Valor invalido");
		}
	}

	/*
	 * atribui o cnpj vefica se o cnpj é valido
	 */
	public String setCnpj(String cnpj) {
		try {
			if (cnpjIsValido(cnpj)) {
				return cnpj;
			} else {
				throw new IllegalArgumentException("CNPJ invalido");
			}
		} catch (Exception e) {
			StackTraceElement[] stack = e.getStackTrace();
			String exception = "";
			for (StackTraceElement s : stack) {
		        exception = exception + s.toString() + "\n\t\t";
		    }
		    System.out.println(">>>>>>" + exception);
			throw new IllegalArgumentException("CNPJ invalido");
		}

	}

	public String getServicoContratado() {
		return servicoContratado;
	}

	public String setServicoContratado(String servico) {
		if ((servico == null) || (servico.isBlank())) {
			logger.info(">>>>>> setServicoContratado invalido ");
			throw new IllegalArgumentException("Descricao do servico invalido");
		} else {
			logger.info(">>>>>> setServicoContratado valido ");
			return servico;
		}
	}

	public boolean cnpjIsValido(String cnpj) {
		char dig13, dig14;
		int sm, i, r, num, peso;
		if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") || cnpj.equals("22222222222222")
				|| cnpj.equals("33333333333333") || cnpj.equals("44444444444444") || cnpj.equals("55555555555555")
				|| cnpj.equals("66666666666666") || cnpj.equals("77777777777777") || cnpj.equals("88888888888888")
				|| cnpj.equals("99999999999999") || (cnpj.length() != 14)) {
			return (false);
		}
		// "try" - protege o código para eventuais erros de conversao de tipo (int)
		try { // Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 11; i >= 0; i--) {
				// converte o i-ésimo caractere do CNPJ em um número:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posição de '0' na tabela ASCII)
				num = (int) (cnpj.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}
			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else
				dig13 = (char) ((11 - r) + 48);

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = (int) (cnpj.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}
			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else
				dig14 = (char) ((11 - r) + 48);
			// Verifica se os dígitos calculados conferem com os dígitos informados.
			if ((dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13)))
				return (true);
			else
				return (false);
		}

		catch (InputMismatchException erro) {
			erro.printStackTrace();
			return (false);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataEmissao, dataVencimento, numero, valor);
	}

}
