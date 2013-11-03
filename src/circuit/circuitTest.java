package circuit;

import java.io.*;

public class circuitTest {
	
	private static String data = "";
	private static ObservationData obsData;
	private static int popSize = 5;
	private static Population pop;
	private static GeneticAlgorithm ga;
	private static float pMutate = (float) 0.01;
	private static float pCrossover = (float) 0.9;

	public static void main(String[] args) {	
		
		try{
			
			  FileInputStream fstream = new FileInputStream(System.getProperty("user.dir")+"/src/circuit/cincolinha.txt");
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String input;
			  while ((input = br.readLine()) != null)   {
				  data += input + '\n';
			  }
			  
			  in.close();
		}catch (Exception e){
			  System.err.println("Erro ao abrir ficheiro.");
		}
		
		obsData = new ObservationData(data);
		initPop();
		ga = new GeneticAlgorithm(pop, pCrossover, pMutate);
		
		double startTime = System.currentTimeMillis();
		Object[] output = ga.search();
		
		RoverCircuit best = (RoverCircuit) output[0];
		RoverCircuit worst = (RoverCircuit) output[1];
		
		System.out.println("Crossover: "+best.getCrossoverType());
		System.out.println("Mutation: "+best.getMutationType());
		System.out.println("Best: "+best+"("+best.fitness()+")");
		System.out.println("Worst: "+worst+"("+worst.fitness()+")");
		System.out.println("Execution time: "+(System.currentTimeMillis()-startTime)/1000+"s");
		
		
	}
	
	private static void initPop(){	
		pop = new Population();
		for(int i = 0; i < popSize; i++) {
			pop.addIndividual(new RoverCircuit(obsData));
		}
	}
	
	
}
