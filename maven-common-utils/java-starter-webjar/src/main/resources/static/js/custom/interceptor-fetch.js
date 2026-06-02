const requestInterceptors = [];
const responseInterceptors = [];

export function addRequestInterceptor(interceptor) {
	requestInterceptors.push(interceptor);
}

export function addResponseInterceptor(interceptor) {
	responseInterceptors.push(interceptor);
}

export function enableInterceptor() {
	// Save original fetch only once
	if (!window.originalFetch) {
		window.originalFetch = window.fetch.bind(window);
	}
	window.fetch = async function(input, init = {}) {
		let url = typeof input === "string" ? input : input.url;
		let options = typeof input === "string" ? init : input;

		// Apply request interceptors
		for (const interceptor of requestInterceptors) {
			const result = await interceptor(url, options);
			url = result.url;
			options = result.options;
		}

		let response = await window.originalFetch(url, options);

		// Apply response interceptors
		for (const interceptor of responseInterceptors) {
			response = await interceptor(response);
		}

		return response;
	};
}
