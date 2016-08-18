package de.hk.bfit.process;

import de.hk.bfit.action.ResetAction;
import de.hk.bfit.action.ReferenceAction;
import de.hk.bfit.action.SelectAction;
import de.hk.bfit.action.InitAction;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class TestCase {

    @XmlElement
    private String description;
    @XmlElement
    private InitAction initAction;
    @XmlElement
    private ReferenceAction referenceActionBefore;
    @XmlElement
    private ReferenceAction referenceActionAfter;
    @XmlElement
    private ResetAction resetAction;

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

}
