<template>
  <div class="bill-list-container">
    <div class="profile-title">My Orders</div>
    <div class="order-box" v-for="item in billList" :key="item.billId">
      <div class="order-header">
        <span class="order-date">Order Time: {{item.createTime}}</span>
        <span class="order-id">Order ID: {{item.billId}}</span>
        <span class="del-order" @click="deleteBill(item.billId)">
          <i class="el-icon-delete"></i>
        </span>
      </div>
      <div class="order-body">
        <div class="poster">
          <img :src="item.sysSession.sysMovie.moviePoster" alt="Movie Poster">
        </div>
        <div class="order-content">
          <div class="movie-name">"{{item.sysSession.sysMovie.movieName}}"</div>
          <div class="cinema-name">{{item.sysSession.sysHall.sysCinema.cinemaName}}</div>
          <div class="hall-ticket">
            <span>Hall: {{item.sysSession.sysHall.hallName}} ({{item.sysSession.sysHall.hallCategory}})</span>
            <span></span>
          </div>
          <div class="show-time">Show Time: {{item.sysSession.sessionDate + ' ' + item.sysSession.playTime + ' - ' + item.sysSession.endTime}}</div>
        </div>
        <div class="order-price">
          ¥{{item.sysSession.sessionPrice * JSON.parse(item.seats).length}} ({{JSON.parse(item.seats).length}} * {{item.sysSession.sessionPrice}}/ticket)
        </div>
        <div class="order-status">
          {{item.payState ? 'Completed' : 'Pending'}}
        </div>
        <div class="actions">
          <div>
            <a @click="toBillDetail(item.billId)" class="order-detail">View Details</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment'
import billDetail from '../pay/BillDetail'
export default {
  name: "BillInfo",
  data() {
    return {
      queryInfo: {
        userId: ''
      },
      billList: [
        {
          sysSession: {
            sysMovie: {
              moviePoster: ''
            },
            sysCinema: {},
            sysHall: {}
          }
        }
      ]
    }
  },
  created() {
    this.getUser()
    this.getBillList()
  },
  methods: {
    getUser() {
      const user = JSON.parse(window.sessionStorage.getItem('loginUser'))
      if (user) {
        this.queryInfo.userId = user.userId
      }
    },
    async getBillList() {
      try {
        const response = await axios.get('sysBill', { params: this.queryInfo })
        this.billList = response.data.data

        for (let idx in this.billList) {
          this.billList[idx].sysSession.sessionDate = moment(this.billList[idx].sysSession.sessionDate).format('YYYY年MM月DD日')
          this.billList[idx].sysSession.sysMovie.moviePoster = this.global.base + JSON.parse(this.billList[idx].sysSession.sysMovie.moviePoster)[0]
        }
      } catch (error) {
        console.error('Error fetching bill list:', error)
      }
    },
    toBillDetail(id) {
      this.$router.push('/billDetail/' + id)
    },
    async deleteBill(id) {
      try {
        const response = await this.$confirm('This action will permanently delete the order information. Continue?', 'Warning', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        })

        if (response !== 'confirm') {
          return this.$message.info('Deletion cancelled')
        }

        await axios.delete('sysBill/' + id)
        await this.getBillList()
        this.$message.success('Deleted successfully!')
      } catch (error) {
        console.error('Error deleting bill:', error)
        this.$message.error('Deletion failed!')
      }
    }
  }
}
</script>

<style scoped>
.bill-list-container{
  min-height: 900px;
}

.profile-title {
  padding: 26px 0;
  color: #ec443f;
  font-size: 18px;
  border-bottom: 1px solid #e1e1e1;
  margin-bottom: 30px;
}

.order-box {
  border: 1px solid #e5e5e5;
  margin: 0 40px 30px 0;
}

.order-box .order-header {
  background-color: #f7f7f7;
  font-size: 14px;
  padding: 16px 20px;
}

.order-box .order-header .order-date {
  color: #333;
  display: inline-block;
  margin-right: 30px;
}

.order-box .order-header .order-id {
  color: #999;
}

.del-order{
  float: right;
}

.order-box .order-body {
  padding: 20px;
  padding-right: 0;
  display: flex;
}

.order-box .order-body .poster {
  width: 66px;
  height: 91px;
  margin-right: 11px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}

.poster img{
  width: 100%;
  height: 100%;
}

.order-content{
  width: 49%;
}

.movie-name{
  font-size: 16px;
  font-weight: 700;
  color: #333;
  margin: 4px 0 7px -6px;
}

.cinema-name, .hall-ticket {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.show-time {
  font-size: 12px;
  color: #f03d37;
}

.order-price, .order-status, .actions{
  font-size: 14px;
  color: #333;
  width: 15%;
  line-height: 95px;
}
.order-detail {
  color: #2d98f3;
}
a {
  text-decoration: none;
  cursor: pointer;
  color: #333;
}
</style>
