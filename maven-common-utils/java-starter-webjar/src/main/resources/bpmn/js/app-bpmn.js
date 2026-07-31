let modeler;

const STORAGE_KEY = "CURRENT_BPMN_XML";

const defaultDiagram = `<?xml version="1.0" encoding="UTF-8"?>
<bpmn:definitions
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL"
xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
xmlns:dc="http://www.omg.org/spec/DD/20100524/DC"
xmlns:di="http://www.omg.org/spec/DD/20100524/DI"
id="Definitions_1"
targetNamespace="http://bpmn.io/schema/bpmn">

<bpmn:process
    id="Process_1"
    isExecutable="false">

    <bpmn:startEvent id="StartEvent_1"/>

</bpmn:process>

<bpmndi:BPMNDiagram id="BPMNDiagram_1">

    <bpmndi:BPMNPlane
        id="BPMNPlane_1"
        bpmnElement="Process_1">

        <bpmndi:BPMNShape
            id="_BPMNShape_StartEvent_2"
            bpmnElement="StartEvent_1">

            <dc:Bounds
                x="173"
                y="102"
                width="36"
                height="36"/>

        </bpmndi:BPMNShape>

    </bpmndi:BPMNPlane>

</bpmndi:BPMNDiagram>

</bpmn:definitions>`;

function updateStatus(message) {

    const element =
        document.getElementById("statusText");

    if (element) {

        element.innerText = message;
    }
}

async function initializeModeler() {

    try {

        modeler = new BpmnJS({

            container: "#canvas",

            propertiesPanel: {
                parent: "#properties"
            },

            additionalModules: [

                BpmnJSPropertiesPanel.BpmnPropertiesPanelModule,
                BpmnJSPropertiesPanel.BpmnPropertiesProviderModule

            ],

            keyboard: {
                bindTo: window
            }

        });

        updateStatus(
            "BPMN Designer Ready"
        );

    } catch (err) {

        console.error(err);

        updateStatus(
            "Initialization Failed"
        );
    }
}

/*
 * -----------------------------------
 * LOAD DIAGRAM
 * -----------------------------------
 */
async function loadDiagram(
    xml,
    message
) {

    try {

        await modeler.importXML(xml);

        const canvas =
            modeler.get("canvas");

        canvas.zoom("fit-viewport");

        updateStatus(message);

    } catch (err) {

        console.error(err);

        updateStatus(
            "Failed To Load BPMN"
        );
    }
}

/*
 * -----------------------------------
 * CREATE NEW
 * -----------------------------------
 */
async function createNewDiagram() {

    await loadDiagram(
        defaultDiagram,
        "New Diagram Created"
    );
}

/*
 * -----------------------------------
 * SAVE
 * -----------------------------------
 */
async function saveDiagram() {

    try {

        const { xml } =
            await modeler.saveXML({
                format: true
            });

        localStorage.setItem(
            STORAGE_KEY,
            xml
        );
		
		// Send to parent window
		        if (
		            window.opener &&
		            !window.opener.closed
		        ) {

		            window.opener.postMessage({
		                type: "BPMN_XML",
		                xml: xml
		            }, "*");
		        }


        updateStatus(
            "Workflow Sent Successfully"
        );

		// Auto close popup
		       window.close();
    } catch (err) {

        console.error(err);

        updateStatus(
            "Save Failed"
        );
    }
}

/*
 * -----------------------------------
 * DOWNLOAD
 * -----------------------------------
 */
async function downloadDiagram() {

    try {

        const { xml } =
            await modeler.saveXML({
                format: true
            });

        const blob =
            new Blob(
                [xml],
                {
                    type: "application/xml"
                }
            );

        const url =
            URL.createObjectURL(blob);

        const link =
            document.createElement("a");

        link.href = url;

        link.download =
            "workflow.bpmn";

        document.body.appendChild(link);

        link.click();

        document.body.removeChild(link);

        URL.revokeObjectURL(url);

        updateStatus(
            "Diagram Downloaded"
        );

    } catch (err) {

        console.error(err);

        updateStatus(
            "Download Failed"
        );
    }
}

/*
 * -----------------------------------
 * OPEN FILE PICKER
 * -----------------------------------
 */
function openDiagram() {

    document
        .getElementById("fileInput")
        .click();
}

