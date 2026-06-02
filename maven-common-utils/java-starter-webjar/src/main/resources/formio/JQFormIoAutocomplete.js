const FieldComponent = Formio.Components.components.field;

class JQFormIoAutocomplete extends FieldComponent {
	selectedItems = [];
	prefetchData = [];
	isPrefetched = false;
	runtimeRequestParams = {};
	currentStartIndex = 0;
	pageSize = 10;
	isLoading = false;
	hasMoreData = true;
	lastQuery = '';


	static schema(...extend) {
		return super.schema({
			type: 'typeautocompletecomponent',
			label: 'Autocomplete',
			key: 'ac_id',
			input: true,
			multiple: false,
			persistent: true,
			defaultValue: [],
			clearOnHide: false,
			validate: { required: false },
			typeautotype: '',
			additionalParamaters: {},
			requestParameters: {},
			prefetch: true
		}, ...extend);
	}

	static get builderInfo() {
		return {
			title: 'Autocomplete',
			group: 'jquiver',
			icon: 'fa fa-search',
			weight: 70,
			schema: JQFormIoAutocomplete.schema()
		};
	}

	static editForm = (...args) => {
		const editForm = FieldComponent.editForm(...args);
		const display = Formio.Utils.getComponent(editForm.components, 'display', true);

		if (display) {
			display.components.unshift({
				type: 'select',
				label: 'TypeAhead Source',
				key: 'typeautotype',
				dataSrc: 'values',
				data: {
					values: JSON.parse(getAutocompletes().responseText || '[]')
				}
			});
		}
		return editForm;
	};

	getRuntimeConfig() {
		return (
			window.formioTypeAheadConfig?.[this.component.typeautotype] ||
			window.formioTypeAheadConfig?.__default__ ||
			{}
		);
	}

	render() {
		const isRuntime =
			!this.builderMode &&
			!this.options?.builder &&
			!this.options?.preview;

		return super.render(`
      <div class="form-group customedropselctbox">
        <div class="col-inner-form full-form-fields">

          ${isRuntime && this.getRuntimeConfig().multiple ? `
		  <div class="multiselectcount_clear_block">
            <div id="${this.component.key}_removeAll"
                 class="pull-right disable_cls"
                 style="display:none;">
              <span class="clearall-disabled-cls">Clear All</span>
            </div>

            <div id="${this.component.key}_count"
                 class="multiselectcount pull-right">
              <span title="hide show">0</span>
            </div>
          </div>
          ` : ''}

          <div class="search-cover">
            <input
              class="form-control jws-autocomplete-input"
              type="text"
              autocomplete="off"
              spellcheck="false"
            />
            <i class="fa fa-search" aria-hidden="true"></i>
            <ul class="dropdown-menu jws-autocomplete-dropdown" style="max-height: 200px; overflow-y: auto;"></ul>
          </div>
        </div>

		${this.getRuntimeConfig().multiple ? `
        <div id="${this.component.key}_selectedOptionsWrapper"
             class="ml-selected-items-wrapper">
          <div class="ml-selected-items-div">
            <ul id="${this.component.key}_selectedOptions_ul"
                class="ml-selected-items-list"></ul>
          </div>
        </div>
		` : ''}

        <input type="hidden" name="data[${this.component.key}]" />
      </div>
    `);
	}

