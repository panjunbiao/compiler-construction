/*
    This file is one of the component a Context-free Grammar Parser Generator,
    which accept a piece of text as the input, and generates a parser
    for the inputted context-free grammar.
    Copyright (C) 2013, Junbiao Pan (Email: panjunbiao@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package generator;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 5/21/13
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */

import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import abnf.Rule;
import abnf.RuleName;
import abnf.AbnfParser;
import analyzer.*;
//import analyzer.RegularAnalyzer;
//import automata.DFA;
import automata.DFA;
import automata.NFA;
import automata.NFAState;
//import automata.NFAUtils;

public class JavaParser {
    public boolean checkUndefinedSymbol(List<Rule> ruleList) {
        DefinitionAnalyzer analyzer = new DefinitionAnalyzer(ruleList);

        System.out.println("==========================Undefined Rule Names Start==========================");
        System.out.println("Undefined rule numbers: " + analyzer.getUndefinedRuleNames().size());
        Iterator<String> it = analyzer.getUndefinedRuleNames().iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        System.out.println("==========================Undefined Rule Names End==========================");
        return !analyzer.getUndefinedRuleNames().isEmpty();
    }
//
//    public NFA generateNFA(String ruleName, List<Rule> ruleList) throws Exception {
//        Map<String, Rule> rules = new HashMap<String, Rule>();
//        for(int index = 0; index < ruleList.size(); index ++) {
//            rules.put(ruleList.get(index).getRuleName().toString(), ruleList.get(index));
//        }
//        return rules.get(ruleName).toNFA(rules);
//    }
//
    public boolean checkRegularExpression(List<Rule> ruleList) {
        RegularAnalyzer analyzer = new RegularAnalyzer(ruleList);
        System.out.println("=====================Regular Expressions Begin=====================");
        for(int index = 0; index < analyzer.getRegularRules().size(); index ++) {
            System.out.println(analyzer.getRegularRules().get(index).toString());
        }
        System.out.println("=====================Regular Expressions End=====================");
        System.out.println("=====================Nonregular Expressions Begin=====================");
        for(int index = 0; index < analyzer.getNonRegularRules().size(); index ++) {
            System.out.println(analyzer.getNonRegularRules().get(index).toString());
        }
        System.out.println("=====================Nonregular Expressions End=====================");
//        System.out.println("=====================Undefined Expressions Begin=====================");
//        for(int index = 0; index < analyzer.getUndefinedRules().size(); index ++) {
//            System.out.println(analyzer.getUndefinedRules().get(index).toString());
//        }
//        System.out.println("=====================Undefined Expressions End=====================");
        return analyzer.getNonRegularRules().isEmpty();// && analyzer.getUndefinedRules().isEmpty();
    }

    private NFA generateNFA(String ruleName, List<Rule> regularRuleList) throws Exception {
        Map<String, Rule> rules = new HashMap<String, Rule>();
        for(int index = 0; index < regularRuleList.size(); index ++) {
            rules.put(regularRuleList.get(index).getRuleName().toString(), regularRuleList.get(index));
        }
        NFAState startState = new NFAState();
        NFAState acceptingState = new NFAState();
        rules.get(ruleName).getElements().toNFA(startState, acceptingState, rules);
        return new NFA(startState, acceptingState);
    }

    public static void main(String[] args) throws IOException, Exception {
        JavaParser javaParser = new JavaParser();

        String prefix = "";
        if (args.length > 1) prefix = args[1];
        AbnfParser abnfParser = new AbnfParser(prefix, System.in);
        List<Rule> ruleList = abnfParser.parse();
        System.out.println("RuleList size = " + ruleList.size());
        for(int index = 0; index < ruleList.size(); index ++) {
            System.out.println(ruleList.get(index).toString());
        }
        if (javaParser.checkUndefinedSymbol(ruleList)) {
            System.out.println("Error: There are undefined rule names.");
        }

        if (!javaParser.checkRegularExpression(ruleList)) {
            System.out.println("Error: There are non-regular expression.");
        }

        RegularAnalyzer regularAnalyzer = new RegularAnalyzer(ruleList);
        List<Rule> regularRuleList = regularAnalyzer.getRegularRules();

        DependanceClosure dependanceClosure = new DependanceClosure(ruleList, "RFC3261-SIP-message");
        System.out.println("RFC3261-SIP-message depends " +
                dependanceClosure.getRuleList().size() + " rules.");
        System.out.println("=============Rule Closure Begin=================");
        for(int index = 0; index < dependanceClosure.getRuleList().size(); index ++) {
            System.out.println(dependanceClosure.getRuleList().get(index).toString());
        }
        System.out.println("=============Rule Closure End=================");

//        for(int index = 334; index < regularRuleList.size(); index ++) {
//            if ("RFC3261-To".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-rplyto-spec".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-Reply-To".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-Record-Route".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-challenge".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-Proxy-Authenticate".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-from-spec".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-From".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-contact-param".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-Contact".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-WWW-Authenticate".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-Route".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-message-header".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-Request".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-Response".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            if ("RFC3261-SIP-message".equals(regularRuleList.get(index).getRuleName().toString())) continue;
//            System.out.print("Regular Rule List[" + index + "]");
//
//            System.out.print("Converting NFA " + regularRuleList.get(index).getRuleName().toString());
//            NFA nfa = javaParser.generateNFA(regularRuleList.get(index).getRuleName().toString(), regularRuleList);
//            System.out.println(" OK");
//            System.out.println("Total states = " + nfa.getStateSet().size());
//            System.out.println("Converting DFA " + regularRuleList.get(index).getRuleName().toString());
//            DFA dfa = nfa.toDFA();
//            System.out.print(", NFA States = " + String.format("%6d", nfa.getStateSet().size()) + ", DFA States = " + String.format("%6d", dfa.getStateSet().size()));
//            System.out.println(", " + regularRuleList.get(index).toString());
//            dfa.setName(regularRuleList.get(index).getRuleName().toString());
//            if (dfa.getStateSet().size() < 100) dfa.printToDot();
////            dfa.printToDot();
////            dfa.getStart().printToDot();
//        }
//        NFA nfa = javaParser.generateNFA("RFC3261-SIP-message", regularRuleList);
//        NFA nfa = javaParser.generateNFA(args[0], regularRuleList);
//        nfa.getStartState().printToDot();
//        System.out.println("Total states = " + nfa.getStateSet().size());
//        nfa.getStartState().printToDot();
//        System.out.println("NFA print completed.");
//        DFA dfa = nfa.toDFA();
//        dfa.getStart().mergeTransits();
//        dfa.getStart().printToDot();
//        NFA nfa = javaParser.generateNFA("RFC3261-SIP-message", ruleList);
//
//        NFAUtils.genrateDFA(nfa.getStart());//.printToDot();

        System.out.println(JavaParser.class.getName() + ".main() ended.");
    }
}
