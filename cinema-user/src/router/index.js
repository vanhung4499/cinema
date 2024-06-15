import Vue from 'vue'
import VueRouter from 'vue-router'
import { Message }from 'element-ui'

import Login from "../views/Login";
import Welcome from "../views/Welcome";
import Register from "../views/Register";
import Error404 from "../views/Error404";

import Home from "../views/home/Home";
import Movie from "../views/movie/Movie";
import AboutUs from "../views/cinema/AboutUs";
import RankingList from "../views/rankinglist/RankingList";
import TotalBoxOfficeList from "../views/rankinglist/TotalBoxOfficeList";
import DomesticBoxOfficeList from "../views/rankinglist/DomesticBoxOfficeList";
import ForeignBoxOfficeList from "../views/rankinglist/ForeignBoxOfficeList";
import MovieOngoing from "../views/movie/MovieOngoing";
import MovieUpcoming from "../views/movie/MovieUpcoming";
import MovieClassics from "../views/movie/MovieClassics";
import MovieInfo from "../views/movie/MovieInfo";
import ChooseSession from "../views/cinema/ChooseSession";
import ChooseSeat from "../views/pay/ChooseSeat";
import BillDetail from "../views/pay/BillDetail";
import Search from "../views/search/Search";
import SearchMovie from "../views/search/SearchMovie";
import UserMenu from "../views/user/UserMenu";
import UserInfo from "../views/user/UserInfo";
import BillInfo from "../views/user/BillInfo";

Vue.use(VueRouter)

const routes = [
  {
    path:'/', redirect: 'home'
  },
  {
    path: '/login',
    component: Login
  },
  {
    path: '/welcome',
    component: Welcome,
    redirect: { name: 'home'},
    children: [
      { path: '/home/', name: 'home', component: Home },
      {
        path: '/userMenu',
        name: 'userMenu',
        component: UserMenu,
        redirect: {name: 'userInfo'},
        children:[
          {path: '/user', component: UserInfo,name:'userInfo'},
          {path: '/bill', component: BillInfo, name:'billInfo'}
        ]
      },
      {
        path: '/movie/',
        component: Movie,
        name: 'movie',
        redirect: {name: 'NowPlaying'},
        children: [
          { path: 'movieOngoing', name:'NowPlaying', component: MovieOngoing },
          { path: 'movieUpcoming', name: 'Upcoming', component: MovieUpcoming },
          { path: 'movieClassics', name: 'Classic Films', component: MovieClassics }
        ]
      },
      {
        path: '/movieInfo/:movieId',
        name: 'Movie Details',
        component: MovieInfo
      },
      {
        path: '/aboutUs/',
        name: 'About Us',
        component: AboutUs
      },
      {
        path: '/chooseSession/:cinemaId',
        name: 'Cinema Details',
        component: ChooseSession
      },
      {
        path: '/rankingList/',
        component: RankingList,
        name: 'rankingList',
        redirect: '/rankingList/totalBoxOfficeList',
        children: [
          { path: 'totalBoxOfficeList', name: 'Total Box Office', component: TotalBoxOfficeList},
          { path: 'domesticBoxOfficeList', name: 'Domestic Box Office', component: DomesticBoxOfficeList},
          { path: 'foreignBoxOfficeList', name: 'Foreign Box Office', component: ForeignBoxOfficeList}
        ]
      },
      {
        path: '/chooseSeat/:sessionId',
        component: ChooseSeat,
        name: 'chooseSeat'
      },
      {
        path: '/billDetail/:billId',
        component: BillDetail,
        name: 'billDetail'
      },
      {
        path: '/search/',
        component: Search,
        name: 'search',
        redirect: { name:'searchMovie' },
        children: [
          {
            path:'searchMovie',
            component: SearchMovie,
            name: 'searchMovie'
          }
        ]
      }
    ]
  },
  {
    path: '/register',
    component: Register
  },
  {
    path: '/*',
    component: Error404
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

// Mounting router navigation guards
router.beforeEach((to, from, next) =>{
  // 'to' represents the path to be accessed
  // 'from' represents the path from which the user is coming
  // 'next' is a function for passing control to the next middleware or route handler

  if (to.path === '/user' || to.path === '/bill' || to.path.indexOf('/billDetail') !== -1) {
    // Get token
    const token = window.sessionStorage.getItem("token");
    if (!token) {
      Message.error('Sorry, please login first');
      return next('/login');
    }
  }
  next();
});

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location){
  return originalPush.call(this, location).catch(err => err)
}
export default router
