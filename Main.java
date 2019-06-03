import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<City> cities = new ArrayList<>();

        createCities(args[0], cities);
        handleConnections(args[0], cities);

        BufferedWriter output = new BufferedWriter(new FileWriter(args[2]));

        executeCommands(args[1], cities, output);
        output.close();

    }


    static void executeCommands(String filePath, List<City> cities, BufferedWriter out) throws IOException {

        HashMap<Character, Integer> limits = new HashMap<>();


        ArrayList<String> path = new ArrayList<>();

        BufferedReader bufferedReader;
        String line;
        String[] afterSplit;
        ArrayList<String> foundRoutes;

        try {
            bufferedReader = new BufferedReader(new java.io.FileReader(filePath));
            while ((line = bufferedReader.readLine()) != null){

                if (line.length() <1 ){ //empty line
                    continue;
                }

                path.clear();
                limits.put('H', 0);
                limits.put('R', 0);
                limits.put('A', 0);

                afterSplit = line.split("\\s+");
                for (int i = 0; i < afterSplit.length-1; i++) {
                    out.write(afterSplit[i]);
                    out.write(", ");
                }
                out.write(afterSplit[afterSplit.length-1]);
                out.write("\n");
                //Printed the command
                if (afterSplit[0].equals("PRINTGRAPH")){
                    for (City c:cities) {
                        out.write(c.getNeighbours());
                        out.write('\n');
                    }
                    continue;
                }

                int queryNumber = Character.getNumericValue(afterSplit[0].charAt(1));
                path.add(afterSplit[1]); // Source city

                if (queryNumber == 1){
                    limits.put(afterSplit[4].charAt(0), Integer.parseInt(afterSplit[3]));
                }else if (queryNumber == 2){
                    path.addAll(Arrays.asList(afterSplit).subList(3, afterSplit.length));
                }else if (queryNumber == 3) {
                    limits.put(afterSplit[3].charAt(0), 1);
                }else if (queryNumber == 4){
                    for (int i = 3; i < afterSplit.length; i++) {
                        limits.put(afterSplit[i].charAt(0), Integer.parseInt(afterSplit[i].substring(1)));
                    }
                }

                // Handled the limits

                path.add(afterSplit[2]); //Destination city

                foundRoutes = cities.get(findIndex(cities, path.get(0)))
                        .search(queryNumber, path, limits, new ArrayList<>(), new ArrayList<>());

                if (foundRoutes.isEmpty()){
                    out.write("There is no way!");
                    out.write('\n');
                }else {
                    for (String route:foundRoutes) {
                        out.write(route);
                        out.write('\n');
                    }
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void createCities(String path, ArrayList<City> cities){
        BufferedReader bufferedReader;
        String line;
        String[] afterSplit = {"Initial", "Value", "Won't be used"};
        try {
            bufferedReader = new BufferedReader(new java.io.FileReader(path));
            line = bufferedReader.readLine(); // We only need the city names so we ignore the first part
            while ((line = bufferedReader.readLine()) != null && line.length() > 1){
                afterSplit = line.split("\\s+");
                cities.add(new City(afterSplit[0]));
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleConnections(String path, ArrayList<City> cities){
        char route = 'Ğ'; // Initial value, will be overridden immediately
        City city;
        String connectionInfo;

        BufferedReader bufferedReader;
        String line;
        String[] afterSplit;
        try {
            bufferedReader = new BufferedReader(new java.io.FileReader(path));
            while ((line = bufferedReader.readLine()) != null){
                afterSplit = line.split("\\s+");
                if (line.length() == 0){
                    continue;
                }if (afterSplit.length == 1){
                    route = afterSplit[0].toUpperCase().charAt(0);
                    continue;
                }

                city = cities.get(findIndex(cities, afterSplit[0]));
                connectionInfo = afterSplit[1];

                for (int i=0; i<afterSplit[1].length(); i++){
                    if (connectionInfo.charAt(i) == '1'){
                        city.addNeighbour(cities.get(i), route);
                    }
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    static int findIndex(List<City> cities, String cityName){
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getName().equals(cityName)){
                return i;
            }
        }
        return -1;
    }



}
