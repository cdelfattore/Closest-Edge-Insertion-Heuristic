/* Assignment: Project 3 - TSP – Closest Edge Insertion Heuristic
** Name: Chris Del Fattore
** Email: crdelf01@cardmail.louisville.edu
*/
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class ClosestEdge {
	public static Map<Integer,Point> points;
	//public static Map<Integer,Double> edgeMap;
	public static List<Integer> finalPath;
	public static Map<Integer,Map<Integer,Double>> edgeLengths;

	public static void main(String[] args) throws IOException {
		//Takes the filename as a parameter. File contains points and the x and y cooridnates.
		String filename = args[0];

		//The point class is defined at the bottom of this file.
		//The point class is a basic class to store information about a point.
		//The Below list is used to store the point information from the input file
		points = new HashMap<Integer,Point>();

		//BufferedReader used to read input from a file
		BufferedReader reader = new BufferedReader(new FileReader(filename));

		//pattern is the regular expression used to parse throught the input file and find the point number and the point's x and y value.
		//The pattern will find all of the points in the file
		String pattern = "(?m)^\\d+\\s\\d+\\.\\d+\\s\\d+\\.\\d+";
		Pattern r = Pattern.compile(pattern);

		String value = null;

		//the below while loop with go through the file line by line and see if a match has been made with the regular expression.
		//If a match is made, the line is parsed, retrieving the piont name, x and y coordinate values
		//the points are saved in the points list.
		while((value = reader.readLine()) != null){
			Matcher m = r.matcher(value);
			if(m.find()) {
				//add the point to the List of points
				Point p = new Point(Integer.parseInt(value.split(" ")[0]), Double.parseDouble(value.split(" ")[1]), Double.parseDouble(value.split(" ")[2]));
				points.put(p.name,p);
			}
		}
		/*for(int i = 0;i<points.size();i++){
			System.out.println("i: " + i + " name: " + points.get(i).name + " " + points.get(i).x + " " + points.get(i).y);
		}*/

		/*for(Integer a : points.keySet()){
			System.out.println(a + " " + points.get(a).x + " " + points.get(a).y);
		}

		System.out.println(points.size());*/

		edgeLengths = new HashMap<Integer,Map<Integer,Double>>();

		for(Integer a : points.keySet()){
			//System.out.println(a + " " + points.get(a).x + " " + points.get(a).y);
			Map<Integer,Double> listDoubles = new HashMap<Integer,Double>();
			for(Integer b : points.keySet()){
				if(a==b) {
					listDoubles.put(b,Double.MAX_VALUE);
				}
				else {
					listDoubles.put(b,computeDistance(points.get(a),points.get(b)));	
				}
			}
			edgeLengths.put(a,listDoubles);
		}

		/*for (Integer i : edgeLengths.keySet()) {
			for(Double a : edgeLengths.get(i)) {
				System.out.println(i + " " + a);
			}
		}*/

		/*for (Integer i : edgeLengths.keySet()) {
			for (int a = 1;a <=  edgeLengths.get(i).size(); a++ ) {
				System.out.println(i + "-" + a + " " + edgeLengths.get(i).get(a));	
			}
		}*/

		//edgeMap = new HashMap<Integer,Double>();


		/*for(Integer a : edgeMap.keySet()){
			System.out.println(a + " " + edgeMap.get(a));
		}*/

		/*visited = new ArrayList<Integer>();
		
		for(int i = 1;i < points.size(); i++){
			findClosetsEdge(i);
		}
		for(Integer i : visited){
			if (i == 0) continue;
			System.out.print(i + "-");
		}*/

		//Start the search
		finalPath = new ArrayList<Integer>();
		int inputNode = Integer.parseInt(args[1]);
		int initNodeA = findClosestNode(inputNode);
		//int initNodeB = findShortestPath(1,initNodeA);
		//System.out.println(initNodeA);
		//System.out.println(initNodeB);
		
		finalPath.add(inputNode);
		finalPath.add(initNodeA);
		//finalPath.add(initNodeB);
		//System.out.println(inputNode + "-" + initNodeA);


		//System.out.println( finalPath.get(finalPath.size()-3)  + "-" + finalPath.get(finalPath.size()-2) + "-" + finalPath.get(finalPath.size()-1));
		while(finalPath.size() < edgeLengths.get(1).size()){
			int tempShortest = findShortestPath(finalPath.get(finalPath.size()-2),finalPath.get(finalPath.size()-1));
			if(tempShortest != -1){
				finalPath.add( finalPath.size()-1, tempShortest);
			}
			/*for(Integer i : finalPath){
				System.out.print(i + "-");
			}
			System.out.println();*/
		}

		System.out.println(calculateAllEdges());

	}

	//Method to compute distance
	//Takes to points as parameters and computes the distance between them.
	//Uses distance formula
	public static double computeDistance(Point a, Point b){
		return Math.sqrt( ((a.x - b.x) * (a.x - b.x )) + ((a.y - b.y ) * (a.y - b.y ) ) );
	}

	//used when finding the first smallest edge in the graph
	public static int findClosestNode(int node){
		int sNode = -1;
		double sDistance = Double.MAX_VALUE;
			for (int a = 1;a <=  edgeLengths.get(node).size(); a++ ) {
				//System.out.println(i + "-" + a + " " + edgeLengths.get(i).get(a));
				//System.out.println(node + "-" + a + " " +edgeLengths.get(node).get(a));
				if(sDistance > edgeLengths.get(node).get(a)){
					sNode = a;
					sDistance = edgeLengths.get(node).get(a);
				}
			}
		return sNode;
	}

	//find the node with the shortest combined distance
	public static int findShortestPath(int a, int b){
		int sNode = -1; //the node that has the shortest combined path from both nodes
		Double sDistance = Double.MAX_VALUE;
		for (int i = 1;i <=  edgeLengths.get(a).size(); i++ ) {
			Double tempDistance = edgeLengths.get(a).get(i) + edgeLengths.get(b).get(i);
			if(sDistance > tempDistance && !finalPath.contains(i)){
				sNode = i;
				sDistance = tempDistance;
			}
		}
		return sNode;
	}

	public static Double calculateAllEdges(){
		double totalDistance = 0.0;
		for(int i = 0;i < finalPath.size();i++){
			if(i == 0) continue;
			else {
				totalDistance += edgeLengths.get(finalPath.get(i-1)).get(finalPath.get(i));
			}
		}
		//need to calculate distance from the first node to the last node
		totalDistance += edgeLengths.get(finalPath.get(finalPath.size()-1)).get(finalPath.get(0));

		//return the total distance for the path
		return totalDistance;
	}


}


//Object used to represent a single point
//Point Stores the Name, X and Y Value
//with methods to retrieve the name, x and y value
//and a method to set the name.
class Point {
	int name;
	double x, y;
	//constructor
	Point(int name, double x, double y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}
	//needed when converting a number to a letter and vise versa
	void setName(int a) {
		this.name = a;
	}
}