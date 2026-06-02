package com.trigyn.jws.tags.entities;

import java.io.Serializable;
import java.util.List;

public class TagEntityDetailsRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tagName;
    private List<EntityMapping> entityMappings;

    // ---------- Inner Class ----------
    public static class EntityMapping implements Serializable {

        private static final long serialVersionUID = 1L;

        private String entityId;
        private String moduleId;

        // Getters and Setters
        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public String getModuleId() {
            return moduleId;
        }

        public void setModuleId(String moduleId) {
            this.moduleId = moduleId;
        }
    }

    // ---------- Getters and Setters ----------
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<EntityMapping> getEntityMappings() {
        return entityMappings;
    }

    public void setEntityMappings(List<EntityMapping> entityMappings) {
        this.entityMappings = entityMappings;
    }

    @Override
    public String toString() {
        return "TagEntityDetailsRequest{" +
                "tagName='" + tagName + '\'' +
                ", entityMappings=" + entityMappings +
                '}';
    }
}

