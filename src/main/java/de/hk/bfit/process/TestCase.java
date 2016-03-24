package de.hk.bfit.process;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestCase {

    @XmlElement
    private String description;
    @XmlElement
    private InitAction initAction;
    @XmlElement
    private List<ReferenceAction> referenceAction;
    @XmlElement
    private ResetAction resetAction;

    public TestCase() {
    }

    public TestCase(String description, ReferenceAction select) {
        // InitAction
        this.initAction = new InitAction();
        // Reference
        this.referenceAction = new ArrayList<>();
        this.referenceAction.add(select);
        // ResetAction
        this.resetAction = new ResetAction();
    }

    public String getDescription() {
        return description;
    }

    public InitAction getInitAction() {
        return initAction;
    }

    public List<ReferenceAction> getReferenceActions() {
        return referenceAction;
    }
    
}
