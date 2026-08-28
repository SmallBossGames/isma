package ru.isma.next.app.services.blueprint

import org.junit.jupiter.api.Test
import ru.isma.next.editor.blueprint.models.BlueprintLoopTransactionModel
import ru.isma.next.editor.blueprint.models.BlueprintModel
import ru.isma.next.editor.blueprint.models.BlueprintStateModel
import ru.isma.next.editor.blueprint.models.BlueprintTransactionModel

class LismaCodegenTest {

    private fun state(name: String, text: String = "") = BlueprintStateModel(0.0, 0.0, name, text)

    private fun tx(start: String, end: String, predicate: String = "") =
        BlueprintTransactionModel(start, end, predicate, "")

    @Test
    fun `state blocks are generated with correct line regions`() {
        val model = BlueprintModel(
            main = state("Main", "main body"),
            init = state("init"),
            states = arrayOf(state("A", "a body"), state("B", "b body")),
            transactions = arrayOf(tx("Main", "A"), tx("Main", "B", "x > 1")),
        )

        val result = model.toLismaText()

        val expected = """
            main body
            state A (1 > 0) {
            a body
            } from Main;

            state B (x > 1) {
            b body
            } from Main;

        """.trimIndent() + "\n"

        assert(result.fullText == expected) {
            "Unexpected LISMA text:\n${result.fullText.lineNumbered()}"
        }

        assert(result.regions == listOf(
            ru.isma.next.app.models.CodeRegion("A", 2, 4),
            ru.isma.next.app.models.CodeRegion("B", 6, 8),
        )) { "Unexpected regions: ${result.regions}" }
    }

    @Test
    fun `transactions to the same state with the same predicate are merged`() {
        val model = BlueprintModel(
            main = state("Main"),
            init = state("init"),
            states = arrayOf(state("A", "a body")),
            transactions = arrayOf(tx("Main", "A"), tx("init", "A")),
        )

        val result = model.toLismaText()

        assert(result.fullText.contains("} from Main,init;")) {
            "Expected merged input states:\n${result.fullText.lineNumbered()}"
        }
        assert(result.regions.size == 1) { "Expected 1 region, got ${result.regions}" }
    }

    @Test
    fun `state blocks appear in first-transaction order`() {
        val model = BlueprintModel(
            main = state("Main"),
            init = state("init"),
            states = arrayOf(state("A"), state("B")),
            transactions = arrayOf(tx("Main", "B"), tx("Main", "A")),
        )

        val result = model.toLismaText()

        val bIndex = result.fullText.indexOf("state B (")
        val aIndex = result.fullText.indexOf("state A (")

        assert(bIndex in 0 until aIndex) {
            "Expected B before A:\n${result.fullText.lineNumbered()}"
        }
    }

    @Test
    fun `loop transaction generates pseudo state and region`() {
        val model = BlueprintModel(
            main = state("Main", "main body"),
            init = state("init"),
            states = arrayOf(state("A", "a body")),
            transactions = arrayOf(tx("Main", "A")),
            loopTransactions = arrayOf(BlueprintLoopTransactionModel("A", "p > 0", "", "loop body")),
        )

        val result = model.toLismaText()

        val expected = """
            main body
            state A (1 > 0) {
            a body
            } from Main;

            state A_pseudo_1 (p > 0) {
            loop body
            } from A;

            state A (1 > 0) {
            a body
            } from A_pseudo_1;




        """.trimIndent() + "\n"

        assert(result.fullText == expected) {
            "Unexpected LISMA text:\n${result.fullText.lineNumbered()}"
        }

        assert(result.regions == listOf(
            ru.isma.next.app.models.CodeRegion("A", 2, 4),
            ru.isma.next.app.models.CodeRegion("A", 6, 13),
        )) { "Unexpected regions: ${result.regions}" }
    }

    @Test
    fun `fragmentNameByLine maps lines to owning fragment`() {
        val model = BlueprintModel(
            main = state("Main", "main body"),
            init = state("init"),
            states = arrayOf(state("A", "a body"), state("B", "b body")),
            transactions = arrayOf(tx("Main", "A"), tx("Main", "B", "x > 1")),
        )

        val result = model.toLismaText()

        assert(result.fragmentNameByLine(2) == "A")
        assert(result.fragmentNameByLine(4) == "A")
        assert(result.fragmentNameByLine(6) == "B")
        assert(result.fragmentNameByLine(8) == "B")
        assert(result.fragmentNameByLine(1) == ru.isma.next.app.models.LismaTextModel.DefaultFragment.name)
        assert(result.fragmentNameByLine(5) == ru.isma.next.app.models.LismaTextModel.DefaultFragment.name)
        assert(result.fragmentNameByLine(99) == ru.isma.next.app.models.LismaTextModel.DefaultFragment.name)
    }

    @Test
    fun `empty model produces only main text`() {
        val result = BlueprintModel.empty.toLismaText()

        assert(result.fullText == "\n") { "Unexpected text: '${result.fullText}'" }
        assert(result.regions.isEmpty()) { "Expected no regions, got ${result.regions}" }
    }

    private fun String.lineNumbered(): String =
        lineSequence().withIndex().joinToString("\n") { (index, line) -> "${index + 1}: $line" }
}
