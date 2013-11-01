package circuit;

import java.util.Random;

/**
 * Classe que "implementa" o algoritmo genético
 */
public class GeneticAlgorithm {

	private static int DEFAULT_TIME = 5; //segundos
	private static boolean elitism = true;

	private Individual elite;
	private Population pop;
	private float pcrossover;
	private float pmutate, time, startTime;
	Random rand;

	/**
	 * Construtor
	 * 
	 * @param pop
	 *            uma população
	 * @param pcrossover
	 *            a probabilidade de crossover
	 * @param pmutate
	 *            a probabilidade de mutação
	 */
	GeneticAlgorithm(Population pop, float pcrossover, float pmutate) {
		this.pop = pop;
		this.pcrossover = pcrossover;
		this.pmutate = pmutate;
		elite = pop.getBestIndividual();
		time = DEFAULT_TIME;
	}

	/**
	 * Construtor 2
	 * 
	 * @param pop
	 *            uma população
	 * @param pcrossover
	 *            a probabilidade de crossover
	 * @param pmutate
	 *            a probabilidade de mutação
	 * @param tempo
	 *            limite
	 */
	GeneticAlgorithm(Population pop, float pcrossover, float pmutate, int time) {
		this.pop = pop;
		this.pcrossover = pcrossover;
		this.pmutate = pmutate;
		elite = pop.getBestIndividual();
		this.time = time;
	}

	/**
	 * Método que pesquisa e devolve o melhor indivíduo encontrado
	 * 
	 * @return pop.getBestIndividual(), o melhor indivíduo
	 */
	public Individual search() {

		startTime = System.currentTimeMillis();
		
		do {
			Population newPop = new Population();

			// se impar individuo ia faltar no for a seguir
			if (pop.getSize() % 2 != 0)
				newPop.addIndividual(pop.selectIndividual());

			for (int i = 0; i < pop.getSize() / 2; i++) {

				Individual x = pop.selectIndividual();
				Individual y = pop.selectIndividual();
				Individual[] children = new Individual[2];

				// crossover probability
				if (rand.nextFloat() <= pcrossover) {
					children = x.crossover(y);
				}

				else {
					children[0] = x;
					children[1] = y;
				}

				// mutation probability
				if (rand.nextFloat() <= pmutate)
					children[0].mutate();

				if (rand.nextFloat() <= pmutate)
					children[1].mutate();

				newPop.addIndividual(children[0]); 												
				newPop.addIndividual(children[1]);
			}

			// Elitismo
			if (elitism) {

				if (elite.fitness() > newPop.getBestIndividual().fitness()) {
					// REMOVER PIOR E METER ELITE, PORQUE ASSIM VAI ADICIONANDO 1 A CADA GERACAO
					newPop.addIndividual(elite);
				}

				else
					elite = newPop.getBestIndividual();
			}
			
			pop = newPop;
	
		} while (!done());

		return pop.getBestIndividual();
	}
	
	/**
	 * Determina se o algoritmo acaba
	 * @return booleano, acaba ou nao
	 */
	private boolean done() {
		
		if(System.currentTimeMillis() - startTime > time * 1000)
			return true;
		
		return false;
	}

}
