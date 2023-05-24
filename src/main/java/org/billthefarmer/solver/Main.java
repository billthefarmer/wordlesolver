////////////////////////////////////////////////////////////////////////////////
//
//  Wordle solver
//
//  Copyright (C) 2022	Bill Farmer
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Bill Farmer	 william j farmer [at] yahoo [dot] co [dot] uk.
//
///////////////////////////////////////////////////////////////////////////////

package org.billthefarmer.solver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Activity
{
    public static final String TAG = "WordleSolver";
    public static final String PREF_THEME = "pref_theme";
    public static final String PREF_LANG = "pref_lang";
    public static final String GREEN = "green";
    public static final String YELLOW_1 = "yellow-1";
    public static final String YELLOW_2 = "yellow-2";
    public static final String YELLOW_3 = "yellow-3";
    public static final String GREY = "grey";

    public static final int ROWS[] =
    {
        R.id.yellow1,  R.id.yellow2,  R.id.yellow3
    };

    public static final int DARK   = 1;
    public static final int CYAN   = 2;
    public static final int BLUE   = 3;
    public static final int ORANGE = 4;
    public static final int PURPLE = 5;
    public static final int RED    = 6;

    public static final int ENGLISH    = 0;
    public static final int ITALIAN    = 1;
    public static final int SPANISH    = 2;
    public static final int CATALAN    = 3;
    public static final int FRENCH     = 4;
    public static final int PORTUGUESE = 5;
    public static final int GERMAN     = 6;
    public static final int DUTCH      = 7;
    public static final int AFRIKAANS  = 8;

    private TextView greyText;
    private TextView resultText;
    private TextView greenArray[];
    private TextView yellowArray[][];
    private List<String> greenList;
    private List<List<String>> yellowList;

    private int theme;
    private int language;

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(this);

        theme = preferences.getInt(PREF_THEME, DARK);
        language = preferences.getInt(PREF_LANG, ENGLISH);

        switch (theme)
        {
        default:
        case DARK:
            setTheme(R.style.AppTheme);
            break;

        case CYAN:
            setTheme(R.style.AppCyanTheme);
            break;

        case BLUE:
            setTheme(R.style.AppBlueTheme);
            break;

        case ORANGE:
            setTheme(R.style.AppOrangeTheme);
            break;

        case PURPLE:
            setTheme(R.style.AppPurpleTheme);
            break;

        case RED:
            setTheme(R.style.AppRedTheme);
            break;
        }

        setContentView(R.layout.main);

        setLanguage();

        TextView.OnEditorActionListener listener = new
            TextView.OnEditorActionListener()
        {
            // onEditorAction
            @Override
            public boolean onEditorAction(TextView view,
                                          int actionId,
                                          KeyEvent event)
            {
                // Check id
                switch (actionId)
                {
                    // Do a solve if there is a letter in the slot
                case EditorInfo.IME_ACTION_NEXT:
                    if (view.length() > 0)
                        solve();
                    break;
                }

                return false;
            }
        };

        TextWatcher watcher = new TextWatcher()
        {
            // afterTextChanged
            @Override
            public void afterTextChanged(Editable s) {}

            // beforeTextChanged
            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {}
            // onTextChanged
            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count)
            {
                TextView text = (TextView) getCurrentFocus();

                // Can't be sure if we got the right slot, but move
                // focus to the next one if there is a letter in the
                // slot
                if (text != null && text.length() > 0)
                {
                    View next = text.focusSearch(View.FOCUS_RIGHT);
                    if (next != null)
                        next.requestFocus();
                }
            }
        };

        ViewGroup greenRow = (ViewGroup) findViewById(R.id.green);
        greenArray = new TextView[greenRow.getChildCount()];
        for (int i = 0; i < greenArray.length; i++)
        {
            greenArray[i] = (TextView) greenRow.getChildAt(i);
            greenArray[i].setOnEditorActionListener(listener);
            greenArray[i].addTextChangedListener(watcher);
        }

        yellowArray = new TextView[ROWS.length][];
        for (int i = 0; i < yellowArray.length; i++)
        {
            ViewGroup yellowRow = findViewById(ROWS[i]);
            yellowArray[i] = new TextView[yellowRow.getChildCount()];
            for (int j = 0; j < yellowArray[i].length; j++)
            {
                yellowArray[i][j] = (TextView) yellowRow.getChildAt(j);
                yellowArray[i][j].setOnEditorActionListener(listener);
                yellowArray[i][j].addTextChangedListener(watcher);
            }
        }

        greyText = (TextView) findViewById(R.id.grey);
        greyText.setOnEditorActionListener(listener);

        greenList = new ArrayList<String>();
        yellowList = new ArrayList<List<String>>();
        for (int i = 0; i < ROWS.length; i++)
            yellowList.add(new ArrayList<String>());

        resultText = (TextView) findViewById(R.id.result);
    }

    // onPause
    @Override
    public void onPause()
    {
        super.onPause();

        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(PREF_THEME, theme);
        editor.putInt(PREF_LANG, language);
        editor.apply();
    }

    // On create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it
        // is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    // On options item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Get id
        int id = item.getItemId();
        switch (id)
        {
        case R.id.refresh:
            refresh();
            break;

        case R.id.solve:
            solve();
            break;

        case R.id.dark:
            theme(DARK);
            break;

        case R.id.cyan:
            theme(CYAN);
            break;

        case R.id.blue:
            theme(BLUE);
            break;

        case R.id.orange:
            theme(ORANGE);
            break;

        case R.id.purple:
            theme(PURPLE);
            break;

        case R.id.red:
            theme(RED);
            break;

        case R.id.english:
            setLanguage(ENGLISH);
            break;

        case R.id.italian:
            setLanguage(ITALIAN);
            break;

        case R.id.spanish:
            setLanguage(SPANISH);
            break;

        case R.id.catalan:
            setLanguage(CATALAN);
            break;

        case R.id.french:
           setLanguage(FRENCH);
           break;

       case R.id.portuguese:
           setLanguage(PORTUGUESE);
           break;

        case R.id.german:
           setLanguage(GERMAN);
           break;

        case R.id.dutch:
           setLanguage(DUTCH);
           break;

        case R.id.afrikaans:
           setLanguage(AFRIKAANS);
           break;

        case R.id.help:
            help();
            break;

        case R.id.about:
            about();
            break;
        }

        return true;
    }

    // refresh
    private void refresh()
    {
        for (TextView text: greenArray)
            text.setText("");

        for (TextView row[]: yellowArray)
            for (TextView text: row)
                text.setText("");
        greyText.setText("");
    }

    // solve
    private void solve()
    {
        InputMethodManager manager = (InputMethodManager)
            getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(resultText.getWindowToken(), 0);

        greenList.clear();
        for (TextView green: greenArray)
            greenList.add(green.getText().toString()
                          .toLowerCase(Locale.getDefault()));

        for (int i = 0; i < yellowArray.length; i++)
        {
            yellowList.get(i).clear();
            for (TextView yellow: yellowArray[i])
                yellowList.get(i).add(yellow.getText().toString()
                                      .toLowerCase(Locale.getDefault()));
        }

        String grey = greyText.getText().toString()
            .toLowerCase(Locale.getDefault());
        List<List<String>> result = new Solver(greenList,
                                               yellowList.get(0),
                                               yellowList.get(1),
                                               yellowList.get(2),
                                               grey).solve();
        if (BuildConfig.DEBUG)
            Log.d(TAG, "Result: " + result);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < result.size(); i++)
        {
            List w = result.get(i);
            for (int j = 0; j < w.size(); j++)
                builder.append(String.format("%s ", w.get(j))
                               .toUpperCase(Locale.getDefault()));

            builder.append("\n");
        }

        resultText.setText(builder);
    }

    // theme
    private void theme(int t)
    {
        theme = t;
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.M)
            recreate();
    }

    // setLanguage
    private void setLanguage(int l)
    {
        language = l;
        setLanguage();
        refresh();
    }

    // setLanguage
    private void setLanguage()
    {
        Words.setLanguage(this, language);
        Solver.Companion.emptyDicWords();

        switch (language)
        {
        default:
        case ENGLISH:
            getActionBar().setSubtitle(R.string.english);
            break;

        case ITALIAN:
            getActionBar().setSubtitle(R.string.italian);
            break;

        case SPANISH:
            getActionBar().setSubtitle(R.string.spanish);
            break;

        case CATALAN:
            getActionBar().setSubtitle(R.string.catalan);
            break;

        case FRENCH:
            getActionBar().setSubtitle(R.string.french);
            break;

        case PORTUGUESE:
            getActionBar().setSubtitle(R.string.portuguese);
            break;

        case GERMAN:
            getActionBar().setSubtitle(R.string.german);
            break;

        case DUTCH:
            getActionBar().setSubtitle(R.string.dutch);
            break;

        case AFRIKAANS:
            getActionBar().setSubtitle(R.string.afrikaans);
            break;
        }
    }

    // help
    private void help()
    {
        Intent intent = new Intent(this, Help.class);
        startActivity(intent);
    }

    // about
    @SuppressWarnings("deprecation")
    private void about()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.appName);
        builder.setIcon(R.drawable.ic_launcher);

        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        SpannableStringBuilder spannable =
            new SpannableStringBuilder(getText(R.string.version));
        Pattern pattern = Pattern.compile("%s");
        Matcher matcher = pattern.matcher(spannable);
        if (matcher.find())
            spannable.replace(matcher.start(), matcher.end(),
                              BuildConfig.VERSION_NAME);
        matcher.reset(spannable);
        if (matcher.find())
            spannable.replace(matcher.start(), matcher.end(),
                              dateFormat.format(BuildConfig.BUILT));
        builder.setMessage(spannable);

        // Add the button
        builder.setPositiveButton(android.R.string.ok, null);

        // Create the AlertDialog
        Dialog dialog = builder.show();

        // Set movement method
        TextView text = dialog.findViewById(android.R.id.message);
        if (text != null)
        {
            text.setTextAppearance(builder.getContext(),
                                   android.R.style.TextAppearance_Small);
            text.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
