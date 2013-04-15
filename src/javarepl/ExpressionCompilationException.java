package javarepl;

import com.googlecode.totallylazy.Function1;

import javax.tools.Diagnostic;
import java.io.File;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.lines;
import static java.lang.String.format;

public class ExpressionCompilationException extends Exception {

    ExpressionCompilationException(File file, Iterable<? extends Diagnostic> diagnostics) {
        super(diagnosticsAsMessage(file, diagnostics));
    }

    private static String diagnosticsAsMessage(final File file, final Iterable<? extends Diagnostic> diagnostics) {
        return sequence(diagnostics).map(new Function1<Diagnostic, String>() {
            @Override
            public String call(Diagnostic diagnostic) throws Exception {
                String line = lines(file).drop((int) diagnostic.getLineNumber() - 1).head();
                String marker = repeat(' ').take((int) diagnostic.getColumnNumber() - 1).toString("", "", "^");
                String message = sequence(diagnostic.toString().split("\n")[0].split(":")).drop(2).toString(":").trim();
                return format("%s: %s\n%s\n%s", diagnostic.getKind(), message, line, marker);
            }
        }).toString("\n");
    }
}
