package br.ufal.ic.ppgi.tf.conector2;

import br.ufal.aracomp.cosmos.emprestimo.spec.dt.UsuarioDT;
import br.ufal.aracomp.cosmos.emprestimo.spec.req.ILimiteReq;
import br.ufal.aracomp.cosmos.limite.spec.dt.ClienteDT;
import br.ufal.aracomp.cosmos.limite2.spec.dt.ClienteDT2;
import br.ufal.aracomp.cosmos.limite.spec.prov.ILimiteOps;
import br.ufal.aracomp.cosmos.limite2.spec.prov.ILimiteOps2;
import java.lang.Math;

public class ConectorEmprestimoLimite2 implements ILimiteReq {

	private ILimiteOps 	intReq1;
	private ILimiteOps2 intReq2;

	public ConectorEmprestimoLimite2(ILimiteOps intReq1,
			ILimiteOps2 intReq2) {
		this.intReq1 = intReq1;
		this.intReq2 = intReq2;
	}

	@Override
	public double estimarLimite(UsuarioDT usuario) {
		double limite1 = 0;
		double limite2 = 0;
		// porcentagem da diferenca
		double diferencaLimites = 0;
		ClienteDT cliente1 = new ClienteDT();
		ClienteDT2 cliente2 = new ClienteDT2();
		cliente1.salario = Double.parseDouble(usuario.rendimentos);
		cliente2.salario = Double.parseDouble(usuario.rendimentos);
		limite1 = intReq1.calcularLimite(cliente1);
		limite2 = intReq2.calcularLimite(cliente2);
		diferencaLimites = Math.abs(limite1 - limite2) / Math.max(limite1,
				limite2);
		if (diferencaLimites > 0.05) {
			System.out.printf("A diferenca e de %f%%\n", diferencaLimites*100);
			throw new RuntimeException();
		}
		return (limite1 + limite2) / 2;
	}

}
