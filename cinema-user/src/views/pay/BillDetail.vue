<template>
  <div>
    <el-steps :active="3" align-center>
      <el-step title="Choose Movie Session"></el-step>
      <el-step title="Select Seats"></el-step>
      <el-step title="Pay within 15 minutes"></el-step>
      <el-step title="Pick Up Tickets at Cinema" status="wait"></el-step>
    </el-steps>
    <div class="bill-info-container">
      <!-- Order Status -->
      <!-- Unpaid, Not Canceled, Within 15 minutes -->
      <div class="bill-info-status" v-if="payState === false && cancelState === false && (minutes > 0 || seconds > 0)">
        <div class="pay-icon">
          <img src="../../assets/money.png">
        </div>
        <div class="pay-info">
          <div class="pay-info-status">Pending Payment</div>
          <div class="pay-info-desc">Please complete payment within <span>{{minutes}} minutes {{seconds}} seconds</span></div>
        </div>
      </div>
      <!-- Unpaid, Cancelled due to Timeout, 15 minutes -->
      <div class="bill-info-status invalid" v-if="payState === false && cancelState === true && cancelTime === null && minutes <= 0 && seconds <= 0">
        <div class="pay-icon">
          <img src="../../assets/bill-invalid.png">
        </div>
        <div class="pay-info">
          <div class="pay-info-status invalid">The order has expired due to non-payment</div>
        </div>
      </div>
      <div class="bill-info-status finish" v-if="payState === true">
        <div class="pay-icon">
          <img src="../../assets/finish.png">
        </div>
        <div class="pay-info">
          <div class="pay-info-status finish">Completed</div>
        </div>
      </div>
      <div class="bill-info-status invalid" v-if="cancelState === true && cancelTime !== null">
        <div class="pay-icon">
          <img src="../../assets/bill-invalid.png">
        </div>
        <div class="pay-info">
          <div class="pay-info-status invalid">Order Cancelled</div>
        </div>
      </div>

      <!-- Order Information -->
      <div class="bill-info-desc">
        <div class="bill-info-header">
          <span>Order ID: {{billInfo.billId}}</span>
          <span class="bill-info-helper">
            (For any inquiries, please call customer service <div>{{billInfo.sysSession.sysHall.sysCinema.cinemaPhone}}</div>,
            Working hours: {{billInfo.sysSession.sysHall.sysCinema.workStartTime}}-{{billInfo.sysSession.sysHall.sysCinema.workEndTime}})</span>
        </div>

        <table class="order-table">
          <thead>
          <tr>
            <th>Movie</th>
            <th>Language Version</th>
            <th>Showtime</th>
            <th>Seats</th>
            <th>Tips</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td class="movie-name">《{{billInfo.sysSession.sysMovie.movieName}}》</td>
            <td class="movie-name">{{billInfo.sysSession.languageVersion}}</td>
            <td class="showtime">{{billInfo.sysSession.sessionDate}} {{billInfo.sysSession.playTime}} - {{billInfo.sysSession.endTime}}</td>
            <td>
              <span class="hall">{{billInfo.sysSession.sysHall.hallName}}</span>
              <div class="seats">
                <span class="bill-seat" v-for="item in billSeats" :key="item"> {{item}} </span>
              </div>
            </td>
            <td>
              <span>{{billInfo.sysSession.sessionTips}}</span>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <!-- Cinema Information, Submit Order -->
      <div class="bill-info-area">
        <div class="cinema-info">
          <span class="bill-cinema-name">{{billInfo.sysSession.sysHall.sysCinema.cinemaName}}</span>
          <div class="bill-cinema-info">
            <span>Address: {{billInfo.sysSession.sysHall.sysCinema.cinemaAddress}} </span>
            <span>Phone: {{billInfo.sysSession.sysHall.sysCinema.cinemaPhone}} </span>
          </div>
        </div>
        <div class="submit-bill">
          <div>Total Price：<span>{{(billInfo.sysSession.sessionPrice * billSeats.length).toFixed(1)}}</span></div>
          <div v-if="payState === false && cancelState === false && (minutes > 0 || seconds > 0)">
            <el-button @click="payForBill" type="primary" style="width: 200px; margin-top: 20px;" round>Pay Now</el-button></div>
          <div v-if="payState === false && cancelState === false && (minutes > 0 || seconds > 0)">
            <el-button @click="cancelForBill" type="danger" style="width: 200px; margin-top: 20px;" round>Cancel Order</el-button></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment'
