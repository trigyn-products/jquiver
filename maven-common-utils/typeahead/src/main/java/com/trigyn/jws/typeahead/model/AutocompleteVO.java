package com.trigyn.jws.typeahead.model;

import java.util.Objects;

public class AutocompleteVO {

    private String autocompleteId = null;

    private String autocompleteDesc = null;

    private String autocompleteQuery = null;


    public AutocompleteVO() {
    }

    public AutocompleteVO(String autocompleteId, String autocompleteDesc, String autocompleteQuery) {
        this.autocompleteId = autocompleteId;
        this.autocompleteDesc = autocompleteDesc;
        this.autocompleteQuery = autocompleteQuery;
    }

    public String getAutocompleteId() {
        return this.autocompleteId;
    }

    public void setAutocompleteId(String autocompleteId) {
        this.autocompleteId = autocompleteId;
    }

    public String getAutocompleteDesc() {
        return this.autocompleteDesc;
    }

    public void setAutocompleteDesc(String autocompleteDesc) {
        this.autocompleteDesc = autocompleteDesc;
    }

    public String getAutocompleteQuery() {
        return this.autocompleteQuery;
    }

    public void setAutocompleteQuery(String autocompleteQuery) {
        this.autocompleteQuery = autocompleteQuery;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AutocompleteVO)) {
            return false;
        }
        AutocompleteVO autocompleteVO = (AutocompleteVO) o;
        return Objects.equals(autocompleteId, autocompleteVO.autocompleteId) && Objects.equals(autocompleteDesc, autocompleteVO.autocompleteDesc) && Objects.equals(autocompleteQuery, autocompleteVO.autocompleteQuery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(autocompleteId, autocompleteDesc, autocompleteQuery);
    }

    @Override
    public String toString() {
        return "{" +
            " autocompleteId='" + getAutocompleteId() + "'" +
            ", autocompleteDesc='" + getAutocompleteDesc() + "'" +
            ", autocompleteQuery='" + getAutocompleteQuery() + "'" +
            "}";
    }

    
}