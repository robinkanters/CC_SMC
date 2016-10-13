package smc.generators.nestedSwitchCaseGenerator;

import java.util.ArrayList;
import java.util.List;

public interface NSCNode {
    void accept(NSCNodeVisitor visitor);

    class SwitchCaseNode implements NSCNode {
        public String variableName;
        List<NSCNode> caseNodes = new ArrayList<>();

        SwitchCaseNode(String variableName) {
            this.variableName = variableName;
        }

        public void accept(NSCNodeVisitor visitor) {
            visitor.visit(this);
        }

        public void generateCases(NSCNodeVisitor visitor) {
            for (NSCNode c : caseNodes) {
                c.accept(visitor);
            }
        }
    }

    class CaseNode implements NSCNode {
        public String switchName;
        public String caseName;
        public NSCNode caseActionNode;

        CaseNode(String SwitchName, String caseName) {
            switchName = SwitchName;
            this.caseName = caseName;
        }

        public void accept(NSCNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    class FunctionCallNode implements NSCNode {
        public String functionName;
        public NSCNode argument;

        FunctionCallNode(String functionName) {
            this.functionName = functionName;
        }

        FunctionCallNode(String functionName, NSCNode argument) {
            this.functionName = functionName;
            this.argument = argument;
        }

        public void accept(NSCNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    class CompositeNode implements NSCNode {
        private List<NSCNode> nodes = new ArrayList<>();

        public void accept(NSCNodeVisitor visitor) {
            for (NSCNode node : nodes)
                node.accept(visitor);
        }

        public void add(NSCNode node) {
            nodes.add(node);
        }
    }

    class EnumNode implements NSCNode {
        public String name;
        public List<String> enumerators;

        EnumNode(String name, List<String> enumerators) {
            this.name = name;
            this.enumerators = enumerators;
        }

        public void accept(NSCNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    class StatePropertyNode implements NSCNode {
        public String initialState;

        StatePropertyNode(String initialState) {
            this.initialState = initialState;
        }

        public void accept(NSCNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    class EventDelegatorsNode implements NSCNode {
        public List<String> events;

        EventDelegatorsNode(List<String> events) {
            this.events = events;
        }

        public void accept(NSCNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    class FSMClassNode implements NSCNode {
        public EventDelegatorsNode delegators;
        public EnumNode eventEnum;
        public EnumNode stateEnum;
        public StatePropertyNode stateProperty;
        public HandleEventNode handleEvent;
        public String className;
        public String actionsName;
        public List<String> actions;

        public void accept(NSCNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    class HandleEventNode implements NSCNode {
        public SwitchCaseNode switchCase;

        public HandleEventNode(SwitchCaseNode switchCase) {
            this.switchCase = switchCase;
        }

        public void accept(NSCNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    class EnumeratorNode implements NSCNode {
        public String enumeration;
        public String enumerator;

        EnumeratorNode(String enumeration, String enumerator) {
            this.enumeration = enumeration;
            this.enumerator = enumerator;
        }

        public void accept(NSCNodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    class DefaultCaseNode implements NSCNode {
        public String state;

        DefaultCaseNode(String state) {
            this.state = state;
        }

        public void accept(NSCNodeVisitor visitor) {
            visitor.visit(this);
        }
    }
}
