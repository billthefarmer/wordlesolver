import org.billthefarmer.wordlesolver.Solver
import org.billthefarmer.wordlesolver.joinListColumns

fun main(args: Array<String>) {


    var green = listOf("", "", "", "", "") //  is correct and in the correct position
    var yellow1 = listOf("", "", "", "", "") // answer but not in the right position
    var yellow2 = listOf("", "", "", "", "")
    var yellow3 = listOf("", "", "", "", "")

    var grey = ""   //  it is not in the answer at all


    val matchWords = Solver(green, yellow1,yellow2,yellow3, grey).solve()

    println(joinListColumns(matchWords,20))

    println("\nTotal: ${matchWords.size}\n----------end------------")
}



