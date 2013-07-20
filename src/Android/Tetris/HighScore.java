package Android.Tetris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Interacts with a mySQL database using JSON and PHP in order to maintain
 * a high score table.
 * @author Zach Cotter
 */
public class HighScore {

    private ArrayList<HighScoreEntry> list;    

    /**
     * Connects to the database and initializes the list.
     */
    public HighScore() {
        list = new ArrayList<HighScoreEntry>();
        try {
            //connects to and runs the mySQL query in the PHP script.
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(
                    "http://www.zachcotter.com/Tetris/scorecheck.php");
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            //converts the response into a string
            BufferedReader reader =
                           new BufferedReader(new InputStreamReader(is));
            String result = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
            reader.close();
            is.close();

            //converts the JSON representation of data to HighScoreEntry objects.
            JSONArray data = new JSONArray(result);
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonEntry = data.getJSONObject(i);
                list.add(new HighScoreEntry(jsonEntry.getString("name"),
                                            jsonEntry.getInt("score")));
            }
        }
        catch (JSONException ex) {
            Logger.getLogger(HighScore.class.getName()).log(Level.SEVERE,
                                                            null,
                                                            ex);
        }
        catch (ClientProtocolException ex) {
            Logger.getLogger(HighScore.class.getName()).log(Level.SEVERE,
                                                            null,
                                                            ex);
        }
        catch (IOException ex) {
            Logger.getLogger(HighScore.class.getName()).log(Level.SEVERE,
                                                            null,
                                                            ex);
        }
    }
    
    /**
     * Posts a new entry to the database.
     * @param e The high score entry to add.
     */
    public static void postScore(HighScoreEntry e){
        try {
            //converts the HighScoreEntry to a type that can be used by the client
            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("name", e.getName()));
            pairs.add(new BasicNameValuePair("score", "" + e.getScore()));
            
            //passes the data retrieved from the HighScoreEntry to the php
            //script,which executes a SQL insert.
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(
                    "http://www.zachcotter.com/Tetris/postscore.php");
            post.setEntity(new UrlEncodedFormEntity(pairs));
            client.execute(post);
        }
        catch (ClientProtocolException ex) {
            Logger.getLogger(HighScore.class.getName()).log(Level.SEVERE,
                                                            null,
                                                            ex);
        }
        catch (IOException ex) {
            Logger.getLogger(HighScore.class.getName()).log(Level.SEVERE,
                                                            null,
                                                            ex);
        }
    }
    
    public int getValueToQualify(){
        return list.get(list.size() - 1).getScore();
    }
    
    @Override
    public String toString(){
        String table = "";
        for(HighScoreEntry e : list){
            table += e.toString() + "\n";
        }
        return table;
    }
}
