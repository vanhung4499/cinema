<template>
  <div class="search-container">
    <!-- A movie in the search results -->
    <div class="search-result" v-for="item in movieList" :key="item.movieId">
      <!-- Movie poster -->
      <div class="img-container">
        <a :href="'/movieInfo/' + item.movieId">
          <img :src="item.moviePoster">
        </a>
      </div>
      <!-- Movie information -->
      <div class="movie-info">
        <div class="movie-name">{{item.movieName}}</div>
        <br/>
        <div class="movie-category">{{item.movieCategoryList.join('/')}}</div>
        <br/>
        <div class="movie-releaseDate">{{item.releaseDate}} Released</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "SearchMovie",
  props:{
    keyword: {
      type: String,
      default: ''
    }
  },
  data() {
    return{
      movieList: [
        {
          movieCategoryList: [],
          movieId: 0,
          movieLength: 0,
          movieName: '',
          moviePoster: '',
          releaseDate: ''
        }
      ]
    }
  },
  created() {
    this.getSearchMovieList()
  },
  methods: {
    async getSearchMovieList() {
      let queryInfo = {
        movieName: this.keyword
      };
      try {
        const { data: res } = await axios.get('sysMovie/find', { params: queryInfo });
        if (res.code !== 200) {
          return this.$message.error('Failed to retrieve information');
        }
        this.movieList = res.data;
        // Process data
        for (let movie of this.movieList) {
          movie.moviePoster = this.global.base + JSON.parse(movie.moviePoster)[0];
          movie.movieCategoryList = movie.movieCategoryList.map((obj, index) => {
            return obj.movieCategoryName;
          });
        }
      } catch (error) {
        console.error('Error fetching movie list:', error);
      }
    }
  },
  watch:{
    '$route'(){
      this.getSearchMovieList()
    }
  }
}
</script>

<style scoped>
.search-container{
  width: 980px;
  margin: 0px auto;
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
}

.search-result{
  width: 460px;
  margin: 30px 0 0 30px;
  display: flex;
}

.img-container{
  width: 160px;
  height: 220px;
  margin-right: 20px;
}

.img-container img{
  width: 100%;
  height: 100%;
}

a{
  text-decoration: none;
  cursor:pointer;
}

.movie-info{
  display: flex;
  flex-direction: column;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  margin-top: 5px;
  font-size: 16px;
  width: 280px;
  border-bottom: solid #eee 1px;
}

.movie-name{
  font-size: 26px;
  margin-top: 27px;
}

.movie-ename{
  font-size: 14px;
  color: #999;
  margin-top: 3px;
}

.movie-score{
  font-size: 26px;
  margin-bottom: 13px;
  color: #ffb400;
  height: 31px;
  line-height: 31px;
}

.movie-category{
  color: #333;

}

.movie-releaseDate{
  color: #999;
}
</style>
