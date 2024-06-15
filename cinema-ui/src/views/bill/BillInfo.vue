<template>
  <div>
    <!-- Breadcrumb navigation area -->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/welcome' }">Home</el-breadcrumb-item>
        <el-breadcrumb-item>Order Management</el-breadcrumb-item>
        <el-breadcrumb-item>Order Information Management</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- Card view -->
    <el-card class="box-card">

      <!-- Row for inputs and buttons -->
      <el-row :gutter="20">
        <!-- Username input -->
        <el-col :span="5">
          <el-input v-model="inputUserName" placeholder="Enter username" clearable></el-input>
        </el-col>
        <!-- Order state selection -->
        <el-col :span="4">
          <el-select v-model="selectedState" placeholder="Select order status" clearable>
            <el-option v-for="item in payStates" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-col>
        <!-- Date picker -->
        <el-col :span="5">
          <el-date-picker
            v-model="selectedDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="Select date">
          </el-date-picker>
        </el-col>
        <!-- Search button -->
        <el-col :span="2">
          <el-button icon="el-icon-search" @click="getBillList">Search</el-button>
        </el-col>
        <!-- Batch delete button -->
        <el-col :span="2">
          <el-button type="danger" @click="multipleDelete">Batch Delete</el-button>
        </el-col>
      </el-row>

      <!-- Order list table -->
      <el-table :data="billList" style="width: 100%" border stripe @selection-change="handleSelectionChange">
        <!-- Selection column -->
        <el-table-column type="selection" width="55"></el-table-column>
        <!-- Bill ID column -->
        <el-table-column prop="billId" label="#" width="40"></el-table-column>
        <!-- Username column -->
        <el-table-column prop="sysUser.userName" label="Username" width="80px"></el-table-column>
        <!-- Movie name column -->
        <el-table-column prop="sysSession.sysMovie.movieName" label="Movie Name"></el-table-column>
        <!-- Hall name column -->
        <el-table-column prop="sysSession.sysHall.hallName" label="Hall Name"></el-table-column>
        <!-- Session date column -->
        <el-table-column prop="sysSession.sessionDate" label="Session Date"></el-table-column>
        <!-- Play time column -->
        <el-table-column prop="sysSession.playTime, sysSession.endTime" label="Play Time">
          <template slot-scope="scope"> {{scope.row.sysSession.playTime}}-{{scope.row.sysSession.endTime}} </template>
        </el-table-column>
        <!-- Seats column -->
        <el-table-column prop="seats" label="Seats"></el-table-column>
        <!-- Booking time column -->
        <el-table-column prop="createTime" label="Booking Time"></el-table-column>
        <!-- Payment state column -->
        <el-table-column prop="payState" label="Order Status" width="80">
          <template slot-scope="scope">
            <span v-if="scope.row.payState === true" style="color: #13ce66">Completed</span>
            <span v-else-if="!scope.row.payState && !scope.row.cancelState" style="color: #145ddc">Unpaid</span>
            <span v-else-if="scope.row.cancelState && !scope.row.cancelTime" style="color: #e6a23c">Cancelled (Timeout)</span>
            <span v-else-if="scope.row.cancelState && scope.row.cancelTime" style="color: crimson">Cancelled (User)</span>
          </template>
        </el-table-column>
        <!-- Operations column -->
        <el-table-column label="Actions" width="120">
          <template slot-scope="scope">
            <!-- Edit button -->
            <el-tooltip effect="dark" content="Edit Order" placement="top" :enterable="false" :open-delay="500">
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.billId)"></el-button>
            </el-tooltip>
            <!-- Delete button -->
            <el-tooltip effect="dark" content="Delete Order" placement="top" :enterable="false" :open-delay="500">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteBillById(scope.row.billId)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination area -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pageNum"
        :page-sizes="[5, 7, 9]"
        :page-size="queryInfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>

    </el-card>

    <!-- Edit Hall Dialog -->
    <el-dialog title="Modify Order" :visible.sync="editDialogVisible" width="60%" @close="editDialogClosed">
      <!-- Form for editing order details -->
      <el-form :model="editForm" ref="editFormRef" label-width="150px">
        <!-- Order ID -->
        <el-form-item label="Order ID" prop="billId">
          <el-input v-model="editForm.billId" disabled></el-input>
        </el-form-item>
        <!-- Username -->
        <el-form-item label="Username" prop="sysUser.userName">
          <el-input v-model="editForm.sysUser.userName" disabled></el-input>
        </el-form-item>
        <!-- Movie Name -->
        <el-form-item label="Movie Name" prop="sysSession.sysMovie.movieName">
          <el-input v-model="editForm.sysSession.sysMovie.movieName" disabled></el-input>
        </el-form-item>
        <!-- Hall Name -->
        <el-form-item label="Hall Name" prop="sysSession.sysHall.hallName">
          <el-input v-model="editForm.sysSession.sysHall.hallName" disabled></el-input>
        </el-form-item>
        <!-- Seats -->
        <el-form-item label="Seats" prop="sysSession.sysHall.hallName">
          <el-input v-model="editForm.seats" disabled></el-input>
        </el-form-item>
        <!-- Session Date -->
        <el-form-item label="Session Date" prop="sysSession.sessionDate">
          <el-input v-model="editForm.sysSession.sessionDate" disabled></el-input>
        </el-form-item>
        <!-- Play Time -->
        <el-form-item label="Play Time" prop="sysSession.sessionDate">
          <el-input v-model="editForm.sysSession.playTime" disabled></el-input>
        </el-form-item>
        <!-- End Time -->
        <el-form-item label="End Time" prop="sysSession.sessionDate">
          <el-input v-model="editForm.sysSession.endTime" disabled></el-input>
        </el-form-item>
        <!-- Order Time -->
        <el-form-item label="Order Time" prop="sysSession.sessionDate">
          <el-input v-model="editForm.createTime" disabled></el-input>
        </el-form-item>
        <!-- Payment State -->
        <el-form-item label="Payment State" prop="payState" v-if="!editForm.cancelState">
          <el-select v-model="editForm.payState" placeholder="Select payment state" clearable>
            <el-option v-for="item in payStates" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <!-- Cancel State -->
        <el-form-item label="Cancellation State" prop="cancelState" v-if="!editForm.payState">
          <el-select v-model="editForm.cancelState" placeholder="Select cancellation state" clearable>
            <el-option v-for="item in cancelStates" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <!-- Dialog footer with buttons -->
      <span slot="footer" class="dialog-footer">
    <el-button @click="editDialogVisible = false">Cancel</el-button>
    <el-button type="primary" @click="editBillInfo">Confirm</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