/*
 * -----------------------------------
 * IMPORT FILE
 * -----------------------------------
 */
async function handleFileSelect(event) {

    const file =
        event.target.files[0];

    if (!file) {
        return;
    }

    const reader =
        new FileReader();

    reader.onload =
        async function(e) {

            try {

                const xml =
                    e.target.result;

                await loadDiagram(
                    xml,
                    "Diagram Imported"
                );

                localStorage.setItem(
                    STORAGE_KEY,
                    xml
                );

            } catch (err) {

                console.error(err);

                updateStatus(
                    "Invalid BPMN File"
                );
            }
        };

    reader.readAsText(file);
}

/*
 * -----------------------------------
 * ZOOM
 * -----------------------------------
 */
function zoomIn() {

    const canvas =
        modeler.get("canvas");

    canvas.zoom(
        canvas.zoom() + 0.1
    );
}

function zoomOut() {

    const canvas =
        modeler.get("canvas");

    canvas.zoom(
        canvas.zoom() - 0.1
    );
}

function fitDiagram() {

    modeler
        .get("canvas")
        .zoom("fit-viewport");
}

function registerEvents() {

    let isImporting = false;

    const eventBus =
        modeler.get("eventBus");

    /*
     * BEFORE IMPORT
     */
    eventBus.on(
        "import.parse.start",
        function() {

            isImporting = true;
        }
    );

    /*
     * AFTER IMPORT
     */
    eventBus.on(
        "import.done",
        function() {

            setTimeout(function() {

                isImporting = false;

            }, 500);
        }
    );

    /*
     * AUTO SAVE
     */
    eventBus.on(
        "commandStack.changed",
        async function() {

            /*
             * DO NOT SAVE DURING IMPORT
             */
            if (isImporting) {
                return;
            }

            try {

                const result =
                    await modeler.saveXML({
                        format: true
                    });

                if (
                    result &&
                    result.xml
                ) {

                    localStorage.setItem(
                        STORAGE_KEY,
                        result.xml
                    );

                    updateStatus(
                        "Auto Saved"
                    );
                }

            } catch (err) {

                console.error(err);
            }
        }
    );
}

/*
 * -----------------------------------
 * UI EVENTS
 * -----------------------------------
 */
function bindUIEvents() {

    document
        .getElementById("newDiagramBtn")
        .addEventListener(
            "click",
            createNewDiagram
        );

    document
        .getElementById("saveBtn")
        .addEventListener(
            "click",
            saveDiagram
        );

    document
        .getElementById("downloadBtn")
        .addEventListener(
            "click",
            downloadDiagram
        );

    document
        .getElementById("uploadBtn")
        .addEventListener(
            "click",
            openDiagram
        );

    document
        .getElementById("zoomInBtn")
        .addEventListener(
            "click",
            zoomIn
        );

    document
        .getElementById("zoomOutBtn")
        .addEventListener(
            "click",
            zoomOut
        );

    document
        .getElementById("fitBtn")
        .addEventListener(
            "click",
            fitDiagram
        );

    document
        .getElementById("fileInput")
        .addEventListener(
            "change",
            handleFileSelect
        );
}

/*
 * -----------------------------------
 * SHORTCUTS
 * -----------------------------------
 */
document.addEventListener(
    "keydown",
    async function(e) {

        if (!modeler) {
            return;
        }

        /*
         * CTRL + S
         */
        if (
            e.ctrlKey &&
            e.key === "s"
        ) {

            e.preventDefault();

            await saveDiagram();
        }

        /*
         * CTRL + O
         */
        if (
            e.ctrlKey &&
            e.key === "o"
        ) {

            e.preventDefault();

            openDiagram();
        }
    }
);

/*
 * -----------------------------------
 * START APPLICATION
 * -----------------------------------
 */
window.addEventListener(
    "load",
    async function() {

        try {

            await initializeModeler();

            registerEvents();

            bindUIEvents();

            const existingXml =
                localStorage.getItem(
                    STORAGE_KEY
                );

            if (
                existingXml &&
                existingXml.trim() !== ""
            ) {

                await loadDiagram(
                    existingXml,
                    "Existing BPMN Loaded"
                );

            } else {

                await createNewDiagram();
            }

        } catch (err) {

            console.error(err);

            updateStatus(
                "Startup Failed"
            );
        }
    }
);