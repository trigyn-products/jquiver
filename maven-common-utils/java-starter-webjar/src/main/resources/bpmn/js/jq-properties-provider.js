/*const ROLE_OPTIONS = [
    "MANAGER",
    "HR",
    "FINANCE",
    "ADMIN"
];

const USER_OPTIONS = [
    "john",
    "mary",
    "smith"
];

const GROUP_OPTIONS = [
    "SUPPORT",
    "OPERATIONS",
    "SALES"
];

const FORM_OPTIONS = [

    {
        id: "LEAVE_FORM",
        name: "Leave Request Form"
    },

    {
        id: "EXPENSE_FORM",
        name: "Expense Claim Form"
    },

    {
        id: "TRAVEL_FORM",
        name: "Travel Request Form"
    },

    {
        id: "PURCHASE_FORM",
        name: "Purchase Request Form"
    }
];*/

let ROLE_OPTIONS = [];

let USER_OPTIONS = [];

let GROUP_OPTIONS = [];

let FORM_OPTIONS = [];

async function loadMasterData() {

    try {

        const response = await fetch(
            contextPath+"/cf/master-data"
        );

        if (!response.ok) {
            throw new Error("Unable to load master data");
        }

        const data = await response.json();

        ROLE_OPTIONS = data.roles || [];
        USER_OPTIONS = data.users || [];
        GROUP_OPTIONS = data.groups || [];
        FORM_OPTIONS = data.forms || [];

        console.log("Master Data Loaded", data);

    }
    catch (e) {

        console.error(e);

    }

}

function JqPropertiesProvider(propertiesPanel) {

    propertiesPanel.registerProvider(
        500,
        this
    );
}

JqPropertiesProvider.prototype.getGroups =
function(element) {

    return function(groups) {

        if (
            element.type !== "bpmn:UserTask"
        ) {
            return groups;
        }

        groups.push({

            id: "jquiver",

            label: "JQuiver Configuration",

            entries: []
        });

        return groups;
    };
};

JqPropertiesProvider.$inject = [
    "propertiesPanel"
];

window.JqPropertiesProvider = {

    __init__: [
        "jqPropertiesProvider"
    ],

    jqPropertiesProvider: [
        "type",
        JqPropertiesProvider
    ]
};

function saveTaskCode(
    element,
    value
) {

    const moddle =
        window.modeler.get(
            "moddle"
        );

    const modeling =
        window.modeler.get(
            "modeling"
        );

    const taskConfig =
        getOrCreateTaskConfig(
            element,
            moddle,
            modeling
        );

    taskConfig.taskCode =
        value;

    modeling.updateProperties(
        element,
        {}
    );

    console.log(
        "Task Code Saved:",
        value
    );
}

function getTaskConfig(element) {

    const businessObject =
        element.businessObject;

    const extensionElements =
        businessObject.extensionElements;

    if (!extensionElements) {
        return null;
    }

    console.log("Extension Values:",extensionElements.values);

    extensionElements.values.forEach(function(v) {

        console.log("TYPE = ",v.$type,v);

    });

    return extensionElements.values.find(
        function(v) {

            return (
                v.$type ===
                "jq:TaskConfig"
            );
        }
    );
}

function getVariables(
    element
) {

    const extensionElements =
        element.businessObject
        .extensionElements;

    if (
        !extensionElements
    ) {

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

function addVariable(
    element
) {

    const moddle =
        window.modeler.get(
            "moddle"
        );

    const modeling =
        window.modeler.get(
            "modeling"
        );

    const taskConfig =
        getOrCreateTaskConfig(
            element,
            moddle,
            modeling
        );

    const extensionElements =
        element.businessObject
        .extensionElements;

    extensionElements.values.push(

        moddle.create(
            "jq:Variable",
            {
                name:
                    "newVariable",

                type:
                    "String"
            }
        )
    );

    modeling.updateProperties(
        element,
        {}
    );
}

function getCondition(
    element
) {

    const extensionElements =
        element.businessObject
        .extensionElements;

    if (!extensionElements) {

        return null;
    }

    const condition =
        extensionElements.values.find(

            function(v) {

                return (
                    v.$type ===
                    "jq:Condition"
                );
            }
        );

    console.log(
        "Condition found:",
        condition
    );

    return condition;
}

function getOrCreateCondition(
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

    let condition =
        extensionElements.values.find(

            function(v) {

                return (
                    v.$type ===
                    "jq:Condition"
                );
            }
        );

    if (!condition) {

        condition =
            moddle.create(
                "jq:Condition",
                {
                    type:
                        "EXPRESSION",

                    expression:
                        ""
                }
            );

        extensionElements.values.push(
            condition
        );
    }

    return condition;
}

function saveConditionProperty(
    element,
    propertyName,
    value
) {

    const moddle =
        window.modeler.get(
            "moddle"
        );

    const modeling =
        window.modeler.get(
            "modeling"
        );

    const condition =
        getOrCreateCondition(
            element,
            moddle,
            modeling
        );

    condition[propertyName] =
        value;

    modeling.updateProperties(
        element,
        {}
    );

    console.log(
        propertyName +
        " saved:",
        value
    );
}

function buildRecipientOptions(
    recipientType,
    selectedValue
) {

    let options = [];

    switch (recipientType) {

        case "ROLE":
            options = ROLE_OPTIONS;
            break;

        case "USER":
            options = USER_OPTIONS;
            break;

        case "GROUP":
            options = GROUP_OPTIONS;
            break;

        default:
            options = [];
    }

    let html =
        '<option value="">Select</option>';

    options.forEach(function(item) {

        html +=

            '<option value="' +

            item.id +

            '"' +

            (item.id === selectedValue
                ? ' selected'
                : '') +

            '>' +

            item.name +

            '</option>';

    });

    return html;
}