import global from "@/assets/css/global.css"
import moment from 'moment'
export default {
  name: "billInfo",
  data() {
    return {
      queryInfo: {
        queryByUserName: '',
        payState: '',
        createTime: '',
        pageNum: 1,
        pageSize: 7
      },
      billList: [],
      total: 0,
      multipleSelection: [],
      inputUserName: '',
      selectedState: '',
      selectedDate: '',
      editDialogVisible: false,
      editForm: {
        sysUser: {
          userName: ''
        },
        sysSession: {
          sysMovie: {},
          sysHall: {}
        }
      },
      payStates: [
        {
          id: 0,
          name: 'Unpaid'
        },
        {
          id: 1,
          name: 'Completed'
        }
      ],
      cancelStates: [
        {
          id: 0,
          name: 'Not Cancelled'
        },
        {
          id: 1,
          name: 'Cancelled'
        }
      ],
    }
  },
  created() {
    this.getBillList()
  },
  methods: {
    async getBillList() {
      // this.userName = this.inputUserName
      this.queryInfo.payState = this.selectedState
      this.queryInfo.createTime = this.selectedDate
      console.log('queryInfo'+this.inputUserName)
      this.queryInfo.queryByUserName = this.inputUserName
      console.log('sysUser.userName')
      console.log(this.queryInfo.queryByUserName)
      const _this = this;
      await axios.get('sysBill', {params: _this.queryInfo}).then(resp => {
        console.log(resp)
        _this.billList = resp.data.data;
        _this.total = resp.data.total;
        _this.queryInfo.pageSize = resp.data.pageSize;
        _this.queryInfo.pageNum = resp.data.pageNum;
      })
    },
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize
      this.getBillList()
      console.log(newSize)
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage
      this.getBillList()
      console.log(newPage)
    },
    // Listen for changes in the multiple selection
    handleSelectionChange(val){
      this.multipleSelection = val
    },
    // Show the edit dialog and display the data
    async showEditDialog(id) {
      let isAbleEdit = true
      await axios.get('sysBill/' + id ).then(resp => {
        console.log(resp)
        this.editForm = resp.data.data
        this.editForm.sysUser = resp.data.data.sysUser
        console.log('this.editForm')
        console.log(this.editForm)

        if (this.editForm.payState === true || this.editForm.cancelState === true) {
          isAbleEdit = false
          this.$alert('Sorry! The order is completed or cancelled and cannot be modified.', 'Modification Request Exception Notification', {
            confirmButtonText: 'I understand',
            callback: action => {
              this.$router.push('/bill')
            }
          })
        }
      })
      if (isAbleEdit) {
        this.editForm.payState = 0
        this.editForm.cancelState = 0
        this.editDialogVisible = true
      }
    },
    // Listen for the edit dialog close event
    editDialogClosed(){
      this.$refs.editFormRef.resetFields()
    },

    // Edit cinema category information and submit
    async editBillInfo() {
      const _this = this
      if (_this.editForm.cancelState && _this.editForm.payState) {
        this.$alert('Sorry, modification failed! Cancel state and payment state cannot be true at the same time.', 'Exception in Modifying Order Information', {
          confirmButtonText: 'Got it',
          callback: action => {
            this.$router.push('/bill')
          }
        })
        this.editDialogVisible = false
        return
      }
      if (_this.editForm.payState) {
        const { data: res} = await axios.put('sysBill', JSON.stringify(_this.editForm))
        if(res.code !== 200) return this.$message.error('Payment failed')
        this.$message.success('Successfully paid the order')
        this.editDialogVisible = false
        this.getBillList()
        return
      } else if (_this.editForm.cancelState) {
        _this.editForm.cancelState = true
        _this.editForm.cancelTime = moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
        // Get session seat information
        const { data : curSession } = await axios.get('sysSession/find/' + _this.editForm.sessionId)
        let sessionSeats = JSON.parse(curSession.data.sessionSeats)
        // Parse out the seats selected in the order and update seat information
        for(let seat of JSON.parse(_this.editForm.sests)){
          let row = seat.substring(0, seat.indexOf('row'))
          let col = Number.parseInt(seat.substring(seat.indexOf('row') + 1, seat.length - 1))
          sessionSeats[row][col - 1] = 0
        }
        // Update order information and session seat information
        axios.defaults.headers.put['Content-Type'] = 'application/json'
        const { data: resp } = await axios.put('sysBill/cancel',JSON.stringify({sysBill: _this.editForm, sessionSeats: JSON.stringify(sessionSeats)}))
        if(resp.code !== 200) return this.$message.error('Cancellation failed')
        this.getBillList()
        this.editDialogVisible = false
        this.$message.success('Order cancellation successful')
      }
    },
    async multipleDelete() {
      const _this = this
      // Ask the user to confirm deletion
      const resp = await this.$confirm('This operation will permanently delete these items. Continue?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).catch(err => err)

      // If the user cancels deletion, resp will be "cancel"
      if (resp === 'cancel') {
        return _this.$message.info('Deletion cancelled.')
      }

      let ids = []
      this.multipleSelection.forEach(data => {
        ids.push(data.billId)
      })
      await axios.delete('sysBill/' + ids).then(resp => {
        if (resp.data.code !== 200) {
          this.$message.error('Batch deletion failed!')
        }
      })
      this.getBillList()
      this.$message.success('Batch deletion successful!')
    },

    // Delete the bill by its ID
    async deleteBillById(id) {
      const _this = this
      // Ask the user to confirm deletion
      const resp = await this.$confirm('This operation will permanently delete this item. Continue?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).catch(err => err)

      // If the user cancels deletion, resp will be "cancel"
      if (resp === 'cancel') {
        return _this.$message.info('Deletion cancelled.')
      }

      await axios.delete('sysBill/' + id).then(resp => {
        if (resp.data.code !== 200) {
          _this.$message.error('Failed to delete the order!')
        }
      })
      this.getBillList()
      this.$message.success('Order deletion successful!')
    }
  }
}
</script>

<style scoped>

</style>
