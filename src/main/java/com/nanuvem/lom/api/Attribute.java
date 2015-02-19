package com.nanuvem.lom.api;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Attribute implements Serializable {

	public final static String MAXVALUE_CONFIGURATION_NAME = "maxvalue";
	public final static String MINVALUE_CONFIGURATION_NAME = "minvalue";
	public final static String MAXREPEAT_CONFIGURATION_NAME = "maxRepeat";
	public final static String MINSYMBOLS_CONFIGURATION_NAME = "minSymbols";
	public final static String MINNUMBERS_CONFIGURATION_NAME = "minNumbers";
	public final static String MINUPPERS_CONFIGURATION_NAME = "minUppers";
	public final static String MAXLENGTH_CONFIGURATION_NAME = "maxlength";
	public final static String MINLENGTH_CONFIGURATION_NAME = "minlength";
	public final static String REGEX_CONFIGURATION_NAME = "regex";
	public final static String DEFAULT_CONFIGURATION_NAME = "default";
	public final static String MANDATORY_CONFIGURATION_NAME = "mandatory";

	private Long id;

	private Integer version;

	private Integer sequence;

	private String name;

	private AttributeType type;

	private String configuration;

	private Entity entity;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((configuration == null) ? 0 : configuration.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Attribute other = (Attribute) obj;
		if (configuration == null) {
			if (other.configuration != null)
				return false;
		} else if (!configuration.equals(other.configuration))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		if (type != other.type)
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
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

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AttributeType getType() {
		return type;
	}

	public void setType(AttributeType type) {
		this.type = type;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public String toString() {
		return "Attribute [sequence=" + sequence + ", name=" + name + ", type="
				+ type + ", configuration=" + configuration + ", id=" + id
				+ ", version=" + version + "]";
	}

}
