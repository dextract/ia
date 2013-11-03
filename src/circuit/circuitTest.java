package circuit;

import java.io.*;

public class circuitTest {
	
	private static String data = "";
	private static String file = "";
	private static ObservationData obsData;
	private static int popSize = 5;
	private static Population pop;
	private static GeneticAlgorithm ga;
	private static float pMutate = (float) 0.01;
	private static float pCrossover = (float) 0.9;
	
	private static int crossOption = 0;
	private static int mutateOption = 0;
	private static boolean elitism = false;
	private static int elitismSize = 0;
	private static boolean graph = false;
	private static int executionTime = 0;
	

	public static void main(String[] args) {
		data ="";
		file = args[0];
		popSize = Integer.parseInt(args[1]);
		crossOption = Integer.parseInt(args[2]);
		pCrossover = Float.parseFloat(args[3]);
		mutateOption = Integer.parseInt(args[4]);
		pMutate = Float.parseFloat(args[5]);
		elitism = Boolean.valueOf(args[6]);
		elitismSize = Integer.parseInt(args[7]);
		graph = Boolean.valueOf(args[8]);
		executionTime = Integer.parseInt(args[9]);

		
		try{
			
			  FileInputStream fstream = new FileInputStream(file);
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
		ga = new GeneticAlgorithm(pop, popSize, pCrossover, pMutate, elitism, elitismSize, executionTime);
		double startTime = System.currentTimeMillis();
		Object[] output = ga.search();
		RoverCircuit best = (RoverCircuit) output[0];
		RoverCircuit worst = (RoverCircuit) output[1];
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Best: "+best+"("+best.fitness()+")"+"\n");
		sb.append("Worst: "+worst+"("+worst.fitness()+")"+"\n");
		sb.append("Generations: "+ga.getGen()+"\n");
		sb.append("Execution time: "+(System.currentTimeMillis()-startTime)/1000+"s"+"\n");
		
		GuiCircuit.appendTextOutput(sb.toString());
		
	}
	
	private static void initPop(){	
		pop = new Population();
		for(int i = 0; i < popSize; i++) {
			pop.addIndividual(new RoverCircuit(obsData, crossOption, mutateOption));
		}
	}
	
	
}
