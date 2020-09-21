package app.trigyn.common.typeahead.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "autocomplete_details")
public class Autocomplete {

    @Id
    @Column(name = "ac_id")
    private String autocompleteId = null;

    @Column(name = "ac_description")
    private String autocompleteDesc = null;
    
    @Column(name = "ac_select_query")
    private String autocompleteSelectQuery = null;
    

    public Autocomplete() {
    }

    public Autocomplete(String autocompleteId, String autocompleteDesc, String autocompleteSelectQuery) {
        this.autocompleteId = autocompleteId;
        this.autocompleteDesc = autocompleteDesc;
        this.autocompleteSelectQuery = autocompleteSelectQuery;
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

    public String getAutocompleteSelectQuery() {
        return this.autocompleteSelectQuery;
    }

    public void setAutocompleteSelectQuery(String autocompleteSelectQuery) {
        this.autocompleteSelectQuery = autocompleteSelectQuery;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Autocomplete)) {
            return false;
        }
        Autocomplete autocomplete = (Autocomplete) o;
        return Objects.equals(autocompleteId, autocomplete.autocompleteId) && Objects.equals(autocompleteDesc, autocomplete.autocompleteDesc) && Objects.equals(autocompleteSelectQuery, autocomplete.autocompleteSelectQuery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(autocompleteId, autocompleteDesc, autocompleteSelectQuery);
    }

    @Override
    public String toString() {
        return "{" +
            " autocompleteId='" + getAutocompleteId() + "'" +
            ", autocompleteDesc='" + getAutocompleteDesc() + "'" +
            ", autocompleteSelectQuery='" + getAutocompleteSelectQuery() + "'" +
            "}";
    }

    
}