package br.ufal.ppgi.tf.conector3;

import br.ufal.aracomp.cosmos.emprestimo.spec.dt.UsuarioDT;
import br.ufal.aracomp.cosmos.emprestimo.spec.req.ILimiteReq;
import br.ufal.aracomp.cosmos.limite.spec.dt.ClienteDT;
import br.ufal.aracomp.cosmos.limite.spec.prov.ILimiteOps;
import br.ufal.aracomp.cosmos.limite2.spec.dt.ClienteDT2;
import br.ufal.aracomp.cosmos.limite2.spec.prov.ILimiteOps2;
import br.ufal.aracomp.cosmos.limite3.spec.dt.ClienteDT3;
import br.ufal.aracomp.cosmos.limite3.spec.prov.ILimiteOps3;

public class ConectorEmprestimoLimite3 implements ILimiteReq {
	private ILimiteOps 	intReq1;
	private ILimiteOps2 intReq2;
	private ILimiteOps3 intReq3;

	public ConectorEmprestimoLimite3(ILimiteOps intReq1,
			ILimiteOps2 intReq2, ILimiteOps3 intReq3) {
		this.intReq1 = intReq1;
		this.intReq2 = intReq2;
		this.intReq3 = intReq3;
	}

	private double calcularDiferenca(double limiteMin, double limiteMax) {
		return (limiteMax - limiteMin)/limiteMax;
	}

	@Override
	public double estimarLimite(UsuarioDT usuario) {
		double limiteMaximo = 0;
		double limiteIntermediario = 0;
		double limiteMinimo = 0;
		double limite = 0;
		ClienteDT cliente1 = new ClienteDT();
		ClienteDT2 cliente2 = new ClienteDT2();
		ClienteDT3 cliente3 = new ClienteDT3();
		cliente1.salario = Double.parseDouble(usuario.rendimentos);
		cliente2.salario = Double.parseDouble(usuario.rendimentos);
		cliente3.salario = Double.parseDouble(usuario.rendimentos);
		// considera este limite como o maximo
		limiteMaximo = intReq1.calcularLimite(cliente1);
		// considera este limite como minimo
		limiteMinimo = intReq2.calcularLimite(cliente2);
		if (limiteMinimo > limiteMaximo) {
			limite = limiteMaximo;
			limiteMaximo = limiteMinimo;
			limiteMinimo = limite;
		}
		// considera como limite intermediario
		limiteIntermediario = intReq3.calcularLimite(cliente3);
		// atualiza maximo
		if (limiteIntermediario > limiteMaximo) {
			limite = limiteMaximo;
			limiteMaximo = limiteIntermediario;
			limiteIntermediario = limite;
		}
		// atualiza minimo
		if (limiteIntermediario < limiteMinimo) {
			limite = limiteMinimo;
			limiteMinimo = limiteIntermediario;
			limiteIntermediario = limite;
		}
		System.out.printf("min: %f, inter: %f, max: %f\n",
				limiteMinimo, limiteIntermediario, limiteMaximo);
		if (calcularDiferenca(limiteIntermediario, limiteMaximo) > 0.05) {
			if (calcularDiferenca(limiteMinimo, limiteIntermediario) > 0.05) {
				throw new RuntimeException();
			} else {
				return (limiteMinimo + limiteIntermediario) / 2;
			}
		} else if (calcularDiferenca(limiteMinimo, limiteIntermediario) > 0.05) {
			return (limiteIntermediario + limiteMaximo) / 2;
		}
		return (limiteMinimo + limiteIntermediario + limiteMaximo) / 3;
	}
}
