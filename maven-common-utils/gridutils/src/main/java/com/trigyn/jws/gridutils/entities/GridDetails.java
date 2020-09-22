package com.trigyn.jws.gridutils.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "grid_details")
public class GridDetails implements Serializable{

    private static final long serialVersionUID  = 4472368074028971649L;
    
    @Id
    @Column(name = "grid_id", nullable = false)
    private String gridId                        = null;

    @Column(name = "grid_name")
    private String gridName                      = null;

    @Column(name = "grid_description")
    private String gridDescription               = null;

    @Column(name = "grid_table_name")
    private String gridTableName                 = null;

    @Column(name = "grid_column_names")
    private String gridColumnNames               = null;

    @Column(name = "query_type")
    private Integer queryType                    = null;

    
    public GridDetails() {
        
    }

    
    public GridDetails(String gridId, String gridName, String gridDescription, String gridTableName,
            String gridColumnNames, Integer queryType) {
        this.gridId             = gridId;
        this.gridName           = gridName;
        this.gridDescription    = gridDescription;
        this.gridTableName      = gridTableName;
        this.gridColumnNames    = gridColumnNames;
        this.queryType          = queryType;
    }

    
    public String getGridId() {
        return gridId;
    }

    
    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    
    public String getGridName() {
        return gridName;
    }

    
    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    
    public String getGridDescription() {
        return gridDescription;
    }

    
    public void setGridDescription(String gridDescription) {
        this.gridDescription = gridDescription;
    }

    
    public String getGridTableName() {
        return gridTableName;
    }

    
    public void setGridTableName(String gridTableName) {
        this.gridTableName = gridTableName;
    }

    
    public String getGridColumnNames() {
        return gridColumnNames;
    }

    
    public void setGridColumnNames(String gridColumnNames) {
        this.gridColumnNames = gridColumnNames;
    }

    public Integer getQueryType() {
        return this.queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof GridDetails)) {
            return false;
        }
        GridDetails gridDetails = (GridDetails) o;
        return Objects.equals(gridId, gridDetails.gridId) && Objects.equals(gridName, gridDetails.gridName) && Objects.equals(gridDescription, gridDetails.gridDescription) && Objects.equals(gridTableName, gridDetails.gridTableName) && Objects.equals(gridColumnNames, gridDetails.gridColumnNames) && Objects.equals(queryType, gridDetails.queryType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gridId, gridName, gridDescription, gridTableName, gridColumnNames, queryType);
    }


    @Override
    public String toString() {
        return "{" +
            " gridId='" + getGridId() + "'" +
            ", gridName='" + getGridName() + "'" +
            ", gridDescription='" + getGridDescription() + "'" +
            ", gridTableName='" + getGridTableName() + "'" +
            ", gridColumnNames='" + getGridColumnNames() + "'" +
            ", queryType='" + getQueryType() + "'" +
            "}";
    }

}