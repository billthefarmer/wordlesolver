# ![Icon](src/main/res/drawable-xhdpi/ic_launcher.png) WordleSolver [![.github/workflows/build.yml](https://github.com/billthefarmer/wordlesolver/actions/workflows/build.yml/badge.svg)](https://github.com/billthefarmer/wordlesolver/actions/workflows/build.yml)
[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.svg" alt="Get it on F-Droid" height="80">](https://f-droid.org/packages/org.billthefarmer.solver/)

[Wordle](https://www.powerlanguage.co.uk/wordle/)
([Gurgle](https://github.com/billthefarmer/gurgle)) solver with code
from https://github.com/PoorLazyCoder/Wordle-Solver.

See https://github.com/PoorLazyCoder/Wordle-Solver for explanation.

![Gurgle](https://github.com/billthefarmer/billthefarmer.github.io/raw/master/images/Gurgle-solver.png) ![Solver](https://github.com/billthefarmer/billthefarmer.github.io/raw/master/images/WordleSolver.png)

 * Multiple coloured themes
 * Multiple languages

Usually solves [Wordle](href="https://www.powerlanguage.co.uk/wordle)
and [Gurgle](https://github.com/billthefarmer/gurgle) puzzles in four
tries.

Uses solver code from
[Wordle-Solver](https://github.com/PoorLazyCoder/Wordle-Solver).

## Using
Letters that are shown green in the game
([Wordle](https://www.powerlanguage.co.uk/wordle),
[Gurgle](https://github.com/billthefarmer/gurgle)) go in the first
green row in the correct place. Yellow letters go in the next yellow
rows in the correct place. Letters shown grey go in the last row in
any order.

Tap either the next button on the keyboard or the solve button in the
toolbar. The next button only works if there is text where the cursor
is, so it can be used to move to the next slot without triggering a
search. The app will show a scrolling list of possible next guesses at
the bottom.

Select coloured theme from the **Theme** item in the menu.

Select language from the **Language** item in the menu.

## External control
Share text from another app, or send an
[Intent](https://developer.android.com/reference/android/content/Intent)
with an
[EXTRA_TEXT](https://developer.android.com/reference/android/content/Intent#EXTRA_TEXT)
with the text containing the known letters and blanks or dots for the
unknown letters. The app will start or restart and display the
results.

| Parameter | Activity/Action/Category/Extra | Type | Value |
| --------- | ------------------------------ | ---- | ----- |
| Activity | org.billthefarmer.solver.Main |
| Action | android.intent.action.MAIN |
| | android.intent.action.DEFAULT |
| | android.intent.action.SEND |
| | android.intent.action.VIEW |
| Category | android.intent.category.LAUNCHER |
| | android.intent.category.DEFAULT |
| Extras | android.intent.extra.TEXT | string | The text may contain |
| | | | blanks or dots for |
| | | | unknown letters, a |
| | | | comma delimiter and |
| | | | letters known to be |
| | | | in results, repeated |
| | | | three times, a comma |
| | | | delimiter and letters |
| | | | known to not be in |
| | | | results |

This may be tested using the [Android Debug
Bridge](https://developer.android.com/studio/command-line/adb#am).
```shell
$ adb shell am start -e android.intent.extra.TEXT b.ll,..g,...x,,pb  -n org.billthefarmer.solver/.Main
Starting: Intent { cmp=org.billthefarmer.solver/.Main (has extras) }
Warning: Activity not started, its current task has been brought to the front
```
