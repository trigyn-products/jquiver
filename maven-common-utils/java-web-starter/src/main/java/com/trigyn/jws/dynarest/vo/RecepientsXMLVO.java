package com.trigyn.jws.dynarest.vo;

import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "recepients")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecepientsXMLVO {

	@XmlAttribute
	private Boolean					separatemails	= null;

	@XmlElement(name = "recepient")
	private List<RecepientXMLVO>	recepientXMLVO	= null;

	public Boolean getSeparatemails() {
		return separatemails;
	}

	public void setSeparatemails(Boolean separatemails) {
		this.separatemails = separatemails;
	}

	public List<RecepientXMLVO> getRecepientXMLVO() {
		return recepientXMLVO;
	}

	public void setRecepientXMLVO(List<RecepientXMLVO> recepientXMLVO) {
		this.recepientXMLVO = recepientXMLVO;
	}

	@Override
	public int hashCode() {
		return Objects.hash(recepientXMLVO, separatemails);
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
		RecepientsXMLVO other = (RecepientsXMLVO) obj;
		return Objects.equals(recepientXMLVO, other.recepientXMLVO) && Objects.equals(separatemails, other.separatemails);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RecepientsXMLVO [separatemails=").append(separatemails).append(", recepientXMLVO=").append(recepientXMLVO)
				.append("]");
		return builder.toString();
	}

}
