import java.util.*;

public class City {

    private String name;
    private HashMap<Character, ArrayList<City>> neighbours;

    public City(String name) {
        this.name = name;
        neighbours = new HashMap<>();
        neighbours.put('R', new ArrayList<>());
        neighbours.put('A', new ArrayList<>());
        neighbours.put('H', new ArrayList<>());
    }


    public ArrayList<String> search(int queryNumber, ArrayList<String> path, HashMap<Character, Integer> limits,
                               ArrayList<String> passedCities, ArrayList<Character> passedRoutes) {

        ArrayList<String> routes = new ArrayList<>(); // All the routes that branch from here

        passedCities.add(this.getName());

        if (!name.equals(path.get(path.size()-1))) {   // Not in the final destination

            if (limitsNotExceeded(queryNumber, passedRoutes, limits)){ // If we exceeded any limits, we can't continue
                for (Map.Entry<Character, ArrayList<City>> entry : neighbours.entrySet()) {
                    char type = entry.getKey();
                    ArrayList<City> adjacent = entry.getValue();
                    for (City city:adjacent) {
                        passedRoutes.add(type);
                        if (occurences(passedCities, city.getName()) > 0){ // This city was visited before
                            continue;
                        }
                        routes.addAll(city.search(queryNumber, path, limits, passedCities, passedRoutes)); // Add the new routes rom that branch
                        passedRoutes.remove(passedRoutes.size()-1); // Remove the last added type. It shouldn't be in the list in this context
                    }
                }
                // Visited all the neighbours and added their routes to the list.
            }

        } else {  // In the final destination
            if (limitsFulfilled(queryNumber, path, passedCities, passedRoutes, limits)){
                routes.add(createString(passedCities, passedRoutes));
            }
        }

        passedCities.remove(this.getName()); // The name of this city must not exist in the context we are returning to
        return routes;
    }


    public boolean limitsNotExceeded(int queryNumber, ArrayList<Character> passedRoutes,
                                     HashMap<Character, Integer> limits){

        if (queryNumber == 4){  // For query number 4 there are upper limits
            for (Map.Entry<Character, Integer> entry : limits.entrySet()) {
                char route = entry.getKey();
                int limit = entry.getValue();
                if (occurences(passedRoutes, route) > limit){ // Limit is exceeded
                    return false;
                }
            }
        }
        
        return true;
    }



    public boolean limitsFulfilled(int queryNumber, ArrayList<String> path,
                                   ArrayList<String> passedCities, ArrayList<Character> passedRoutes,
                                   HashMap<Character, Integer> limits){

        for (String city : path) {
            if (occurences(passedCities, city) != 1){ // Did we visit all the cities on the path exactly once
                return false;
            }
        }

        if (queryNumber == 1){ // Can't be lower than lower limit. All except one limit will be 0
            for (Map.Entry<Character, Integer> entry : limits.entrySet()) {
                char route = entry.getKey();
                int limit = entry.getValue();
                if (occurences(passedRoutes, route) < limit){
                    return false;
                }
            }
        }


        if (queryNumber == 3){ // Can't be nonzero if upper limit in not. All except one limit will be 0
            for (Map.Entry<Character, Integer> entry : limits.entrySet()) {
                char route = entry.getKey();
                int limit = entry.getValue();
                if (occurences(passedRoutes, route) != 0 && limit == 0){
                    return false;
                }
            }
        }

        if (queryNumber == 4){ // Must be exactly the same as limit
            for (Map.Entry<Character, Integer> entry : limits.entrySet()) {
                char key = entry.getKey();
                int limit = entry.getValue();
                if (occurences(passedRoutes, key) != limit){
                    return false;
                }
            }
        }

        return true;
    }


    private String createString(ArrayList<String> passedCities, ArrayList<Character> passedRoutes){
        String string = "";

        for (int i = 0; i < passedCities.size() - 1; i++) {
            string = string.concat(passedCities.get(i));
            string = string.concat(", ");
            string = string.concat(passedRoutes.get(i).toString());
            string = string.concat(", ");
        }
        string = string.concat(passedCities.get(passedCities.size()-1));

        return string;
    }

    public String getNeighbours(){
        ArrayList<City> neighbours;
        String str = name;

        neighbours = new ArrayList<>();
        neighbours.addAll(this.neighbours.get('A'));
        neighbours.addAll(this.neighbours.get('H'));
        neighbours.addAll(this.neighbours.get('R'));
        neighbours = removeDuplicates(neighbours);

        str = str.concat(" -->");

        for (City neigbour : neighbours) {
            str = str.concat(" " + neigbour.getName());
        }

        return str;
    }

    public void addNeighbour(City city, char route){
        neighbours.get(route).add(city);
    }


    private ArrayList<City> removeDuplicates(ArrayList<City> withDuplicates) {
        ArrayList<City> withoutDuplicates = new ArrayList<City>();
        for (City city : withDuplicates) {
            if (!withoutDuplicates.contains(city)) {
                withoutDuplicates.add(city);
            }
        }
        return withoutDuplicates;
    }


    private int occurences(ArrayList<Character> list, char c){
        int total = 0;
        for (char chr:list){
            if (chr == c){
                total++;
            }
        }
        return total;
    }

    private int occurences(ArrayList<String> list, String s){
        int total = 0;
        for (String str:list){
            if (str.equals(s)){
                total++;
            }
        }
        return total;
    }



    public String getName() {
        return name;
    }


}
