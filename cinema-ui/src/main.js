import Vue from 'vue'
import './plugins/axios'         // Imports Axios plugin configuration
import App from './App.vue'      // Imports the root component App.vue
import router from './router'    // Imports Vue Router configuration
import store from './store'      // Imports Vuex store configuration
import './plugins/element.js'    // Imports Element UI plugin configuration
// Import icon fonts
import './assets/css/fonts/iconfont.css'
import { Message } from 'element-ui'
import has from './assets/js/permission'
import global_variable from "@/components/global_variable";

Vue.prototype.global = global_variable

axios.defaults.baseURL = 'http://127.0.0.1:8181/'   // Sets default base URL for Axios requests
axios.interceptors.request.use(config => {
  config.headers.Token = window.sessionStorage.getItem('token')   // Sets Token header for Axios requests
  return config
})

// Error code messages
const codeMessage = {
  401: 'User does not have permission, please log in',
  403: 'User does not have permission, please contact the administrator',
  404: 'The requested resource does not exist',
  406: 'The request format is not available',
  410: 'The requested resource is permanently deleted and will not be obtained again',
  422: 'A validation error occurred when creating an object',
  500: 'Server error, please check the server',
  502: 'Gateway error',
  503: 'Service unavailable, server temporarily overloaded or under maintenance',
  504: 'Gateway timeout'
};

axios.interceptors.response.use(response => {
  return response
}, error => {
  if (error) {
    if (!error.response) {
      return console.log('Error', error.message);
    }
    // Get status code
    const status = error.response.status;
    const errorText = codeMessage[status] || error.response.statusText;

    // Display error message
    Message.error(errorText)

    // Error status handling
    if (status === 401 || status === 403) {
      router.push('/login')   // Redirects to login page for unauthorized access
    } else if (status >= 404 && status < 422) {
      router.push('/404')     // Redirects to 404 page for not found errors
    }
  }
  return Promise.reject(error)
})

Vue.config.productionTip = false   // Prevents Vue from generating production tips in the console

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')   // Mounts the root component App.vue to the DOM element with id 'app'
