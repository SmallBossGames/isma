package ru.nstu.isma.in.lisma.analysis.parser;

import error.IsmaError;
import error.IsmaErrorList;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;

import java.util.BitSet;

/**
 * Created by Bessonov Alex
 * Date: 04.01.14
 * Time: 22:01
 */
public class LismaErrorListener implements ANTLRErrorListener {
    private IsmaErrorList errors;

    public LismaErrorListener(IsmaErrorList errors) {
        this.errors = errors;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, @Nullable Object o, int i, int i2, String s, @Nullable RecognitionException e) {
        IsmaError err = new IsmaError(i, i2, s);
        err.setType(IsmaError.Type.SYNTAX);
        errors.add(err);
    }

    @Override
    public void reportAmbiguity(@NotNull Parser parser, @NotNull DFA dfa, int i, int i2, boolean b, @NotNull BitSet bitSet, @NotNull ATNConfigSet atnConfigs) {
    }

    @Override
    public void reportAttemptingFullContext(@NotNull Parser parser, @NotNull DFA dfa, int i, int i2, @Nullable BitSet bitSet, @NotNull ATNConfigSet atnConfigs) {
    }

    @Override
    public void reportContextSensitivity(@NotNull Parser parser, @NotNull DFA dfa, int i, int i2, int i3, @NotNull ATNConfigSet atnConfigs) {
    }
}
