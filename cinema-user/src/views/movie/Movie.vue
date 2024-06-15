<template>
  <el-container>
    <el-header>
      <el-menu :default-active="$route.path"
               class="nav-menu"
               mode="horizontal"
               :router="true"
               background-color="#545c64"
               text-color="#fff"
               active-text-color="#ffd04b">
        <el-menu-item :index="item.path" v-for="item in menuList" :key="item.id">{{item.name}}</el-menu-item>
      </el-menu>
    </el-header>
    <el-main>
      <!-- Category Query -->
      <div class="tags-container">
        <div class="tags-line">
          <div class="tags-title">Category:</div>
          <el-radio-group v-model="categoryRadio">
            <el-radio-button :label="0">All</el-radio-button>
            <el-radio-button :label="item.movieCategoryId" v-for="item in categoryList" :key="item.movieCategoryId">{{item.movieCategoryName}}</el-radio-button>
          </el-radio-group>
        </div>
      </div>

      <!-- Sort by Conditions -->
      <div class="order-by-container">
        <el-radio v-model="orderByColumn" label="releaseDate">Sort by Time</el-radio>
        <!--        <el-radio v-model="orderByColumn" label="movieScore">Sort by Rating</el-radio>-->
      </div>

      <!--      <router-view :movieCategoryId="categoryRadio" :movieAreaId="areaRadio" :movieAgeId="ageRadio" :orderByColumn="orderByColumn"></router-view>-->
      <router-view :movieCategoryId="categoryRadio" :orderByColumn="orderByColumn"></router-view>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: "Movie",
  data(){
    return {
      menuList: [
        {
          id: '1',
          name: 'Now Showing',
          path: '/movie/movieOngoing'
        },
        {
          id: '2',
          name: 'Coming Soon',
          path: '/movie/movieUpcoming'
        },
        {
          id: '3',
          name: 'Classic Movies',
          path: '/movie/movieClassics'
        }
      ],
      categoryRadio: 0,
      categoryList: [],
      orderByColumn: 'releaseDate'
    }
  },
  created() {
    this.getCategoryList()
  },
  methods: {
    async getCategoryList() {
      const { data : res } = await axios.get('sysMovieCategory/find')
      if(res.code !== 200) return this.$message.error('Failed to fetch server information')
      this.categoryList = res.data
    }
  },
  watch:{
    '$route'(){
      // Reset category options when the page changes
      this.categoryRadio = 0
      this.orderByColumn = 'releaseDate'
    }
  }
}
</script>

<style scoped>
.el-header{
  padding: 0px;
}

.nav-menu{
  display: flex;
  justify-content: space-around;
}

.el-menu-item{
  font-size: 16px;
}

.el-menu-item:hover{
  color: #FFFFFF !important;
}

.el-main{
  width: 1200px;
  margin: 0 auto;
}

.tags-container{
  border: solid #EEEEEE 1px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
}

.tags-line{
  width: 100%;
  display: flex;
  padding: 10px;
  box-sizing: border-box;
  border-bottom: solid #EEEEEE 1px;
}

.tags-line:last-child{
  border-bottom: 0;
}

.tags-title {
  width: 5%;
  height: 24px;
  line-height: 24px;
  float: left;
  color: #999;
  font-size: 14px;
}

.el-radio-group{
  width: 92%;
  margin-left: 20px;
}

.el-radio-button {
  margin-right: 10px;
}

.el-radio-button:last-child{
  margin-right: 0;
}

.el-radio-button >>> .el-radio-button__inner {
  border: solid #eee 0px !important;
  border-radius: 50px !important;
}

.order-by-container{
  margin: 30px 10px 0 10px;
}
</style>
