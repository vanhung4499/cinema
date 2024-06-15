<template>
  <div>
    <!-- Breadcrumb Navigation Area -->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/welcome' }">Home</el-breadcrumb-item>
        <el-breadcrumb-item>User Management</el-breadcrumb-item>
        <el-breadcrumb-item>User Information Management</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- Card View -->
    <el-card class="box-card">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input placeholder="Please enter username" v-model="inputUserName" clearable />
        </el-col>
        <el-col :span="4">
          <el-input placeholder="Please enter email" v-model="inputEmail" clearable />
        </el-col>
        <el-col :span="4">
          <el-input placeholder="Please enter phone number" v-model="inputPhoneNumber" clearable />
        </el-col>
        <el-col :span="4">
          <el-select v-model="selectedSex" placeholder="Please select gender" clearable>
            <el-option v-for="item in gender" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-button icon="el-icon-search" @click="getUserList">Search</el-button>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="showAddDialog">Add</el-button>
        </el-col>
        <el-col :span="2">
          <el-button type="danger" @click="multipleDelete">Batch Delete</el-button>
        </el-col>
      </el-row>

      <el-table :data="userList" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column type="index" label="#"></el-table-column>
        <el-table-column prop="userName" label="Username" width="120px"></el-table-column>
        <el-table-column prop="email" label="Email"></el-table-column>
        <el-table-column prop="phoneNumber" label="Phone Number"></el-table-column>
        <el-table-column prop="sex" label="Gender" :formatter="sexFormat" width="100px"></el-table-column>
        <el-table-column prop="sysRole.roleName" label="Role" width="150px"></el-table-column>

        <el-table-column label="Action" width="180">
          <template slot-scope="scope">
            <el-tooltip effect="dark" content="Edit User Information" placement="top" :enterable="false" :open-delay="500">
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.userId)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Delete User" placement="top" :enterable="false" :open-delay="500">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteUserById(scope.row.userId)"></el-button>
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


    <!-- Adding User Dialog -->
    <el-dialog title="Add User" :visible.sync="addDialogVisible" width="60%" @close="addDialogClosed">
      <!-- Content Area -->
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px">
        <el-form-item label="Username" prop="userName">
          <el-input v-model="addForm.userName"></el-input>
        </el-form-item>
        <el-form-item label="Password" prop="password">
          <el-input v-model="addForm.password" type="password" show-password></el-input>
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="addForm.email"></el-input>
        </el-form-item>
        <el-form-item label="Phone Number" prop="phoneNumber">
          <el-input v-model="addForm.phoneNumber"></el-input>
        </el-form-item>
        <el-form-item label="Gender" prop="sex">
          <el-select v-model="addForm.sex" placeholder="Please select gender" clearable>
            <el-option v-for="item in gender" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Role" prop="roleId">
          <el-select v-model="addForm.roleId" placeholder="Please select role" clearable>
            <el-option v-for="item in roleList" :key="item.roleId" :label="item.roleName" :value="item.roleId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Avatar">
          <el-upload action=""
                     list-type="picture-card"
                     :auto-upload="false"
                     :limit="1"
                     :file-list="pics"
                     :on-change="handleChange"
                     :on-success="handleSuccess"
                     :on-error="handleError"
                     :on-exceed="handleExceed"
                     ref="pictureRef"
                     :http-request="submitFile">
            <i slot="default" class="el-icon-plus"></i>
            <div slot="file" slot-scope="{file}">
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
          <!-- Preview Dialog -->
          <el-dialog :visible.sync="dialogVisible" append-to-body>
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-form-item>
      </el-form>
      <!-- Footer Area -->
      <span slot="footer" class="dialog-footer">
    <el-button @click="addDialogVisible = false">Cancel</el-button>
    <el-button type="primary" @click="addUser">Confirm</el-button>
  </span>
    </el-dialog>

    <!-- Editing User Dialog -->
    <el-dialog title="Edit User Information" :visible.sync="editDialogVisible" width="60%" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px">
        <el-form-item label="Username" prop="userName">
          <el-input v-model="editForm.userName"></el-input>
        </el-form-item>
        <el-form-item label="Password" prop="password">
          <el-input v-model="editForm.password" type="password" show-password></el-input>
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="editForm.email"></el-input>
        </el-form-item>
        <el-form-item label="Phone Number" prop="phoneNumber">
          <el-input v-model="editForm.phoneNumber"></el-input>
        </el-form-item>
        <el-form-item label="Gender" prop="sex">
          <el-select v-model="editForm.sex" placeholder="Please select gender" clearable>
            <el-option v-for="item in gender" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Role" prop="roleId">
          <el-select v-model="editForm.roleId" placeholder="Please select role" clearable>
            <el-option v-for="item in roleList" :key="item.roleId" :label="item.roleName" :value="item.roleId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Avatar">
          <el-upload action=""
                     list-type="picture-card"
                     :auto-upload="false"
                     :limit="1"
                     :file-list="pics"
                     :on-change="handleChange"
                     :on-success="handleSuccess"
                     :on-error="handleError"
                     :on-exceed="handleExceed"
                     ref="pictureEditRef"
                     :http-request="submitFile">
            <i slot="default" class="el-icon-plus"></i>
            <div slot="file" slot-scope="{file}">
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
          <!-- Preview Dialog -->
          <el-dialog :visible.sync="dialogVisible" append-to-body>
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="cancelEdit">Cancel</el-button>
    <el-button type="primary" @click="editUserInfo">Confirm</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
