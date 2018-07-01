package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestCase {

    private String description;

    private InitAction initAction;

    private ReferenceAction referenceActionBefore;

    private ReferenceAction referenceActionAfter;

    private ResetAction resetAction;

    private List<DefinedAction> definedActions;

    public TestCase() {
    }

    public TestCase(ReferenceAction referenceAction) {
        this("This is a new testcase", referenceAction);
    }

    public TestCase(String description, ReferenceAction referenceAction) {
        this.description = description;
        // InitAction
        this.initAction = new InitAction();
        // Reference
        this.referenceActionAfter = referenceAction;
        this.referenceActionBefore = new ReferenceAction("This is a referenceAction", new ArrayList<SelectCmd>());
        // ResetAction
        this.resetAction = new ResetAction();

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InitAction getInitAction() {
        return initAction;
    }

    public void setInitAction(InitAction initAction) {
        this.initAction = initAction;
    }

    public ReferenceAction getReferenceActionBefore() {
        return referenceActionBefore;
    }

    public void setReferenceActionBefore(ReferenceAction referenceActionBefore) {
        this.referenceActionBefore = referenceActionBefore;
    }

    public ResetAction getResetAction() {
        return resetAction;
    }

    public void setResetAction(ResetAction resetAction) {
        this.resetAction = resetAction;
    }

    public ReferenceAction getReferenceActionAfter() {
        return referenceActionAfter;
    }

    public void setReferenceActionAfter(ReferenceAction referenceActionAfter) {
        this.referenceActionAfter = referenceActionAfter;
    }

    public List<DefinedAction> getDefinedActions() {
        return definedActions;
    }

    public void setDefinedActions(List<DefinedAction> definedActions) {
        this.definedActions = definedActions;
    }
}
