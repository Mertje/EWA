import { getLocalStorageItem } from './localStorage';


export const BASEURL = import.meta.env.VITE_APP_BACKEND_URL;

function getUrl(endpoint) {
  return `${BASEURL}/api/v1/${endpoint}`;
}

export async function getData(endpoint) {
  const url = getUrl(endpoint);
  const token = getLocalStorageItem('token');

  try {
    if (!token) {
      throw new Error('No token found');
    }
    const response = await fetch(url, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });
    return await response.json();
  } catch (error) {
    console.error('Error during GET request:', error);
    throw error;
  }
}

export async function postData(endpoint, data, applicationJsonType = true) {
    const url = getUrl(endpoint);
    const token = getLocalStorageItem('token');
    const headers = {'Authorization': `Bearer ${token}`}

    if (applicationJsonType) {
      headers['Content-Type'] = 'application/json'
    }

    try {
      if (!token && !endpoint.startsWith('auth')) {
        throw new Error('No token found');
      }
      const response = await fetch(url, {
        method: 'POST',
        headers: headers,
        body: applicationJsonType ? JSON.stringify(data) : data,
      });

      if(response.ok) {
        return await response.json();
      }
      throw await response.json();

    } catch (error) {
      return error;
    }
  }

export async function putData(endpoint, data) {
    const url = getUrl(endpoint);
    const token = getLocalStorageItem('token');

    try {
      if (!token) {
        throw new Error('No token found');
      }
      const response = await fetch(url, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(data),
      });

      if(response.ok) {
        return response;
      }
      throw await response.json();

    } catch (error) {
      return error;
    }
  }