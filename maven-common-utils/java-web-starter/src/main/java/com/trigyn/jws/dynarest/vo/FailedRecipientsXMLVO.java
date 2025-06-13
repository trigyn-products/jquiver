package com.trigyn.jws.dynarest.vo;

import java.util.List;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "failedrecepients")
@XmlAccessorType(XmlAccessType.FIELD)
public class FailedRecipientsXMLVO {

	@XmlAttribute
	private Boolean					separatemails	= null;

	@XmlElement(name = "failedrecepient")
	private List<FailedRecipientXMLVO>	failedrecipientXMLVO	= null;

	public Boolean getSeparatemails() {
		return separatemails;
	}

	public void setSeparatemails(Boolean separatemails) {
		this.separatemails = separatemails;
	}

	public List<FailedRecipientXMLVO> getFailedrecipientXMLVO() {
		return failedrecipientXMLVO;
	}

	public void setFailedrecipientXMLVO(List<FailedRecipientXMLVO> failedrecipientXMLVO) {
		this.failedrecipientXMLVO = failedrecipientXMLVO;
	}

	@Override
	public int hashCode() {
		return Objects.hash(failedrecipientXMLVO, separatemails);
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
		FailedRecipientsXMLVO other = (FailedRecipientsXMLVO) obj;
		return Objects.equals(failedrecipientXMLVO, other.failedrecipientXMLVO) && Objects.equals(separatemails, other.separatemails);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FailedRecipientsXMLVO [separatemails=").append(separatemails).append(", failedrecipientXMLVO=").append(failedrecipientXMLVO)
				.append("]");
		return builder.toString();
	}

}
