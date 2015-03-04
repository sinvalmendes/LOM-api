package com.nanuvem.lom.api.accountability;

public class Accountability {

    private Long id;
    private Integer version;
<<<<<<< HEAD
    private Instance parent;
    private Instance child;
=======
    private Entity parent;
    private Entity child;
>>>>>>> 62c3c42a8a2b3ab25ffbf87098dfa6f9877f925a
    private AccountabilityType accountabilityType;

    public Accountability(Entity parent, Entity child, AccountabilityType accountabilityType) {
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

<<<<<<< HEAD
    public static Accountability create(Instance parent, Instance child, AccountabilityType accountabilityType) {
=======
    public static Accountability create(Entity parent, Entity child, AccountabilityType accountabilityType) {
>>>>>>> 62c3c42a8a2b3ab25ffbf87098dfa6f9877f925a
        if (!canCreate(parent, child, accountabilityType))
            throw new IllegalArgumentException("Invalid Accountability");
        else
            return new Accountability(parent, child, accountabilityType);
    }

    public static boolean canCreate(Entity parent, Entity child, AccountabilityType accountabilityType) {
        if (parent.equals(child))
            return false;
        if (parent.ancestorsInclude(child, accountabilityType))
            return false;
        return true;
    }

    public Entity getParent() {
        return parent;
    }

    public void setParent(Entity parent) {
        this.parent = parent;
    }

    public Entity getChild() {
        return child;
    }

    public void setChild(Entity child) {
        this.child = child;
    }

    public AccountabilityType getAccountabilityType() {
        return accountabilityType;
    }

    public void setAccountabilityType(AccountabilityType accountabilityType) {
        this.accountabilityType = accountabilityType;
    }

}
