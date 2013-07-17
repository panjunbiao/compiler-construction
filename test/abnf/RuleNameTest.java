package abnf;

import org.junit.Test;
import automata.NFA;
import automata.NFAState;
import automata.NFAStateFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 6/18/13
 * Time: 11:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class RuleNameTest {
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

//        s = NFAStateFactory.newInstances(2);
//        s[0].addTransit("aBc123", s[1]);
//        expected = new NFA(s[0], s[1]);
//
//        System.out.println("====================");
//        nfa = tester.test(AbnfParserFactory.newInstance("aBc123"));
//        Assertion.assertEquivalent(expected, nfa);

        Tester<NFA> ruleListTester = new Tester<NFA>() {
            @Override
            public NFA test(AbnfParser parser) throws MatchException, IOException, CollisionException, IllegalAbnfException {
                List<Rule> rules = parser.parse();
                Map<String, Rule> ruleMap = new HashMap<String, Rule>();
                for(int index = 0; index < rules.size(); index ++) {
                    ruleMap.put(rules.get(index).getRuleName().toString(), rules.get(index));
                }
                return ruleMap.get("S0").getElements().toNFA(ruleMap);
            }
        };

        nfa = ruleListTester.test(AbnfParserFactory.newInstance(
                "S0=B/C" + (char)0x0D + (char)0x0A +
                        "B=%xbb" + (char)0x0D + (char)0x0A +
                        "C=[D E]" + (char)0x0D + (char)0x0A +
                        "D=%xdd" + (char)0x0D + (char)0x0A +
                        "E=%xee" + (char)0x0D + (char)0x0A
        ));
        s = NFAStateFactory.newInstances(3);
        s[0].addTransit(0xbb, s[1]);
        s[0].addTransit(s[1]);
        s[0].addTransit(0xdd, s[2]).addTransit(0xee, s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);
    }
}