import global from "../../assets/css/global.css"
export default {
  data() {
    let checkEmail = (rule, value, cb) => {
      const regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/
      if (regEmail.test(value)){
        return cb()
      }
      cb(new Error('Please enter a valid email'))
    }
    let checkMobile = (rule, value, cb) => {
      const regMobile = /^(0|86|17951)?(13[0-9]|15[0123456789]|17[678]|18[0-9]|14[57])[0-9]{8}$/
      if (regMobile.test(value)){
        return cb()
      }
      cb(new Error('Please enter a valid mobile number'))
    }
    return {
      queryInfo: {
        userName: '',
        phoneNumber: '',
        email: '',
        sex: '',
        pageNum: 1,
        pageSize: 7
      },
      total: 0,
      addDialogVisible: false,
      addForm: {
        userId: '',
        userName: '',
        password: '',
        email: '',
        phoneNumber: '',
        sex: '',
        userPicture: ''
      },
      addFormRules: {
        userName: [
          { required: true, message: 'Please enter username', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Please enter password', trigger: 'blur' }
        ],
        email: [
          { required: true, message: 'Please enter email', trigger: 'blur' },
          { validator: checkEmail, trigger: 'blur'}
        ],
        phoneNumber: [
          { required: true, message: 'Please enter phone number', trigger: 'blur' },
          { validator: checkMobile, trigger: 'blur'}
        ],
        sex: [
          { required: true, message: 'Please select gender', trigger: 'change' }
        ]
      },
      editDialogVisible: false,
      editForm: {},
      editFormRules: {
        userName: [
          { required: true, message: 'Please enter username', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Please enter password', trigger: 'blur' }
        ],
        email: [
          { required: true, message: 'Please enter email', trigger: 'blur' },
          { validator: checkEmail, trigger: 'blur'}
        ],
        phoneNumber: [
          { required: true, message: 'Please enter phone number', trigger: 'blur' },
          { validator: checkMobile, trigger: 'blur'}
        ],
        sex: [
          { required: true, message: 'Please select gender', trigger: 'change' }
        ]
      },
      inputUserName: '',
      inputEmail: '',
      inputPhoneNumber: '',
      selectedSex: '',
      userList: [],
      gender: [
        {
          id: false,
          name: 'Female'
        },
        {
          id: true,
          name: 'Male'
        }
      ],
      multipleSelection: [],
      dialogImageUrl: '',
      dialogVisible: false,
      pics: [],
      pictureList: [],
      picNums: 0,
      deletePicList: [],
      roleList: []
    }
  },
  created() {
    this.getUserList()
    this.getRoleList()
  },
  methods: {
    getUserList() {
      this.queryInfo.userName = this.inputUserName
      this.queryInfo.email = this.inputEmail
      this.queryInfo.phoneNumber = this.inputPhoneNumber
      this.queryInfo.sex = this.selectedSex
      const _this = this;
      axios.get('sysUser', {params: _this.queryInfo}).then(resp => {
        console.log(resp)
        _this.userList = resp.data.data;
        _this.total = resp.data.total;
        _this.queryInfo.pageSize = resp.data.pageSize;
        _this.queryInfo.pageNum = resp.data.pageNum;
      })
    },
    getRoleList(){
      const _this = this
      axios.get('sysRole').then(resp => {
        _this.roleList = resp.data.data
      })
    },
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize
      this.getUserList()
      console.log(newSize)
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage
      this.getUserList()
      console.log(newPage)
    },
    showAddDialog(){
      this.addDialogVisible = true
    },
// Listen for the closing event of the add dialog
    addDialogClosed(){
      this.$refs.addFormRef.resetFields()     // Reset form fields in add form
      this.$refs.pictureRef.clearFiles()      // Clear uploaded picture files
      this.pics = []                          // Clear picture preview list
      this.pictureList = []                   // Clear picture list for submission
    },

// Listen for the add user button click event
    async addUser(){
      await this.submitFile()                 // Submit uploaded files (pictures)
      console.log(this.pictureList)           // Log the list of uploaded pictures
      this.addForm.userPicture = JSON.stringify(this.pictureList)  // Convert picture list to JSON string

      let success = true                      // Flag to track success status
      const _this = this                      // Preserve component instance

      // Validate the add form fields
      this.$refs.addFormRef.validate(async valid => {
        console.log(valid)                    // Log validation result
        if (!valid) return                    // If validation fails, abort

        // If pre-validation is successful, send network request
        axios.defaults.headers.post['Content-Type'] = 'application/json'
        await axios.post('sysUser', JSON.stringify(_this.addForm)).then(resp => {
          console.log(resp)                    // Log response from server
          if (resp.data.code !== 200){
            this.$message.error('Failed to add user!')
            success = false                    // Set success flag to false on error
          }
        })

        this.getUserList()                    // Refresh user list after adding user

        if (!success) return                   // If failed, abort further actions

        // Hide the add dialog
        this.addDialogVisible = false
        this.$message.success('User added successfully!') // Show success message
      })
    },

// Show edit dialog and populate data
    async showEditDialog(id){
      const _this = this                       // Preserve component instance
      await axios.get('sysUser/' + id).then(resp => {
        _this.editForm = resp.data.data        // Set edit form data from server response
      })

      // Populate picture preview list for editing
      for (const item of JSON.parse(this.editForm.userPicture)) {
        let pic = {}
        pic['name'] = ''                       // Set name of picture (optional)
        pic['url'] = this.global.base + item   // Set URL of picture using base URL and item
        this.pics.push(pic)                    // Push picture to preview list
      }

      this.editDialogVisible = true            // Show edit dialog
    },

// Listen for the closing event of the edit dialog
    editDialogClosed(){
      this.$refs.editFormRef.resetFields()    // Reset form fields in edit form
      this.$refs.pictureEditRef.clearFiles()  // Clear uploaded picture files
      this.pics = []                          // Clear picture preview list
      this.pictureList = []                   // Clear picture list for submission
    },

// Cancel editing
    cancelEdit(){
      this.editDialogVisible = false    // Hide the edit dialog
      this.deletePicList.splice(0, this.deletePicList.length)  // Clear the delete picture list
    },

// Edit user information
    async editUserInfo(){
      await this.submitFile()           // Submit uploaded files (pictures)
      this.editForm.userPicture = JSON.stringify(this.pictureList)  // Convert picture list to JSON string

      // Validate the edit form fields
      this.$refs.editFormRef.validate(async valid => {
        const _this = this              // Preserve component instance

        if (!valid) return              // If validation fails, abort

        let success = true              // Flag to track success status
        axios.defaults.headers.put['Content-Type'] = 'application/json'

        // Send PUT request to update user information
        await axios.put('sysUser', JSON.stringify(_this.editForm)).then(resp => {
          if (resp.data.code !== 200){
            this.$message.error('Failed to modify user information!')
            success = false              // Set success flag to false on error
          }
        })

        if (!success) return             // If failed, abort further actions

        // Hide the edit dialog
        this.editDialogVisible = false
        this.getUserList()               // Refresh user list after modification
        this.$message.success('User information modified successfully!')

        // Delete pictures that were marked for deletion
        for(let s of this.deletePicList){
          await axios.get('/upload/delete?filePath=' + s.substring(s.indexOf('/images')))
        }
      })
    },

// Listen for checkbox selection change
    handleSelectionChange(val){
      this.multipleSelection = val     // Update the selected items array
    },

// Perform multiple deletion of selected users
    async multipleDelete(){
      const _this = this                // Preserve component instance

      // Confirm with the user if they want to proceed with deletion
      const resp = await this.$confirm('This action will permanently delete these items. Continue?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).catch(err => err)

      // If user cancels deletion
      if (resp === 'cancel'){
        return _this.$message.info('Deletion cancelled')
      }

      let ids = []
      this.multipleSelection.forEach(data => {
        ids.push(data.userId)           // Collect user IDs to be deleted
      })

      // Send DELETE request to delete selected users
      await axios.delete('sysUser/' + ids).then(resp => {
        if (resp.data.code !== 200){
          this.$message.success('Failed to delete users!')
        }
      })

      this.getUserList()                // Refresh user list after deletion
      this.$message.success('Users deleted successfully!')
    },

// Delete user by ID
    async deleteUserById(id){
      const _this = this

      // Confirm with the user if they want to proceed with deletion
      const resp = await this.$confirm('This action will permanently delete this item. Continue?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).catch(err => err)

      // If user cancels deletion
      if (resp === 'cancel'){
        return _this.$message.info('Deletion cancelled')
      }

      // Send DELETE request to delete user by ID
      await axios.delete('sysUser/' + id).then(resp => {
        if (resp.data.code !== 200){
          _this.$message.success('Failed to delete user!')
        }
      })

      this.getUserList()                // Refresh user list after deletion
      this.$message.success('User deleted successfully!')
    },

// Remove picture file from upload list
    handleRemove(file) {
      const filePath = file.url
      const idx = this.pics.findIndex(x => x.url === filePath)

      // Add file URL to deletePicList if upload status is 'success'
      if(file.status === 'success'){
        this.deletePicList.push(file.url)
      }

      // Remove file from pics array
      this.pics.splice(idx, 1)
    },

// Preview picture in dialog
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },

// Handle change in uploaded files
    handleChange(file, fileList){
      this.pics = fileList.slice(0)
    },

// Handle successful file upload
    handleSuccess(response){
      this.pictureList.push(response.data)
      this.addForm = JSON.stringify(this.pictureList)  // Update addForm with JSON string of pictureList
      this.editForm = JSON.stringify(this.pictureList) // Update editForm with JSON string of pictureList
    },

// Handle file upload error
    handleError(err){
      console.log(err)
    },

// Handle file exceed limit
    handleExceed(){
      this.$message.error("Only one user avatar can be uploaded!")
    },

// Submit uploaded files
    async submitFile(){
      const _this = this

      // Iterate through each file in pics array
      for (let i = 0; i < this.pics.length; i++){
        let formData = new FormData()

        // If file upload status is 'success', push file URL to pictureList and continue to next file
        if (this.pics[i].status === 'success') {
          let s = this.pics[i].url
          this.pictureList.push(s.substring(s.indexOf('/images')))
          continue
        }

        // Get raw file and append to formData
        let file = this.pics[i].raw
        formData.append('file', file)

        // Send POST request to upload file to server
        await axios.post('upload/user', formData).then(response =>{
          _this.pictureList.push(response.data.data)  // Push uploaded file path to pictureList
        })
      }
    },

// Format sex column in table
    sexFormat(row){
      if (row.sex === true){
        return 'Male'
      } else {
        return 'Female'
      }
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


</style>
