package app.trigyn.core.dynarest.vo;

import java.util.Objects;

public class RestApiDetails {

    private Integer dynamicId = null;

    private String dynamicRestUrl = null;

    private Integer rbacId = null;

    private String methodName = null;

    private String methodDescription = null;

    private String reponseType = null;

    private String serviceLogic = null;

    private Integer platformId = null;

    private String methodType = null;

    public RestApiDetails() {
    }

    public RestApiDetails(Integer dynamicId, String dynamicRestUrl, Integer rbacId, String methodName,
            String methodDescription, String reponseType, String serviceLogic, Integer platformId, String methodType) {
        this.dynamicId = dynamicId;
        this.dynamicRestUrl = dynamicRestUrl;
        this.rbacId = rbacId;
        this.methodName = methodName;
        this.methodDescription = methodDescription;
        this.reponseType = reponseType;
        this.serviceLogic = serviceLogic;
        this.platformId = platformId;
        this.methodType = methodType;
    }

    public Integer getDynamicId() {
        return this.dynamicId;
    }

    public void setDynamicId(Integer dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getDynamicRestUrl() {
        return this.dynamicRestUrl;
    }

    public void setDynamicRestUrl(String dynamicRestUrl) {
        this.dynamicRestUrl = dynamicRestUrl;
    }

    public Integer getRbacId() {
        return this.rbacId;
    }

    public void setRbacId(Integer rbacId) {
        this.rbacId = rbacId;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodDescription() {
        return this.methodDescription;
    }

    public void setMethodDescription(String methodDescription) {
        this.methodDescription = methodDescription;
    }

    public String getReponseType() {
        return this.reponseType;
    }

    public void setReponseType(String reponseType) {
        this.reponseType = reponseType;
    }

    public String getServiceLogic() {
        return this.serviceLogic;
    }

    public void setServiceLogic(String serviceLogic) {
        this.serviceLogic = serviceLogic;
    }

    public Integer getPlatformId() {
        return this.platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getMethodType() {
        return this.methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public RestApiDetails dynamicId(Integer dynamicId) {
        this.dynamicId = dynamicId;
        return this;
    }

    public RestApiDetails dynamicRestUrl(String dynamicRestUrl) {
        this.dynamicRestUrl = dynamicRestUrl;
        return this;
    }

    public RestApiDetails rbacId(Integer rbacId) {
        this.rbacId = rbacId;
        return this;
    }

    public RestApiDetails methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public RestApiDetails methodDescription(String methodDescription) {
        this.methodDescription = methodDescription;
        return this;
    }

    public RestApiDetails reponseType(String reponseType) {
        this.reponseType = reponseType;
        return this;
    }

    public RestApiDetails serviceLogic(String serviceLogic) {
        this.serviceLogic = serviceLogic;
        return this;
    }

    public RestApiDetails platformId(Integer platformId) {
        this.platformId = platformId;
        return this;
    }

    public RestApiDetails methodType(String methodType) {
        this.methodType = methodType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RestApiDetails)) {
            return false;
        }
        RestApiDetails restApiDetails = (RestApiDetails) o;
        return Objects.equals(dynamicId, restApiDetails.dynamicId)
                && Objects.equals(dynamicRestUrl, restApiDetails.dynamicRestUrl)
                && Objects.equals(rbacId, restApiDetails.rbacId)
                && Objects.equals(methodName, restApiDetails.methodName)
                && Objects.equals(methodDescription, restApiDetails.methodDescription)
                && Objects.equals(reponseType, restApiDetails.reponseType)
                && Objects.equals(serviceLogic, restApiDetails.serviceLogic)
                && Objects.equals(platformId, restApiDetails.platformId)
                && Objects.equals(methodType, restApiDetails.methodType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dynamicId, dynamicRestUrl, rbacId, methodName, methodDescription, reponseType, serviceLogic,
                platformId, methodType);
    }

    @Override
    public String toString() {
        return "{" + " dynamicId='" + getDynamicId() + "'" + ", dynamicRestUrl='" + getDynamicRestUrl() + "'"
                + ", rbacId='" + getRbacId() + "'" + ", methodName='" + getMethodName() + "'" + ", methodDescription='"
                + getMethodDescription() + "'" + ", reponseType='" + getReponseType() + "'" + ", serviceLogic='"
                + getServiceLogic() + "'" + ", platformId='" + getPlatformId() + "'" + ", methodType='"
                + getMethodType() + "'" + "}";
    }

}