export default {
  name: "BillDetail",
  data() {
    return {
      billId: this.$route.params.billId,
      billInfo: {
        sysSession: {
          sysMovie: {},
          sysHall: {
            sysCinema: {
              user: {}
            }
          }
        }
      },
      billSeats: [],
      payState: null,
      cancelState: null,
      cancelTime: null,
      //Timing
      minutes: 1,
      seconds: 0,
    }
  },
  created() {
    this.getBillInfo()
  },
  mounted() {
    this.add()
  },
  methods: {
    async getBillInfo() {
      try {
        const { data: res } = await axios.get('sysBill/' + this.billId);
        if (res.code !== 200) {
          return this.$message.error('Failed to fetch information');
        }
        const userId = JSON.parse(window.sessionStorage.getItem('loginUser')).userId;
        if (userId !== res.data.userId) {
          this.$alert('Illegal operation! Operating on someone else\'s order.', 'Illegal Order Operation Warning', {
            confirmButtonText: 'Got it',
            callback: action => {
              this.$router.push('/bill');
            }
          });
          return;
        }
        this.billInfo = res.data;
        this.cancelTime = this.billInfo.cancelTime;
        // Deadline
        console.log(this.billInfo.deadline);
        // Process order seat information
        this.billSeats = JSON.parse(this.billInfo.seats);
        this.payState = this.billInfo.payState;
        this.cancelState = this.billInfo.cancelState;
        console.log('the create_time' + this.billInfo.createTime);
        this.computeLeftTime();
      } catch (error) {
        console.error('Error while fetching bill information:', error);
        this.$message.error('An error occurred while fetching bill information');
      }
    },
    async payForBill() {
      try {
        // Update order status
        this.billInfo.payState = true;
        // Update order information and session seat information
        const { data: res } = await axios.put('sysBill', this.billInfo);
        if (res.code !== 200) {
          return this.$message.error('Payment failed');
        }
        this.payState = true;
        this.$alert('Payment successful! You can go to the personal center to view order information', 'Payment Notification', {
          confirmButtonText: 'Got it',
          callback: action => {
            this.$router.push('/bill');
          }
        });
      } catch (error) {
        console.error('Error while processing payment:', error);
        this.$message.error('An error occurred while processing payment');
      }
    },
    async cancelForBill() {
      try {
        // Update order status
        this.billInfo.cancelState = true;
        this.billInfo.cancelTime = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
        // Get session seat information
        const { data: curSession } = await axios.get('sysSession/find/' + this.billInfo.sessionId);
        let sessionSeats = JSON.parse(curSession.data.sessionSeats);
        // Parse out the seats selected by the order and update the seat information
        for (let seat of this.billSeats) {
          let row = seat.substring(0, seat.indexOf('排'));
          let col = Number.parseInt(seat.substring(seat.indexOf('排') + 1, seat.length - 1));
          sessionSeats[row][col - 1] = 0;
        }
        // Update order information and session seat information
        const { data: res } = await axios.put('sysBill/cancel', { sysBill: this.billInfo, sessionSeats: JSON.stringify(sessionSeats) });
        if (res.code !== 200) {
          return this.$message.error('Cancellation failed');
        }
        this.payState = false;
        this.cancelState = true;
        this.$alert('Cancellation successful! You can go to the personal center to view order information', 'Cancellation Notification', {
          confirmButtonText: 'Got it',
          callback: action => {
            this.$router.push('/billDetail/' + this.billInfo.billId);
          }
        });
      } catch (error) {
        console.error('Error while cancelling order:', error);
        this.$message.error('An error occurred while cancelling order');
      }
    },
    computeLeftTime() {
      let cur = new Date()
      let createTime = new Date(this.billInfo.createTime)
      let diff = 15 * 60 - (cur - createTime) / 1000
      if (diff < 0) {
        this.minutes = 0
        this.seconds = 0
      } else {
        this.minutes = Math.floor(diff / 60)
        this.seconds = Math.floor(diff % 60)
      }
    },
    // Convert time to two-digit format
    num: function (n) {
      return n < 10 ? '0' + n : '' + n;
    },
    // Timer countdown
    add: function () {
      let _this = this;
      let time = window.setInterval(function () {
        if (_this.seconds === 0 && _this.minutes !== 0) {
          _this.seconds = 59;
          _this.minutes -= 1;
        } else if (_this.minutes === 0 && _this.seconds === 0) {
          _this.seconds = 0;
          window.clearInterval(time);
        } else {
          _this.seconds -= 1;
        }
      }, 1000);
    },
  },
  watch: {
    second: {
      handler (newVal) {
        this.num(newVal)
      }
    },
    minute: {
      handler (newVal) {
        this.num(newVal)
      }
    }
  },
  computed: {
    second: function () {
      return this.num(this.seconds)
    },
    minute: function () {
      return this.num(this.minutes)
    }
  }
}
</script>

