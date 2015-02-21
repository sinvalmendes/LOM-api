package com.nanuvem.lom.api;

public class Relation {

    private Long id;
    private Integer version;
    private RelationType relationType;
    private Instance source;
    private Instance target;

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

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    public Instance getSource() {
        return source;
    }

    public void setSource(Instance source) {
        this.source = source;
    }

    public Instance getTarget() {
        return target;
    }

    public void setTarget(Instance target) {
        this.target = target;
    }
}
