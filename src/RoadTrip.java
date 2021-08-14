import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RoadTrip {
    static ArrayList<Road> roads;  //ArrayList for all the roads
    static ArrayList<String> allAttractions;  //used for input validation
    static ArrayList<String> allCities;  //used for input validation
    static HashMap<String, ArrayList<String>> attractionsInCity;  //<city name, arraylist of all attractions in that city>
    static HashSet<String> cities;  //names of cities



    public static List<String> route(String starting_city, String ending_city, ArrayList<String> attractions) {

        //get all permutations of the attractions, so that we may implement
        //Dijkstra's Algorithm on every combination and get the fastest one
        ArrayList<ArrayList<String>> permutations = permutate(attractions);

        int bestTime = Integer.MAX_VALUE;
        ArrayList<String> bestPath = new ArrayList<>();

        //Run Dijkstra on every permutation of the attractions
        for (ArrayList<String> thisPermAttractions : permutations) {

            thisPermAttractions.add(0, starting_city);  //add starting city at the beginning
            thisPermAttractions.add(ending_city);  //add ending city to the back
            ArrayList<String> currentPath = new ArrayList<>();
            int currentTime = 0;

            //current city is the one we are in, next city is the next one in the ArrayList
            for (int i = 1; i < thisPermAttractions.size(); i++) {
                String currentCity, nextCity;

                //if next index is the last one, we have city name and not attraction name,
                //so we don't have to call getCityOfAttraction
                //otherwise we have attraction name and we need to get city name for Dijkstra's
                if (i != thisPermAttractions.size() - 1)
                    nextCity = getCityOfAttraction(thisPermAttractions.get(i));
                else
                    nextCity = thisPermAttractions.get(i);

                //same with current city, if we are on the first, we have city name, otherwise we get city name
                //from attraction name
                if(i == 1)
                    currentCity = thisPermAttractions.get(0);
                else
                    currentCity = getCityOfAttraction(thisPermAttractions.get(i - 1));

                //do Dijkstra on the current city
                ArrayList<Triple<String, Integer, String>> dijkstraFromThisCity = Dijkstra(currentCity);

                //get path from this city to the next city / attraction
                ArrayList<String> path = getPathFromDijkstra(currentCity, nextCity, dijkstraFromThisCity);

                //get the amount of time needed to go from current city to the next one and add it
                // to the current iteration time
                int thatCityTime = 0;
                for (var triple : dijkstraFromThisCity) {
                    if (triple.getFirst().equals(nextCity))
                        thatCityTime = triple.getSecond();
                }
                currentPath.addAll(path);  //add path to the current iteration path
                currentTime += thatCityTime;

            }
            if (currentTime < bestTime) {  //if current iteration is better, save its time and path
                bestTime = currentTime;
                bestPath = currentPath;
            }
        }

        return bestPath;  //returns the best path
    }



    public static ArrayList<String> getPathFromDijkstra(String startCity, String endCity, ArrayList<Triple<String, Integer, String>> dijkstraPath) {
        ArrayList<String> reversedPath = new ArrayList<>();
        String currentCity = endCity;

        //using Dijkstra's Algorithm we save a previous city for every new city visited,
        //we then backtrack and find the first city beginning from the last
        while (!currentCity.equals(startCity) && !currentCity.equals("START")) {
            reversedPath.add(currentCity);

            boolean found = false;
            for (Triple<String, Integer, String> triple : dijkstraPath) {
                if (triple.getFirst().equals(currentCity) && !found) {
                    currentCity = triple.getThird();
                    found = true;
                }
            }
        }

        ArrayList<String> goodPath = new ArrayList<>();
        goodPath.add(startCity);

        //once we get the shortest path, its backwards so we have to reverse the ArrayList
        for (int i = reversedPath.size() - 1; i >= 0; i--)
            goodPath.add(reversedPath.get(i));

        return goodPath;
    }

    //optimizing Map Iteration by looping over a collection of Map.Entry objects
    public static String getCityOfAttraction(String attraction) {
        for (HashMap.Entry<String, ArrayList<String>> e : attractionsInCity.entrySet())  //iterates through attractionsInCity
            for (String entryAttraction : e.getValue())
                if (entryAttraction.equals(attraction))   //finds which city the attraction belongs to
                    return e.getKey();

        return null;
    }



    //gets shortest path from current city to all the cities
    public static ArrayList<Triple<String, Integer, String>> Dijkstra(String source) {

        ArrayList<Triple<String, Integer, String>> distances = new ArrayList<>();

        //helper for cities, we will remove shortest from it and visit its neighbours
        ArrayList<String> priorityQueue = new ArrayList<>();

        //source has distance 0, others have distance Integer.MAX(infinity)
        distances.add(new Triple<>(source, 0, "START"));
        for (String city : cities) {
            if (!city.equals(source))
                distances.add(new Triple<>(city, Integer.MAX_VALUE, "NOT VISITED YET"));

            priorityQueue.add(city);
        }

        //we loop until we remove all the cities from the priority queue (we need to visit all of them)
        while (priorityQueue.size() > 0) {
            String currentShortest = "";  //get the one with the lowest distance
            int shoretestDist = Integer.MAX_VALUE;

            for (Triple<String, Integer, String> e : distances) {
                if (e.getSecond() < shoretestDist && priorityQueue.contains(e.getFirst())) {
                    shoretestDist = e.getSecond();
                    currentShortest = e.getFirst();
                }
            }

            //remove it from the queue because we will use it to visit its neighbours
            priorityQueue.remove(currentShortest);

            HashMap<String, Integer> neighbours = new HashMap<>();

            for (Road r : roads) {  //iterate through roads and get its neighbours
                if (r.getCity1().equals(currentShortest))
                    neighbours.put(r.getCity2(), r.getTime());

                if (r.getCity2().equals(currentShortest))
                    neighbours.put(r.getCity1(), r.getTime());
            }

            for (HashMap.Entry<String, Integer> neighbour : neighbours.entrySet()) {
                if (priorityQueue.contains(neighbour.getKey())) {  //if neighbours are in prioryQueue we can visit them
                    int newDist = shoretestDist + neighbour.getValue();

                    int currentDist = 0;
                    for (Triple<String, Integer, String> dist : distances)
                        if (dist.getFirst().equals(neighbour.getKey()))  //we update their distance variables if closer
                            currentDist = dist.getSecond();

                    //if its less then before, we update its distance, and its previous city
                    if (newDist < currentDist)
                        for (Triple<String, Integer, String> dist : distances)
                            if (dist.getFirst().equals(neighbour.getKey())) {
                                dist.setSecond(newDist);
                                dist.setThird(currentShortest);
                            }
                }
            }
        }
        return distances;  //returns map with Triple<City name, distance, previous city name>
    }



    public static ArrayList<ArrayList<String>> permutate(ArrayList<String> attractions) {
        ArrayList<ArrayList<String>> ret = new ArrayList<>();  //create an empty array list
        getPerms(ret, new ArrayList<>(), attractions);  //start getPerms
        return ret;
    }



    private static void getPerms(ArrayList<ArrayList<String>> perms, ArrayList<String> currentPermutation, ArrayList<String> source) {
        if (currentPermutation.size() == source.size())  //if current perm is the same size as all of the attractions
            perms.add(new ArrayList<>(currentPermutation));  //add it to the perms ArrayList
        else
            for (int i = 0; i < source.size(); i++)
                if (!currentPermutation.contains(source.get(i))) {
                    currentPermutation.add(source.get(i));  //otherwise, add new non visited attraction to the current perm
                    getPerms(perms, currentPermutation, source); //make a recursive call with it
                    currentPermutation.remove(currentPermutation.size() - 1);  //and then backtrack
                }

    }



    public static void readFiles(String roadFile, String eventFile) throws FileNotFoundException {
        Scanner roadScanner = new Scanner(new File(roadFile));
        Scanner eventScanner = new Scanner(new File(eventFile));

        while (roadScanner.hasNextLine()) {
            String line = roadScanner.nextLine();  //read line by line
            String[] spl = line.split(",");  //split line at the comma
            String city1 = spl[0].strip();
            String city2 = spl[1].strip();
            Road r = new Road(city1, city2, Integer.parseInt(spl[2]), Integer.parseInt(spl[3]));
            cities.add(city1);
            cities.add(city2);
            roads.add(r);
            allCities.add(city1);
            allCities.add(city2);
        }
        roadScanner.close();

        eventScanner.nextLine();
        //if an attraction belongs to a city not yet in the attractionsInCity Map,
        //create a key with that cities name, otherwise just append it to the Arraylist under that Key
        while (eventScanner.hasNextLine()) {
            String line = eventScanner.nextLine();
            String[] spl = line.split(",");

            String attraction = spl[0].strip();
            String location = spl[1].strip();

            if (!attractionsInCity.containsKey(location))
                attractionsInCity.put(location, new ArrayList<>());

            attractionsInCity.get(location).add(attraction);
            allAttractions.add(attraction);
        }
        eventScanner.close();
    }


    public static void main(String[] args) throws FileNotFoundException {

        roads = new ArrayList<>();
        attractionsInCity = new HashMap<>();
        allAttractions = new ArrayList<>();
        allCities = new ArrayList<>();
        cities = new HashSet<>();

        final Scanner sc = new Scanner(System.in);
        boolean valid = false;
        String event;
        String roadFile = "/Users/nickeastham/Downloads/roads.csv";
        String eventFile = "/Users/nickeastham/Downloads/attractions.csv";
        readFiles(roadFile, eventFile);

        try {
            System.out.print("Name of starting city: ");
            String firstCity = sc.nextLine();

            System.out.print("Name of ending city: ");
            String secondCity = sc.nextLine();

            if(!allCities.contains(firstCity) || !allCities.contains(secondCity))
                throw new Exception();

            ArrayList<String> attractions = new ArrayList<>();

            while (!valid) {
                System.out.print("List an attraction along the way (or ENOUGH to stop listing): ");
                event = sc.nextLine();

                if (!event.equals("ENOUGH")) {
                    if (!allAttractions.contains(event))
                        System.out.printf("Attraction \"%s\" unknown.%n", event);
                    else
                        attractions.add(event);
                } else {
                    System.out.println();
                    valid = true;
                }
            }

            List<String> roadTrip = route(firstCity, secondCity, attractions);
            System.out.println("Here is the best route for your trip:");

            for (int i = 0; i < roadTrip.size(); i++) {
                if (i % 2 == 0)
                    System.out.println();

                if (i < roadTrip.size() - 1 && !roadTrip.get(i).equals(roadTrip.get(i + 1)) || i == roadTrip.size() - 1) {
                    System.out.print(roadTrip.get(i));

                    if (i != roadTrip.size() - 1)
                        System.out.print(" -> ");
                }
            }
        }catch (Exception e){
            System.out.println("Invalid City");
        }
    }
}