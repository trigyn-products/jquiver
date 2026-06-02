const HTMLComponent = Formio.Components.components.htmlelement;

class JQFormIOFileBin extends HTMLComponent {

    static schema(...extend) {
        return super.schema({
            type: 'filebincomponent',
            label: 'File Bin',
            key: 'filebin_' + Math.random().toString(36).substr(2, 9),
            input: true, // important for attach() to be called in render
            persistent: false,
            clearOnHide: false,
            validate: { required: false },
            content: `
<div class="dragpasteblock">
    <div class="fileuploadmaster dropzone"></div>
    <div class="jqFmioFileUploadImg" style="display:flex; justify-content:flex-start;">
        <img class="fileUploadIcon"
             src="../webjars/1.0/images/fileupload.png"
             style="cursor:pointer;"
             height="50px" />
    </div>
</div>`
        }, ...extend);
    }

    static get builderInfo() {
        return {
            title: 'File Bin',
            group: 'jquiver',
            icon: 'bi bi-folder2-open',
            weight: 2,
            schema: this.schema()
        };
    }

    render() {
        return super.render(this.component.content);
    }

    attach(element) {
        const attached = super.attach(element);

        // Only initialize Shadow DOM in render mode
        if (!this.builderMode) {
            console.log("FileBin attach running in render mode");
            setTimeout(() => {
                this.initializeShadowInput(element);
            }, 100);
        }

        return attached;
    }

    initializeShadowInput(element) {
        const compEl = element.querySelector(".dragpasteblock");
        if (!compEl || compEl.querySelector(".shadow-host")) return;

        const shadowHost = document.createElement("div");
        shadowHost.className = "shadow-host";
        compEl.appendChild(shadowHost);
        const shadow = shadowHost.attachShadow({ mode: 'open' });

        const fileInput = document.createElement("input");
        fileInput.type = "file";
        fileInput.className = "fileUploadInput";
        fileInput.style.display = "none";
        shadow.appendChild(fileInput);

        const fileUploadIcon = compEl.querySelector(".fileUploadIcon");
        if (fileUploadIcon) {
            fileUploadIcon.addEventListener("click", () => fileInput.click());
        }

        fileInput.addEventListener("change", (e) => {
            const files = e.target.files;
            if (files.length) {
                $(compEl).fileUpload("addFiles", files);
            }
            e.target.value = "";
        });
    }

    // Non-data overrides
    isDataComponent() { return false; }
    checkConditions() { return true; }
    setConditionFlags() { }
    getValue() { return null; }
    setValue() { }
}

// -------------------------------------------------------------
// File Bin dropdown in Builder
JQFormIOFileBin.editForm = (...args) => {
    const HTMLComponent = Formio.Components.components.htmlelement;
    const editForm = HTMLComponent.editForm(...args);

    let displayTab = Formio.Utils.getComponent(editForm.components, "display", true);
    const existing = displayTab.components;

    displayTab.components = [];

    displayTab.components.push({
        type: 'select',
        label: 'File Bin',
        key: 'fileBinType',
        dataSrc: 'values',
        data: { values: JSON.parse(getFileBins().responseText) },
        enableSearch: true,
        persistent: true,
        validate: { required: false }
    });

    existing.forEach(c => displayTab.components.push(c));
    return editForm;
};

// -------------------------------------------------------------
// Fetch file bins
function getFileBins() {
    return $.ajax({
        type: "GET",
        url: contextPath + apiPath + "/loadallfilebins",
        async: false,
        error: () => showMessage("Error fetching File Bins", "error")
    });
}

function getFileBinJsContent(fileBinId) {
	return $.ajax({
		type: "POST",
		url: contextPath + "/cf/gadc",
		async: !1,
		data: {
			templateName: "filebin-default-template",
			selectedTab: "jsContent"
		},
		success: function(data) {
			var jsContent = data.replaceAll("```JavaScript", "");
			jsContent = data.replaceAll("yourFileBinId", fileBinId);
			return jsContent;
		},
		error: function(xhr, error) {
			showMessage("Error occurred while fetching Filebin Javascript content", "error");
		}
	});
}

// -------------------------------------------------------------
// Register
Formio.Components.addComponent('filebincomponent', JQFormIOFileBin);