	attach(element) {
		const attached = super.attach(element);

		this.input = element.querySelector('.jws-autocomplete-input');
		this.dropdown = element.querySelector('.jws-autocomplete-dropdown');
		this.hiddenInput = element.querySelector('input[type=hidden]');
		this.selectedList = element.querySelector(
			`#${this.component.key}_selectedOptions_ul`
		);

		this.countDiv = element.querySelector(`#${this.component.key}_count`);
		this.clearDiv = element.querySelector(`#${this.component.key}_removeAll`);

		const isRuntime = !this.builderMode && !this.options?.builder && !this.options?.preview;

		if (this.builderMode || this.options?.builder || this.options?.preview) {
			return attached;
		}

		this.addEventListener(this.dropdown, 'scroll', () => {

			const scrollTop = this.dropdown.scrollTop;
			const scrollHeight = this.dropdown.scrollHeight;
			const clientHeight = this.dropdown.clientHeight;

			if (scrollTop + clientHeight >= scrollHeight - 5) {
				this.loadAutocomplete(this.lastQuery, true);
			}
		});

		this.addEventListener(this.input, 'focus', () => {
			if (!this.getRuntimeConfig().prefetch) return;

			const parentKey = this.component.additionalParamaters?.parent_id;

			if (parentKey && this.root?.data?.[parentKey] == null) {
				return;
			}

			this.loadAutocomplete('', false);
		});

		this.addEventListener(this.input, 'input', e =>
			this.loadAutocomplete(e.target.value, false)
		);

		this.addEventListener(this.input, 'blur', () =>
			setTimeout(() => this.dropdown.classList.remove('show'), 200)
		);

		if (this.countDiv) {
			this.addEventListener(this.countDiv, 'click', () => {
				const wrapper = element.querySelector(
					`#${this.component.key}_selectedOptionsWrapper`
				);
				wrapper.style.display =
					wrapper.style.display === 'none' ? 'block' : 'none';
			});
		}

		if (this.clearDiv) {
			this.addEventListener(this.clearDiv, 'click', () => {
				showClearAllConfirm(
					'Are you sure you want to clear all selected items?',
					() => {
						this.selectedItems = [];
						this.renderSelectedItems();
						this.updateHiddenValue();
						this.input.value = '';
						this.dropdown.classList.remove('show');
					}
				);
			});
		}

		document.addEventListener('click', e => {
			if (!this.dropdown.contains(e.target) && e.target !== this.input) {
				this.dropdown.classList.remove('show');
			}
		});

		if (isRuntime) {
			const runtimeCfg = this.getRuntimeConfig() || {};

			this.component.additionalParamaters = {
				parent_id: this.component.additionalParamaters?.parent_id ?? null,
				...(runtimeCfg.additionalParamaters || {})
			};

			const parentKey = this.component.additionalParamaters?.parent_id;

			if (parentKey) {
				this.root.on('change', (event) => {

					if (!event.changed) return;

					if (event.changed.component.key === parentKey) {
						const parentValue = event.changed.value;
						this.handleParentChange(parentValue);
					}
				});
			}
		}

		return attached;
	}

	loadAutocomplete(query, append = false) {
		if (!this.component.typeautotype) return;
		const parentKey = this.component.additionalParamaters?.parent_id;

		if (parentKey && this.root?.data?.[parentKey] == null) {
			return;
		}

		// Reset when new search
		if (!append) {
			this.currentStartIndex = 0;
			this.hasMoreData = true;
			this.dropdown.innerHTML = '';
		}

		if (!this.hasMoreData || this.isLoading) return;

		this.isLoading = true;
		this.lastQuery = query;

		getAutocompleteData({
			type: this.component.typeautotype,
			query,
			startIndex: this.currentStartIndex,
			pageSize: this.pageSize,
			additionalParamaters: this.runtimeRequestParams,
			requestParameters: this.component.requestParameters
		}).done(data => {

			if (!data || data.length < this.pageSize) {
				this.hasMoreData = false;
			}

			this.currentStartIndex += this.pageSize;

			const cfg = this.getRuntimeConfig();
			const firstItem = data[0] || {};
			const valueField = cfg.valueField || Object.keys(firstItem)[0];
			const labelField = cfg.labelField || Object.keys(firstItem)[1];

			const items = data.map(d => ({
				label: cfg.extractText ? cfg.extractText(d) : d[labelField],
				value: d[valueField],
				raw: d
			}));

			this.renderDropdown(items, append);

			this.isLoading = false;
		});
	}

	renderDropdown(items, append = false) {
		if (!append) {
			this.dropdown.innerHTML = '';
		}

		if (!items.length && !append) {
			this.dropdown.classList.remove('show');
			return;
		}

		const cfg = this.getRuntimeConfig();

		items.forEach(item => {
			const li = document.createElement('li');
			li.className = 'dropdown-item jws-rich-autocomplete-multiple';
			li.innerHTML = cfg.render
				? cfg.render(item.raw)
				: `<div class="jws-rich-autocomplete-text">${item.label}</div>`;

			this.addEventListener(li, 'mousedown', e => {
				e.preventDefault();
				if (this.getRuntimeConfig().multiple) {
					if (this.selectedItems.some(si => si.value === item.value)) return;

					this.selectedItems.push(item);
					this.renderSelectedItems();
					this.input.value = '';
				}
				else {
					this.selectedItems = [item];
					this.input.value = item.label;
				}
				//this.renderSelectedItems();
				this.updateHiddenValue();
				this.dropdown.classList.remove('show');
			});

			this.dropdown.appendChild(li);
		});

		this.dropdown.classList.add('show');
	}

	renderSelectedItems() {
		if (this.getRuntimeConfig().multiple) {
			this.selectedList.innerHTML = '';
			this.selectedItems.forEach(item => {
				const li = document.createElement('li');
				li.className = 'jws-rich-autocomplete-multiple';

				li.innerHTML = `
        <div class="jws-rich-autocomplete-text">${item.label}</div>
        <span class="float-right closeicon">
          <i class="fa-regular fa-circle-xmark"></i>
        </span>
      `;

				li.querySelector('.closeicon').addEventListener('click', () => {
					this.selectedItems = this.selectedItems.filter(
						si => si.value !== item.value
					);
					this.renderSelectedItems();
					this.updateHiddenValue();
				});

				this.selectedList.appendChild(li);
			});

			this.countDiv.innerHTML =
				`<span title="hide show">${this.selectedItems.length}</span>`;

			this.clearDiv.style.display =
				this.selectedItems.length ? 'inline-block' : 'none';
		}
		else {
			if (this.selectedItems.length) {
				this.input.value = this.selectedItems[0].label;
			} else {
				this.input.value = '';
			}
		}


	}

