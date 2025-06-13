package com.trigyn.jws.formio.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.trigyn.jws.formio.resources.ResourceLoader;
import com.trigyn.jws.formio.storages.FileStorage;

public interface FormClient {
	
	
	 /**
     * Build a json string with a form definition and data according to the form fields. The form definition is got using
     * formKey value. Current variables are variables that already are in a system.
     *
     * @param formKey The id of the form
     * @param formData The object in a system
     * @return Json string with the form definition and data for the form
     */
    String getFormWithData(String formKey, Object formData);

    /**
     * Build a json string with a form definition and data according to the form fields. The form definition is got using
     * formKey value. Current variables are variables that already are in a system.
     *
     * @param formKey The id of the form
     * @param currentVariables The variables in a system
     * @return Json string with the form definition and data for the form
     */
    String getFormWithData(String formKey, ObjectNode currentVariables);

    /**
     * Build a json string with a form definition and data according to the form fields. The form definition is got using
     * formKey value. Current variables are variables that already are in a system. {@link FileStorage} is used to store files
     * separately in an external storage.
     *
     * @param formKey The id of the form
     * @param currentVariables The variables in a system
     * @param fileStorage Implementation of {@link FileStorage}
     * @return Json string with the form definition and data for the form
     */
    String getFormWithData(String formKey, ObjectNode currentVariables, FileStorage fileStorage);

    /**
     * Build a json string with a form definition and data according to the form fields. The form definition is got using
     * formKey value. Current variables are variables that already are in a system. {@link ResourceLoader} is used to make
     * resource (e.g. forms, scripts etc.) loading approach more dynamic.
     *
     * @param formKey The id of the form
     * @param currentVariables The variables in a system
     * @param resourceLoader Implementation of {@link ResourceLoader}
     * @return Json string with the form definition and data for the form
     */
    String getFormWithData(String formKey, ObjectNode currentVariables, ResourceLoader resourceLoader);

    /**
     * Build a json string with a form definition and data according to the form fields. The form definition is got using
     * formKey value. Current variables are variables that already are in a system. {@link FileStorage} is used to store files
     * separately in an external storage. {@link ResourceLoader} is used to make resource (e.g. forms, scripts etc.)
     * loading approach more dynamic.
     *
     * @param formKey The id of the form
     * @param currentVariables The variables in a system
     * @param resourceLoader Implementation of {@link ResourceLoader}
     * @param fileStorage Implementation of {@link FileStorage}
     * @return Json string with the form definition and data for the form
     */
    String getFormWithData(String formKey, ObjectNode currentVariables, ResourceLoader resourceLoader, FileStorage fileStorage);

    /**
     * Define whether submitted data should go through the whole submission lifecycle.
     *
     * @param formKey The id of the form
     * @param submissionState A submission state due to which the decision is made
     * @return Boolean indicating whether submitted data should be processed or not
     */
    boolean shouldProcessSubmission(String formKey, String submissionState);

    /**
     * Define whether submitted data should go through the whole submission lifecycle. {@link ResourceLoader} is used to
     * make resource (e.g. forms, scripts etc.) loading approach more dynamic.
     *
     * @param formKey The id of the form
     * @param submissionState A submission state due to which the decision is made
     * @param resourceLoader Implementation of {@link ResourceLoader}
     * @return Boolean indicating whether submitted data should be processed or not
     */
    boolean shouldProcessSubmission(String formKey, String submissionState, ResourceLoader resourceLoader);

    /**
     * Get root field names of a form
     *
     * @param formKey The id of the form
     * @return List of root fields of the form
     */
    List<String> getRootFormFieldNames(String formKey);

    /**
     * Get root field names of a form. {@link ResourceLoader} is used to make resource (e.g. forms, scripts etc.)
     * loading approach more dynamic.
     *
     * @param formKey The id of the form
     * @param resourceLoader Implementation of {@link ResourceLoader}
     * @return List of root fields of the form
     */
    List<String> getRootFormFieldNames(String formKey, ResourceLoader resourceLoader);


    /**
     * Get all form field names. {@link ResourceLoader} is used to make resource (e.g. forms, scripts etc.)
     * loading approach more dynamic.
     *
     * @param formKey The id of the form
     * @param resourceLoader Implementation of {@link ResourceLoader}
     * @return List of all form field names
     */
    List<String> getFormFieldPaths(String formKey, ResourceLoader resourceLoader);

}
