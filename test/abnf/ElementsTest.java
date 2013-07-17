package abnf;

import org.junit.Test;
import automata.NFA;
import automata.NFAState;
import automata.NFAStateFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 6/18/13
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class ElementsTest {
    @Test
    public void testToNFA() throws Exception {
        Tester<NFA> tester = new Tester<NFA>() {
            @Override
            public NFA test(AbnfParser parser) throws MatchException, IOException, CollisionException, IllegalAbnfException {
                Elements elements = parser.elements();
                return elements.toNFA(new HashMap<String, Rule>());
            }
        };

        NFA nfa;
        NFAState state;
        NFA expected;
        NFAState[] s;

        s = NFAStateFactory.newInstances(2);
        s[0].addTransit(s[1]);
        s[0].addTransit(0x11, s[1]);
        s[0].addTransit(0x22, s[1]);
        expected = new NFA(s[0], s[1]);

        System.out.println("====================");
        nfa = tester.test(AbnfParserFactory.newInstance("[%x11/%x22]"));
        Assertion.assertEquivalent(expected, nfa);

    }
}
