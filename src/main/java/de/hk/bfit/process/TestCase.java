package de.hk.bfit.process;

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
    private ReferenceAction referenceAction;
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
        this.referenceAction = referenceAction;
        // ResetAction
        this.resetAction = new ResetAction();
    }

    public String getDescription() {
        return description;
    }

    public InitAction getInitAction() {
        return initAction;
    }

    public ReferenceAction getReferenceAction() {
        return referenceAction;
    }

    public ResetAction getResetAction() {
        return resetAction;
    }

}
