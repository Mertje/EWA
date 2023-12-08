export function setLocalStorageItem(key, value) {
  localStorage.setItem(key, value);
}

export function getLocalStorageItem(key) {
  return localStorage.getItem(key);
}

export function deleteLocalStorageItem(key) {
  localStorage.removeItem(key);
  return true;
}