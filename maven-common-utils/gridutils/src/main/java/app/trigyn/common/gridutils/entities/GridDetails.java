package app.trigyn.common.gridutils.entities;

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

    /**
     * 
     */
    public GridDetails() {
        
    }

    /**
     * @param gridId
     * @param gridName
     * @param gridDescription
     * @param gridTableName
     * @param gridColumnNames
     */
    public GridDetails(String gridId, String gridName, String gridDescription, String gridTableName,
            String gridColumnNames, Integer queryType) {
        this.gridId             = gridId;
        this.gridName           = gridName;
        this.gridDescription    = gridDescription;
        this.gridTableName      = gridTableName;
        this.gridColumnNames    = gridColumnNames;
        this.queryType          = queryType;
    }

    /**
     * @return the gridId
     */
    public String getGridId() {
        return gridId;
    }

    /**
     * @param gridId the gridId to set
     */
    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    /**
     * @return the gridName
     */
    public String getGridName() {
        return gridName;
    }

    /**
     * @param gridName the gridName to set
     */
    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    /**
     * @return the gridDescription
     */
    public String getGridDescription() {
        return gridDescription;
    }

    /**
     * @param gridDescription the gridDescription to set
     */
    public void setGridDescription(String gridDescription) {
        this.gridDescription = gridDescription;
    }

    /**
     * @return the gridTableName
     */
    public String getGridTableName() {
        return gridTableName;
    }

    /**
     * @param gridTableName the gridTableName to set
     */
    public void setGridTableName(String gridTableName) {
        this.gridTableName = gridTableName;
    }

    /**
     * @return the gridColumnNames
     */
    public String getGridColumnNames() {
        return gridColumnNames;
    }

    /**
     * @param gridColumnNames the gridColumnNames to set
     */
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