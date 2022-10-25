package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** put your name here ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private Hashtable database = new Hashtable<String, SoccerPlayer>();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {

        String playerName = firstName + "-" + lastName;

        if (!database.containsKey(playerName)) {

            SoccerPlayer newPlayer = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
            database.put(playerName, newPlayer);

            return true;
        }

        return false;
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {

        String playerName = firstName + "-" + lastName;

        if (database.containsKey(playerName)) {

            database.remove(playerName);
            return true;
        }

        return false;
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {

        String playerName = firstName + "-" + lastName;

        if (database.containsKey(playerName)) {
            return (SoccerPlayer) database.get(playerName);
        }

        return null;
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {

        String playerName = firstName + "-" + lastName;

        if (database.containsKey(playerName)) {
            SoccerPlayer player = (SoccerPlayer) database.get(playerName);
            player.bumpGoals();

            return true;
        }

        return false;
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {

        String playerName = firstName + "-" + lastName;

        if (database.containsKey(playerName)) {
            SoccerPlayer player = (SoccerPlayer) database.get(playerName);
            player.bumpYellowCards();

            return true;
        }

        return false;
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {

        String playerName = firstName + "-" + lastName;

        if (database.containsKey(playerName)) {
            SoccerPlayer player = (SoccerPlayer) database.get(playerName);
            player.bumpRedCards();

            return true;
        }

        return false;
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {

        int count = 0;

        if (teamName == null) {
            count = database.size();

        } else {
            for (Object value : database.values()) {

                SoccerPlayer player = (SoccerPlayer) value;
                if (player.getTeamName().equals(teamName)) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {

        int count = -1;

        for (Object value : database.values()) {
            SoccerPlayer player = (SoccerPlayer) value;

            if (player.getTeamName().equals(teamName) || teamName == null) {
                count++;
            }

            if (count == idx) {
                return player;
            }
        }

        return null;
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {

        try {

            Scanner sc = new Scanner(file);
            sc.useDelimiter("\n");

            String first = "";
            String last = "";
            int uniform = 0;
            String team = "";
            SoccerPlayer player = new SoccerPlayer(first, last, uniform, team);

            while (sc.hasNext()) {
                String str = sc.next();

                if (!str.equals("------------------")) {

                    int colonIndex = str.indexOf(":");
                    String prefix = str.substring(0, colonIndex);
                    String suffix = str.substring(colonIndex + 2);

                    switch (prefix) {
                        case "First Name":
                            first = suffix;
                            break;

                        case "Last Name":
                            last = suffix;
                            break;

                        case "Uniform":
                            uniform = Integer.parseInt(suffix);
                            break;

                        case "Team Name":
                            team = suffix;

                            String name = first + "-" + last;
                            if (database.containsKey(name)) {
                                removePlayer(first, last);
                            }

                            addPlayer(first, last, uniform, team);
                            player = getPlayer(first, last);
                            break;

                        case "Goals":
                            for (int i = 0; i < Integer.parseInt(suffix); i++) {
                                player.bumpGoals();
                            }
                            break;

                        case "Red Cards":
                            for (int i = 0; i < Integer.parseInt(suffix); i++) {
                                player.bumpRedCards();
                            }
                            break;

                        case "Yellow Cards":
                            for (int i = 0; i < Integer.parseInt(suffix); i++) {
                                player.bumpYellowCards();
                            }
                            break;
                    }
                }
            }

        } catch (Exception FileNotFoundException) {
            return false;
        }

        return file.exists();
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {

        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");

            for (Object key : database.keySet()) {
                String name = (String) key;

                SoccerPlayer player = (SoccerPlayer) database.get(name);

                writer.println(logString("------------------"));
                writer.println(logString("First Name: " + player.getFirstName()));
                writer.println(logString("Last Name: " + player.getLastName()));
                writer.println(logString("Uniform: " + player.getUniform()));
                writer.println(logString("Team Name: " + player.getTeamName()));
                writer.println(logString("Goals: " + player.getGoals()));
                writer.println(logString("Red Cards: " + player.getRedCards()));
                writer.println(logString("Yellow Cards: " + player.getYellowCards()));
            }

            writer.close();

        } catch (Exception FileNotFoundException) {
            Log.i("file", "not found");
            return false;
        }

        return true;
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
//        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        HashSet<String> teams = new HashSet<>();

        for (Object key : database.keySet()) {
            String name = (String) key;
            String team = ((SoccerPlayer) database.get(name)).getTeamName();

            teams.add(team);
        }

        return teams;
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
