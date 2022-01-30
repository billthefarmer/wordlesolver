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

package org.billthefarmer.wordlesolver;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Main extends Activity
{
    public static final String TAG = "WordleSolver";
    public static final int ROWS[] =
    {
        R.id.yellow1,  R.id.yellow2,  R.id.yellow3
    };

    private TextView greyText;
    private TextView resultText;
    private TextView greenArray[];
    private TextView yellowArray[][];
    private List<String> greenList;
    private List<List<String>> yellowList;

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

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
        greenArray = new TextView[greenRow.getChildCount() - 1];
        for (int i = 0; i < greenArray.length; i++)
        {
            greenArray[i] = (TextView) greenRow.getChildAt(i + 1);
            greenArray[i].setOnEditorActionListener(listener);
            greenArray[i].addTextChangedListener(watcher);
        }

        yellowArray = new TextView[ROWS.length][];
        for (int i = 0; i < yellowArray.length; i++)
        {
            ViewGroup yellowRow = findViewById(ROWS[i]);
            yellowArray[i] = new TextView[yellowRow.getChildCount() - 1];
            for (int j = 0; j < yellowArray[i].length; j++)
            {
                yellowArray[i][j] = (TextView) yellowRow.getChildAt(j + 1);
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

    private void solve()
    {
        greenList.clear();
        for (TextView green: greenArray)
            greenList.add(green.getText().toString());

        for (int i = 0; i < yellowArray.length; i++)
        {
            yellowList.get(i).clear();
            for (TextView yellow: yellowArray[i])
                yellowList.get(i).add(yellow.getText().toString());
        }

        String grey = greyText.getText().toString();
        List<List<String>> result = new Solver(greenList,
                                               yellowList.get(0),
                                               yellowList.get(1),
                                               yellowList.get(2),
                                               grey).solve();
        Log.d(TAG, "Result: " + result);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < result.size(); i++)
        {
            builder.append(result.get(i).toString());
            builder.append("\n");
        }

        resultText.setText(builder);
    }
}
