import { describe, it, expect, vi } from 'vitest';
import { mount, flushPromises } from '@vue/test-utils';
import UserLogin from "@/components/authentication/UserLogin.vue";
import { postData } from "@/utils/fetch";
import { authUser } from "@/utils/auth";

vi.mock('@/utils/fetch', () => ({
  postData: vi.fn(),
}));
vi.mock('@/utils/auth', () => ({
  authUser: vi.fn(),
}));

describe('UserLogin', () => {

  // Mert
  it('displays an error message on login failure', async () => {
    const mockResponse = { error: true, message: 'Invalid credentials' };
    postData.mockImplementation(() => Promise.resolve(mockResponse));

    const wrapper = mount(UserLogin);
    const form = wrapper.find('form');
    await form.trigger('submit.prevent');

    await flushPromises();
    expect(wrapper.text()).toContain(mockResponse.message);
  });

  // Mert
  it('calls authUser on successful login', async () => {
    const mockResponse = { error: false, token: 'fake-token' };
    postData.mockImplementation(() => Promise.resolve(mockResponse));

    const wrapper = mount(UserLogin);
    const form = wrapper.find('form');
    await form.trigger('submit.prevent');

    await flushPromises();
    expect(authUser).toHaveBeenCalledWith(mockResponse);
  });

  // Mert
  it('binds input field values correctly', async () => {
    const wrapper = mount(UserLogin);
    const emailInput = wrapper.find('input[type="email"]');
    const passwordInput = wrapper.find('input[type="password"]');

    await emailInput.setValue('test@example.com');
    await passwordInput.setValue('password123');

    expect(emailInput.element.value).toBe('test@example.com');
    expect(passwordInput.element.value).toBe('password123');
  });

  // Mert
  it('submits the form with input values', async () => {
    postData.mockImplementation(() => Promise.resolve({ error: false }));

    const wrapper = mount(UserLogin);
    const form = wrapper.find('form');
    const emailInput = wrapper.find('input[type="email"]');
    const passwordInput = wrapper.find('input[type="password"]');

    await emailInput.setValue('test@example.com');
    await passwordInput.setValue('password123');
    await form.trigger('submit.prevent');

    await flushPromises();
    expect(postData).toHaveBeenCalledWith('auth/login', {
      email: 'test@example.com',
      password: 'password123',
    });
  });

  // Mert
  it('renders all required elements', () => {
    const wrapper = mount(UserLogin);

    expect(wrapper.find('label[for="email"]').exists()).toBe(true);
    expect(wrapper.find('input[type="email"]').exists()).toBe(true);
    expect(wrapper.find('label[for="password"]').exists()).toBe(true);
    expect(wrapper.find('input[type="password"]').exists()).toBe(true);
    expect(wrapper.find('button[type="submit"]').exists()).toBe(true);
  });


  // Mert
  it('sets login error message on failed login attempt', async () => {
    postData.mockImplementationOnce(() => Promise.resolve({ error: true, message: 'Invalid credentials' }));

    const wrapper = mount(UserLogin);
    let form = wrapper.find('form');

    // Simulate a failed login attempt
    await form.trigger('submit.prevent');
    await flushPromises();

    // Check that error message is set
    expect(wrapper.vm.loginError).toBe('Invalid credentials');
  });

  // Mert
  it('clears login error message on successful login attempt', async () => {
    postData.mockImplementationOnce(() => Promise.resolve({ error: false, token: 'fake-token' }));

    const wrapper = mount(UserLogin);
    let form = wrapper.find('form');

    // Simulate a successful login attempt
    await form.trigger('submit.prevent');
    await flushPromises();

    // Check that error message is cleared
    expect(wrapper.vm.loginError).toBe('');
  });



});