<style scoped>
.el-step {
  margin: 40px auto;
}

.bill-info-container{
  width: 80%;
  margin: 0 auto 40px auto;
}

.bill-info-status{
  height: 120px;
  background: #FFFBF2;
  display: flex;
  align-items: center;
  margin-bottom: 40px;
}

.pay-icon{
  width: 40px;
  height:40px;
  margin-left: 50px;
}

.pay-icon>img{
  width: 100%;
  height: 100%;
}

.pay-info{
  display: flex;
  flex-direction: column;
  margin-left: 20px;
}

.pay-info-status{
  color: #FAAF00;
  font-size: 24px;
  font-weight: 700;
}

.pay-info-desc{
  color: #999;
  font-size: 13px;
}

.pay-info-desc>span{
  font-size: 16px;
  color: #faaf00;
  margin: 5px 5px;
}

.bill-info-status.finish{
  background: #F4FFF8;
}

.bill-info-status.invalid{
  background: #FFE0E0;
}

.pay-info-status.finish{
  color: #26CE61;
}

.pay-info-status.invalid{
  color: #faaaaa;
}

.bill-info-header{
  display: flex;
  justify-content: space-between;
}

.bill-info-header>span>div{
  color: #f03d37;
}

.bill-info-helper{
  color: #999;
  font-size: 13px;
  display: flex;
  margin-bottom: 8px;
}

.order-table{
  width: 100%;
  border: 1px solid #e5e5e5;
  border-spacing: 0;
  margin-bottom: 60px;
}

.order-table thead {
  background-color: #f7f7f7;
}

.order-table thead th {
  font-size: 16px;
  color: #333;
  width: 20%;
  padding: 14px 0;
  font-weight: 400;
}

.order-table td {
  text-align: center;
  padding: 20px 0;
  color: #333;
}

.order-table .movie-name {
  font-size: 16px;
}

.order-table .showtime {
  font-size: 14px;
  color: #f03d37;
}

.order-table .cinema-name {
  font-size: 14px;
}

.order-table .hall {
  display: inline-block;
  font-size: 14px;
  color: #424242;
  font-weight: 700;
  margin-right: 10px;
  vertical-align: top;
}

.order-table .seats {
  display: inline-flex;
  text-align: left;
  flex-wrap: wrap;
}

.order-table .seats span{
  margin-right: 5px;
}

.bill-info-area{
  display: flex;
  justify-content: space-between;
}

.bill-cinema-name{
  font-weight: 700;
}

.bill-cinema-info{
  display: flex;
  flex-direction: column;
  margin-top: 15px;
}

.submit-bill{
  margin-top: 40px;
}

.submit-bill span{
  color: #f03d37;
  font-size: 32px;
  font-weight: 700;
}

.submit-bill span:before{
  content: '￥';
  font-size: 20px;
}
</style>
