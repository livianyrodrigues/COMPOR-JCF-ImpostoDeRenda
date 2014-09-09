package br.ufcg.ppgcc.compor.ir.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ufcg.ppgcc.compor.ir.impl.excecaoCriarTitular;
import br.ufcg.ppgcc.compor.ir.FachadaExperimento;
import br.ufcg.ppgcc.compor.ir.FontePagadora;
import br.ufcg.ppgcc.compor.ir.Titular;

public class ImpostoDeRenda implements FachadaExperimento{

	private Map<Titular, List<FontePagadora>> titulares = new LinkedHashMap<Titular, List<FontePagadora>>();

		
	public void criarNovoTitular(Titular titular){
		
			
		if(titular.getNome() == null ){
			throw new excecaoCriarTitular("O campo nome é obrigatório");
		}else if(titular.getCpf() == null){
			throw new excecaoCriarTitular("O campo CPF é obrigatório");
		
		}else if(titular.getCpf().length() != 14 ){
			throw new excecaoCriarTitular("O campo CPF está inválido");
		}
		titulares.put(titular, new ArrayList<FontePagadora>());
	}

	public List<Titular> listarTitulares() {
		return new ArrayList<Titular>(titulares.keySet());
	}

	public void criarFontePagadora(Titular titular, FontePagadora fonte) {
		ArrayList<FontePagadora> fontesDoTitular = (ArrayList<FontePagadora>) titulares.get(titular);
			fontesDoTitular.add(fonte);
		}


	public List<FontePagadora> listarFontes(Titular titular) {
		return new ArrayList<FontePagadora>(titulares.get(titular));
	}

	
	
}

