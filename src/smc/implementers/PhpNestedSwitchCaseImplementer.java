package smc.implementers;

import smc.generators.nestedSwitchCaseGenerator.NSCNode;
import smc.generators.nestedSwitchCaseGenerator.NSCNodeVisitor;

import java.util.Map;

import static java.lang.String.format;

public class PhpNestedSwitchCaseImplementer implements NSCNodeVisitor {
    private static final String FLAG_NAMESPACE = "namespace";

    private String output = "<?php\n";
    private String phpNamespace = null;

    public PhpNestedSwitchCaseImplementer(Map<String, String> flags) {
        if (flags.containsKey(FLAG_NAMESPACE))
            phpNamespace = flags.get("namespace");
    }

    public void visit(NSCNode.SwitchCaseNode switchCaseNode) {
        String variableName = switchCaseNode.variableName.replace("state", "this->state");
        output += format("switch($%s) {\n", variableName);
        switchCaseNode.generateCases(this);
        output += "}\n";
    }

    public void visit(NSCNode.CaseNode caseNode) {
        output += format("case self::%s_%s:\n", caseNode.switchName, caseNode.caseName);
        caseNode.caseActionNode.accept(this);
        output += "break;\n";
    }

    public void visit(NSCNode.FunctionCallNode functionCallNode) {
        output += format("$this->%s(", functionCallNode.functionName);
        if (functionCallNode.argument != null)
            functionCallNode.argument.accept(this);
        output += ");\n";
    }

    public void visit(NSCNode.EnumNode enumNode) {
        for (String enumValue : enumNode.enumerators) {
            output += format("const %1$s_%2$s = '%2$s';\n", enumNode.name, enumValue);
        }
    }

    public void visit(NSCNode.StatePropertyNode statePropertyNode) {
        output += format("private $state = self::State_%s;\n", statePropertyNode.initialState);
        output += "private function setState($s) {$this->state = $s;}\n";
    }

    public void visit(NSCNode.EventDelegatorsNode eventDelegatorsNode) {
        for (String event : eventDelegatorsNode.events)
            output += format("public function %s() {$this->handleEvent(self::Event_%s);}\n", event, event);
    }

    public void visit(NSCNode.FSMClassNode fsmClassNode) {
        if (phpNamespace != null)
            output += "namespace " + phpNamespace + ";\n";

        String actionsName = fsmClassNode.actionsName;
        if (actionsName == null)
            output += format("abstract class %s {\n", fsmClassNode.className);
        else
            output += format("abstract class %s implements %s {\n", fsmClassNode.className, actionsName);

        fsmClassNode.stateEnum.accept(this);
        fsmClassNode.eventEnum.accept(this);
        fsmClassNode.stateProperty.accept(this);
        fsmClassNode.delegators.accept(this);
        fsmClassNode.handleEvent.accept(this);
        if (actionsName == null) {
            for (String action : fsmClassNode.actions)
                output += format("protected abstract function %s();\n", action);
        }
        output += "public abstract function unhandledTransition($state, $event);\n";

        output += "}\n";
    }

    public void visit(NSCNode.HandleEventNode handleEventNode) {
        output += "private function handleEvent($event) {\n";
        handleEventNode.switchCase.accept(this);
        output += "}\n";
    }

    public void visit(NSCNode.EnumeratorNode enumeratorNode) {
        output += format("self::%s_%s", enumeratorNode.enumeration, enumeratorNode.enumerator);
    }

    public void visit(NSCNode.DefaultCaseNode defaultCaseNode) {
        output += "default: $this->unhandledTransition($this->state, $event); break;\n";
    }

    public String getOutput() {
        return output;
    }
}
