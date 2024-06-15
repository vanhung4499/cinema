import Vue from 'vue';
import './plugins/axios';
import App from './App.vue';
import router from './router';
import store from './store';
import './plugins/element.js';
import { Message } from 'element-ui';
import './assets/css/fonts/iconfont.css';
import global_variable from "@/components/global_variable";

Vue.prototype.global = global_variable;

Vue.config.productionTip = false;

axios.defaults.baseURL = 'http://127.0.0.1:8181/';
axios.interceptors.request.use(config => {
  config.headers.Token = window.sessionStorage.getItem('token');
  return config;
});

// Error code messages
const codeMessage = {
  401: 'User does not have permission, please login',
  403: 'User does not have permission, please contact the administrator',
  404: 'The resource you are trying to access does not exist',
  406: 'The requested format is not available',
  410: 'The requested resource is permanently deleted and will not be obtained again',
  422: 'An error occurred when creating an object',
  500: 'Server error, please check the server',
  502: 'Gateway error',
  503: 'Service unavailable, server temporarily overloaded or under maintenance',
  504: 'Gateway timeout'
};

axios.interceptors.response.use(response => {
  return response;
}, error => {
  if (error) {
    if (!error.response) {
      return console.log('Error', error.message);
    }
    // Get the status code
    const status = error.response.status;
    const errorText = codeMessage[status] || error.response.statusText;

    // Display error message
    Message.error(errorText);

    // Error status handling
    if (status === 401) {
      router.push('/login');
    } else if (status === 403) {
      router.push('/login');
    } else if (status >= 404 && status < 422) {
      router.push('/404');
    }
  }
  return Promise.reject(error);
});
//
// const i18n = new VueI18n({
//     locale: 'en',
// });


new Vue({
  // i18n,
  router,
  store,
  render: h => h(App)
}).$mount('#app');
