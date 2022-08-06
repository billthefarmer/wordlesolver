
package org.billthefarmer.solver

class Solver(private var g: List<String>,
             var y1: List<String>, var y2: List<String>,
             var y3: List<String>, private var gray: String)
{
    // g green:  is correct and in the correct position
    // y yellow: is in answer but not in the right position
    // gray:     is not in the answer at all

    fun solve(): List<List<String>> {

        if (g.joinToString("").trim().isEmpty()) g = listOf()
        if (y1.joinToString("").trim().isEmpty()) y1 = listOf()
        if (y2.joinToString("").trim().isEmpty()) y2 = listOf()
        if (y3.joinToString("").trim().isEmpty()) y3 = listOf()

        gray = gray.trim()

        var words = getAllDicWords().toList()

        if (g.isNotEmpty()) {
            words = words.filter { w ->

                (w[0] == g[0] || g[0] == "") &&
                        (w[1] == g[1] || g[1] == "") &&
                        (w[2] == g[2] || g[2] == "") &&
                        (w[3] == g[3] || g[3] == "") &&
                        (w[4] == g[4] || g[4] == "")
            }
        }

        if (y1.isNotEmpty()) {
            words = filterOutYellow(y1, words)
        }
        if (y2.isNotEmpty()) {
            words = filterOutYellow(y2, words)
        }
        if (y3.isNotEmpty()) {
            words = filterOutYellow(y3, words)
        }

        // letters remove from gray
        @Suppress("UNUSED_VARIABLE")
        var removeWs = mutableListOf<String>().apply {
            addAll(g)
            addAll(y1)
            addAll(y2)
            addAll(y3)
            forEach { gray = gray.replace(it, "") }
        }

        if (gray.isNotEmpty()) {
            words = words.filter { w ->
                w.none { gray.contains(it) }
            }
        }

        return words
    }

    fun filterOutYellow(y: List<String>, words: List<List<String>>) = run {
        words.filter { w ->
            ((w[0] != y[0]) &&
                    (w[1] != y[1]) &&
                    (w[2] != y[2]) &&
                    (w[3] != y[3]) &&
                    (w[4] != y[4]))
        }.filter { w ->
            val wordSt: String = w.joinToString("")
            y.all {
                wordSt.contains(it)
            }
        }
    }

    fun emptyDicWords() {

        clearDicWords()
    }

    companion object {

        private var dicWords: List<List<String>> = listOf()

        private fun getAllDicWords(): List<List<String>> {

            if (dicWords.isEmpty()) {

                var tempDicWords = mutableListOf<List<String>>()
                var tempWords = mutableListOf<String>()
                tempWords.addAll(Words.getWords())

                tempWords.sort()

                tempWords.forEach {
                    val w = it.split("").toMutableList()
                    w.removeFirst()
                    w.removeLast()
                    tempDicWords.add(w)
                }

                dicWords = tempDicWords.toList()
            }

            return dicWords
        }

        private fun clearDicWords() {

            dicWords = listOf()
        }
    }
}

fun joinListColumns(words: List<List<String>>, numOfColumn: Int): String {
    var sb: StringBuffer = StringBuffer()
    var counter = 1

    words.forEach {
        sb.append(it.joinToString("") + " ")
        if (counter++ % numOfColumn == 0)
            sb.append("\n")
    }

    return sb.toString()
}

