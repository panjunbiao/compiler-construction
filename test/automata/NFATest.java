package automata;

import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 7/10/13
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class NFATest {
    @Test
    public void testGetStartState() throws Exception {

    }

    @Test
    public void testGetAcceptingStates() throws Exception {

    }

    @Test
    public void testAddAcceptingState() throws Exception {

    }

    @Test
    public void testGetStateSet() throws Exception {

    }

    @Test
    public void testGetStateList() throws Exception {

    }

    @Test
    public void testGetEpsilonClosure() throws Exception {

    }

    @Test
    public void testMove() throws Exception {

    }

    @Test
    public void testGetImportantStates() throws Exception {

    }

    @Test
    public void testToDFA() throws Exception {
        NFA nfa;
        NFAState[] s;

        s = NFAStateFactory.newInstances(4);
        s[0].addTransit(s[0]);
        s[0].addTransit(s[1]);

        s[1].addTransit(s[0]);
        s[1].addTransit(s[1]);
        s[1].addTransit(s[2]);

        s[2].addTransit(s[0]);
        s[2].addTransit(s[1]);
        s[2].addTransit(s[2]);
        s[2].addTransit(s[3]);

        s[3].addTransit(s[0]);
        s[3].addTransit(s[1]);
        s[3].addTransit(s[2]);
        s[3].addTransit(s[3]);

        nfa = new NFA(s[0], s[3]);

        DFA dfa = nfa.toDFA();

        dfa.getStart().printToDot();
        System.out.println(dfa.getStateSet().size());

        s = NFAStateFactory.newInstances(2);
        s[0].addTransit(1, s[1]);
        nfa = new NFA(s[0], s[1]);
        dfa = nfa.toDFA();
        dfa.setName("TestCase 0002");
        dfa.printToDot();

        s = NFAStateFactory.newInstances(2);
        s[0].addTransit(s[1]);
        s[0].addTransit(1, s[1]);
        nfa = new NFA(s[0], s[1]);
        dfa = nfa.toDFA();
        dfa.setName("TestCase 0003");
        dfa.printToDot();

        s = NFAStateFactory.newInstances(2);
        s[0].addTransit(1, s[1]);
        s[1].addTransit(2, s[1]);
        nfa = new NFA(s[0], s[1]);
        dfa = nfa.toDFA();
        dfa.setName("TestCase 0004");
        dfa.printToDot();

        s = NFAStateFactory.newInstances(2);
        s[0].addTransit(1, s[0]);
        s[0].addTransit(2, s[1]);
        s[1].addTransit(3, s[1]);
        nfa = new NFA(s[0], s[1]);
        dfa = nfa.toDFA();
        dfa.setName("TestCase 0005");
        dfa.printToDot();

        s = NFAStateFactory.newInstances(2);
        s[0].addTransit(s[1]);
        s[0].addTransit(1, s[0]);
        s[0].addTransit(2, s[1]);
        s[1].addTransit(3, s[1]);
        nfa = new NFA(s[0], s[1]);
        dfa = nfa.toDFA();
        dfa.setName("TestCase 0005");
        dfa.printToDot();

        s = NFAStateFactory.newInstances(2);
        s[0].addTransit(s[1]);
        s[0].addTransit(1, s[0]);
        s[0].addTransit(2, s[1]);
        s[1].addTransit(3, s[1]);
        s[1].addTransit(s[0]);
        nfa = new NFA(s[0], s[1]);
        dfa = nfa.toDFA();
        dfa.setName("TestCase 0005");
        dfa.printToDot();

    }
}
