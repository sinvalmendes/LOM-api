package com.nanuvem.lom.api;

import java.lang.reflect.Field;

public class RelationType {

    private Long id;
    private Integer version;
    private String name;
    private EntityType sourceEntityType;
    private EntityType targetEntityType;
    private boolean isBidirectional;
    private String reverseName;
    private Cardinality sourceCardinality;
    private Cardinality targetCardinality;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityType getSourceEntityType() {
        return sourceEntityType;
    }

    public void setSourceEntityType(EntityType sourceEntityType) {
        this.sourceEntityType = sourceEntityType;
    }

    public EntityType getTargetEntityType() {
        return targetEntityType;
    }

    public void setTargetEntityType(EntityType targetEntityType) {
        this.targetEntityType = targetEntityType;
    }

    public boolean isBidirecticonal() {
        return isBidirectional;
    }

    public void setBidirectional(boolean isBidirectional) {
        this.isBidirectional = isBidirectional;
    }

    public Cardinality getSourceCardinality() {
        return sourceCardinality;
    }

    public void setSourceCardinality(Cardinality sourceCardinality) {
        this.sourceCardinality = sourceCardinality;
    }

    public Cardinality getTargetCardinality() {
        return targetCardinality;
    }

    public void setTargetCardinality(Cardinality targetCardinality) {
        this.targetCardinality = targetCardinality;
    }

    public boolean isBidirectional() {
        return isBidirectional;
    }

    public String getReverseName() {
        return reverseName;
    }

    public void setReverseName(String reverseName) {
        this.reverseName = reverseName;
    }
    
    public static RelationType cloneObject(RelationType obj){
        try{
            RelationType clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(clone, field.get(obj));
            }
            return clone;
        }catch(Exception e){
            return null;
        }
    }

}
