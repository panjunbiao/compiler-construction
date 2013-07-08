package abnf;

import junit.framework.Assert;
import org.junit.Test;
import abnf.*;
import automata.NFA;
import automata.NFAState;
import automata.NFATransitType;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 6/14/13
 * Time: 10:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class NumValTest {

    //		        num-val        =  "%" (bin-val / dec-val / hex-val)
    @Test
    public void testToNFA() throws MatchException, IOException, CollisionException, IllegalAbnfException {
        Tester<NFA> tester = new Tester<NFA>() {
            @Override
            public NFA test(AbnfParser parser) throws MatchException, IOException, CollisionException, IllegalAbnfException {
                Element numVal = parser.num_val();
                return numVal.toNFA(new HashMap<String, Rule>());
            }
        };

        NFA nfa;
        NFAState state;

        nfa = tester.test(AbnfParserFactory.newInstance("%x00.11.eE.Ff"));

        state = nfa.getStartState();
        Assert.assertEquals(1, state.getTransits().size());
        Assert.assertEquals(NFATransitType.BYTE, state.getTransits().get(0).getTransitType());
        Assert.assertEquals((byte)0x00, state.getTransits().get(0).getInput());

        state = state.getTransits().get(0).getNext();
        Assert.assertEquals(1, state.getTransits().size());
        Assert.assertEquals(NFATransitType.BYTE, state.getTransits().get(0).getTransitType());
        Assert.assertEquals((byte)0x11, state.getTransits().get(0).getInput());

        state = state.getTransits().get(0).getNext();
        Assert.assertEquals(1, state.getTransits().size());
        Assert.assertEquals(NFATransitType.BYTE, state.getTransits().get(0).getTransitType());
        Assert.assertEquals((byte)0xee, state.getTransits().get(0).getInput());

        state = state.getTransits().get(0).getNext();
        Assert.assertEquals(1, state.getTransits().size());
        Assert.assertEquals(NFATransitType.BYTE, state.getTransits().get(0).getTransitType());
        Assert.assertEquals(0xff, state.getTransits().get(0).getInput());

        state = state.getTransits().get(0).getNext();
        Assert.assertTrue(nfa.getAcceptingStates().contains(state));

        nfa = tester.test(AbnfParserFactory.newInstance("%d56"));

        state = nfa.getStartState();
        Assert.assertEquals(1, state.getTransits().size());
        Assert.assertEquals(NFATransitType.BYTE, state.getTransits().get(0).getTransitType());
        Assert.assertEquals(56, state.getTransits().get(0).getInput());

        state = state.getTransits().get(0).getNext();
        Assert.assertTrue(nfa.getAcceptingStates().contains(state));

        nfa = tester.test(AbnfParserFactory.newInstance("%b00.11.1111"));

        state = nfa.getStartState();
        Assert.assertEquals(1, state.getTransits().size());
        Assert.assertEquals(NFATransitType.BYTE, state.getTransits().get(0).getTransitType());
        Assert.assertEquals((byte)0x00, state.getTransits().get(0).getInput());

        state = state.getTransits().get(0).getNext();
        Assert.assertEquals(1, state.getTransits().size());
        Assert.assertEquals(NFATransitType.BYTE, state.getTransits().get(0).getTransitType());
        Assert.assertEquals(0x03, state.getTransits().get(0).getInput());

        state = state.getTransits().get(0).getNext();
        Assert.assertEquals(1, state.getTransits().size());
        Assert.assertEquals(NFATransitType.BYTE, state.getTransits().get(0).getTransitType());
        Assert.assertEquals(0x0f, state.getTransits().get(0).getInput());

        state = state.getTransits().get(0).getNext();
        Assert.assertTrue(nfa.getAcceptingStates().contains(state));
    }
}
