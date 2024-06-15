<template>
  <div>
    <!-- Breadcrumb Navigation Area -->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/welcome' }">Home</el-breadcrumb-item>
        <el-breadcrumb-item>Session Management</el-breadcrumb-item>
        <el-breadcrumb-item>Session Information Management</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- Card View -->
    <el-card class="box-card">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-select v-model="selectedHallId" placeholder="Select Hall Name" clearable>
            <el-option v-for="item in hallList" :key="item.hallId" :label="item.hallName" :value="item.hallId">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="selectedMovieId" placeholder="Select Movie Name" clearable>
            <el-option v-for="item in movieList" :key="item.movieId" :label="item.movieName" :value="item.movieId">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="5">
          <el-date-picker v-model="selectedDate" value-format="yyyy-MM-dd" type="date" placeholder="Select Date">
          </el-date-picker>
        </el-col>
        <el-col :span="3">
          <el-button icon="el-icon-search" @click="getSessionList">Search</el-button>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="showAddDialog">Add</el-button>
        </el-col>
        <el-col :span="2">
          <el-button type="danger" @click="isAbleMultipleDelete">Batch Delete</el-button>
        </el-col>
      </el-row>

      <!-- Session List Table -->
      <el-table :data="sessionList" style="width: 100%" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="sessionId" label="#" width="40"></el-table-column>
        <el-table-column prop="sysHall.hallName" label="Hall Name"></el-table-column>
        <el-table-column prop="sysMovie.movieName" label="Movie Name"></el-table-column>
        <el-table-column prop="languageVersion" label="Language"></el-table-column>
        <el-table-column prop="sessionDate" label="Session Date" width="100"></el-table-column>
        <el-table-column prop="playTime" label="Play Time" width="110">
          <template slot-scope="scope">
            <span>{{ scope.row.playTime }} - {{ scope.row.endTime }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sessionPrice" label="Session Price"></el-table-column>
        <el-table-column prop="seatNums" label="Total Seats"></el-table-column>
        <el-table-column prop="sallNums" label="Sold"></el-table-column>
        <el-table-column prop="seatNums - sallNums" label="Remaining">
          <template slot-scope="scope">
            <span>{{ scope.row.seatNums - scope.row.sallNums }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sessionTips" label="Tips"></el-table-column>

        <el-table-column label="Actions" width="180" align="center">
          <template slot-scope="scope">
            <el-tooltip effect="dark" content="Edit Session" placement="top" :enterable="false" :open-delay="500">
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="isAbleEdit(scope.row.sessionId)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Delete Session" placement="top" :enterable="false" :open-delay="500">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="isAbleDelete(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="View Seats" placement="top" :enterable="false" :open-delay="500">
              <el-button type="warning" icon="el-icon-setting" size="mini" @click="checkSeat(scope.row.sessionId)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination Area -->
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

    <!-- Add Session Dialog -->
    <el-dialog title="Add Session" :visible.sync="addDialogVisible" width="60%" @close="addDialogClosed">
      <!-- Content Area -->
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px">
        <el-form-item label="Hall Name" prop="hallId">
          <el-select v-model="addForm.hallId" placeholder="Select Hall Name" clearable>
            <el-option v-for="item in hallList" :key="item.hallId" :label="item.hallName" :value="item.hallId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Language Version" prop="languageVersion">
          <el-input v-model="addForm.languageVersion"></el-input>
        </el-form-item>
        <el-form-item label="Movie Name" prop="movieId">
          <el-select v-model="addForm.movieId" placeholder="Select Movie Name" clearable>
            <el-option v-for="item in movieList" :key="item.movieId" :label="item.movieName" :value="item.movieId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Session Date" prop="sessionDate">
          <el-date-picker v-model="addForm.sessionDate" value-format="yyyy-MM-dd" type="date" placeholder="Select Date">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="Play Time" prop="sessionTime">
          <el-time-picker v-model="addForm.playTime" value-format="HH:mm" placeholder="Select Play Time">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="Session Price" prop="sessionPrice">
          <el-input v-model="addForm.sessionPrice"></el-input>
        </el-form-item>
        <el-form-item label="Tips" prop="sessionTips">
          <el-input v-model="addForm.sessionTips"></el-input>
        </el-form-item>
      </el-form>

      <!-- Footer Area -->
      <span slot="footer" class="dialog-footer">
    <el-button @click="addDialogVisible = false">Cancel</el-button>
    <el-button type="primary" @click="addSession">Confirm</el-button>
  </span>
    </el-dialog>

    <!-- Edit Session Dialog -->
    <el-dialog title="Edit Session" :visible.sync="editDialogVisible" width="60%" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px">
        <el-form-item label="Hall Name" prop="hallId">
          <el-select v-model="editForm.hallId" placeholder="Select Hall Name" clearable>
            <el-option v-for="item in hallList" :key="item.hallId" :label="item.hallName" :value="item.hallId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Language Version" prop="languageVersion">
          <el-input v-model="editForm.languageVersion"></el-input>
        </el-form-item>
        <el-form-item label="Movie Name" prop="movieId">
          <el-select v-model="editForm.movieId" placeholder="Select Movie Name" clearable>
            <el-option v-for="item in movieList" :key="item.movieId" :label="item.movieName" :value="item.movieId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Session Date" prop="sessionDate">
          <el-date-picker v-model="editForm.sessionDate" value-format="yyyy-MM-dd" type="date" placeholder="Select Date">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="Play Time" prop="sessionTime">
          <el-time-picker v-model="editForm.playTime" value-format="HH:mm" placeholder="Select Play Time">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="Session Price" prop="sessionPrice">
          <el-input v-model="editForm.sessionPrice"></el-input>
        </el-form-item>
        <el-form-item label="Tips" prop="sessionTips">
          <el-input v-model="editForm.sessionTips"></el-input>
        </el-form-item>
      </el-form>

      <!-- Footer Area -->
      <span slot="footer" class="dialog-footer">
    <el-button @click="editDialogVisible = false">Cancel</el-button>
    <el-button type="primary" @click="editSessionInfo">Confirm</el-button>
  </span>
    </el-dialog>

    <!-- Check Seats Dialog -->
    <el-dialog title="Check Seats" :visible.sync="checkDialogVisible" :width="checkDialogWidth">
      <div style="display:flex; align-content: center">
        <span style="margin-right: 10px">Enabled</span>
        <span class="seat" :class="isSelected[0]"></span>
        <span style="margin: 0 10px">Disabled</span>
        <span class="seat" :class="isSelected[1]"></span>
        <span style="margin: 0 10px">Sold</span>
        <span class="seat" :class="isSelected[2]"></span>
      </div>
      <br/>
      <div class="row" v-for="(value, key) in seats" :key="key">
        <span style="margin-right: 100px">{{ key }}</span>
        <div style="display: flex; justify-content: flex-end">
          <span class="seat" :class="isSelected[item]" v-for="(item, index) in value" :key="index"></span>
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import global from "@/assets/css/global.css"
import echarts from 'echarts'
import moment from 'moment'
export default {
  name: "HallInfo",
  data() {
    return {
      queryInfo: {
        hallId: '',
        movieId: '',
        sessionDate: '',
        pageNum: 1,
        pageSize: 7
      },
      sessionList: [],
      total: 0,
      // Control the visibility of dialogs
      addDialogVisible: false,
      // Form data for adding sessions
      addForm: {
        hallId: '',
        languageVersion: '',
        movieId: '',
        playTime: '',
        endTime: '',
        deadline: '',
        sessionDate: '',
        sessionPrice: '',
        sessionTips: '',
        sessionSeats: '',
        seatNums: 0
      },
      // Validation rules for addForm
      addFormRules: {
        hallId: [
          { required: true, message: 'Please select the hall name', trigger: 'change' }
        ],
        languageVersion: [
          { required: true, message: 'Please enter the movie language', trigger: 'blur' }
        ],
        movieId: [
          { required: true, message: 'Please select the movie name', trigger: 'change' }
        ],
        sessionDate: [
          { required: true, message: 'Please select the session date', trigger: 'blur' }
        ],
        playTime: [
          { required: true, message: 'Please select the play time', trigger: 'blur' }
        ],
        sessionPrice: [
          { required: true, message: 'Please enter the session price', trigger: 'blur' }
        ]
      },
      editDialogVisible: false,
      editForm: {},
      checkAbleId: {},
      editFormRules: {
        hallId: [
          { required: true, message: 'Please select the hall name', trigger: 'change' }
        ],
        languageVersion: [
          { required: true, message: 'Please enter the movie language', trigger: 'blur' }
        ],
        movieId: [
          { required: true, message: 'Please select the movie name', trigger: 'change' }
        ],
        sessionDate: [
          { required: true, message: 'Please select the session date', trigger: 'blur' }
        ],
        playTime: [
          { required: true, message: 'Please select the play time', trigger: 'blur' }
        ],
        sessionPrice: [
          { required: true, message: 'Please enter the session price', trigger: 'blur' }
        ]
      },
      selectedHallId: '',
      endPlayTime: '',
      selectedMovieId: '',
      selectedDate: '',
      hallList: [],
      movieList: [],
      multipleSelection: [],
      checkDialogVisible: false,
      seats: {},
      col: '',
      isSelected: {
        0: 'seat-default',
        1: 'seat-choose',
        3: 'seat-sold'
      },
      editSeat: {},
      checkDialogWidth: '',
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        }
      },
      rowNums: '',
      colNums: '',
      rowStart: '',
      loginUser: JSON.parse(window.sessionStorage.getItem('loginUser'))
    }
  },
  created() {
    this.getSessionList()
    this.getHallList()
    this.getMovieList()
    // this.curCinemaName = this.loginUser.cinemaName
  },
  methods: {
    getSessionList() {
      this.queryInfo.hallId = this.selectedHallId
      this.queryInfo.movieId = this.selectedMovieId
      this.queryInfo.sessionDate = this.selectedDate
      console.log(String(this.selectedDate))
      const _this = this;
      axios.get('sysSession', {params: _this.queryInfo}).then(resp => {
        console.log(resp)
        _this.sessionList = resp.data.data;
        _this.total = resp.data.total;
        _this.queryInfo.pageSize = resp.data.pageSize;
        _this.queryInfo.pageNum = resp.data.pageNum;
      })
    },
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize
      this.getSessionList()
      console.log(newSize)
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage
      this.getSessionList()
      console.log(newPage)
    },
    showAddDialog(){
      this.addDialogVisible = true
    },
// Listen to the close event of the add dialog
    addDialogClosed() {
      this.$refs.addFormRef.resetFields()
    },

// Listen to the add button
    addSession() {
      console.log('Before processing session data addition')
      console.log(this.addForm)
      const _this = this

      // Handle end time and deadline
      this.handleEndTimeAndDeadline(_this.addForm.movieId, _this.addForm.sessionDate, _this.addForm.playTime)
      console.log('End time for adding session function: ' + _this.addForm.endTime)

      this.$refs.addFormRef.validate(async valid => {
        console.log(valid)
        if (!valid) return this.$message.error('Failed to add session information')

        // Assign cinema id of the logged-in user to the form as validation passes
        let seats = ''
        let seatNums = 0

        // Retrieve hall information to populate sessionSeats field with corresponding row and seat numbers
        await axios.get('sysHall/' + this.addForm.hallId).then(resp => {
          _this.rowNums = resp.data.data.rowNums
          _this.colNums = resp.data.data.seatNumsRow
          _this.rowStart = resp.data.data.rowStart
          seatNums = resp.data.data.seatNums
          seats = resp.data.data.seatState
        })

        // Assign seat information from hall to the session when creating a new session
        this.$set(this.addForm, 'seatNums', seatNums)
        this.$set(this.addForm, 'sessionSeats', seats)
        console.log(_this.addForm.sessionSeats)

        // If pre-validation is successful, send a network request
        axios.defaults.headers.post['Content-Type'] = 'application/json'
        await axios.post('sysSession', JSON.stringify(_this.addForm)).then(resp => {
          console.log(resp)
          if (resp.data.code !== 200) {
            this.$message.error('Failed to add session information!')
          }
        })

        // Hide the add dialog
        this.addDialogVisible = false
        // Reload the list
        this.getSessionList()
        this.$message.success('Session information added successfully!')
      })
    },

// Cannot make any modifications if there are orders
    isAbleEdit(id) {
      this.checkAbleId.sessionId = id
      axios.get('sysBill', {params: this.checkAbleId}).then(response => {
        console.log(response.data.total)
        console.log('data')
        console.log(response.data)
        console.log('data.data')
        console.log(response.data.data)
        console.log(response.data.data.length)
        let bills = response.data.data
        if (response.data.total === 0) {
          console.log('Can be edited because it is empty')
          this.showEditDialog(id)
        } else {
          console.log('There are related orders before modification')
          let billIds = ''
          for (let temp of bills) {
            billIds += temp.billId+' '
          }
          console.log('billIds is : '+billIds)
          this.$alert('Sorry! There are orders related to the movie session, so the movie session information cannot be modified.\n'+'The abnormal order numbers are: '+billIds, 'Modification Request Exception Notification', {
            confirmButtonText: 'Got it',
            callback: action => {
              this.$router.push('/session')
            }
          })
        }
      })
    },

// Show the edit dialog and echo data
    async showEditDialog(id){
      const _this = this
      const { data : res} = await axios.get('sysSession/find/' + id)
      this.editForm = res.data
      this.editDialogVisible = true
    },

// Listen to the close event of the edit dialog
    editDialogClosed(){
      this.$refs.editFormRef.resetFields()
    },

// Modify session information and submit
    editSessionInfo(){
      this.handleEndTimeAndDeadline(this.editForm.movieId, this.editForm.sessionDate, this.editForm.playTime)
      let row = this.editForm.rowNums
      let col = this.editForm.seatNumsRow
      this.$set(this.editForm, 'seatNums', row * col)
      let seat = {}
      for (let i = 0; i < row; i++) {
        let arr = []
        for (let j = 0; j < col; j++) {
          arr.push(0)
        }
        seat[isNaN(parseInt(this.editForm.rowStart)) ? String.fromCharCode(i + this.editForm.rowStart.charCodeAt(0)) : i + parseInt(this.editForm.rowStart)] = arr
      }
      this.$set(this.editForm, 'seatState', JSON.stringify(seat))

      this.$refs.editFormRef.validate(async valid => {
        const _this = this
        if (!valid) return
        let success = true
        axios.defaults.headers.put['Content-Type'] = 'application/json'
        await axios.put('sysSession', JSON.stringify(_this.editForm)).then(resp => {
          if (resp.data.code !== 200){
            this.$message.error('Failed to modify session information!')
            success = false
          }
        })
        if (!success) return
        this.editDialogVisible = false
        this.getSessionList()
        this.$message.success('Session information modified successfully!')
      })
    },

// Handle end time and deadline (modify or delete movie and hall information)
    handleEndTimeAndDeadline(movieId, sessionDate, playTime){
      this.addForm.deadline = ''
      this.editForm.deadline = ''
      this.addForm.endTime = ''
      this.editForm.endTime = ''
      let resultEndTime = ''
      // Movie duration
      let movieLengthTime
      for (let item of this.movieList) {
        if (item.movieId === movieId) {
          movieLengthTime = item.movieLength
          break
        }
      }
      // Temporary time to record the start time, using moment utility
      let tempTime = new Date(sessionDate)
      // Separate play time
      let hoursMin = playTime.split(':')
      tempTime.setHours(hoursMin[0])
      tempTime.setMinutes(hoursMin[1])
      tempTime.setSeconds(0)
      let tempEndTime = moment(tempTime).add(movieLengthTime,'m')._d
      // Add 20 minutes to the movie end time
      let tempDeadline = moment(moment(tempEndTime).add(20,'m')._d).format('YYYY-MM-DD HH:mm:ss')

      // Set deadline
      this.addForm.deadline = tempDeadline
      this.editForm.deadline = tempDeadline
      console.log('Modified time'+tempEndTime);
      let endHours = tempEndTime.getHours()
      let endMins = tempEndTime.getMinutes()
      // Time formatting
      if (endHours < 10) {
        resultEndTime = '0'+endHours+':'
      } else {
        resultEndTime = endHours+':'
      }
      if (endMins < 10) {
        resultEndTime += '0'+endMins
      } else {
        resultEndTime += endMins
      }
      console.log('End time for adding sessions: ' + resultEndTime)
      // return resultEndTime
      this.addForm.endTime = resultEndTime
      this.editForm.endTime = resultEndTime
    },

// Listen to changes in checkbox selection
    handleSelectionChange(val){
      this.multipleSelection = val
    },

// Pre-validation before batch deletion
    async isAbleMultipleDelete() {
      let ableMultipleDelete = true
      let ids = ''
      for (let i = 0; i < this.multipleSelection.length; i++) {
        this.checkAbleId.sessionId = this.multipleSelection[i].sessionId

        console.log('sessionid' + this.checkAbleId.sessionId)
        // Each data item is validated individually
        const { data : resp } = await axios.get('sysSession/find/' + this.checkAbleId.sessionId)
        console.log('Retrieved session information')
        console.log(resp.data)
        let curSessionInfo = resp.data
        // If the current session is completed, do not process
        if (curSessionInfo.deadline <= moment(new Date()).format('YYYY-MM-DD HH:mm:ss')) continue

        const { data : res } = await axios.get('sysBill', {params: this.checkAbleId})
        if (res.data.length !== 0) {
          ids += this.multipleSelection[i].sessionId + ' '
          ableMultipleDelete = false
        }
      }
      if (!ableMultipleDelete) {
        // 'Sorry!\\n' + 'The session ID(s) causing the issue are: '
        this.$alert('Sorry! There are related orders for the movie sessions, so the movie session information cannot be deleted.\n' + 'The session ID(s) causing the issue are: ' + ids, 'Batch Deletion Request Exception Notification', {
          confirmButtonText: 'I understand',
          callback: action => {
            this.$router.push('/session')
          }
        })
        return
      }
      await this.multipleDelete()
    },

    async multipleDelete() {
      const _this = this
      // Ask the user to confirm deletion
      const resp = await this.$confirm('This operation will permanently delete these items, do you want to continue?', 'Tip', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).catch(err => err)

      // If the user confirms deletion, resp is the string "confirm"
      // If the user cancels deletion, resp is the string "cancel"
      if (resp === 'cancel') {
        return _this.$message.info('Deletion cancelled')
      }

      let ids = []
      this.multipleSelection.forEach(data => {
        ids.push(data.sessionId)
      })
      console.log(ids)
      await axios.delete('sysSession/' + ids).then(resp => {
        if (resp.data.code !== 200) {
          this.$message.error('Batch deletion of sessions failed!')
        }
      })
      this.getSessionList()
      this.$message.success('Batch deletion of sessions succeeded!')
    },

// Dual validation: first check if the session is completed, then it can be deleted; second check if it's not completed, check if there are any orders, and if there are orders, deletion is not allowed. Payment status of the orders is disregarded.
    async isAbleDelete(session) {
      if (session.deadline <= moment(new Date()).format('YYYY-MM-DD HH:mm:ss')) {
        await this.deleteSessionById(session.sessionId)
        return
      }
      this.checkAbleId.sessionId = session.sessionId
      axios.get('sysBill', {params: this.checkAbleId}).then(response => {
        console.log(response.data.total)
        let bills = response.data.data
        if (response.data.total === 0) {
          console.log('Empty and can be changed')
          this.deleteSessionById(session.sessionId)
        } else {
          console.log('Check related sessions before deletion')
          let billIds = ''
          for (let temp of bills) {
            console.log(temp)
            billIds += temp.billId+' '
          }
          console.log('billIds is : '+billIds)
          this.$alert('Sorry! There are related orders for the movie session, so the movie session information cannot be deleted.\n'+'The order ID(s) causing the issue are: '+billIds, 'Deletion Request Exception Notification', {
            confirmButtonText: 'I understand',
            callback: action => {
              this.$router.push('/session')
            }
          })
        }
      })
    },

// Delete the session category corresponding to the ID
    async deleteSessionById(id){
      const _this = this
      // Ask the user to confirm the deletion
      const resp = await this.$confirm('This operation will permanently delete this item. Continue?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).catch(err => err)

      // If the user confirms deletion, resp will be "confirm"
      // If the user cancels deletion, resp will be "cancel"
      console.log(resp)
      if (resp === 'cancel'){
        return _this.$message.info('Deletion canceled')
      }

      await axios.delete('sysSession/' + id).then(resp => {
        if (resp.data.code !== 200){
          _this.$message.success('Failed to delete session information!')
        }
      })
      this.getSessionList()
      this.$message.success('Session information deleted successfully!')
    },

    getHallList(){
      const _this = this
      axios.get('sysHall').then(resp => {
        _this.hallList = resp.data.data;
      })
    },
    getMovieList(){
      const _this = this
      axios.get('sysMovie/find').then(resp => {
        _this.movieList = resp.data.data;
      })
    },
    async checkSeat(id){
      const _this = this
      await axios.get('sysSession/find/' + id).then(resp => {
        console.log(resp)
        _this.seats = JSON.parse(resp.data.data.sessionSeats)
        _this.col = resp.data.data.sysHall.seatNumsRow
      })
      console.log(this.col)
      this.checkDialogWidth = 200 + this.col * 40 + 'px'
      this.checkDialogVisible = true
    }
  }
}
</script>

