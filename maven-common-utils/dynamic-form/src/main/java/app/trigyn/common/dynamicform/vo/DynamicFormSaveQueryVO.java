/**
 * 
 */
package app.trigyn.common.dynamicform.vo;

public class DynamicFormSaveQueryVO {

	private String formSaveQuery = null;

	private Integer sequence = null;
	
	private String checksum = null;

	public DynamicFormSaveQueryVO() {

	}

	public DynamicFormSaveQueryVO(String formSaveQuery, Integer sequence) {
		this.formSaveQuery = formSaveQuery;
		this.sequence = sequence;
	}
	
	public DynamicFormSaveQueryVO(String formSaveQuery, Integer sequence,String checksum) {
		this.formSaveQuery = formSaveQuery;
		this.sequence = sequence;
		this.checksum = checksum;
	}

	public String getFormSaveQuery() {
		return formSaveQuery;
	}

	public void setFormSaveQuery(String formSaveQuery) {
		this.formSaveQuery = formSaveQuery;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	
	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formSaveQuery == null) ? 0 : formSaveQuery.hashCode());
		result = prime * result + ((sequence == null) ? 0 : sequence.hashCode());
		result = prime * result + ((checksum == null) ? 0 : checksum.hashCode());
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
		DynamicFormSaveQueryVO other = (DynamicFormSaveQueryVO) obj;
		if (formSaveQuery == null) {
			if (other.formSaveQuery != null)
				return false;
		} else if (!formSaveQuery.equals(other.formSaveQuery))
			return false;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		if (checksum == null) {
			if (other.checksum != null)
				return false;
		} else if (!checksum.equals(other.checksum))
			return false;
		return true;
	}

}
