package circuit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe que "implementa" o algoritmo genético
 */
public class GeneticAlgorithm {


	private static int defaultSize;
	
	private Individual elite;
	private Population pop;
	private int popSize;
	private float pcrossover;
	private float pmutate;
	private double time, startTime;
	private boolean elitism;
	private int elitismSize;

	private int currGen;
	Random r;

	/**
	 * Construtor
	 * 
	 * @param pop
	 *            uma população
	 * @param pcrossover
	 *            a probabilidade de crossover
	 * @param pmutate
	 *            a probabilidade de mutação
	 * @param elitismSize 
	 * @param elitism 
	 * @param pMutate 
	 * @param mutateOption 
	 * @param pCrossover 
	 */
	GeneticAlgorithm(Population pop, int popSize, float pcrossover, float pmutate, boolean elitism, int elitismSize, int executionTime) {
		this.pop = pop;
		this.pcrossover = pcrossover;
		this.pmutate = pmutate;
		this.popSize = popSize;
		this.elitism = elitism;
		currGen = 0;
		this.elitismSize = elitismSize;
		elite = pop.getBestIndividual();
		time = executionTime;
		defaultSize = pop.getSize()/2;
		r= new Random();
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
		currGen = 0;
		elite = pop.getBestIndividual();
		this.time = time;
	}

	/**
	 * Método que pesquisa e devolve o melhor indivíduo encontrado
	 * 
	 * @return pop.getBestIndividual(), o melhor indivíduo
	 */
	public Object[] search() {
		
		List<Double> bestFitness = new ArrayList<Double>();
		
		System.out.println("Crossover: "+pcrossover*100+"%");
		System.out.println("Mutation: "+pmutate*100+"%");
		System.out.println("Elitism: "+elitism);
		System.out.println("---------------------------------");
		System.out.println("Initial population");
		System.out.println(pop.toString());
		
		startTime = System.currentTimeMillis();
		do {
			Individual worstTmp = null;
			Population newPop = new Population();
			// se impar individuo ia faltar no for a seguir
			if (pop.getSize() % 2 != 0) {
				worstTmp =  pop.getWorstIndividual();
				pop.removeIndividual(worstTmp);
			}
			for (int i = 0; i < defaultSize; i++) {
				Individual x = pop.selectIndividual();
				pop.removeIndividual(x);
				Individual y = pop.selectIndividual();	
				pop.removeIndividual(y);
				Individual[] children = new Individual[2];
	
				// crossover probability
				if (r.nextFloat() <= pcrossover) {
					//System.out.println("before cross");
					children = x.crossover(y);
					//System.out.println("after cross");
				}
				else {
					children[0] = x;
					if (pop.getSize() % 2 == 0)children[1] = y;
				}

				// mutation probability
				if (r.nextFloat() <= pmutate)
					children[0].mutate();

				if (r.nextFloat() <= pmutate)
					children[1].mutate();

				newPop.addIndividual(children[0]); 												
				newPop.addIndividual(children[1]);
			}	
			
			pop = newPop;
			
			if(worstTmp!=null)
				pop.addIndividual(worstTmp);
			
			// Elitismo
			if (elitism) {

				if (elite.fitness() < pop.getBestIndividual().fitness()) { //se o elite da geraçao anterior continuar a ser melhor que todos
					pop.addIndividual(elite); //adiciona-se o elite da geraçao anterior
					pop.removeIndividual(pop.getWorstIndividual()); //remove-se o pior de geraçao corrente para equilibrar o tamanho da pop
				}
				else
					elite = pop.getBestIndividual(); //o elite deixa de ser o da geração anterior
			}
			
			currGen++;
			bestFitness.add(pop.getBestIndividual().fitness());
		//} while (currGen < 5000);
		} while (!done());
		System.out.println("Final population");
		System.out.println(pop.toString());
		System.out.println("---------------------------------");

		return new Object[] {pop.getBestIndividual(),pop.getWorstIndividual(),bestFitness};
	}
	
	public int getGen() {
		return currGen;
	}
	
	/**
	 * Determina se o algoritmo acaba
	 * @return booleano, acaba ou nao
	 */
	private boolean done() {
		
		if((System.currentTimeMillis()-startTime)/1000 >= time)
			return true;
		
		return false;
	}

}