<style scoped>
.row{
  white-space: nowrap;
  margin-top: 10px;
  padding: 0 10px;
  text-align: center;
  display: flex;
  justify-content: space-between;
}
.seat{
  display: inline-block;
  width: 30px;
  height: 26px;
  margin: 0 5px;
  background-position: 0 1px;
}
.seat-default {
  background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAAHKSURBVEhLxZfbysIwDMezKeJUVAR9A/H9X0FQZMOLeem14sWc4AnP9muybM4557eT/mCYdLT/tknaqQgJ/ACVf7/Oz4TfbvVisYDlcgm1Wg2SRON0OoGqqtDr9bglAAoHGY1GQgqzl5zz+SyGwyF7z7wITyYTtrJD13W2HrzEuFqtspUdnU4Hbrcbew5fSa5SqfQbYbmzoCgKew5fK6disciWgyd8vV5hPB5DoVAA27bpSct6vQbLsmjFhmFwK0MpJjFNky2H3W4nZEf24rPZbMTlcmFPiPv9LgaDAXu+rNY0jS0HzG5Zh+zFRwo9bS/GuNvtgpwM+SQsVwetVosa/GDnpMhFsfUAJ3I4HMgmYRQIZl1awsbzTyYyqzHRkhK2Yj+RwklwBYMHRpDMhd0tTrXiT52jwCsxCrqPZc3RARLM7O1266V/XMrlMlQqFfYcVqsVZXa9Xo8Wzhq/cOYx/i+e8KeYZIFfgyyMxWw2o4Y8mc/nXty9jz08NqfTKTSbTapBLAt8kh6buDocGh+3xNrtNjQaDbLxRSj7/f7pNolLv98Xx+ORvVfeBla+S3VJ4K7hGO/I7S8MDht2UbjklspRokj+NRQKwB/pWISi3oSUQQAAAABJRU5ErkJggg==") no-repeat;
}
.seat-choose{
  background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAAKVSURBVEhLxZXPThpRFMY/hhFBG4okbdiYxgXpxmJi0phi4sKlL+HWlSsfwMSFr+DKjSufwMSFiyakaaagQaOxW1txUUjDHwVh5vacmdtxgGEsA1N/yYHLvdzzzT33nDMhQeAFUOT3f+fFhAeGutRs4tfpKV5dXECoqpz9R8hlKxqFsrqK96mUnOzGVfhrsYh3+/tIXV8Dk5Nydkh0HW3a+21zE59WVuTkE33C5/k8PmxvW4ITE/SPkFzxQacDUOS0nR18XFiQkxZ9dzx9dARwaCOR0UQZ9kP29vAQupz6S39y8UmVMeZcOIwInfp54QAQdJDe2LkLt9tAq2Vmp294L/vhe6Zxb13YwrSMvKZRZMIoLy2hvLYG3N/7E+c9FN7f2SzK8/MQiQS0szO5aGFndfHkBJndXSAeNzc2ZmehUw3GCwUru4eBTlpbXETs6grqwwNgGBBkX7a2kF1eNv9inziWy1minFxU/NM3N2jHYr5PbJAPtdGwHpp8hiiS6YMDUPBNTOE6WfL21sxAGx7Xav5KivaIx0fy7kghGqv0QHR+6yd/GGQUcx52M0Idu+0UDn+ORxovLp24i8CEO9SrvQhM+LlL8hQWnJHcBHxYiKvDA7OOqzTobGwgWalYjZ2hjlObm0N7Zmb4kqIkipZKmLq7e6oU8ldJJqHu7YGK1kOYke3OF1xKzvLsEfa+Y34IDrcfc4q6YAsrfDcBozgy3RSeIvuRyVidinsrNfixW7WKn+m0qcXYLwlum9+Pj5G4vITO709KEDaDmrsfFPLBrtnMrkhvqDfr63gt13nBlYZhiFyhIH8Nz2dNE005dmNgcgl6Fxv8dvGJXq9DcIgHYId63LBbvqpBeJfTCHiJMoEJewP8Afy6sw903o8jAAAAAElFTkSuQmCC") no-repeat;
}
.seat-sold{
  background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAANwSURBVEhLxZZLTBNRFIb/mWlpjbWAYgISNBofGyMGwQdEg0sTdaciaNy40UTxmeiKuDPGxwZXsjAuDNHohmhcYARjjGJBoYSFhAUYA+EpSksfQ8dz7gwwbacj5SEfXMq9c+757z3n3DuVNALLgGx8/neWTThlqIMjgL+3Gd3B11AklzE6NzTEkKFloTT/LNZtzDJG47EUbm/twouhk+hTOpAhGYNpMkXNSX+qPO+wv6xcHzSRJNzW/hm1A3vhcgD0C2mewswUeQ6T+IUcPwqLthujOkk5bh68AycpOklwIaKMQvMdCvBqsNoYmSVJOENeiQXqxcECEYzrHRP/paplKVnGUlil3ERiVJ2W9Z4atg9Gab4xN0qfXGRWB2dWmJ598bVAURzY4T6CA9mXEUpDnO1GQ8D5nfXYlFmMICkWeSux2XEQ3ow8fPX5DUudmapuamlA3dhReKiweCTXsRVrXdvQFWyAYw5JH54ErhY/pfqQ8aCtAnuyD6Mn0IgQ/bBAjBZyxvMBZaVlwn5mx22jT4Qon1sXjQ6o3+GWssWkKE0KUAitds9jLHqtuF707/oq4HGyDy8mtZA4HexToepuGD8tbBhdOAIMSa1xCeejMBEZFvkqyTuGm7ubMEKhNIvz/xxe3qlG2bzvq8Qat34MI1NBKirDkBC+ZRW0FoGuxbmkiSY7gUTV6CSL9z+e4+dEB2r2NYrdTYvzQq4Y4b3nq8JqQ5SRLC8Baea5LmxlY8JLV/Uj/0X0Bzpxq6xRCCaGN2fFrChjVclmzNG1hXdT57+E/okuXC95hnOFtXHhTURVqShssBVOXDWL885/h/vhdmSRaHx44/hHFG2FnbJbXCbmxmF/3FWNh+2nkEmifEEk2nBzKR5xIlKhn+MwcKNpA/44+kQ1M/xmyXfuglcpIAdUfWkgQ8Gg2olhtTvO3yp1PW6X99KqbIQZNrZbtR3sJtGXWdg21DyRb635NLOoFUKYa0ilq22piSE6cwcIYYmKZAuOI0CpDHOjh4vdJshvQeyQ0BKaIseERnl+2/oSPeE3kDWnuHm4xWLpFdY0siyL4yga7dXrysWJohpIdNEIWNiKqZCmtX7yG730afn4TdOiRseClMU1qQbopf7L6KVPIDyGkJq6bpK+ZS4W7Nb6RaFje5wWgp0os2TC9gB/AXsuAMA34noyAAAAAElFTkSuQmCC") no-repeat;
}
</style>
