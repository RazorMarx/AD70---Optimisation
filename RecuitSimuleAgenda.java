import java.util.ArrayList;

public class RecuitSimuleAgenda {

	//Matrice graphe
	static int[][] matrice = new int[][]{
	   //A B C D E F G H I J K
		{0,1,1,0,1,0,1,1,0,1,0},	//A
		{1,0,1,1,0,0,1,1,1,0,1},	//B
		{1,1,0,0,1,1,0,1,0,0,0},	//C
		{0,1,0,0,0,1,0,1,0,1,0},	//D
		{1,0,1,0,0,0,1,0,1,0,1},	//E
		{0,0,1,1,0,0,1,0,0,1,0},	//F
		{1,1,0,0,1,1,0,1,1,0,0},	//G
		{1,1,1,1,0,0,1,0,1,0,1},	//H
		{0,1,0,0,1,0,1,1,0,1,0},	//I
		{1,0,0,1,0,1,0,0,1,0,0},	//J
		{0,1,0,0,1,0,0,1,0,0,0}		//K
	};
	
	//Constraints Couple of predecessor
	static int[][] predecessorConstraint = new int[][]{
		{4 ,9},	
		{3,10},
		{5,10}
	};
	
	//Coloration initial
	static int[] colorInitial = new int[]{
	  //A B C D E F G H I J K
		0,1,2,3,0,1,2,3,0,1,2
	};
	
	static int numberOfColor = 4;
	static int maxNumberOfVerticePerColor = 3;
	
	//Some const for playing :)
	static int weightColorationConflict = 1;
	static int weightPredescessorConflict = 5;
	static int weightMaxVerticePerColorConflict = 5;

	//Const for Temperature
	static double temperature = 30;
	static double coolingGraduatation = 0.95; //[0.9 - 0.99]
	
	//Number of step used by algorihtm
	static int step = 0;
	static int stepBeforeCooling = 100;
	
	public static void main(String[] args) {
		/** Algo recuit simule**/
		
		int[] coloration = colorInitial;
		int scoreCurrent = countScoreOfColoration(coloration);
		int bufferStepForCooling = 0;
		int maxStep = 1000000;
		
		while(scoreCurrent > 0 && step < maxStep){
			//Pick One algo for neighbour search
			int[] colorationNext = rand2Change(coloration);
			//int[] colorationNext = rand3Change(coloration);
			//int[] colorationNext = alg2OptChange(coloration);
			//int[] colorationNext = alg3OptChange(coloration);
			
			
			int scoreNext = countScoreOfColoration(colorationNext);
			
			if(bufferStepForCooling > stepBeforeCooling)
				temperature *= coolingGraduatation;
			
			if(shouldAccept( temperature, (double)(scoreNext - scoreCurrent)) ){
				scoreCurrent = scoreNext;
				coloration = colorationNext;
				bufferStepForCooling = 0;
			}
			bufferStepForCooling++;
			step++;
		}
		
		printAgenda(coloration);
	}
	
	/**
	 * 
	 * @param temperature
	 * @param deltaE difference between newScore - score
	 * @return
	 */
	public static boolean shouldAccept(double temperature, double deltaE) {
		return (deltaE > 0.0) || (Math.random() <= probabilityOfAcceptance(temperature, deltaE));
	}
	
	/**
	 * Recuit acceptance
	 * @param temperature
	 * @param deltaE
	 * @return
	 */
	public static double probabilityOfAcceptance(double temperature, double deltaE) {
		System.out.println(Math.exp(deltaE / temperature));
		return Math.exp(deltaE / temperature);
	}
	
	/**
	 * Randomize two color
	 * @param color
	 * @return
	 */
	public static int[] rand2Change(int[] color){
		int[] colorBuffer = color;
		int id1 = (int)((Math.random() * colorBuffer.length));
		int id2 = (int)((Math.random() * colorBuffer.length));
		
		while(id1 == id2){
			id2 = (int)((Math.random() * colorBuffer.length));
		}
		
		colorBuffer[id1] = (int)((Math.random() * numberOfColor));
		colorBuffer[id2] = (int)((Math.random() * numberOfColor));
		
		return colorBuffer;
	}
	
	/**
	 * Randomize three color
	 * @param color
	 * @return
	 */
	public static int[] rand3Change(int[] color){
		int[] colorBuffer = color;
		int id1 = (int)((Math.random() * colorBuffer.length));
		int id2 = (int)((Math.random() * colorBuffer.length));
		int id3 = (int)((Math.random() * colorBuffer.length));
		while(id1 != id2 && id2 != id3){
			id2 = (int)((Math.random() * colorBuffer.length));
		}
		while(id3 != id2 && id1 != id3){
			id3 = (int)((Math.random() * colorBuffer.length));
		}
		
		colorBuffer[id1] = (int)((Math.random() * numberOfColor));
		colorBuffer[id2] = (int)((Math.random() * numberOfColor));
		colorBuffer[id3] = (int)((Math.random() * numberOfColor));
		
		return color;
	}
	
