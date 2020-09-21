package app.trigyn.common.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dashlet_configuration")
public class DashletConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@EmbeddedId
	private DashletConfigurationPK id				= null;
	
	@Column(name = "x_coordinate", nullable = false)
	private Integer				xCoordinate			= null;

	@Column(name = "y_coordinate", nullable = false)
	private Integer				yCoordinate			= null;


	public DashletConfiguration() {
		
	}

	public DashletConfiguration(DashletConfigurationPK id, Integer xCoordinate, Integer yCoordinate) {
		this.id 			= id;
		this.xCoordinate 	= xCoordinate;
		this.yCoordinate 	= yCoordinate;
	}

	/**
	 * @return the id
	 */
	public DashletConfigurationPK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(DashletConfigurationPK id) {
		this.id = id;
	}

	/**
	 * @return the xCoordinate
	 */
	public Integer getxCoordinate() {
		return xCoordinate;
	}

	/**
	 * @param xCoordinate the xCoordinate to set
	 */
	public void setxCoordinate(Integer xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	/**
	 * @return the yCoordinate
	 */
	public Integer getyCoordinate() {
		return yCoordinate;
	}

	/**
	 * @param yCoordinate the yCoordinate to set
	 */
	public void setyCoordinate(Integer yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, xCoordinate, yCoordinate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DashletConfiguration other = (DashletConfiguration) obj;
		return Objects.equals(id, other.id) && Objects.equals(xCoordinate, other.xCoordinate) && Objects.equals(yCoordinate, other.yCoordinate);
	}

	@Override
	public String toString() {
		return "DashletConfiguration [id=" + id + ", xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate + "]";
	}


}
