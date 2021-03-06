/* Assignment: Project 3 - TSP – Closest Edge Insertion Heuristic
** Name: Chris Del Fattore
** Email: crdelf01@cardmail.louisville.edu
** Description: Implement the closest edge insertion heuritic to find a hamiltonian path.
*/
import java.io.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;
import java.awt.*;

public class ClosestEdge {
	public static Map<Integer,PointA> points;
	//public static Map<Integer,Double> edgeMap;
	public static ArrayList<Integer> finalPath;
	public static Map<Integer,Map<Integer,Double>> edgeLengths;
	public static ArrayList<Integer> drawArray;
	public static void main(String[] args) throws IOException {
		//Takes the filename as a parameter. File contains points and the x and y cooridnates.
		String filename = args[0];

		//The pointa class is defined at the bottom of this file.
		//The pointa class is a basic class to store information about a point.
		//The Below list is used to store the point information from the input file
		points = new HashMap<Integer,PointA>();

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
				PointA p = new PointA(Integer.parseInt(value.split(" ")[0]), Double.parseDouble(value.split(" ")[1]), Double.parseDouble(value.split(" ")[2]));
				points.put(p.name,p);
			}
		}

		//Map to store the edgeLengths for all of the edges
		edgeLengths = new HashMap<Integer,Map<Integer,Double>>();

		//Iterate through all of the points
		//create all the permutaions of edges
		//find their distance
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

		//fianlPath is the arraylist that will store the order of the path as it is being built
		finalPath = new ArrayList<Integer>();
		//map to store the start node int and the double of the path length for that start node
		Map<Integer,Double> finalPathDistances = new HashMap<Integer,Double>();
		//map to store the start node int and the string of the hamiltonian path
		Map<Integer,String> finalPathString = new HashMap<Integer,String>();

		//b is the start node int for the path
		//iterate through all of the points from the input file
		for(Integer b : points.keySet()) {
			int inputNode = b;
			int initNodeA = findClosestNode(inputNode);
			finalPath.add(inputNode);
			finalPath.add(initNodeA);
			/*System.out.println();
			System.out.println(inputNode + "-" + initNodeA);*/

			//algorithm start
			while(finalPath.size() < edgeLengths.get(1).size()){
				//find the node with the shortest path
				int[] tempShortest = findShortestPath();
				if(tempShortest[0] != -1){
					finalPath.add( tempShortest[2], tempShortest[0]);
					//finalPath.add(tempShortest);
				}
				/*for(Integer i : finalPath){
					System.out.print(i + "-");
				}
				System.out.println();*/
			}
			
			//output the finall path to a map, will iterate over it to 
			//find the shortest path between all of the starting points
			String path = "";
			for(Integer i : finalPath){
					//System.out.print(i + "-");
					path += i + "-";
			}
				//System.out.println(calculateAllEdges());
				finalPathDistances.put(b,calculateAllEdges());
				finalPathString.put(b,path);
				finalPath.clear();
				//System.out.println();
		}

		//Find the shortest path out of all of the starting points
		double finalShortest = Double.MAX_VALUE;
		int finalStartNode = -1;
		for(Integer k : finalPathDistances.keySet()){
			if(finalPathDistances.get(k) < finalShortest) {
				finalShortest = finalPathDistances.get(k);
				finalStartNode = k;
			}
		}
		System.out.println("The distance of the final path is " + finalShortest + " and the path is " + finalPathString.get(finalStartNode) + finalStartNode);
		drawArray = new ArrayList<Integer>();
		//Parse the final path string into an array in order to print them in the jFrame.	
		for(String s : finalPathString.get(finalStartNode).split("-")){
			//System.out.println(s);
			drawArray.add(Integer.parseInt(s));
		}
		
		//Create the JFrame that will display the points and the edges
		JFrame frame = new JFrame();
		frame.setSize(600,600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Hamiltonian Path");
		
		//add the custom JPanel that will display the points and edges.
		frame.add(new MyPanel());
		frame.setVisible(true);
	}

	//mypanel class will display the points on a jframe
	public static class MyPanel extends JPanel{
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;

			for(int i=0;i< points.size();i++){
				//System.out.println(drawArray.get(i)+": " + (int)points.get(drawArray.get(i)).x + " " + (int)points.get(drawArray.get(i)).y);
				if(i+1>=points.size()){
					//System.out.println("here");
					//System.out.println((int)points.get(drawArray.get(i)).x + " " + (int)points.get(drawArray.get(i)).y + " " + (int)points.get(drawArray.get(0)).x + " " + (int)points.get(drawArray.get(0)).y);
					//g2.drawLine((int)points.get(drawArray.get(i)).x * 4,(int)points.get(drawArray.get(i)).x * 4, (int)points.get(drawArray.get(0)).x * 4, (int)points.get(drawArray.get(0)).y * 4);	
					//draw the point and the line
					g2.drawOval((int)points.get(drawArray.get(drawArray.size()-1)).x *4, (int)points.get(drawArray.get(drawArray.size()-1)).y *4, 5, 5);
					g2.drawLine( (int)points.get(drawArray.get(0)).x *4, (int)points.get(drawArray.get(0)).y*4,(int)points.get(drawArray.get(drawArray.size()-1)).x*4,(int)points.get(drawArray.get(drawArray.size()-1)).y*4);
				}
				else {
					//System.out.println((int)points.get(drawArray.get(i)).x + " " + (int)points.get(drawArray.get(i)).y + " " + (int)points.get(drawArray.get(i+1)).x + " " + (int)points.get(drawArray.get(i+1)).y);
					//draw the point and the line
					g2.drawOval((int)points.get(drawArray.get(i)).x *4, (int)points.get(drawArray.get(i)).y *4, 5, 5);
					g2.drawLine( (int)points.get(drawArray.get(i)).x *4, (int)points.get(drawArray.get(i)).y*4,(int)points.get(drawArray.get(i+1)).x*4,(int)points.get(drawArray.get(i+1)).y*4);
				}
			}
		}	
	}
	

	//Method to compute distance
	//Takes to points as parameters and computes the distance between them.
	//Uses distance formula
	public static double computeDistance(PointA a, PointA b){
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
	public static Result findShortestPath(int a, int b){
		int sNode = -1; //the node that has the shortest combined path from both nodes
		Double sDistance = Double.MAX_VALUE; //set the intial distance to the max distance
		int nodeFrom, nodeTo; //shortest nodeFrom and nodeTo
		for (int i = 1;i <=  edgeLengths.get(a).size(); i++ ) {
			Double tempDistance = edgeLengths.get(a).get(i) + edgeLengths.get(b).get(i);
			if(sDistance > tempDistance && !finalPath.contains(i)){
				sNode = i;
				sDistance = tempDistance;
				nodeFrom = a;
				nodeTo = b;
			}
		}

		return new Result(sNode,sDistance,a,b);
	}

	//find the node with the shortest combined distance
	public static int[] findShortestPath(){
		double shortestPath = Double.MAX_VALUE;
		int shortPathNode = -1;
		int[] c = new int[3];
		//iterate through all of the edges in the list
		for(int j = 0; j < finalPath.size(); j++) {
			Result res = new Result();
			//System.out.print(j + " ");
			if(j+1==finalPath.size()){
				//System.out.println("here");
				res = findShortestPath(finalPath.get(j),finalPath.get(0));	
			}
			else {
				res = findShortestPath(finalPath.get(j),finalPath.get(j+1));	
			}
			//check if new result is the shortest
			if(res.dis < shortestPath){
				shortestPath = res.dis;
				shortPathNode = res.node;
				c[0] = shortPathNode;
				c[1] = j;
				c[2] = j+1;

				//System.out.println(res.dis + " " + res.node + " " + res.nodeFrom + " " + res.nodeTo) ;
			}
		}
		
		return c;
	}

	//method used to add the distances up for all of the edges
	//used at the end of the program
	public static Double calculateAllEdges(){
		double totalDistance = 0.0;
		for(int i = 0;i < finalPath.size();i++){
			if(i == 0) continue;
			else {
				double temp = edgeLengths.get(finalPath.get(i-1)).get(finalPath.get(i));
				totalDistance += temp;
				//System.out.println(finalPath.get(i-1) + " " + finalPath.get(i) + " Distance: " + temp);
			}
		}
		//need to calculate distance from the first node to the last node
		//System.out.println(finalPath.get(finalPath.size()-1) + " " + finalPath.get(0) + " Distance: " + edgeLengths.get(finalPath.get(finalPath.size()-1)).get(finalPath.get(0)) );
		totalDistance += edgeLengths.get(finalPath.get(finalPath.size()-1)).get(finalPath.get(0));

		//return the total distance for the path
		return totalDistance;
	}
}

//result class will return the edge, point and distance stored in one object
//
class Result {
	int node, nodeFrom, nodeTo;
	double dis;

	Result(int a, double b, int nodeFrom, int nodeTo){
		this.node = a;
		this.dis = b;
		this.nodeFrom = nodeFrom;
		this.nodeTo = nodeTo;
	}
	Result(){
		this.node = 0;
		this.dis = Double.MAX_VALUE;
	}

}
//Object used to represent a single point
//Point Stores the Name, X and Y Value
//with methods to retrieve the name, x and y value
//and a method to set the name.
//Turns out there is a java class called point
class PointA {
	int name;
	double x, y;
	//constructor
	PointA(int name, double x, double y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}
	//needed when converting a number to a letter and vise versa
	void setName(int a) {
		this.name = a;
	}
}