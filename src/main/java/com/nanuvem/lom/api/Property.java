package com.nanuvem.lom.api;

import java.io.Serializable;

public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer version;

    private Entity entity;
    private PropertyType propertyType;
    private String value;

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

    public PropertyType getAttribute() {
        return propertyType;
    }

    public void setAttribute(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public Entity getInstance() {
        return entity;
    }

    public void setInstance(Entity entity) {
        this.entity = entity;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Property other = (Property) obj;
        if (propertyType == null) {
            if (other.propertyType != null)
                return false;
        } else if (!propertyType.equals(other.propertyType))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (entity == null) {
            if (other.entity != null)
                return false;
        } else if (!entity.equals(other.entity))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((propertyType == null) ? 0 : propertyType.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((entity == null) ? 0 : entity.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "AttributeValue [value=" + value + ", id=" + id + ", version=" + version + "]";
    }

}