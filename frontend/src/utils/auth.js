import { getLocalStorageItem, setLocalStorageItem } from "./localStorage";
import router from "@/router";
import { ref } from "vue";


// TODO hou er rekening mee dat de ref reset als je de pagina hard refreshed in de router/index.js.
//  Ik denk zelf dat het te maken heeft met dat die file geen Vue file is.
export const isAuthenticated = ref(false);

const parseJWT = (JWT) => {
    return JSON.parse(atob(JWT.split('.')[1]));
}

const isTokenExpired = (tokenTime) => {
    return Date.now() >= tokenTime * 1000;
}

export const checkAuthentication = () => {
    const token = getLocalStorageItem('token');
    if (!token) {
        isAuthenticated.value = false;
        return false;
    }
    if (token === 'undefined') {
        logoutUser();
        return false;
    };

    const parsedToken = parseJWT(token);
    const isTokenValid = parsedToken && !isTokenExpired(parsedToken.exp);
    isAuthenticated.value = isTokenValid;
    return isTokenValid;
}

export const authUser = (data) => {
    setLocalStorageItem('token', data.token);
    setLocalStorageItem('profilePicture', data.profilePictureUrl);
    setLocalStorageItem('userName', data.userName);

    checkAuthentication();
    router.push('/');
}

export const logoutUser = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('profilePicture');
    localStorage.removeItem('userName');

    checkAuthentication();
    router.push('/');
}