	/**
	 * Switch two color
	 * @param color
	 * @return
	 */
	public static int[] alg2OptChange(int[] color){
		int[] colorBuffer = color;
		int id1 = (int)((Math.random() * colorBuffer.length));
		int id2 = (int)((Math.random() * colorBuffer.length));
		
		while(id1 == id2){
			id2 = (int)((Math.random() * colorBuffer.length));
		}
		
		int buffer = colorBuffer[id1];
		colorBuffer[id1] = colorBuffer[id2];
		colorBuffer[id2] = buffer;
		
		return colorBuffer;
	}
	
	
	/**
	 * Switch three color
	 * @param color
	 * @return
	 */
	public static int[] alg3OptChange(int[] color){
		int[] colorBuffer = color;
		int id1 = (int)((Math.random() * colorBuffer.length));
		int id2 = (int)((Math.random() * colorBuffer.length));
		int id3 = (int)((Math.random() * colorBuffer.length));
		while(id1 != id2 && id2 != id3){
			id2 = (int)((Math.random() * colorBuffer.length));
		}
		while(id3 != id2 && id1 != id3){
			id3 = (int)((Math.random() * colorBuffer.length));
		}
		
		int buffer = colorBuffer[id1];
		colorBuffer[id1] = colorBuffer[id2];
		colorBuffer[id2] = colorBuffer[id3];
		colorBuffer[id3] = buffer;
		
		return color;
	}
	
	/**
	 * @return the total number of conflict on the current coloration 
	 */
	public static int countScoreOfColoration(int[] color){
		int conflict = 0;
		//Coloration Conflict
		for(int i=0; i<matrice.length;i++)
			conflict += weightColorationConflict * countConflictOnAVertice(i, color);
		
		//Predecessor Constraint
		for(int[] couple : predecessorConstraint)
			conflict += weightPredescessorConflict * additionalConstraintOnPredecessor(couple[0], couple[1], color);
		
		conflict += weightMaxVerticePerColorConflict * additionalConstraintOnMaxVerticePerColor(3, color);
		return conflict;
	}
	
	/**
	 * @return the  number of conflict of a specific vertice on the curent coloration
	 */
	public static int countConflictOnAVertice(int verticeId, int[] color){
		int conflict = 0;
		for(int j=0; j<matrice[verticeId].length;j++)
			if(matrice[verticeId][j] == 1 && color[verticeId] == color[j])
				conflict++;
		return conflict;
	}
	
	/**
	 * Additional constraint Predecessor
	 * @return
	 */
	public static int additionalConstraintOnPredecessor(int idOfFirstVertice, int idOfSecondVertice, int[] color){
		int conflict = 0;
		if(color[idOfFirstVertice] > color[idOfSecondVertice])
			conflict++;
		return conflict;
	}
	
	/**
	 * Additional constraint on max vertices/color
	 * @return
	 */
	public static int additionalConstraintOnMaxVerticePerColor(int maxNumber, int[] color){
		int conflict = 0;
		int[] colorOccurence = new int[numberOfColor];
		
		for(int i = 0; i < color.length ; i ++){
			colorOccurence[color[i]]++;
			if(colorOccurence[color[i]] > maxNumber)
				conflict++;
		}
		return conflict;
	}
	
	/**
	 * Print a console formating Agenda from the coloration
	 */
	public static void printAgenda(int[] color){
		ArrayList<ArrayList<Integer>> sortedVerticeByColor = new ArrayList<>();
		for(int i=0;i<numberOfColor;i++)
			sortedVerticeByColor.add(new ArrayList<Integer>());
		
		for(int i=0;i<color.length;i++)
			sortedVerticeByColor.get(color[i]).add(i);
		
		System.out.println("Score Total : " + countScoreOfColoration(color));
		System.out.println("Step number : " + step);
		System.out.println("#############");
		for(ArrayList<Integer> seance : sortedVerticeByColor){
			for(Integer session : seance)
				System.out.print(charPicker(session) + " ");
			System.out.println();
			System.out.println("########");
		}
	}
	
	
	/**
	 * Translate the vertice ID into a Character
	 * @param index of the vertice
	 * @return
	 */
	public static String charPicker(Integer index){
		switch(index){
		case 0 : return "A";
		case 1 : return "B";
		case 2 : return "C";
		case 3 : return "D";
		case 4 : return "E";
		case 5 : return "F";
		case 6 : return "G";
		case 7 : return "H";
		case 8 : return "I";
		case 9 : return "J";
		case 10 : return "K";
		default : return "unknow";
		}
	}
	
	
}