	updateHiddenValue() {
		const values = this.selectedItems.map(i => i.value);

		if (this.getRuntimeConfig().multiple) {
			this.hiddenInput.value = JSON.stringify(values);
			this.dataValue = values;
		} else {
			const singleValue = values[0] || null;
			this.hiddenInput.value = singleValue;
			this.dataValue = singleValue;
		}

		this.triggerChange();
	}

	setValue(value) {
		if (value === this.dataValue) {
			return true;
		}

		if (value == null || value === '') {
			if (this.dataValue == null) {
				return true;
			}

			this.selectedItems = [];
			this.dataValue = null;
			this.renderSelectedItems();
			this.updateHiddenValue();
			return true;
		}

		// Normalize value into an array
		let ids = [];
		if (Array.isArray(value)) {
			ids = value;
		} else {
			try {
				const parsed = JSON.parse(value);
				ids = Array.isArray(parsed) ? parsed : [parsed];
			} catch (e) {
				ids = [value]; // single value
			}
		}

		this.dataValue = this.getRuntimeConfig().multiple ? ids : ids[0];

		const parentKey = this.component.additionalParamaters?.parent_id;
		const parentValue = parentKey ? this.root?.data?.[parentKey] ?? null : null;
		this.runtimeRequestParams.parent_id = parentValue;

		// Fetch autocomplete data to get labels
		getAutocompleteData({
			type: this.component.typeautotype,
			query: '',
			additionalParamaters: this.runtimeRequestParams,
			requestParameters: this.component.requestParameters
		}).done(data => {
			const cfg = this.getRuntimeConfig();
			const valueField = cfg.valueField || Object.keys(data[0] || {})[0];

			this.selectedItems = ids.map(id => {
				const found = data.find(d => d[valueField] == id);
				return {
					value: id,
					label: found ? (cfg.extractText ? cfg.extractText(found) : found[cfg.labelField || Object.keys(found)[1]]) : id,
					raw: found
				};
			});

			this.renderSelectedItems();
			this.updateHiddenValue();
		});

		return true;
	}

	getValue() {
		return this.dataValue ?? null;
	}

	handleParentChange(parentValue) {
		const value = Array.isArray(parentValue) ? parentValue : parentValue ? [parentValue] : [];

		// Store runtime param with actual parent value
		this.runtimeRequestParams.parent_id = value[0] ?? null;

		this.selectedItems = [];
		this.dropdown.innerHTML = '';
		this.hasMoreData = true;
		this.currentStartIndex = 0;

		if (this.dataValue != null && this.dataValue !== '') {
			this.setValue(this.dataValue);
		}
	}
}

function getAutocompletes() {
	return $.ajax({
		type: "GET",
		url: contextPath + apiPath + "/loadAutocompletes",
		async: false
	});
}

function getAutocompleteData(options) {
	return $.ajax({
		type: "POST",
		url: contextPath + "/cf/autocomplete-data",
		data: {
			searchText: options.query,
			autocompleteId: options.type,
			startIndex: options.startIndex || 0,
			pageSize: options.pageSize || 10,
			additionalParamaters: JSON.stringify(options.additionalParamaters || {}),
			requestParameters: JSON.stringify(options.requestParameters || {})
		},
		dataType: "json"
	});
}

function showClearAllConfirm(message, onConfirm) {
	let $dialog = $('<div>', {
		id: 'deleteConfirmation',
		text: message
	}).appendTo('body');

	$dialog.dialog({
		modal: true,
		title: 'Delete',
		resizable: false,
		draggable: true,
		closeOnEscape: true,
		buttons: [
			{
				text: 'Cancel',
				class: 'btn btn-secondary',
				click: function() {
					$(this).dialog('close');
				}
			},
			{
				text: 'Delete',
				class: 'btn btn-primary',
				click: function() {
					$(this).dialog('close');
					onConfirm();
				}
			}
		],
		open: function() {
			$(".ui-dialog-titlebar button")
				.removeClass("ui-dialog-titlebar-close")
				.addClass(
					"ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close"
				)
				.prepend('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>')
				.append('<span class="ui-button-icon-space"></span>');
		}
	});
}

Formio.Components.addComponent(
	'typeautocompletecomponent',
	JQFormIoAutocomplete
);

window.JQFormIoAutocomplete = JQFormIoAutocomplete;
