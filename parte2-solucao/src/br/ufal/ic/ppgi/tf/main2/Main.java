package br.ufal.ic.ppgi.tf.main2;

import br.ufal.aracomp.cosmos.emprestimo.spec.dt.UsuarioDT;
import br.ufal.aracomp.cosmos.emprestimo.spec.prov.IEmprestimoOps;
import br.ufal.aracomp.cosmos.limite.spec.prov.ILimiteOps;
import br.ufal.aracomp.cosmos.limite2.spec.prov.ILimiteOps2;
import br.ufal.ic.ppgi.tf.conector2.ConectorEmprestimoLimite2;

public class Main {
	public static void main(String[] args) {
		// Cria componentes Emprestimo, Limite1 e Limite2
		br.ufal.aracomp.cosmos.emprestimo.spec.prov.IManager componenteEmprestimo =
				br.ufal.aracomp.cosmos.emprestimo.impl.ComponentFactory.createInstance();
		br.ufal.aracomp.cosmos.limite.spec.prov.IManager componenteLimite1 = 
				br.ufal.aracomp.cosmos.limite.impl.ComponentFactory.createInstance();
		br.ufal.aracomp.cosmos.limite2.spec.prov.IManager componenteLimite2 = 
				br.ufal.aracomp.cosmos.limite2.impl.ComponentFactory.createInstance();
		
		// Obtem interfaces providas dos componentes anteriores
		ILimiteOps conectorIntReq1 = (ILimiteOps) componenteLimite1.getProvidedInterface("ILimiteOps");
		ILimiteOps2 conectorIntReq2 = (ILimiteOps2) componenteLimite2.getProvidedInterface("ILimiteOps2");
		IEmprestimoOps emprestimoFunc = 
				(IEmprestimoOps) componenteEmprestimo.getProvidedInterface("IEmprestimoOps");
		
		// Cria conector e conecta componentes Emprestimo com Limite1 e Limite2
		componenteEmprestimo.setRequiredInterface("ILimiteReq", 
				new ConectorEmprestimoLimite2(conectorIntReq1, conectorIntReq2));
		
		// Cria usuario para teste
		UsuarioDT usuario = new UsuarioDT();
		usuario.rendimentos = "1001";
		
		// Executa chamada de funcao para emprestimo
		double valorEmprestimo = emprestimoFunc.liberarEmprestimoAutomatico(usuario);
		System.out.printf("O emprestimo liberado é %f", valorEmprestimo);
	}
}
