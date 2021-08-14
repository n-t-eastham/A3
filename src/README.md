AUTHOR: Nick Eastham

DESIGN:
What data structure(s) will you use to represent the data in the file “attractions.csv”? 
	-HashMap<String, ArrayList<String>>

What data structure(s) will you use to represent the data in the file “roads.csv”? 
	-A custom defined class, Road, to hold our connections in the graph.

What algorithm(s) will you use to find the shortest route from starting city to ending city through all the specified events?
	-Dijkstra’s algorithm to find the shortest route between any two cities, and then the 	
	    fastest possible path which visits all the attractions 

What classes would you use?
	-The RoadTrip which will make use of the Road class mentioned above. 				
	-(Update) A Triple class containing three templets (current city, distance,
	    previous city) to assist with the implementation of Dijkstra’s algorithm 

What public or private functions would they have? (Feel free to use UML in your explanation.) 
	-RoadTrip: the route function to return a list representing the shortest rout;
	    Dijkstra to find that shortest rout; getCityOfAttraction to return the
	    location of our attraction of interest; getPath    
	-Road: getters/setters 
	-(Update) Triple: getters/setters

Which class / function contains the "route" function?
	-RoadTrip

What data structures would you use?
	-HashMap, ArrayList, Queue

What algorithms on those data structures would be important? 
	-Dijkstra’s or some other algorithm that finds the shortest path in a graph

How will you store the data from the distance file?
	-In a custom class which will hold 2 locations, and their distance and time that it 			
	    takes to get from 1 to the other 

How will you store the data from the interests file?
	-In a map which holds location and list of interests