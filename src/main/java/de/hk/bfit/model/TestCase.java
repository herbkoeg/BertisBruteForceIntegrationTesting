package de.hk.bfit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.hk.bfit.model.*;

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
        this("This is a new testcase",referenceAction);
    }
    
    public TestCase(String description, ReferenceAction referenceAction) {
        this.description = description;
        // InitAction
        this.initAction = new InitAction();
        // Reference
        this.referenceActionAfter = referenceAction;
        this.referenceActionBefore = new ReferenceAction("This is a referenceAction", new ArrayList<SelectAction>());
        // ResetAction
        this.resetAction = new ResetAction();
        
    }

    public String getDescription() {
        return description;
    }

    public InitAction getInitAction() {
        return initAction;
    }

    public ReferenceAction getReferenceActionBefore() {
        return referenceActionBefore;
    }

    public ResetAction getResetAction() {
        return resetAction;
    }

    public ReferenceAction getReferenceActionAfter() {
        return referenceActionAfter;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInitAction(InitAction initAction) {
        this.initAction = initAction;
    }

    public void setReferenceActionBefore(ReferenceAction referenceActionBefore) {
        this.referenceActionBefore = referenceActionBefore;
    }

    public void setReferenceActionAfter(ReferenceAction referenceActionAfter) {
        this.referenceActionAfter = referenceActionAfter;
    }

    public void setResetAction(ResetAction resetAction) {
        this.resetAction = resetAction;
    }

    public List<DefinedAction> getDefinedActions() {
        return definedActions;
    }

    public void setDefinedActions(List<DefinedAction> definedActions) {
        this.definedActions = definedActions;
    }
}
