package ru.nstu.isma.lisma.parser

import ru.nstu.isma.core.hsm.models.IsmaError
import org.antlr.v4.runtime.ANTLRErrorListener
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.atn.ATNConfigSet
import ru.nstu.isma.core.hsm.models.IsmaSyntaxError
import java.util.*

/**
 * Created by Bessonov Alex
 * Date: 04.01.14
 * Time: 22:01
 */
class LismaErrorListener(private val errors: MutableList<IsmaError>) : ANTLRErrorListener {
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        o: Any,
        i: Int,
        i2: Int,
        s: String,
        e: RecognitionException
    ) {
        val err = IsmaSyntaxError(i, i2, s)
        errors.add(err)
    }

    override fun reportAmbiguity(
        parser: Parser,
        dfa: DFA,
        i: Int,
        i2: Int,
        b: Boolean,
        bitSet: BitSet,
        atnConfigs: ATNConfigSet
    ) {
    }

    override fun reportAttemptingFullContext(
        parser: Parser,
        dfa: DFA,
        i: Int,
        i2: Int,
        bitSet: BitSet,
        atnConfigs: ATNConfigSet
    ) {
    }

    override fun reportContextSensitivity(
        parser: Parser,
        dfa: DFA,
        i: Int,
        i2: Int,
        i3: Int,
        atnConfigs: ATNConfigSet
    ) {
    }
}