package com.nanuvem.lom.api.accountability;

public class Accountability {

    private Long id;
    private Integer version;
    private Instance parent;
    private Instance child;
    private AccountabilityType accountabilityType;

    public Accountability(Instance parent, Instance child, AccountabilityType accountabilityType) {
        this.parent = parent;
        this.parent.addChildAccountability(this);
        this.child = child;
        this.child.addParentAccountability(this);
        this.accountabilityType = accountabilityType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public static Accountability create(Instance parent, Instance child, AccountabilityType accountabilityType) {
        if (!canCreate(parent, child, accountabilityType))
            throw new IllegalArgumentException("Invalid Accountability");
        else
            return new Accountability(parent, child, accountabilityType);
    }

    public static boolean canCreate(Instance parent, Instance child, AccountabilityType accountabilityType) {
        if (parent.equals(child))
            return false;
        if (parent.ancestorsInclude(child, accountabilityType))
            return false;
        return true;
    }

    public Instance getParent() {
        return parent;
    }

    public void setParent(Instance parent) {
        this.parent = parent;
    }

    public Instance getChild() {
        return child;
    }

    public void setChild(Instance child) {
        this.child = child;
    }

    public AccountabilityType getAccountabilityType() {
        return accountabilityType;
    }

    public void setAccountabilityType(AccountabilityType accountabilityType) {
        this.accountabilityType = accountabilityType;
    }

}
