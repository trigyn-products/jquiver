package com.trigyn.jws.dynarest.vo;

import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "email")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailXMLVO {

	@XmlElement
	private String							subject			= null;

	@XmlElement(name = "body")
	private EmailBodyXMLVO					body			= null;

	@XmlElementWrapper(name = "header")
	@XmlElement(name = "property")
	private List<EmailHeaderPropertyXMLVO>	header			= null;

	@XmlElement(name = "sender")
	private SenderXMLVO						senderXMLVO		= null;

	@XmlElement(name = "failedrecepients")
	private FailedRecipientsXMLVO					failedrecepientsXMLVO	= null;
	
	@XmlElement(name = "recepients")
	private RecepientsXMLVO					recepientsXMLVO	= null;

	
	@XmlElementWrapper(name = "attachments")
	@XmlElement(name = "attachment")
	private List<AttachmentXMLVO>			attachmentXMLVO	= null;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public EmailBodyXMLVO getBody() {
		return body;
	}

	public void setBody(EmailBodyXMLVO body) {
		this.body = body;
	}

	public List<EmailHeaderPropertyXMLVO> getHeader() {
		return header;
	}

	public void setHeader(List<EmailHeaderPropertyXMLVO> header) {
		this.header = header;
	}

	public RecepientsXMLVO getRecepientsXMLVO() {
		return recepientsXMLVO;
	}

	public void setRecepientsXMLVO(RecepientsXMLVO recepientsXMLVO) {
		this.recepientsXMLVO = recepientsXMLVO;
	}
	
	public FailedRecipientsXMLVO getFailedrecepientsXMLVO() {
		return failedrecepientsXMLVO;
	}

	public void setFailedrecepientsXMLVO(FailedRecipientsXMLVO failedrecepientsXMLVO) {
		this.failedrecepientsXMLVO = failedrecepientsXMLVO;
	}


	public List<AttachmentXMLVO> getAttachmentXMLVO() {
		return attachmentXMLVO;
	}

	public void setAttachmentXMLVO(List<AttachmentXMLVO> attachmentXMLVO) {
		this.attachmentXMLVO = attachmentXMLVO;
	}

	@Override
	public int hashCode() {
		return Objects.hash(attachmentXMLVO, body, header, recepientsXMLVO, subject,failedrecepientsXMLVO);
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
		EmailXMLVO other = (EmailXMLVO) obj;
		return Objects.equals(attachmentXMLVO, other.attachmentXMLVO) && Objects.equals(body, other.body)
				&& Objects.equals(header, other.header) && Objects.equals(recepientsXMLVO, other.recepientsXMLVO)
				&& Objects.equals(subject, other.subject)&& Objects.equals(failedrecepientsXMLVO, other.failedrecepientsXMLVO);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EmailXMLVO [subject=").append(subject).append(", body=").append(body).append(", header=")
				.append(header).append(", recepientsXMLVO=").append(recepientsXMLVO).append(", failedrecepientsXMLVO=").append(failedrecepientsXMLVO).append(", attachmentXMLVO=")
				.append(attachmentXMLVO).append("]");
		return builder.toString();
	}

	public SenderXMLVO getSenderXMLVO() {
		return senderXMLVO;
	}

	public void setSenderXMLVO(SenderXMLVO senderXMLVO) {
		this.senderXMLVO = senderXMLVO;
	}

}
