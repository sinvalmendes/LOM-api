package com.nanuvem.lom.api.accountability;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer version;
    private EntityType entityType;
    private List<Property> values = new LinkedList<Property>();
    private Set<Accountability> parentAccountabilities = new HashSet<Accountability>();
    private Set<Accountability> childAccountabilities = new HashSet<Accountability>();

    public void addChildAccountability(Accountability accountability) {
        this.childAccountabilities.add(accountability);
    }

    public void addParentAccountability(Accountability accountability) {
        this.parentAccountabilities.add(accountability);
    }

    public Set<Entity> parents(AccountabilityType accountabilityType) {
        Set<Entity> result = new HashSet<Entity>();
        Iterator<Accountability> it = parentAccountabilities.iterator();
        while (it.hasNext()) {
            Accountability each = (Accountability) it.next();
            if (each.getAccountabilityType().equals(accountabilityType))
                result.add(each.getParent());
        }
        return result;
    }

    public Set<Entity> children() {
        Set<Entity> result = new HashSet<Entity>();
        Iterator<Accountability> iterator = childAccountabilities.iterator();
        while (iterator.hasNext()) {
            Accountability each = (Accountability) iterator.next();
            result.add(each.getChild());
        }
        return result;
    }

    public boolean ancestorsInclude(Entity sample, AccountabilityType type) {
        Iterator<Entity> it = parents(type).iterator();
        while (it.hasNext()) {
            Entity eachParent = (Entity) it.next();
            if (eachParent.equals(sample))
                return true;
            if (eachParent.ancestorsInclude(sample, type))
                return true;
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Property> getValues() {
        return values;
    }

    public void setValues(List<Property> values) {
        this.values = values;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public EntityType getEntity() {
        return entityType;
    }

    public void setEntity(EntityType entityType) {
        this.entityType = entityType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entityType == null) ? 0 : entityType.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Entity other = (Entity) obj;
        if (entityType == null) {
            if (other.entityType != null)
                return false;
        } else if (!entityType.equals(other.entityType))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Instance [id=" + id + ", version=" + version + ", values=" + values + "]";
    }

}
