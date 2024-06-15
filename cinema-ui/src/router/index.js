import Vue from 'vue'
import VueRouter from 'vue-router'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

import Login from '../views/Login';
import Welcome from "../views/Welcome";
import Home from '../views/Home.vue';
import CinemaInfo from "../views/cinema/CinemaInfo";
import MovieInfo from "../views/movie/MovieInfo";
import MovieCategory from "../views/movie/MovieCategory";
import HallInfo from "../views/hall/HallInfo";
import SessionInfo from "../views/hall/SessionInfo";
import BillInfo from "../views/bill/BillInfo";
import UserInfo from "../views/user/UserInfo";
import RoleInfo from "../views/role/RoleInfo";
import ResourceInfo from "../views/role/ResourceInfo";
import Error404 from "../views/Error404";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: Login,
    redirect: '/login'
  },
  {
    path: '/login',
    component: Login
  },
  {
    path: '/home',
    component: Home,
    redirect: '/welcome',
    children: [
      { path: '/welcome', component: Welcome },
      { path: '/cinema', component: CinemaInfo },
      { path: '/movie', component: MovieInfo },
      { path: '/movieCategory', component: MovieCategory },
      { path: '/hall', component: HallInfo },
      { path: '/session', component: SessionInfo },
      { path: '/user', component: UserInfo },
      { path: '/bill', component: BillInfo },
      { path: '/role', component: RoleInfo },
      { path: '/resource', component: ResourceInfo }
    ]
  },
  {
    path: '/*',
    component: Error404
  }
]

const router = new VueRouter({
  routes,
  mode: 'history',
})

// Route navigation guard
router.beforeEach((to, from, next) => {
  // to: destination route object
  // from: route object where navigation originated from
  // next: function to be called to resolve the hook

  // Allow navigation to /login and /404 without authentication
  if (to.path === '/login' || to.path === '/404') return next();

  // Start top progress bar
  NProgress.start()

  // Check for token in session storage
  const token = window.sessionStorage.getItem("token")
  if (!token) {
    Message.error('Please log in first')
    return next('/login');
  }
  next();
})

// After each navigation, finish the progress bar
router.afterEach(() => {
  NProgress.done()
})

// Override VueRouter's default push method to suppress error messages
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

export default router
