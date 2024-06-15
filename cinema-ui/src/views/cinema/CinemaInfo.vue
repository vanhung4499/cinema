<template>
  <div>
    <!-- Breadcrumb navigation area -->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/welcome' }">Home</el-breadcrumb-item>
        <el-breadcrumb-item>Cinema Management</el-breadcrumb-item>
        <el-breadcrumb-item>Cinema Information Management</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- Card view -->
    <el-card class="box-card">
      <!-- Table displaying cinema information -->
      <el-form :model="cinemaInfo" label-width="150px">
        <el-form-item label="Cinema Name:" prop="cinemaName">
          <el-input class="el-input-show" v-model="cinemaInfo.cinemaName" disabled></el-input>
        </el-form-item>
        <el-form-item label="Cinema Address:" prop="cinemaAddress">
          <el-input class="el-input-show" v-model="cinemaInfo.cinemaAddress" disabled></el-input>
        </el-form-item>
        <el-form-item label="Cinema Phone:" prop="cinemaPhone">
          <el-input class="el-input-show" v-model="cinemaInfo.cinemaPhone" disabled></el-input>
        </el-form-item>
        <el-form-item label="Business Hours:" prop="cinemaPhone">
          <el-input class="el-input-show-time" v-model="cinemaInfo.workStartTime" disabled></el-input>
          to
          <el-input class="el-input-show-time" v-model="cinemaInfo.workEndTime" disabled></el-input>
        </el-form-item>
        <el-form-item label="Owned Hall Categories:" prop="hallCategory">
          <el-tag v-for="hall in halls" :key="hall">{{ hall }}</el-tag>
        </el-form-item>
        <el-form-item label="Cinema Pictures:">
          <span v-for="item in pics" :key="item.url">
            <el-popover placement="left" trigger="click" width="300">
              <img :src="item.url" width="200%">
              <img slot="reference" :src="item.url" :alt="item" style="max-height: 300px; max-width: 300px; padding: 10px">
            </el-popover>
          </span>
        </el-form-item>
        <el-form-item label="">
          <el-button type="primary" @click="showEditDialog">Edit Cinema Information</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Edit Cinema Dialog -->
    <el-dialog title="Edit Cinema" :visible.sync="editDialogVisible" width="60%" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px">
        <el-form-item label="Cinema Name" prop="cinemaName">
          <el-input v-model="editForm.cinemaName"></el-input>
        </el-form-item>
        <el-form-item label="Cinema Address" prop="cinemaAddress">
          <el-input v-model="editForm.cinemaAddress"></el-input>
        </el-form-item>
        <el-form-item label="Cinema Phone" prop="cinemaPhone">
          <el-input v-model="editForm.cinemaPhone"></el-input>
        </el-form-item>
        <el-form-item label="Opening Time" prop="workStartTime">
          <el-time-picker v-model="editForm.workStartTime" value-format="HH:mm" placeholder="Select opening time"></el-time-picker>
        </el-form-item>
        <el-form-item label="Closing Time" prop="workEndTime">
          <el-time-picker v-model="editForm.workEndTime" value-format="HH:mm" placeholder="Select closing time"></el-time-picker>
        </el-form-item>
        <el-form-item label="Owned Hall Categories" prop="hallCategoryList">
          <el-input class="el-input-hall" placeholder="Enter hall category name to add" v-model="inputHall" clearable></el-input>
          <el-button type="primary" @click="addHallCategory()">Add</el-button>
        </el-form-item>
        <el-form-item>
          <el-tag v-for="(item, index) in halls" :key="index" closable @close="deleteHallCategory(index)">
            {{ item }}
          </el-tag>
        </el-form-item>

        <el-form-item label="Cinema Pictures">
          <el-upload action="" list-type="picture-card" :auto-upload="false" :file-list="pics"
                     :on-change="handleChange" :on-success="handleSuccess" :on-error="handleError"
                     ref="pictureEditRef" :http-request="submitFile">
            <i slot="default" class="el-icon-plus"></i>
            <div slot="file" slot-scope="{ file }">
              <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
              <span class="el-upload-list__item-actions">
                <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
                  <i class="el-icon-zoom-in"></i>
                </span>
                <span class="el-upload-list__item-delete" @click="handleRemove(file)">
                  <i class="el-icon-delete"></i>
                </span>
              </span>
            </div>
          </el-upload>
          <!-- Enlarged preview -->
          <el-dialog :visible.sync="dialogVisible" append-to-body>
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelEdit">Cancel</el-button>
        <el-button type="primary" @click="editCinemaInfo">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import global from "../../assets/css/global.css"
  export default {
    data() {
      return {
        // Controls the visibility of the edit dialog
        editDialogVisible: false,

        // Form data for editing cinema information
        editForm: {},

        // Holds the current cinema information to display
        cinemaInfo: {},

        // Validation rules for the edit form fields
        editFormRules: {
          cinemaName: [
            { required: true, message: 'Please enter cinema name', trigger: 'change' }
          ],
          cinemaAddress: [
            { required: true, message: 'Please enter cinema address', trigger: 'change' }
          ],
          cinemaPhone: [
            { required: true, message: 'Please enter cinema phone number', trigger: 'change' }
          ]
        },

        // Input field for adding hall categories
        inputHall: '',

        // Image URL for dialog preview
        dialogImageUrl: '',

        // Controls the visibility of the image preview dialog
        dialogVisible: false,

        // Flag to hide the upload section
        hideUpload: false,

        // Array to hold uploaded pictures
        pics: [],

        // Array to hold hall categories
        halls: [],

        // Array of pictures sent to the backend as JSON
        pictureList: [],

        // Count of pictures
        picNums: 0,

        // List of pictures to delete
        deletePicList: []
      }
    },
    created() {
      this.getCinemaInfo()
    },
    methods: {
      async getCinemaInfo() {
        const _this = this
        await axios.get('sysCinema').then(resp => {
          _this.cinemaInfo = resp.data.data
        })
        _this.pics = []
        _this.halls = []

        for (const item of JSON.parse(this.cinemaInfo.cinemaPicture)) {
          let pic = {}
          pic['name'] = ''
          pic['url'] = this.global.base + item
          this.pics.push(pic)
        }
        for (const item of JSON.parse(this.cinemaInfo.hallCategoryList)) {
          this.halls.push(item)
        }
      },
      // Display the edit dialog and populate it with current cinema information
      async showEditDialog() {
        this.editForm = this.cinemaInfo
        this.editDialogVisible = true
      },

// Handle the closing event of the edit dialog
      editDialogClosed() {
        this.$refs.editFormRef.resetFields()   // Reset form fields
        this.$refs.pictureEditRef.clearFiles() // Clear uploaded files
        this.pics = []                         // Clear displayed pictures
        this.pictureList = []                  // Clear list of pictures
        this.halls = []                        // Clear hall categories
        this.getCinemaInfo()                   // Refresh cinema information
      },

// Cancel the edit operation
      cancelEdit() {
        this.editDialogVisible = false         // Hide the edit dialog
        this.deletePicList.splice(0, this.deletePicList.length)  // Clear list of pictures marked for deletion
      },

// Submit edited cinema information
      async editCinemaInfo() {
        await this.submitFile()                // Upload files if any
        this.editForm.cinemaPicture = JSON.stringify(this.pictureList)  // Update picture list in editForm
        this.editForm.hallCategoryList = JSON.stringify(this.halls)     // Update hall categories in editForm

        // Validate the edit form
        this.$refs.editFormRef.validate(async valid => {
          const _this = this
          if (!valid) return  // If form is not valid, exit function

          let success = true  // Flag to track if update was successful
          axios.defaults.headers.put['Content-Type'] = 'application/json'

          // Send update request to backend
          await axios.put('sysCinema/update', JSON.stringify(_this.editForm)).then(resp => {
            if (resp.data.code !== 200){
              this.$message.error('Failed to update cinema information!')  // Display error message if update fails
              success = false
            }
          })

          if (!success) return  // If update was not successful, exit function

          // Close edit dialog, refresh cinema information, and handle deletion of pictures
          this.editDialogVisible = false
          await this.getCinemaInfo()  // Refresh cinema information
          this.$message.success('Cinema information updated successfully!')  // Display success message

          // Delete pictures marked for deletion
          for(let item of this.deletePicList) {
            await axios.get('/upload/delete?filePath=' + item.substring(item.indexOf('/images')))
          }
        })
      },

      addHallCategory() {
        if (this.inputHall === '' || this.inputHall === null) {
          // Alert if input is empty
          this.$alert('Failed to add hall category! Reason: Hall category cannot be empty.', 'Hall Category Add Exception', {
            confirmButtonText: 'OK'
          })
          return
        } else if (!this.halls.includes(this.inputHall)) {
          // Add hall category if it does not already exist
          this.halls.push(this.inputHall)
        } else {
          // Alert if hall category already exists
          console.log('Already exists')
          this.$alert('Failed to add hall category! Reason: Hall category already exists.', 'Hall Category Add Exception', {
            confirmButtonText: 'OK'
          })
        }
        this.inputHall = '' // Clear input field after adding hall category
      },

      deleteHallCategory(index) {
        this.halls.splice(index, 1)
        console.log(this.halls)
      },
      handleRemove(file, fileList) {
        const filePath = file.url
        console.log(filePath)
        const idx = this.pics.findIndex(x => x.url === filePath)
        if (file.status === 'success') {
          this.deletePicList.push(file.url)
        }
        this.pics.splice(idx, 1)
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url
        this.dialogVisible = true
      },
      handleChange(file, filelist){
        this.pics = filelist.slice(0)
      },
      handleSuccess(response){
        this.pictureList.push(response.data)
        this.editForm = JSON.stringify(this.pictureList)
      },
      handleError(err){
        console.log(err)
      },
      async submitFile() {
        const _this = this
        for (let i = 0; i < this.pics.length; i++){
          let formData = new FormData()
          if (this.pics[i].status === 'success') {
            let s = this.pics[i].url
            this.pictureList.push(s.substring(s.indexOf('/images')))
            continue
          }
          let file = this.pics[i].raw
          formData.append('file', file)
          await axios.post('upload/cinema', formData).then(response =>{
            _this.pictureList.push(response.data.data)
          })
        }
      }
    }
  }
</script>

<style scoped>
.el-tag{
  margin: 0 10px 10px 0;
}
.row{
  white-space: nowrap;
  margin-top: 10px;
  padding: 0 10px;
  text-align: center;
  display: flex;
  justify-content: space-between;
}

.row2{
  margin-top: 20px;
}
.el-input-show{
  width: 420px;
}
.el-input-show-time{
  width: 100px;
}
.el-input-hall {
  width: 240px;
  margin: 0 20px 0px 0;
}
</style>
