package team.fcisquare;

import android.os.AsyncTask;

/**
 * @author Andrew Albert
 * @version 1.1
 * @since 24/3/2016
 *
 * This is just an abstract class to allow dynamic binding of GET and POST connections
 */
public abstract class Connection extends AsyncTask < String, String, String > {
}
