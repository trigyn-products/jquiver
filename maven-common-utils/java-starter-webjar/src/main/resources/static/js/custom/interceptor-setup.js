import { enableInterceptor } from './interceptor-fetch.js';
import { addRequestInterceptor } from './interceptor-fetch.js';

/* -------------------- Utilities -------------------- */

function normalizeUserManagementFlag() {
    window.enableUserManagement =
        (window.enableUserManagement === true || window.enableUserManagement === "true");
}

function getCookie(name) {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return match ? decodeURIComponent(match[2]) : null;
}

/* -------------------- Crypto -------------------- */

function decryptToken(encryptedBase64, keyStr) {
    const key = CryptoJS.enc.Utf8.parse(String(keyStr).padEnd(16, '0').substring(0, 16));
    return CryptoJS.AES.decrypt(encryptedBase64, key, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    }).toString(CryptoJS.enc.Utf8);
}

function encryptToken(plainToken, newKey) {
    const key = CryptoJS.enc.Utf8.parse(newKey.padEnd(16, '0').substring(0, 16));
    return CryptoJS.AES.encrypt(plainToken, key, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    }).toString();
}

/* -------------------- Validation -------------------- */

function hasValidAuthCookies() {
    normalizeUserManagementFlag();

    if (!window.enableUserManagement) {
        return true; // feature OFF → skip auth completely
    }

    const authToken = getCookie('auth_token');
    const requestId = getCookie('r');

    const isValid = !!(authToken && requestId);
    if (!isValid) {
        console.warn('Missing or invalid auth_token or requestId');
    }
    return isValid;
}

/* -------------------- Token Refresh -------------------- */

function getAuthToken() {
    return new Promise((resolve, reject) => {

        const decrypted = localStorage.getItem("decrypted_token");
        if (!decrypted) {
            resolve(null); // 🔒 NEVER re-encrypt without base token
            return;
        }

        $.ajax({
            url: contextPath + "/cf/fetchSalt",
            method: "GET",
            dataType: "json",
            success: (data) => {
                if (data?.salt && data?.requestId) {
                    resolve({
                        encryptedToken: encryptToken(decrypted, data.salt),
                        requestId: data.requestId
                    });
                } else {
                    resolve(null);
                }
            },
            error: reject
        });
    });
}

/* -------------------- Fetch Interceptor -------------------- */

addRequestInterceptor(async (url, options = {}) => {

    normalizeUserManagementFlag();
    if (!window.enableUserManagement) {
        return { url, options };
    }

    if (
        url.includes("/cf/fetchSalt") ||
        url.includes("/fsd") ||
        url.includes("/cf/login") ||
        url.includes("/logout")
    ) {
        return { url, options };
    }

    if (!hasValidAuthCookies()) {
        return { url, options };
    }

    const auth = await getAuthToken();
    if (!auth) {
        return { url, options };
    }

    document.cookie = `auth_token=${auth.encryptedToken}; path=/; secure; samesite=strict`;
    document.cookie = `r=${auth.requestId}; path=/; secure; samesite=strict`;

    options.headers = {
        ...options.headers,
        Authorization: `Bearer ${auth.encryptedToken}`
    };

    return { url, options };
});

/* -------------------- Page Load Decryption -------------------- */

(async function loadDecryptedTokenOnPageLoad() {

    normalizeUserManagementFlag();
    if (!window.enableUserManagement) {
        return;
    }

    if (!hasValidAuthCookies()) {
        return;
    }

    const jwtEncrypted = getCookie("auth_token");
    const oldSaltId = getCookie("r");
    if (!jwtEncrypted || !oldSaltId) {
        return;
    }

    try {
        const res = await fetch(`${contextPath}${window.apiPath}/fsd?oldSaltId=${oldSaltId}`);
        const data = await res.json();

        if (!data?.[0]?.salt) {
            return;
        }

        const decryptedToken = decryptToken(jwtEncrypted, data[0].salt);
        if (!decryptedToken) {
            return;
        }

        localStorage.setItem("decrypted_token", decryptedToken);

        //  enable interceptor ONCE after successful decrypt
        enableInterceptor();

    } catch (e) {
        console.error("Token decrypt error", e);
    }
})();
