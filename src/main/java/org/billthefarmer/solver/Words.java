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

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class Words
{
    public static final String TAG = "Words";

    public static final String WORDS_FILE      = "Words.txt";
    public static final String ENGLISH_FILE    = "English.txt";
    public static final String ITALIAN_FILE    = "Italian.txt";
    public static final String SPANISH_FILE    = "Spanish.txt";
    public static final String CATALAN_FILE    = "Catalan.txt";
    public static final String FRENCH_FILE     = "French.txt";
    public static final String PORTUGUESE_FILE = "Portuguese.txt";
    public static final String GERMAN_FILE     = "German.txt";
    public static final String DUTCH_FILE      = "Dutch.txt";
    public static final String AFRIKAANS_FILE  = "Afrikaans.txt";
    public static final String HUNGARIAN_FILE  = "Hungarian.txt";
    public static final String GREEK_FILE      = "Greek.txt";
    public static final String SWEDISH_FILE    = "Swedish.txt";

    private static List<String> words;

    private Words() {}

    public static List<String> getWords()
    {
        return words;
    }

    public static void setLanguage(Context context, int l)
    {
        words = new ArrayList<String>();

        switch (l)
        {
        case Main.ENGLISH:
            readWords(context, WORDS_FILE, words);
            break;

        case Main.ITALIAN:
            readWords(context, ITALIAN_FILE, words);
            break;

        case Main.SPANISH:
            readWords(context, SPANISH_FILE, words);
            break;

        case Main.CATALAN:
            readWords(context, CATALAN_FILE, words);
            break;

        case Main.FRENCH:
            readWords(context, FRENCH_FILE, words);
            break;

        case Main.PORTUGUESE:
            readWords(context, PORTUGUESE_FILE, words);
            break;

        case Main.GERMAN:
            readWords(context, GERMAN_FILE, words);
            break;

        case Main.DUTCH:
            readWords(context, DUTCH_FILE, words);
            break;

        case Main.AFRIKAANS:
            readWords(context, AFRIKAANS_FILE, words);
            break;

        case Main.HUNGARIAN:
            readWords(context, HUNGARIAN_FILE, words);
            break;

        case Main.GREEK:
            readWords(context, GREEK_FILE, words);
            break;

        case Main.SWEDISH:
            readWords(context, SWEDISH_FILE, words);
            break;
        }
    }

    // readWords
    private static void readWords(Context context, String file,
                                  Collection<String> collection)
    {
        try (BufferedReader reader = new BufferedReader
             (new InputStreamReader(context.getAssets().open(file))))
        {
            String line;
            while ((line = reader.readLine()) != null)
                collection.add(line);
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
