/**
 * Class for holding info about roads (Connections in the graph), holds names of two cities, distance and time,
 * In my project I used time and not distance, but using distance would work as well
 * it contains simple getters and setters and a toString method used for debugging
 */
public class Road {
    private String city1, city2; int distance, time;


    public Road(String city1, String city2, int distance, int time) {
        this.city1 = city1;
        this.city2 = city2;
        this.distance = distance;
        this.time = time;
    }

    public String getCity1() {
        return city1;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }

    public String getCity2() {
        return city2;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Road{" +
                "city1='" + city1 + '\'' +
                ", city2='" + city2 + '\'' +
                ", distance=" + distance +
                ", time=" + time + '}';
    }
}
