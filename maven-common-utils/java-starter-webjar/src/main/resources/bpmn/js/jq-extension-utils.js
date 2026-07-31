function getOrCreateTaskConfig(
    element,
    moddle,
    modeling
) {

    const businessObject =
        element.businessObject;

    let extensionElements =
        businessObject.extensionElements;

    if (!extensionElements) {

        extensionElements =
            moddle.create(
                "bpmn:ExtensionElements",
                {
                    values: []
                }
            );

        modeling.updateProperties(
            element,
            {
                extensionElements
            }
        );
    }

    let taskConfig =
        extensionElements.values.find(
            function(v) {

                return (
                    v.$type ===
                    "jq:TaskConfig"
                );
            }
        );

    if (!taskConfig) {

        taskConfig =
            moddle.create(
                "jq:TaskConfig"
            );

        extensionElements.values.push(
            taskConfig
        );
    }

    return taskConfig;
}


function getProcessVariables(
    processElement
) {

    const businessObject =
        processElement.businessObject;

    const extensionElements =
        businessObject.extensionElements;

    if (!extensionElements) {

        return [];
    }

    return extensionElements.values.filter(
        function(v) {

            return (
                v.$type ===
                "jq:Variable"
            );
        }
    );
}

function addProcessVariable(
    processElement
) {

    const moddle =
        window.modeler.get(
            "moddle"
        );

    const modeling =
        window.modeler.get(
            "modeling"
        );

    let extensionElements =
        processElement.businessObject
        .extensionElements;

    if (!extensionElements) {

        extensionElements =
            moddle.create(
                "bpmn:ExtensionElements",
                {
                    values: []
                }
            );

        modeling.updateProperties(
            processElement,
            {
                extensionElements
            }
        );
    }

    const variable =
        moddle.create(
            "jq:Variable",
            {
                name: "",
                type: "String"
            }
        );

    extensionElements.values.push(
        variable
    );

    modeling.updateProperties(
        processElement,
        {}
    );
}