package ru.nstu.isma.in.lisma;

import error.IsmaErrorList;
import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.in.InputTranslator;
import ru.nstu.isma.in.lisma.analysis.gen.LismaLexer;
import ru.nstu.isma.in.lisma.analysis.gen.LismaParser;
import ru.nstu.isma.in.lisma.analysis.ASTTreeTranslator;
import ru.nstu.isma.in.lisma.analysis.parser.LismaErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * Created by Bessonov Alex
 * Date: 05.12.13
 * Time: 13:11
 */
public class LismaTranslator implements InputTranslator {

    private final String text;

    private final IsmaErrorList errors;

    public LismaTranslator(String text, IsmaErrorList errors) {
        this.text = text;
        this.errors = errors;
    }

    @Override
    public HSM translate() {
        HSM model;

        //макросы
        // объект входной текстовой модели
        CharStream cs = new ANTLRInputStream(text);
        // лексер
        LismaLexer lexer = new LismaLexer(cs);
        // разбираем в поток токенов
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // парсим в синтаксическое дерево
        LismaParser parser = new LismaParser(tokens);
        LismaErrorListener errorListener = new LismaErrorListener(errors);
        parser.addErrorListener(errorListener);
        LismaParser.LismaContext tree = parser.lisma();
        // преобразователь из дерева в объектную модель HMS
        ASTTreeTranslator ast2HSM = new ASTTreeTranslator(tree, errors);
        model = ast2HSM.parse();

        return model;
    }
}
