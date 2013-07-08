package abnf;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import abnf.*;
import automata.*;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 6/17/13
 * Time: 8:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class NFAUtil {
    public static int getSubscript(NFAState[] states, NFAState state) {
        for(int index = 0; index < states.length; index ++) {
            if (states[index].equals(state)) return index;
        }
        return -1;
    }
}
